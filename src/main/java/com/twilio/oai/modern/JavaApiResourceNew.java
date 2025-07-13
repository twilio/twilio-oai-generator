package com.twilio.oai.modern;

import com.twilio.oai.java.nestedmodels.MustacheEnum;

import java.util.List;
import java.util.Set;

public class JavaApiResourceNew {
    String resourceName;
    Set<MustacheEnum> mustacheEnums;
    
    public JavaApiResourceNew(JavaApiResourceBuilderNew javaApiResourceBuilderNew) {
        resourceName = ResourceCache.resourceName;
        this.mustacheEnums = ResourceCache.getEnumsClassesForMustache();
    }
}
