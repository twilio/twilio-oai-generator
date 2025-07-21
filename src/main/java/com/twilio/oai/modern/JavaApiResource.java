package com.twilio.oai.modern;

import com.twilio.oai.java.nestedmodels.MustacheEnum;
import org.openapitools.codegen.CodegenOperation;

import java.util.List;
import java.util.Set;

public class JavaApiResource {
    String resourceName;
    Set<MustacheEnum> mustacheEnums;
    
    List<CodegenOperation> operations;
    
    public JavaApiResource(JavaApiResourceBuilderNew builder) {
        resourceName = ResourceCache.resourceName;
        this.operations = builder.operations;
        this.mustacheEnums = ResourceCache.getEnumsClassesForMustache();
    }
}
