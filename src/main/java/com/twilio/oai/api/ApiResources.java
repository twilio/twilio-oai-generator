package com.twilio.oai.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.twilio.oai.java.cache.ResourceCacheContext;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.utils.StringUtils;

public class ApiResources {
    String apiName;
    String apiPath;
    String recordKey;
    String version;
    List<CodegenProperty> responseModels;
    List<CodegenParameter> requiredPathParams;
    List<CodegenOperation> apiOperations;
    Map<String, Object> metaProperties;
    String namespaceSubPart;
    boolean hasPaginationOperation;
    Set<CodegenModel> nestedModels;
    private Boolean isApiV1 = null; // true or NUll

    public ApiResources(ApiResourceBuilder apiResourceBuilder) {
        apiOperations = apiResourceBuilder.codegenOperationList;
        metaProperties = apiResourceBuilder.metaAPIProperties;
        apiName = apiResourceBuilder.getApiName();
        apiPath = apiResourceBuilder.apiPath;
        version = StringUtils.camelize(apiResourceBuilder.version);
        responseModels = new ArrayList<>(apiResourceBuilder.apiResponseModels);
        recordKey = apiResourceBuilder.recordKey;
        requiredPathParams = new ArrayList<>(apiResourceBuilder.requiredPathParams);
        namespaceSubPart = apiResourceBuilder.namespaceSubPart;
        hasPaginationOperation = apiResourceBuilder.hasPaginationOperation();
        nestedModels = apiResourceBuilder.nestedModels;
        if(ResourceCacheContext.get() != null && ResourceCacheContext.get().isV1()) isApiV1 = true;
    }
}
