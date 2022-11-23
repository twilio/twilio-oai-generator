package com.twilio.oai.template;

import org.openapitools.codegen.CodegenConfig;

import java.util.*;

public class PhpApiActionTemplate implements IApiActionTemplate {
    public final static String TEMPLATE_TYPE_LIST = "list";
    public final static String TEMPLATE_TYPE_CONTEXT = "context";
    public final static String TEMPLATE_TYPE_OPTIONS = "options";
    public final static String TEMPLATE_TYPE_INSTANCE = "instance";
    public final static String TEMPLATE_TYPE_PAGE = "page";

    private final Map<String, List<String>> templates = mapping();
    CodegenConfig codegen ;

    public PhpApiActionTemplate(CodegenConfig defaultCodegen) {
        this.codegen = initialise(defaultCodegen);
    }

    private CodegenConfig initialise(CodegenConfig codegen) {
        codegen.apiTemplateFiles().clear();
        codegen.apiTestTemplateFiles().clear();
        codegen.modelTestTemplateFiles().clear();
        codegen.apiDocTemplateFiles().clear();
        codegen.modelDocTemplateFiles().clear();
        return codegen;
    }

    @Override
    public void clean() {
        for (Map.Entry<String, List<String>> entry: templates.entrySet()) {
            codegen.apiTemplateFiles().remove(entry.getKey());
        }
    }

    @Override
    public void add(String template) {
        List<String> strings = templates.get(template);
        codegen.apiTemplateFiles().put(strings.get(0), strings.get(1));
    }

    private Map<String, List<String>> mapping() {
        return Map.ofEntries(
                new AbstractMap.SimpleEntry<>(TEMPLATE_TYPE_LIST, Arrays.asList(TEMPLATE_TYPE_LIST+".mustache", "List.php")),
                new AbstractMap.SimpleEntry<>(TEMPLATE_TYPE_CONTEXT, Arrays.asList(TEMPLATE_TYPE_CONTEXT+".mustache", "Context.php")),
                new AbstractMap.SimpleEntry<>(TEMPLATE_TYPE_INSTANCE, Arrays.asList(TEMPLATE_TYPE_INSTANCE+".mustache", "Instance.php")),
                new AbstractMap.SimpleEntry<>(TEMPLATE_TYPE_OPTIONS, Arrays.asList(TEMPLATE_TYPE_OPTIONS+".mustache", "Options.php")),
                new AbstractMap.SimpleEntry<>(TEMPLATE_TYPE_PAGE, Arrays.asList(TEMPLATE_TYPE_PAGE+".mustache", "Page.php"))
        );
    }
}
