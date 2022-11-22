package com.twilio.oai.template;

import org.openapitools.codegen.CodegenConfig;

import java.util.*;

public class PHPAPIActionTemplate implements IAPIActionTemplate {
    public final static String TEMPLATE_TYPE_LIST = "list";
    public final static String TEMPLATE_TYPE_CONTEXT = "context";
    public final static String TEMPLATE_TYPE_OPTIONS = "options";
    public final static String TEMPLATE_TYPE_INSTANCE = "instance";
    public final static String TEMPLATE_TYPE_PAGE = "page";

    private final Map<String, List<String>> templates = mapping();
    CodegenConfig codegen ;

    public PHPAPIActionTemplate(CodegenConfig defaultCodegen) {
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
                new AbstractMap.SimpleEntry<>("list", Arrays.asList("list.mustache", "List.php")),
                new AbstractMap.SimpleEntry<>("context", Arrays.asList("context.mustache", "Context.php")),
                new AbstractMap.SimpleEntry<>("instance", Arrays.asList("instance.mustache", "Instance.php")),
                new AbstractMap.SimpleEntry<>("options", Arrays.asList("options.mustache", "Options.php")),
                new AbstractMap.SimpleEntry<>("page", Arrays.asList("page.mustache", "Page.php"))
        );
    }
}
