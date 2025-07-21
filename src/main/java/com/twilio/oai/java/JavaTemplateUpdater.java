package com.twilio.oai.java;

import com.twilio.oai.TwilioJavaGenerator;
import com.twilio.oai.TwilioJavaGeneratorModern;
import com.twilio.oai.common.EnumConstants;
import org.openapitools.codegen.CodegenOperation;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;


/*
The JavaTemplateFile class is responsible for managing template mappings for Java code generation. 
It defines mappings between operation IDs (starting strings) 
and corresponding template files (mustache files) along with their generated file extensions. 
Example:
    Key: Represents the starting string of the operationId (e.g., "create", "fetch").
    Value: Represents a mapping between the mustache template file and the generated file extension using AbstractMap.SimpleEntry.
Note: We don't creating mapping based in http method, we create mapping using operationId.
 */
public class JavaTemplateUpdater {
    public static final String API_TEMPLATE = "api.mustache";
    public static final String NESTED_MODELS = "models.mustache";

    Map<String, AbstractMap.SimpleEntry> apiOperationTemplate;
    Map<String, AbstractMap.SimpleEntry> apiTemplate;

    public JavaTemplateUpdater() {
        // Available templates for Java code generation.
        apiOperationTemplate = Map.of(
                "create", new AbstractMap.SimpleEntry<>("creator.mustache", "Creator.java"),
                "fetch", new AbstractMap.SimpleEntry<>("fetcher.mustache", "Fetcher.java"),
                "delete", new AbstractMap.SimpleEntry<>("deleter.mustache", "Deleter.java"),
                "read", new AbstractMap.SimpleEntry<>("reader.mustache", "Reader.java"),
                "update", new AbstractMap.SimpleEntry<>("updater.mustache", "Updater.java")
        );
        apiTemplate = Map.of(
                API_TEMPLATE, new AbstractMap.SimpleEntry<>("api.mustache", ".java")
        );
//        Map<String, AbstractMap.SimpleEntry> nestedModelTemplate = Map.of(
//                NESTED_MODELS, new AbstractMap.SimpleEntry<>("models.mustache", "Model.java")
//        );
    }

    public void addApiTemplate(TwilioJavaGeneratorModern twilioJavaGenerator, List<CodegenOperation> operations) {
        clearApiTemplate(twilioJavaGenerator);
        for (CodegenOperation operation : operations) {
            String operationId = operation.operationId;
            if (operationId == null || operationId.isEmpty()) {
                throw new RuntimeException("Operation ID cannot be null or empty for path: " + operation.path);
            }
            String lowerCaseOpId = operationId.toLowerCase();

            for (Map.Entry<String, AbstractMap.SimpleEntry> entry : apiOperationTemplate.entrySet()) {
                if (lowerCaseOpId.startsWith(entry.getKey())) {
                    operation.vendorExtensions.put(EnumConstants.SupportedOperation.X_CREATE.getValue(), true);
                    twilioJavaGenerator.apiTemplateFiles().put((String) entry.getValue().getKey(), (String) entry.getValue().getValue());
                    break; // break and continue to the next operation
                }
            }
        }
    }

    void clearApiTemplate(TwilioJavaGeneratorModern twilioJavaGenerator) {
        // Clear the existing templates
        twilioJavaGenerator.apiTemplateFiles().clear();
        // Add the default API template
        twilioJavaGenerator.apiTemplateFiles().put(API_TEMPLATE, ".java");
    }
}
