package com.twilio.oai.api;

import com.twilio.oai.resolver.csharp_new.OperationCache;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsharpApiResources extends ApiResources {
    public CodegenModel responseModel;
    List<IJsonSchemaValidationProperties> enums;
    public boolean hasEnumsInOptions;
    public boolean hasEnumsInResource;
    public boolean hasArrayInResource;
    public String resourceConstant="Resource";
    public Map<String, CsharpOperationApiResources> operationApi; // Composition
    public Map<String, List<CodegenParameter>> requestBodyParams;

    public CsharpApiResources(CsharpApiResourceBuilder apiResourceBuilder) {
        super(apiResourceBuilder);
        this.responseModel = apiResourceBuilder.responseModel;
        this.requestBodyParams = apiResourceBuilder.requestBodyParams;
        enums = new ArrayList<>(OperationCache.enums.values());
        operationApi = new HashMap<>(apiResourceBuilder.operationApi);
    }
}
