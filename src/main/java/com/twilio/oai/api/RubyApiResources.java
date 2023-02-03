package com.twilio.oai.api;

import org.openapitools.codegen.CodegenParameter;

import java.util.List;

public class RubyApiResources extends FluentApiResources {

    private List<CodegenParameter> readParams;
    private List<Object> componentContextClasses;

    public RubyApiResources(RubyApiResourceBuilder apiResourceBuilder) {
        super(apiResourceBuilder);
        this.readParams = apiResourceBuilder.readParams;
        this.componentContextClasses = apiResourceBuilder.componentContextClasses;
    }
}
