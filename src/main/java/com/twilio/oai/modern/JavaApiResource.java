package com.twilio.oai.modern;

import com.twilio.oai.java.nestedmodels.MustacheEnum;
import com.twilio.oai.java.nestedmodels.MustacheModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class JavaApiResource {
    String resourceName;
    String recordKey;
    Set<MustacheEnum> mustacheEnums;
    Set<MustacheModel> mustacheModels;
    List<CodegenOperation> operations;
    Set<CodegenProperty> response;
    
    public JavaApiResource(JavaApiResourceBuilderNew builder) {
        resourceName = ResourceCache.resourceName;
        recordKey = builder.recordKey;
        this.operations = builder.operations;
        this.mustacheEnums = new HashSet<>(ResourceCache.getEnumsClassesForMustache());
        this.mustacheModels = new HashSet<>(ResourceCache.getModelClassesForMustache());
        this.response = ResourceCache.response;
    }
}
