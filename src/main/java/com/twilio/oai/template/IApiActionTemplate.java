package com.twilio.oai.template;

import org.openapitools.codegen.CodegenModel;

import java.util.Map;
import java.util.Set;

public interface IApiActionTemplate {
    void clean();
    void add(String template);
    void addSupportVersion();

    // Method for registering dynamic templates that generate multiple files
    void addDynamicTemplates(String templateType, Set<CodegenModel> models);

    // Method to generate the dynamic files
    void generateDynamicFiles(Map<String, Object> baseContext, String outputDir);
}
