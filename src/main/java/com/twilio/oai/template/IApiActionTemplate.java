package com.twilio.oai.template;

import java.util.Map;

public interface IApiActionTemplate {
    void clean();
    void add(String template);
    void addSupportVersion();

    // Method for registering dynamic templates that generate multiple files from apiResource
    void addDynamicTemplates(String templateType, Object apiResource);

    // Method to generate the dynamic files
    void generateDynamicFiles(Map<String, Object> baseContext, String outputDir);
}
