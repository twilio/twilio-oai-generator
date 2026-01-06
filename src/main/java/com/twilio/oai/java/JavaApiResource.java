package com.twilio.oai.java;

import com.twilio.oai.java.cache.ResourceCacheContext;
import com.twilio.oai.java.nestedmodels.MustacheEnum;
import com.twilio.oai.java.nestedmodels.MustacheModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaApiResource {
    String resourceName;
    String recordKey;
    Set<MustacheEnum> mustacheEnums;
    Set<MustacheModel> mustacheModels;
    List<CodegenOperation> operations;
    Set<CodegenProperty> response;
    Set<MustacheModel> responses;  // Multiple response classes for V1 APIs
    String namespaceSubPart;
    Boolean responseFlag = null; // true or NUll
    Boolean responsesFlag = null; // true or NUll - for multiple responses (V1 APIs)
    Boolean isApiV1 = null; // true or NUll

    public JavaApiResource(JavaApiResourceBuilder builder) {
        resourceName = ResourceCacheContext.get().getResourceName();
        recordKey = builder.recordKey;
        this.operations = builder.operations;
        this.mustacheEnums = new HashSet<>(ResourceCacheContext.get().getEnumsClassesForMustache());
        this.mustacheModels = new HashSet<>(ResourceCacheContext.get().getModelClassesForMustache());
        this.response = ResourceCacheContext.get().getResponse();
        this.responses = new HashSet<>(ResourceCacheContext.get().getResponses());
        if (response != null && !response.isEmpty()) responseFlag = true;
        if (responses != null && !responses.isEmpty()) responsesFlag = true;
        this.namespaceSubPart = builder.namespaceSubPart;
        if (ResourceCacheContext.get().isV1()) isApiV1 = true;
    }
}

