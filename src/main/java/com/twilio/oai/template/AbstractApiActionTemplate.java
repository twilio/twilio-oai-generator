package com.twilio.oai.template;

import org.openapitools.codegen.CodegenConfig;

import java.util.List;
import java.util.Map;

public abstract class AbstractApiActionTemplate implements IApiActionTemplate {
    private final Map<String, List<String>> templates = mapping();
    CodegenConfig codegen ;

    public AbstractApiActionTemplate(CodegenConfig defaultCodegen) {
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
            codegen.apiTemplateFiles().remove(entry.getValue().get(0));
        }
    }

    @Override
    public void add(String template) {
        List<String> templateStrings = templates.get(template);
        codegen.apiTemplateFiles().put(templateStrings.get(0), templateStrings.get(1));
    }
    public abstract Map<String, List<String>> mapping();

}
