package com.twilio.oai.template;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openapitools.codegen.CodegenConfig;

public class PhpApiActionTemplate extends AbstractApiActionTemplate {
    public static final String TEMPLATE_TYPE_LIST = "list";
    public static final String TEMPLATE_TYPE_CONTEXT = "context";
    public static final String TEMPLATE_TYPE_OPTIONS = "options";
    public static final String TEMPLATE_TYPE_INSTANCE = "instance";
    public static final String TEMPLATE_TYPE_PAGE = "page";

    public PhpApiActionTemplate(final CodegenConfig defaultCodegen) {
        super(defaultCodegen);
    }

    @Override
    protected Map<String, List<String>> mapping() {
        return Map.of(TEMPLATE_TYPE_LIST,
                      Arrays.asList(TEMPLATE_TYPE_LIST + ".mustache", "List.php"),
                      TEMPLATE_TYPE_CONTEXT,
                      Arrays.asList(TEMPLATE_TYPE_CONTEXT + ".mustache", "Context.php"),
                      TEMPLATE_TYPE_INSTANCE,
                      Arrays.asList(TEMPLATE_TYPE_INSTANCE + ".mustache", "Instance.php"),
                      TEMPLATE_TYPE_OPTIONS,
                      Arrays.asList(TEMPLATE_TYPE_OPTIONS + ".mustache", "Options.php"),
                      TEMPLATE_TYPE_PAGE,
                      Arrays.asList(TEMPLATE_TYPE_PAGE + ".mustache", "Page.php"),
                      VERSION_TEMPLATE,
                      Arrays.asList(VERSION_TEMPLATE + ".mustache", ".php"));
    }
}
