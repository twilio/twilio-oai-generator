package com.twilio.oai.api;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class APIResources {
    private String apiName;
    private String path;
    private String recordKey;
    private String version;
    private List<CodegenProperty> responseModels = new ArrayList<>();
    private List<CodegenOperation> apiOperations = new ArrayList<>();
    private List<CodegenParameter> requiredPathParams = new ArrayList<>();
    private Map<String, Object> metaProperties = new HashMap();

    public APIResources(List<CodegenOperation> apiOperations, List<CodegenModel> allModels) {
        this.apiOperations = apiOperations;
        this.recordKey = getRecordKey(apiOperations, allModels);
    }

    public void setRecordKey(String recordKey) {
        this.recordKey = recordKey;
    }

    public List<CodegenProperty> getResponseModels() {
        return responseModels;
    }

    public void setResponseModels(List<CodegenProperty> responseModels) {
        this.responseModels = responseModels;
    }

    public void setApiOperations(List<CodegenOperation> apiOperations) {
        this.apiOperations = apiOperations;
    }

    public List<CodegenOperation> getApiOperations() {
        return apiOperations;
    }

    public String getRecordKey() {
        return recordKey;
    }

    public List<CodegenParameter> getRequiredPathParams() {
        return requiredPathParams;
    }

    public void setRequiredPathParams(List<CodegenParameter> requiredPathParams) {
        this.requiredPathParams.addAll(requiredPathParams);
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, Object> getMetaProperties() {
        return metaProperties;
    }

    public void setMetaProperties(Map<String, Object> metaProperties) {
        this.metaProperties = metaProperties;
    }

    private String getRecordKey(List<CodegenOperation> opList, List<CodegenModel> models) {
        String recordKey = "";
        for (CodegenOperation co : opList) {
            for (CodegenModel model : models) {
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
