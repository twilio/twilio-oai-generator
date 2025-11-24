package com.twilio.oai.api;

import com.twilio.oai.*;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resource.Resource;
import com.twilio.oai.template.IApiActionTemplate;
import com.twilio.oai.template.PhpApiActionTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.*;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.twilio.oai.common.ApplicationConstants.*;

public class PhpApiResourceBuilder extends ApiResourceBuilder {
    public final static String TAB_WHITESPACES = "    ";
    private final HashSet<String> pathSet = new HashSet<>();
    protected String apiListPath = "";
    protected String apiContextPath = "";

    protected Resolver<CodegenProperty> codegenPropertyIResolver;
    public Set<IJsonSchemaValidationProperties> enums = new HashSet<>();


    private final IConventionMapper conventionMapper =
            new LanguageConventionResolver("config/" + EnumConstants.Generator.TWILIO_PHP.getValue() + ".json");

    public PhpApiResourceBuilder(IApiActionTemplate template, List<CodegenOperation> codegenOperations,
                                  List<CodegenModel> allModels) {
        super(template, codegenOperations, allModels);
    }

    public PhpApiResourceBuilder(IApiActionTemplate template, List<CodegenOperation> codegenOperations, List<CodegenModel> allModels,
                                 Map<String, Boolean> toggleMap, Resolver<CodegenProperty> codegenPropertyResolver) {
        this(template, codegenOperations, allModels);
        this.codegenPropertyIResolver = codegenPropertyResolver;
        this.toggleMap = toggleMap;
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
            // if any operation in current op list(CRUDF) has application/json request body type
            if (!nestedModels.isEmpty())
                template.add(PhpApiActionTemplate.TEMPLATE_TYPE_MODELS);
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
                List<Resource> dependents = new ArrayList<>();
                String operationPath = PathUtils.removeFirstPart(operation.path);

                for (Resource resource : directoryStructureService.getResourceTree().getResources()) {
                    Optional<String> twilioExtension = PathUtils.getTwilioExtension(resource.getPathItem(), "parent");

                    if (operationPath.equals(twilioExtension.orElse(null))) {
                        dependents.add(resource);
                    }
                }

                List<Resource> methodDependents = new ArrayList<>();

                for (Resource dep : dependents) {
                    Optional<String> twilioExtension = PathUtils.getTwilioExtension(dep.getPathItem(), "pathType");

                    if (twilioExtension.isPresent() && twilioExtension.get().equals("instance")) {
                        methodDependents.add(dep);
                    }
                }
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
        return path.replace("}", ")\n" + TAB_WHITESPACES + TAB_WHITESPACES + ".'");
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

    private List<CodegenParameter> filterPagingParams(List<CodegenParameter> queryParams) {
        List<CodegenParameter> nonPagingParams = new ArrayList<>();
        for (CodegenParameter param : queryParams) {
            if (!param.baseName.startsWith("Page")) {
                nonPagingParams.add(param);
            }
        }
        return nonPagingParams;
    }

    @Override
    public ApiResourceBuilder updateOperations(Resolver<CodegenParameter> codegenParameterIResolver) {
        PhpJsonRequestBodyResolver jsonRequestBodyResolver = new PhpJsonRequestBodyResolver(this, codegenPropertyIResolver);
        this.codegenOperationList.forEach(co -> {
            updateNestedContent(co);
            List<String> filePathArray = new ArrayList<>(Arrays.asList(co.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
            String resourceName = filePathArray.remove(filePathArray.size()-1);

            jsonRequestBodyResolver.setResourceName(resourceName);
            co.allParams.stream()
                    .forEach(item -> {
                        CodegenModel model = getModel(item.dataType);
                        // currently supporting required and conditional parameters only for request body object
                        if (model != null) {
                            model.vendorExtensions.put("x-constructor-required", true);
                            jsonRequestBodyResolver.resolve(item, codegenParameterIResolver);
                        }
                    });
            co.pathParams = co.pathParams.stream()
                    .map(item -> codegenParameterIResolver.resolve(item, this))
                    .collect(Collectors.toList());
            co.queryParams = co.queryParams.stream()
                    .map(item -> codegenParameterIResolver.resolve(item, this))
                    .collect(Collectors.toList());
            List<CodegenParameter> nonPagingParams = filterPagingParams(co.queryParams);
            if (!nonPagingParams.isEmpty()) {
                co.vendorExtensions.put("x-has-non-pagination-params", true);
            }
            co.formParams = co.formParams.stream()
                    .map(item -> codegenParameterIResolver.resolve(item, this))
                    .collect(Collectors.toList());
            co.headerParams = co.headerParams.stream()
                    .map(item -> codegenParameterIResolver.resolve(item, this))
                    .collect(Collectors.toList());
            co.optionalParams = co.optionalParams
                    .stream()
                    .map(item -> codegenParameterIResolver.resolve(item, this))
                    .collect(Collectors.toList());
            co.requiredParams = co.requiredParams
                    .stream()
                    .map(item -> codegenParameterIResolver.resolve(item, this))
                    .collect(Collectors.toList());

            if (co.vendorExtensions.containsKey("x-ignore")) {
                requiredPathParams.addAll(co.pathParams
                        .stream()
                        .filter(PathUtils::isParentParam)
                        .filter(codegenParameter -> requiredPathParams
                                .stream()
                                .noneMatch(param -> param.baseName.equals(codegenParameter.baseName)))
                        .collect(Collectors.toList()));
            } else {
                requiredPathParams.addAll(co.pathParams
                        .stream()
                        .filter(codegenParameter -> requiredPathParams
                                .stream()
                                .noneMatch(param -> param.baseName.equals(codegenParameter.baseName)))
                        .collect(Collectors.toList()));
            }

            co.hasParams = !co.allParams.isEmpty();
            co.hasRequiredParams = !co.requiredParams.isEmpty();

            co.vendorExtensions.putAll(mapOperation(co));

        });
        reorderParams(this);
        this.addOptionFileParams(this);
        categorizeOperations();
        return this;
    }

    @Override
    protected Map<String, Object> mapOperation(CodegenOperation operation) {
        if (StringUtils.startsWithIgnoreCase(operation.operationId, "patch")) {
            addOperationName(operation, EnumConstants.Operation.PATCH.getValue());
        }
        return super.mapOperation(operation);
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


    public IApiResourceBuilder addVersionLessTemplates(OpenAPI openAPI, DirectoryStructureService directoryStructureService) {
        if (directoryStructureService.isVersionLess()) {
            String version = PathUtils.getFirstPathPart(codegenOperationList.get(0).path);
            PhpDomainBuilder.setVersionTemplate(openAPI, directoryStructureService, version);
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
        List<CodegenModel> responseModels = new ArrayList<>();
        codegenOperationList.forEach(codegenOperation -> {
            codegenOperation.responses
                    .stream()
                    .filter(response -> SUCCESS.test(Integer.parseInt(response.code.trim())))
                    .map(response -> response.baseType)
                    .filter(Objects::nonNull)
                    .map(modelName -> this.getModel(modelName, codegenOperation))
                    .flatMap(Optional::stream)
                    .forEach(item -> {
                        item.allVars.stream().filter(var -> var.isModel && var.dataFormat == null).forEach(this::setDataFormatForNestedProperties);
                        item.vars.stream().filter(var -> var.isModel && var.dataFormat == null).forEach(this::setDataFormatForNestedProperties);
                        item.vars.forEach(e -> codegenPropertyResolver.resolve(e, this));
                        item.allVars.forEach(e -> codegenPropertyResolver.resolve(e, this));
                        responseModels.add(item);
                    });
        });
        this.apiResponseModels = getDistinctResponseModel(responseModels);
        return this;
    }

    private void setDataFormatForNestedProperties(CodegenProperty codegenProperty) {
        String ref = codegenProperty.getRef();
        if(ref == null)
            return;
        ref = ref.replaceFirst("#/components/schemas/", "");
        Optional<CodegenModel> model = this.getModelbyName(ref);
        model.ifPresent(item -> codegenProperty.dataFormat = item.getFormat());
    }
}
