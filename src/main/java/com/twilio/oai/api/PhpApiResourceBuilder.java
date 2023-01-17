package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.PathUtils;
import com.twilio.oai.StringHelper;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resource.Resource;
import com.twilio.oai.template.IApiActionTemplate;
import com.twilio.oai.template.PhpApiActionTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;
import static com.twilio.oai.common.ApplicationConstants.TWILIO_EXTENSION_NAME;

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
                if (operation.path.endsWith("}") || operation.path.endsWith("}.json")) {
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
        reorderParams(apiResourceBuilder);
        this.addOptionFileParams(apiResourceBuilder);
        categorizeOperations();
        return apiResourceBuilder;
    }

    private void addOptionFileParams(ApiResourceBuilder apiResourceBuilder) {
        for (CodegenOperation operation : apiResourceBuilder.codegenOperationList) {
            if (operation.vendorExtensions.containsKey("x-ignore")) continue;
            List<CodegenParameter> optionFileParams = new ArrayList<>();
            List<CodegenParameter> optionalQueryParams = new ArrayList<>();
            List<CodegenParameter> optionalFormParams = new ArrayList<>();
            List<CodegenParameter> optionalHeaderParams = new ArrayList<>();
            Set<String> conditionalParamSet = extractConditionalParams(operation);
            for (CodegenParameter param : operation.optionalParams) {
                if (!param.isPathParam) {
                    if (conditionalParamSet.contains(param.baseName)) {
                        param.vendorExtensions.put("optionFileSeparator", ",");
                        if (!(boolean) operation.vendorExtensions.getOrDefault("x-is-read-operation", false) ||
                                !param.baseName.equals("PageSize")) {
                            optionFileParams.add(param);
                        }
                    } else if (param.isQueryParam) {
                        param.vendorExtensions.put("optionFileSeparator", ",");
                        if (!(boolean) operation.vendorExtensions.getOrDefault("x-is-read-operation", false) ||
                                !param.baseName.equals("PageSize")) {
                            optionalQueryParams.add(param);
                        }
                    } else if (param.isFormParam) {
                        param.vendorExtensions.put("optionFileSeparator", ",");
                        optionalFormParams.add(param);
                    } else if (param.isHeaderParam) {
                        param.vendorExtensions.put("optionFileSeparator", ",");
                        optionalHeaderParams.add(param);
                    }
                }
            }
            optionFileParams.addAll(optionalQueryParams);
            optionFileParams.addAll(optionalFormParams);
            optionFileParams.addAll(optionalHeaderParams);
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

    private void reorderParams(ApiResourceBuilder apiResourceBuilder) {
        for (CodegenOperation codegenOperation : apiResourceBuilder.codegenOperationList) {
            rearrangeBeforeAfter(codegenOperation.pathParams);
            rearrangeBeforeAfter(codegenOperation.queryParams);
            rearrangeBeforeAfter(codegenOperation.requiredParams);
            rearrangeBeforeAfter(codegenOperation.optionalParams);
            rearrangeBeforeAfter(codegenOperation.headerParams);
            rearrangeBeforeAfter(codegenOperation.formParams);
            rearrangeBeforeAfter(codegenOperation.allParams);
            rearrangeBeforeAfter(codegenOperation.bodyParams);
        }
    }

    private void rearrangeBeforeAfter(final List<CodegenParameter> parameters) {
        for (int index = 0; index < parameters.size(); index++) {
            CodegenParameter codegenParameter = parameters.get(index);
            if (!StringUtils.isBlank(codegenParameter.paramName) && codegenParameter.paramName.endsWith("Before")) {
                String paramName = codegenParameter.paramName.replace("Before", "");
                if (index > 0 && paramName.equals(parameters.get(index - 1).paramName)) {
                    CodegenParameter codegenParameterToRearrange = parameters.get(index - 1);
                    parameters.set(index, codegenParameterToRearrange);
                    parameters.set(index - 1, codegenParameter);

                    resolveBeforeAfterParamsDataType(parameters, index, paramName, codegenParameter, codegenParameterToRearrange);
                }
            }
        }
    }

    private static void resolveBeforeAfterParamsDataType(List<CodegenParameter> parameters, int index, String paramName, CodegenParameter codegenParameter, CodegenParameter beforeCodegenParameter) {
        String afterParam = paramName + "After";
        if (index < parameters.size() - 1 && afterParam.equals(parameters.get(index + 1).paramName)) {
            CodegenParameter afterCodegenParameter = parameters.get(index + 1);
            afterCodegenParameter.dataType = "string";
            beforeCodegenParameter.dataType = "string";
            codegenParameter.dataType = "string";
        }
    }

    private static Set<String> extractConditionalParams(CodegenOperation operation) {
        Set<String> conditionalParamSet = new HashSet<>();
        Optional<Object> conditional = Optional
                .ofNullable(operation.vendorExtensions)
                .map(ext -> ext.get(TWILIO_EXTENSION_NAME))
                .map(Map.class::cast)
                .map(xTwilio -> xTwilio.get("conditional"));
        if (conditional.isPresent()) {
            List<List<String>> conditionalParams = (List<List<String>>) conditional.get();
            for (List<String> paramList : conditionalParams) {
                for (String conditionalParam : paramList) {
                    conditionalParam = StringHelper.toFirstLetterLower(conditionalParam);
                    String[] wordList = conditionalParam.split("_");
                    conditionalParam = "";
                    for (String word : wordList) {
                        conditionalParam += Character.toUpperCase(word.charAt(0)) + word.substring(1);
                    }
                    conditionalParamSet.add(conditionalParam);
                }
            }
        }
        return conditionalParamSet;
    }

    @Override
    public ApiResourceBuilder updateResponseModel(Resolver<CodegenProperty> codegenPropertyResolver) {
        codegenOperationList.forEach(codegenOperation -> {
            List<CodegenModel> responseModels = new ArrayList<>();
            codegenOperation.responses
                    .stream()
                    .map(response -> response.baseType)
                    .filter(Objects::nonNull)
                    .map(modelName -> this.getModel(modelName, codegenOperation))
                    .flatMap(Optional::stream)
                    .forEach(item -> {
                        item.allVars.stream().filter(var -> var.isModel && var.dataFormat == null).forEach(this::setDataFormatForNestedProperties);
                        item.vars.stream().filter(var -> var.isModel && var.dataFormat == null).forEach(this::setDataFormatForNestedProperties);
                        item.vars.forEach(codegenPropertyResolver::resolve);
                        item.allVars.forEach(codegenPropertyResolver::resolve);
                        responseModels.add(item);
                    });
            this.apiResponseModels.addAll(getDistinctResponseModel(responseModels));
        });
        return this;
    }

    private void setDataFormatForNestedProperties(CodegenProperty codegenProperty) {
        String ref = codegenProperty.getRef();
        ref = ref.replaceFirst("#/components/schemas/", "");
        Optional<CodegenModel> model = this.getModelbyName(ref);
        if (model.isPresent()) {
            CodegenModel item = model.get();
            codegenProperty.dataFormat = item.getFormat();
        }
    }
}
