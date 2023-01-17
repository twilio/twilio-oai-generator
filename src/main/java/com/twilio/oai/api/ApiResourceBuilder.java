package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.PathUtils;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.template.IApiActionTemplate;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.*;
import java.util.stream.Collectors;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;
import static com.twilio.oai.common.EnumConstants.Operation;

public abstract class ApiResourceBuilder implements IApiResourceBuilder {
    public static final String META_LIST_PARAMETER_KEY = "x-list-parameters";
    public static final String META_CONTEXT_PARAMETER_KEY = "x-context-parameters";

    protected IApiActionTemplate template;
    protected List<CodegenModel> allModels;
    protected List<CodegenOperation> codegenOperationList;
    protected List<CodegenParameter> requiredPathParams = new ArrayList<>();
    protected Set<CodegenProperty> apiResponseModels = new LinkedHashSet<>();
    protected Map<String, Object> metaAPIProperties = new HashMap<>();
    protected String version = "";
    protected final String recordKey;
    protected String apiPath ="";
    protected String namespaceSubPart = "";

    protected ApiResourceBuilder(IApiActionTemplate template, List<CodegenOperation> codegenOperations, List<CodegenModel> allModels) {
        this.template = template;
        this.codegenOperationList = codegenOperations;
        this.allModels = allModels;
        this.recordKey = Utility.getRecordKey(allModels, codegenOperations);
    }

    @Override
    public ApiResourceBuilder updateOperations(Resolver<CodegenParameter> codegenParameterIResolver) {
        this.codegenOperationList.forEach(codegenOperation -> {
            codegenOperation.pathParams.forEach(codegenParameterIResolver::resolve);
            codegenOperation.allParams.forEach(codegenParameterIResolver::resolve);
            codegenOperation.queryParams.forEach(codegenParameterIResolver::resolve);
            codegenOperation.formParams.forEach(codegenParameterIResolver::resolve);
            codegenOperation.bodyParams.forEach(codegenParameterIResolver::resolve);
            codegenOperation.requiredParams.forEach(codegenParameterIResolver::resolve);
            codegenOperation.optionalParams.forEach(codegenParameterIResolver::resolve);

            if (codegenOperation.vendorExtensions.containsKey("x-ignore")) {
                requiredPathParams.addAll(codegenOperation.pathParams
                                              .stream()
                                              .filter(PathUtils::isParentParam)
                                              .filter(codegenParameter -> requiredPathParams
                                                  .stream()
                                                  .noneMatch(param -> param.baseName.equals(codegenParameter.baseName)))
                                              .collect(Collectors.toList()));
            } else {
                requiredPathParams.addAll(codegenOperation.pathParams
                                              .stream()
                                              .filter(codegenParameter -> requiredPathParams
                                                  .stream()
                                                  .noneMatch(param -> param.baseName.equals(codegenParameter.baseName)))
                                              .collect(Collectors.toList()));
            }

            codegenOperation.vendorExtensions.putAll(mapOperation(codegenOperation));
        });
        return this;
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
                        item.vars.forEach(codegenPropertyResolver::resolve);
                        item.allVars.forEach(codegenPropertyResolver::resolve);
                        responseModels.add(item);
                    });
            this.apiResponseModels.addAll(getDistinctResponseModel(responseModels));
        });
        return this;
    }

    @Override
    public ApiResourceBuilder updateAdditionalProps(DirectoryStructureService directoryStructureService) {
        version = directoryStructureService.getApiVersionClass().get();
        return this;
    }

    @Override
    public IApiResourceBuilder updateApiPath() {
        apiPath = codegenOperationList.get(0).path;
        return this;
    }

    @Override
    public ApiResources build() {
        return new ApiResources(this);
    }

    protected Map<String, Object> mapOperation(CodegenOperation operation) {
        final String operationType = Utility.populateCrudOperations(operation);
        metaAPIProperties.put(operationType, operation);

        Map<String, Object> operationMap = operation.vendorExtensions;
        if (PathUtils.isInstanceOperation(operation)) {
            applyInstanceOperation(operation, operationMap);
            operationMap.put("x-is-context-operation", "true");
        } else {
            applyListOperation(operation, operationMap);
            operationMap.put("x-is-list-operation", "true");
        }

        if (operation.operationId.startsWith("update")) {
            addOperationName(operation, Operation.UPDATE.getValue());
        } else if (operation.operationId.startsWith("delete")) {
            addOperationName(operation, Operation.DELETE.getValue());
        } else if (operation.operationId.startsWith("create")) {
            addOperationName(operation, Operation.CREATE.getValue());
        } else if (operation.operationId.startsWith("fetch")) {
            addOperationName(operation, Operation.FETCH.getValue());
        } else if (operation.operationId.startsWith("list")) {
            addOperationName(operation, Operation.READ.getValue());
        }

        operationMap.put("hasRequiredNonPathParams",
                (operation.requiredParams.stream().anyMatch(param -> !param.isPathParam)));
        operationMap.put("hasOptionalQueryParams",
                (operation.optionalParams.stream().anyMatch(param -> param.isQueryParam)));
        operationMap.put("hasOptionalHeaderParams",
                (operation.optionalParams.stream().anyMatch(param -> param.isHeaderParam)));
        operationMap.put("hasOptionalFormParams",
                (operation.optionalParams.stream().anyMatch(param-> param.isFormParam)));
        return operationMap;
    }

    private void applyListOperation(CodegenOperation operation, Map<String, Object> operationMap) {
        if(!operation.vendorExtensions.containsKey("x-ignore")) {
            operationMap.put("x-resource-name", getApiName() + "ListInstance");
            metaAPIProperties.put("x-is-list-operation", "true");
            metaAPIProperties.put(META_LIST_PARAMETER_KEY, operation.allParams);
        }
    }

    private void applyInstanceOperation(CodegenOperation operation, Map<String, Object> operationMap) {
        if(!operation.vendorExtensions.containsKey("x-ignore")) {
            operationMap.put("x-resource-name", getApiName() + "Context");
            metaAPIProperties.put("x-is-context-operation", "true");
            metaAPIProperties.put(META_CONTEXT_PARAMETER_KEY, operation.allParams);
        }
    }

    protected void addOperationName(final CodegenOperation operation, final String name) {
        operation.vendorExtensions.put("x-name", name);
        operation.vendorExtensions.put("x-name-lower", name.toLowerCase());
    }

    protected Optional<CodegenModel> getModel(final String className, final CodegenOperation codegenOperation) {
        return Utility.getModel(allModels, className, recordKey, codegenOperation);
    }

    protected Optional<CodegenModel> getModelByClassname(final String classname) {
        return Utility.getModelByClassname(allModels, classname);
    }
    protected Optional<CodegenModel> getModelbyName(final String name){
        return Utility.getModelByName(allModels, name);
    }

    protected void addModel(final Map<String, CodegenModel> models, final String complexType, final String dataType) {
        getModelByClassname(complexType != null ? complexType : dataType).ifPresent(model -> {
            if (models.putIfAbsent(model.getClassname(), model) == null) {
                model.getVars().forEach(property -> addModel(models, property.complexType, property.dataType));
            }
        });
    }

    protected Set<CodegenProperty> getDistinctResponseModel(List<CodegenModel> responseModels) {
        Set<CodegenProperty> distinctResponseModels = new LinkedHashSet<>();
        for (CodegenModel codegenModel : responseModels) {
            for (CodegenProperty property : codegenModel.vars) {
                property.nameInCamelCase = StringHelper.camelize(property.nameInSnakeCase);
                if (Arrays
                    .stream(Operation.values())
                    .anyMatch(value -> value.getValue().equals(property.nameInCamelCase))) {
                    property.nameInCamelCase = "_" + property.nameInCamelCase;
                }
                distinctResponseModels.add(property);
            }
        }
        return distinctResponseModels;
    }

    public boolean hasPaginationOperation() {
        return codegenOperationList.stream().anyMatch(co -> co.operationId.toLowerCase().startsWith("list"));
    }

    public String getApiName() {
        final List<String> filePathArray = new ArrayList<>(Arrays.asList(codegenOperationList.get(0).baseName.split(
            PATH_SEPARATOR_PLACEHOLDER)));
        return filePathArray.remove(filePathArray.size() - 1);
    }
}
