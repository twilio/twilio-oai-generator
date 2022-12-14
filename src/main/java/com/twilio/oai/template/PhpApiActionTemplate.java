package com.twilio.oai.template;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openapitools.codegen.CodegenConfig;
import org.openapitools.codegen.SupportingFile;

public class PhpApiActionTemplate implements IApiActionTemplate {
    public static final String TEMPLATE_TYPE_LIST = "list";
    public static final String TEMPLATE_TYPE_CONTEXT = "context";
    public static final String TEMPLATE_TYPE_OPTIONS = "options";
    public static final String TEMPLATE_TYPE_INSTANCE = "instance";
    public static final String TEMPLATE_TYPE_PAGE = "page";
    public static final String TEMPLATE_TYPE_VERSION = "version";

    public static final Map<String, List<String>> templates = mapping();
    CodegenConfig codegen;

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
        for (Map.Entry<String, List<String>> entry : templates.entrySet()) {
            codegen.apiTemplateFiles().remove(entry.getKey());
        }
    }

    @Override
    public void add(String template) {
        List<String> templateStrings = templates.get(template);
        codegen.apiTemplateFiles().put(templateStrings.get(0), templateStrings.get(1));
    }

    @Override
    public void addSupportVersion() {
        List<String> templateStrings = templates.get(TEMPLATE_TYPE_VERSION);
        String apiVersionClass = codegen.additionalProperties().get("apiVersionClass").toString();
        if (apiVersionClass != null)
            if (apiVersionClass.startsWith("V")) {
                codegen.supportingFiles().add(new SupportingFile(templateStrings.get(0),
                        ".." + File.separator +
                                apiVersionClass +
                                templateStrings.get(1)));
            } else {
                codegen.apiTemplateFiles().put(templateStrings.get(0), templateStrings.get(1));
                codegen.apiFilename(templateStrings.get(0), apiVersionClass);
            }
    }

    private static Map<String, List<String>> mapping() {
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
                      TEMPLATE_TYPE_VERSION,
                      Arrays.asList(TEMPLATE_TYPE_VERSION + ".mustache", ".php"));
    }
}
