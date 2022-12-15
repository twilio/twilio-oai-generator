package com.twilio.oai.template;

import org.openapitools.codegen.CodegenConfig;

import java.util.List;
import java.util.Map;

public abstract class AbstractApiActionTemplate implements IApiActionTemplate {
    public static final String API_TEMPLATE = "api";
    public static final String VERSION_TEMPLATE = "version";

    private final Map<String, List<String>> templates = mapping();
    protected final CodegenConfig codegen;

    protected AbstractApiActionTemplate(CodegenConfig defaultCodegen) {
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
        for (final List<String> entry: templates.values()) {
            codegen.apiTemplateFiles().remove(entry.get(0));
        }
    }

    @Override
    public void add(String template) {
        List<String> templateStrings = templates.get(template);
        codegen.apiTemplateFiles().put(templateStrings.get(0), templateStrings.get(1));
    }
    protected abstract Map<String, List<String>> mapping();
}
