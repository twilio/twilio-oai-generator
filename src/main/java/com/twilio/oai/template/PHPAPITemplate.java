package com.twilio.oai.template;

import org.openapitools.codegen.CodegenConfig;
import org.openapitools.codegen.SupportingFile;

import java.io.File;
import java.util.*;

public class PHPAPITemplate implements ITemplate {
    public final static String TEMPLATE_TYPE_LIST = "list";
    public final static String TEMPLATE_TYPE_CONTEXT = "context";
    public final static String TEMPLATE_TYPE_OPTIONS = "options";
    public final static String TEMPLATE_TYPE_INSTANCE = "instance";
    public final static String TEMPLATE_TYPE_PAGE = "page";
    public final static String TEMPLATE_TYPE_VERSION = "version";

    private final Map<String, List<String>> templates = mapping();
    CodegenConfig codegen ;

    public PHPAPITemplate(CodegenConfig defaultCodegen) {
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
        List<String> templateStrings = templates.get(template);
        if(template=="version"){
            addVersionTemplate(templateStrings);
            return;
        }
        codegen.apiTemplateFiles().put(templateStrings.get(0), templateStrings.get(1));
    }

    @Override
    public void addContextResources(HashMap<String ,List<Object>> contextResourcesMap){
        codegen.additionalProperties().put("contextResources",contextResourcesMap);
    }
    private void addVersionTemplate(List<String> templateStrings) {
        String apiVersionClass=codegen.additionalProperties().get("apiVersionClass").toString();
        if(apiVersionClass!=null)
            codegen.supportingFiles().add(new SupportingFile(templateStrings.get(0),
                    ".." + File.separator +
                            apiVersionClass +
                            templateStrings.get(1)));
    }

    private Map<String, List<String>> mapping() {
        return Map.ofEntries(
                new AbstractMap.SimpleEntry<>("list", Arrays.asList("list.mustache", "List.php")),
                new AbstractMap.SimpleEntry<>("context", Arrays.asList("context.mustache", "Context.php")),
                new AbstractMap.SimpleEntry<>("instance", Arrays.asList("instance.mustache", "Instance.php")),
                new AbstractMap.SimpleEntry<>("options", Arrays.asList("options.mustache", "options.php")),
                new AbstractMap.SimpleEntry<>("page", Arrays.asList("page.mustache", "Page.php")),
                new AbstractMap.SimpleEntry<>("version", Arrays.asList("version.mustache", ".php"))
        );
    }
}
