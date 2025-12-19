package com.twilio.oai.template;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openapitools.codegen.CodegenConfig;

public class PhpApiActionTemplate extends AbstractApiActionTemplate {
    public static final String TEMPLATE_TYPE_LIST = "list";
    public static final String TEMPLATE_TYPE_CONTEXT = "context";
    public static final String TEMPLATE_TYPE_OPTIONS = "options";
    public static final String TEMPLATE_TYPE_INSTANCE = "instance";
    public static final String TEMPLATE_TYPE_MODELS = "models";
    public static final String TEMPLATE_TYPE_PAGE = "page";
    // New template type for individual instance classes (one file per response model)
    public static final String TEMPLATE_TYPE_INSTANCE_CLASS = "instanceClass";

    public PhpApiActionTemplate(final CodegenConfig defaultCodegen) {
        super(defaultCodegen);
    }

    @Override
    protected Map<String, List<String>> mapping() {
        Map<String, List<String>> map = new HashMap<>();
        map.put(TEMPLATE_TYPE_LIST, Arrays.asList(TEMPLATE_TYPE_LIST + ".mustache", "List.php"));
        map.put(TEMPLATE_TYPE_CONTEXT, Arrays.asList(TEMPLATE_TYPE_CONTEXT + ".mustache", "Context.php"));
        map.put(TEMPLATE_TYPE_INSTANCE, Arrays.asList(TEMPLATE_TYPE_INSTANCE + ".mustache", "Instance.php"));
        map.put(TEMPLATE_TYPE_OPTIONS, Arrays.asList(TEMPLATE_TYPE_OPTIONS + ".mustache", "Options.php"));
        map.put(TEMPLATE_TYPE_MODELS, Arrays.asList(TEMPLATE_TYPE_MODELS + ".mustache", "Models.php"));
        map.put(TEMPLATE_TYPE_PAGE, Arrays.asList(TEMPLATE_TYPE_PAGE + ".mustache", "Page.php"));
        map.put(VERSION_TEMPLATE, Arrays.asList(VERSION_TEMPLATE + ".mustache", ".php"));
        // Dynamic template for generating individual instance class files
        map.put(TEMPLATE_TYPE_INSTANCE_CLASS, Arrays.asList("instanceClass.mustache", "Instance.php"));
        return map;
    }
}
