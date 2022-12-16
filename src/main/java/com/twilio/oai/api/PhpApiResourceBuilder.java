package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.PathUtils;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resource.Resource;
import com.twilio.oai.template.IApiActionTemplate;
import com.twilio.oai.template.PhpApiActionTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;

public class PhpApiResourceBuilder extends ApiResourceBuilder {
    private final HashSet<String> pathSet = new HashSet<>();
    private final List<CodegenOperation> listOperations = new ArrayList<>();
    private final List<CodegenOperation> instanceOperations = new ArrayList<>();
    protected String apiListPath = "";
    protected String apiContextPath = "";

    public PhpApiResourceBuilder(IApiActionTemplate template, List<CodegenOperation> codegenOperations, List<CodegenModel> allModels) {
        super(template, codegenOperations, allModels);
    }

    @Override
    public PhpApiResourceBuilder updateTemplate() {
        template.clean();
        template.addSupportVersion();

        codegenOperationList.forEach(codegenOperation -> {
            updateNamespaceSubPart(codegenOperation);
            if (metaAPIProperties.containsKey("hasInstanceOperation") && !codegenOperation.vendorExtensions.containsKey("x-ignore"))
                template.add(PhpApiActionTemplate.TEMPLATE_TYPE_CONTEXT);
            if ((boolean) codegenOperation.vendorExtensions.getOrDefault("hasOptionFileParams", false))
                template.add(PhpApiActionTemplate.TEMPLATE_TYPE_OPTIONS);
            template.add(PhpApiActionTemplate.TEMPLATE_TYPE_PAGE);
            template.add(PhpApiActionTemplate.TEMPLATE_TYPE_LIST);
            template.add(PhpApiActionTemplate.TEMPLATE_TYPE_INSTANCE);
        });
        return this;
    }

    @Override
    public PhpApiResources build() {
        return new PhpApiResources(this);
    }

    @Override
    public IApiResourceBuilder updateApiPath() {
        super.updateApiPath();
        if (!instanceOperations.isEmpty())
            apiContextPath = instanceOperations.get(0).path;
        if (!listOperations.isEmpty())
            apiListPath = listOperations.get(0).path;

        apiContextPath = formatPath(apiContextPath);
        apiListPath = formatPath(apiListPath);

        return this;
    }

    @Override
    public ApiResourceBuilder setImports(DirectoryStructureService directoryStructureService) {
        codegenOperationList.forEach(operation -> {
            if (!pathSet.contains(operation.path)) {
                List<Resource> dependents = StreamSupport.stream(directoryStructureService.getResourceTree().getResources().spliterator(), false)
                        .filter(resource -> PathUtils.removeFirstPart(operation.path).equals(PathUtils.getTwilioExtension(resource.getPathItem(), "parent").orElse(null)))
                        .collect(Collectors.toList());

                List<Resource> methodDependents = dependents.stream().filter(dep ->
                                PathUtils.getTwilioExtension(dep.getPathItem(), "pathType").get().equals("instance"))
                        .collect(Collectors.toList());
                dependents.removeIf(methodDependents::contains);
                dependents.addAll(Optional.ofNullable(methodDependents.stream()).orElse(Stream.empty()).filter(dep ->
                        !dep.getName().endsWith("}") && !dep.getName().endsWith("}.json")).collect(Collectors.toList()));
                List<Object> dependentProperties = new ArrayList<>();
                List<Object> dependentMethods = new ArrayList<>();
                updateDependents(directoryStructureService, methodDependents, dependentMethods);
                updateDependents(directoryStructureService, dependents, dependentProperties);
                if(operation.path.endsWith("}") || operation.path.endsWith("}.json")) {
                    metaAPIProperties.put("contextImportProperties", dependentProperties);
                    metaAPIProperties.put("contextImportMethods", dependentMethods);
                } else {
                    metaAPIProperties.put("listImportProperties", dependentProperties);
                    metaAPIProperties.put("listImportMethods", dependentMethods);
                }
                pathSet.add(operation.path);
            }
        });
        return this;
    }

    private static void updateDependents(DirectoryStructureService directoryStructureService, List<Resource> resourceList, List<Object> dependentList) {
        resourceList.forEach(dependent -> dependent.getPathItem().readOperations()
                .forEach(opr -> directoryStructureService.addContextdependents(dependentList,
                        dependent.getName(),
                        opr)));
        resourceList.stream().filter(dependent -> dependent.getPathItem().readOperations().isEmpty()).
                forEach(dep -> directoryStructureService.addContextdependents(dependentList, dep.getName(), null));

        dependentList.stream().map(DirectoryStructureService.ContextResource.class::cast)
                .map(contextResource -> {
                    if (contextResource.getParent().matches("(.*)Function\\\\(.*)"))
                        contextResource.setParent(contextResource.getParent().replaceAll("\\\\Function\\\\", "\\\\TwilioFunction\\\\"));
                    return (Object) contextResource;
                }).collect(Collectors.toList());
    }

    private String formatPath(String path) {
        path = PathUtils.removeFirstPart(path);
        path = lowerCasePathParam(path);
        path = formatDate(path);
        return replaceBraces(path);
    }

    private String formatDate(String path) {
        return Pattern.compile("\\{date}").matcher(path).replaceAll("{date->format('Y-m-d')}");
    }

    private String lowerCasePathParam(String path) {
        return Pattern.compile("\\{(\\w)").matcher(path)
                .replaceAll(match -> "{" + match.group(1).toLowerCase());
    }

    private String replaceBraces(String path) {
        path = path.replace("{", "' . \\rawurlencode($");
        return path.replace("}", ") . '");
    }

    private void updateNamespaceSubPart(CodegenOperation codegenOperation) {
        List<String> filePathArray = new ArrayList<>(Arrays.asList(codegenOperation.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
        filePathArray = filePathArray.subList(0, filePathArray.size() - 1);
        if (!filePathArray.isEmpty()) {
            final String namespacePath = String.join("\\", filePathArray);
            namespaceSubPart = "\\" + namespacePath;
        }
        namespaceSubPart = namespaceSubPart.replaceAll("\\\\Function$", "\\\\TwilioFunction");
        namespaceSubPart = namespaceSubPart.replaceAll("\\\\Function[\\\\]", "\\\\TwilioFunction\\\\");
    }

    @Override
    public ApiResourceBuilder updateOperations(Resolver<CodegenParameter> codegenParameterIResolver) {
        ApiResourceBuilder apiResourceBuilder = super.updateOperations(codegenParameterIResolver);
        this.addOptionFileParams(apiResourceBuilder);
        categorizeOperations();
        return apiResourceBuilder;
    }

    private void addOptionFileParams(ApiResourceBuilder apiResourceBuilder) {
        for (CodegenOperation operation : apiResourceBuilder.codegenOperationList) {
            if (operation.vendorExtensions.containsKey("x-ignore")) continue;
            List<CodegenParameter> optionFileParams = new ArrayList<>();
            for (CodegenParameter param : operation.optionalParams) {
                if (!param.isPathParam) {
                    param.vendorExtensions.put("optionFileSeparator", ",");
                    if (!(boolean) operation.vendorExtensions.getOrDefault("x-is-read-operation", false) ||
                        !param.baseName.equals("PageSize")) {
                        optionFileParams.add(param);
                    }
                }
            }
            if (!optionFileParams.isEmpty())
                optionFileParams.get(optionFileParams.size() - 1).vendorExtensions.put("optionFileSeparator", "");
            operation.vendorExtensions.put("optionFileParams", optionFileParams);
            operation.vendorExtensions.put("hasOptionFileParams", !optionFileParams.isEmpty());
        }
    }

    private void categorizeOperations() {
        codegenOperationList.stream().filter(operation -> !operation.vendorExtensions.containsKey("x-ignore")).forEach(codegenOperation -> {
            Optional<String> pathType = Optional.ofNullable(codegenOperation.vendorExtensions.get("x-path-type").toString());
            if (pathType.isPresent()) {
                if (pathType.get().equals("list")) {
                    listOperations.add(codegenOperation);
                    codegenOperation.vendorExtensions.put("listOperation", true);
                    metaAPIProperties.put("hasListOperation", true);
                } else {
                    instanceOperations.add(codegenOperation);
                    codegenOperation.vendorExtensions.put("instanceOperation", true);
                    metaAPIProperties.put("hasInstanceOperation", true);
                }
            }
        });
    }

    public IApiResourceBuilder addVersionLessTemplates(OpenAPI openAPI, DirectoryStructureService directoryStructureService) {
        if (directoryStructureService.isVersionLess()) {
            String version = PathUtils.getFirstPathPart(codegenOperationList.get(0).path);
            PhpDomainBuilder.setContextResources(directoryStructureService, version);
        }
        return this;
    }
}
