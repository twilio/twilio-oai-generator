package com.twilio.oai.api;

import com.twilio.oai.java.cache.ResourceCacheContext;
import org.openapitools.codegen.CodegenParameter;

import java.util.ArrayList;
import java.util.List;

public class RubyApiResources extends FluentApiResources {

    private List<CodegenParameter> readParams;
    List<String[]> parentDir = new ArrayList<>();
    boolean hasParents = false;
    private Boolean isApiV1 = null;

    public RubyApiResources(RubyApiResourceBuilder apiResourceBuilder) {
        super(apiResourceBuilder);
        this.readParams = apiResourceBuilder.readParams;
        this.parentDir = apiResourceBuilder.parentDir;
        this.hasParents = apiResourceBuilder.hasParents;
        if (ResourceCacheContext.get() != null && ResourceCacheContext.get().isV1()) {
            isApiV1 = true;
        }
    }
}
