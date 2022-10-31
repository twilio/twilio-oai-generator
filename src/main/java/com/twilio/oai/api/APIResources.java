package com.twilio.oai.api;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.*;
import java.util.stream.Collectors;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;

public class APIResources {
    private String apiName;
    private String path;
    private String recordKey;
    private String version;
    private List<CodegenProperty> responseModels = new ArrayList<>();
    private List<CodegenParameter> requiredPathParams = new ArrayList<>();
    private List<CodegenOperation> apiOperations = new ArrayList<>();
    private Map<String, Object> metaProperties = new HashMap();

    public APIResources(APIResourceBuilder apiResourceBuilder) {
        apiOperations = apiResourceBuilder.codegenOperationList;
        metaProperties = apiResourceBuilder.metaAPIProperties;
        apiName = getName(apiOperations.get(0));
        path = apiOperations.get(0).path;
        version = apiResourceBuilder.version;
        responseModels = apiResourceBuilder.apiResponseModels;
        recordKey = apiResourceBuilder.recordKey;
        requiredPathParams = new ArrayList<>(apiResourceBuilder.requiredPathParams);
    }

    private String getName(CodegenOperation operation) {
        List<String> filePathArray = new ArrayList<>(Arrays.asList(operation.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
        String resourceName = filePathArray.remove(filePathArray.size() - 1);
        return resourceName;
    }

    public String getApiName() {
        return  this.apiName;
    }
}
