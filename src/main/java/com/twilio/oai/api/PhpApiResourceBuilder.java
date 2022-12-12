package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.PathUtils;
import com.twilio.oai.resolver.ISchemaResolver;
import com.twilio.oai.resource.Resource;
import com.twilio.oai.template.IApiActionTemplate;
import com.twilio.oai.template.PhpApiActionTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.*;
import java.util.stream.Stream;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;

public class PhpApiResourceBuilder extends ApiResourceBuilder {
    private HashSet<String> pathSet = new HashSet<>();
    private final List<CodegenOperation> listOperations = new ArrayList<>();
    private final List<CodegenOperation> instanceOperations = new ArrayList<>();
    protected String apiListPath = "";
    protected String apiContextPath = "";

    public PhpApiResourceBuilder(IApiActionTemplate template, List<CodegenOperation> codegenOperations, List<CodegenModel> allModels) {
        super(template, codegenOperations, allModels);
    }

    @Override
    public PhpApiResourceBuilder updateTemplate() {
        codegenOperationList.stream().forEach(codegenOperation -> {
            updateNamespaceSubPart(codegenOperation);
            template.clean();
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
        codegenOperationList.stream().forEach(operation -> {
            if (!pathSet.contains(operation.path)) {
                List<Resource> dependents = directoryStructureService.getResourceTree().dependents(operation.path);
                List<Resource> methodDependents = dependents.stream().filter(dep ->
                                PathUtils.getTwilioExtension(dep.getPathItem(), "pathType").get().equals("instance"))
                        .collect(Collectors.toList());
                dependents.removeIf(dep -> methodDependents.contains(dep));
                List<Resource> propertyDependents = dependents;
                propertyDependents.addAll(Optional.ofNullable(methodDependents.stream()).orElse(Stream.empty()).filter(dep ->
                        !dep.getName().endsWith("}") && !dep.getName().endsWith("}.json")).collect(Collectors.toList()));
                List<Object> dependentProperties = new ArrayList<>();
                List<Object> dependentMethods = new ArrayList<>();
                updateDependents(directoryStructureService, methodDependents, dependentMethods);
                updateDependents(directoryStructureService, propertyDependents, dependentProperties);
                operation.vendorExtensions.put("importProperties", dependentProperties);
                operation.vendorExtensions.put("importMethods", dependentMethods);
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
        return Pattern.compile("\\{([\\w])").matcher(path)
                .replaceAll(match -> "{" + match.group(1).toLowerCase());
    }

    private String replaceBraces(String path) {
        path = path.replaceAll("[{]", "' . \\\\rawurlencode(\\$");
        return path.replaceAll("[}]", ") . '");
    }

    private void updateNamespaceSubPart(CodegenOperation codegenOperation) {
        List<String> filePathArray = new ArrayList<>(Arrays.asList(codegenOperation.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
        filePathArray = filePathArray.subList(0, filePathArray.size() - 1);
        if (!filePathArray.isEmpty()) {
            final String namespacePath = filePathArray
                    .stream()
                    .collect(Collectors.joining("\\"));
            namespaceSubPart = "\\" + namespacePath;
        }
    }

    @Override
    public ApiResourceBuilder updateOperations(ISchemaResolver<CodegenParameter> codegenParameterIResolver) {
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
                    if (!(boolean) operation.vendorExtensions.getOrDefault("x-is-read-operation", false)) {
                        optionFileParams.add(param);
                    } else if (!param.baseName.equals("PageSize")) {
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
            template.addSupportVersion();
        }
        return this;
    }
}
