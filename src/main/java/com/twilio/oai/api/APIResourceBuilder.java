package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.HttpMethod;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.ISchemaResolver;
import com.twilio.oai.template.IAPIActionTemplate;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.*;
import java.util.stream.Collectors;

public class APIResourceBuilder implements IAPIResourceBuilder {
    public final static String API_OPERATION_READ = "Read";
    public final static String API_OPERATION_CREATE = "Create";
    public final static String API_OPERATION_FETCH = "Fetch";
    public final static String API_OPERATION_UPDATE = "Update";
    public final static String API_OPERATION_DELETE = "Delete";
    public final static String META_LIST_PARAMETER_KEY = "x-list-parameters";
    public final static String META_CONTEXT_PARAMETER_KEY = "x-context-parameters";

    protected IAPIActionTemplate phpTemplate;
    protected List<CodegenModel> allModels;
    protected List<CodegenOperation> codegenOperationList;
    protected TreeSet<CodegenParameter> requiredPathParams = new TreeSet<>((cp1, cp2) -> cp1.baseName.compareTo(cp2.baseName));
    protected  List<CodegenProperty> apiResponseModels = new ArrayList<>();
    protected Map<String, Object> metaAPIProperties = new HashMap<>();
    protected String version = "";
    protected String recordKey = "";

    public APIResourceBuilder(IAPIActionTemplate template, List<CodegenOperation> codegenOperations, List<CodegenModel> allModels) {
        this.phpTemplate = template;
        this.codegenOperationList = codegenOperations;
        this.allModels = allModels;
        this.recordKey = findRecordKey();
    }

    @Override
    public APIResourceBuilder operations(ISchemaResolver<CodegenParameter> codegenParameterIResolver) {
        this.codegenOperationList.stream().forEach(codegenOperation -> {
            codegenOperation.pathParams = codegenOperation.pathParams.stream().map(item ->
                    codegenParameterIResolver.resolve(item)).collect(Collectors.toList());
            codegenOperation.allParams = codegenOperation.allParams.stream().map(item ->
                    codegenParameterIResolver.resolve(item)).collect(Collectors.toList());
            codegenOperation.queryParams = codegenOperation.queryParams.stream().map(item ->
                    codegenParameterIResolver.resolve(item)).collect(Collectors.toList());
            codegenOperation.bodyParams = codegenOperation.bodyParams.stream().map(item ->
                    codegenParameterIResolver.resolve(item)).collect(Collectors.toList());
            codegenOperation.requiredParams = codegenOperation.requiredParams.stream()
                    .map(item -> codegenParameterIResolver.resolve(item)).collect(Collectors.toList());
            codegenOperation.optionalParams = codegenOperation.optionalParams.stream()
                    .map(item -> codegenParameterIResolver.resolve(item)).collect(Collectors.toList());

            requiredPathParams.addAll(codegenOperation.pathParams);
            codegenOperation.vendorExtensions = mapOperation(codegenOperation);
        });
        return this;
    }

    @Override
    public APIResourceBuilder responseModel(ISchemaResolver<CodegenProperty> codegenPropertyIResolver) {
        codegenOperationList.stream().forEach(codegenOperation -> {
            List<CodegenModel> responseModels = new ArrayList<>();
            codegenOperation.responses
                    .stream()
                    .map(response -> response.dataType)
                    .filter(Objects::nonNull)
                    .map(modelName -> this.getModel(modelName, codegenOperation, allModels))
                    .flatMap(Optional::stream)
                    .forEach(item -> {
                        item.vars = item.vars.stream().map(v ->
                                codegenPropertyIResolver.resolve(v)).collect(Collectors.toList());
                        item.allVars = item.allVars.stream().map(v ->
                                codegenPropertyIResolver.resolve(v)).collect(Collectors.toList());
                        responseModels.add(item);
                    });
            this.apiResponseModels = getDistinctResponseModel(responseModels);
        });
        return this;
    }

    @Override
    public APIResourceBuilder template() {
        return this;
    }

    @Override
    public APIResourceBuilder additionalProps(DirectoryStructureService directoryStructureService) {
        version = directoryStructureService.getApiVersionClass().get();
        return this;
    }

    @Override
    public APIResources build() {
        return new APIResources(this);
    }

    private Map<String, Object> mapOperation(CodegenOperation operation) {
        Map<String, Object> operationMap = new HashMap<>();
        final HttpMethod httpMethod = HttpMethod.fromString(operation.httpMethod);
        if (isInstanceOperation(operation)) {
            applyInstanceOperation(operation, operationMap, httpMethod);
        } else {
            applyListOperation(operation, operationMap, httpMethod);
        }

        return operationMap;
    }

    private void applyListOperation(CodegenOperation operation, Map<String, Object> operationMap, HttpMethod httpMethod) {
        if (httpMethod == HttpMethod.POST) {
            operationMap.put("x-name", API_OPERATION_CREATE);
            operationMap.put("x-is-create-operation", true);
        } else if (httpMethod == HttpMethod.GET) {
            operationMap.put("x-name", API_OPERATION_READ);
            operationMap.put("x-is-read-operation", true);
        }
        metaAPIProperties.put(META_LIST_PARAMETER_KEY, operation.allParams);
    }

    private void applyInstanceOperation(CodegenOperation operation, Map<String, Object> operationMap, HttpMethod httpMethod) {
        if (httpMethod == HttpMethod.GET) {
            operationMap.put("x-name", API_OPERATION_FETCH);
            operationMap.put("x-is-fetch-operation", true);
        } else if (httpMethod == HttpMethod.POST) {
            operationMap.put("x-name", API_OPERATION_UPDATE);
            operationMap.put("x-is-update-operation", true);
        } else if (httpMethod == HttpMethod.DELETE) {
            operationMap.put("x-name", API_OPERATION_DELETE);
            operation.vendorExtensions.put("x-is-delete-operation", true);
        }
        metaAPIProperties.put(META_CONTEXT_PARAMETER_KEY, operation.allParams);
    }

    private Optional<CodegenModel> getModel(final String modelName, CodegenOperation codegenOperation, List<CodegenModel> allModels) {
        String[] modelNames = modelName.split("\\\\");
        String extractedModelName = modelNames[modelNames.length - 1];
        if (codegenOperation.vendorExtensions.containsKey("x-is-read-operation") && (boolean) codegenOperation.vendorExtensions.get("x-is-read-operation")) {
            Optional<CodegenModel> coModel = allModels.stream().filter(model -> model.getClassname().equals(extractedModelName)).findFirst();
            if (coModel.isEmpty()) {
                return Optional.empty();
            }
            CodegenProperty property = coModel.get().vars.stream().filter(prop -> prop.baseName.equals(findRecordKey())).findFirst().get();
            return allModels.stream().filter(model -> model.getClassname().equals(property.complexType)).findFirst();
        }
        return allModels.stream().filter(model -> model.getClassname().equals(extractedModelName)).findFirst();
    }

    private List<CodegenProperty> getDistinctResponseModel(List<CodegenModel> responseModels) {
        Set<CodegenProperty> distinctResponseModels = new LinkedHashSet<>();
        for (CodegenModel codegenModel : responseModels) {
            for (CodegenProperty property : codegenModel.vars) {
                property.nameInCamelCase = StringHelper.camelize(property.nameInSnakeCase);
                if (Arrays
                        .stream(EnumConstants.Operation.values())
                        .anyMatch(value -> value.getValue().equals(property.nameInCamelCase))) {
                    property.nameInCamelCase = "_" + property.nameInCamelCase;
                }
                distinctResponseModels.add(property);
            }
        }
        return new LinkedList<>(distinctResponseModels);
    }

    protected boolean isInstanceOperation(CodegenOperation operation) {
        boolean is_fetch = operation.nickname.startsWith(EnumConstants.Operation.FETCH.getValue().toLowerCase(Locale.ROOT));
        boolean is_update = operation.nickname.startsWith(EnumConstants.Operation.UPDATE.getValue().toLowerCase(Locale.ROOT));
        boolean is_delete = operation.nickname.startsWith(EnumConstants.Operation.DELETE.getValue().toLowerCase(Locale.ROOT));
        if(is_fetch || is_update || is_delete) {
            return true;
        }
        return false;
    }

    private String findRecordKey() {
        String recordKey = "";
        for (CodegenOperation co : this.codegenOperationList) {
            for (CodegenModel model : allModels) {
                if (co.returnType == null) {
                    continue;
                }
                String[] split = co.returnType.split("\\\\");
                String returnTypeStr = split[split.length-1];
                if (model.name.equals(returnTypeStr)) {
                    recordKey = model.allVars
                            .stream()
                            .filter(v -> v.openApiType.equals("array"))
                            .collect(Collectors.toList()).get(0).baseName;
                }
            }
        }
        return recordKey;
    }

}
