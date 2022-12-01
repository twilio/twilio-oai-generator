package com.twilio.oai.api;

import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.utils.StringUtils;

import java.util.*;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;

public class ApiResources {
    private String apiName;
    private String apiPath;
    private String recordKey;
    private String version;
    private List<CodegenProperty> responseModels = new ArrayList<>();
    private List<CodegenParameter> requiredPathParams = new ArrayList<>();
    private List<CodegenOperation> apiOperations = new ArrayList<>();
    private Map<String, Object> metaProperties = new HashMap();
    private String namespaceSubPart;

    public ApiResources(ApiResourceBuilder apiResourceBuilder) {
        apiOperations = apiResourceBuilder.codegenOperationList;
        metaProperties = apiResourceBuilder.metaAPIProperties;
        apiName = getName(apiOperations.get(0));
        apiPath = apiResourceBuilder.apiPath;
        version = StringUtils.camelize(apiResourceBuilder.version);
        responseModels = apiResourceBuilder.apiResponseModels;
        recordKey = apiResourceBuilder.recordKey;
        requiredPathParams = new ArrayList<>(apiResourceBuilder.requiredPathParams);
        namespaceSubPart = apiResourceBuilder.namespaceSubPart;
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
