package com.twilio.oai.template;

import com.twilio.oai.java.cache.ResourceCacheContext;
import org.openapitools.codegen.CodegenConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CsharpApiActionTemplate extends AbstractApiActionTemplate {

    public static final String TEMPLATE_TYPE_OPTIONS = "Options";
    public static final String TEMPLATE_TYPE_RESOURCE = "Resource";

    public CsharpApiActionTemplate(CodegenConfig defaultCodegen) {
        super(defaultCodegen);
    }

    @Override
    public Map<String, List<String>> mapping() {
        if (ResourceCacheContext.get().isV1()) {
            return Map.of(TEMPLATE_TYPE_RESOURCE,
                    Arrays.asList(TEMPLATE_TYPE_RESOURCE + "_standard.mustache", "Resource.cs"),
                    TEMPLATE_TYPE_OPTIONS,
                    Arrays.asList(TEMPLATE_TYPE_OPTIONS + ".mustache", "Options.cs"));
        } else {
            return Map.of(TEMPLATE_TYPE_RESOURCE,
                    Arrays.asList(TEMPLATE_TYPE_RESOURCE + ".mustache", "Resource.cs"),
                    TEMPLATE_TYPE_OPTIONS,
                    Arrays.asList(TEMPLATE_TYPE_OPTIONS + ".mustache", "Options.cs"));
        }
    }
}
