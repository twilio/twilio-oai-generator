package com.twilio.oai.modern;

import com.twilio.oai.java.nestedmodels.MustacheEnum;

import java.util.Set;

public class JavaApiResource {
    String resourceName;
    Set<MustacheEnum> mustacheEnums;
    
    public JavaApiResource(JavaApiResourceBuilderNew builder) {
        resourceName = ResourceCache.resourceName;
        this.mustacheEnums = ResourceCache.getEnumsClassesForMustache();
    }
}
