package com.twilio.oai.api;

import org.openapitools.codegen.CodegenParameter;

import java.util.ArrayList;
import java.util.List;

public class RubyApiResources extends FluentApiResources {

    private List<CodegenParameter> readParams;
    List<String[]> parentDir = new ArrayList<>();
    boolean hasParents = false;

    public RubyApiResources(RubyApiResourceBuilder apiResourceBuilder) {
        super(apiResourceBuilder);
        this.readParams = apiResourceBuilder.readParams;
        this.parentDir = apiResourceBuilder.parentDir;
        this.hasParents = apiResourceBuilder.hasParents;
    }
}
