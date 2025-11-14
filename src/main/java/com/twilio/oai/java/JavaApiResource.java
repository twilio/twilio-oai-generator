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
    String namespaceSubPart;

    Boolean responseFlag = null; // true or NUll
    
    public JavaApiResource(JavaApiResourceBuilder builder) {
        resourceName = ResourceCacheContext.get().getResourceName();
        recordKey = builder.recordKey;
        this.operations = builder.operations;
        this.mustacheEnums = new HashSet<>(ResourceCacheContext.get().getEnumsClassesForMustache());
        this.mustacheModels = new HashSet<>(ResourceCacheContext.get().getModelClassesForMustache());
        this.response = ResourceCacheContext.get().getResponse();
        if (response != null && !response.isEmpty()) responseFlag = true;
        this.namespaceSubPart = builder.namespaceSubPart;
    }
}

