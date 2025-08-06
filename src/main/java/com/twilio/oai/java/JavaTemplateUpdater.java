package com.twilio.oai.java;

import com.twilio.oai.TwilioJavaGeneratorModern;
import com.twilio.oai.common.EnumConstants;
import org.openapitools.codegen.CodegenOperation;

import java.util.AbstractMap;
import java.util.Map;

import static com.twilio.oai.java.MustacheConstants.ActionMethod;
import static com.twilio.oai.java.MustacheConstants.ActionType;

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
                "list", new AbstractMap.SimpleEntry<>("reader.mustache", "Reader.java"),
                "update", new AbstractMap.SimpleEntry<>("updater.mustache", "Updater.java")
        );
        apiTemplate = Map.of(
                API_TEMPLATE, new AbstractMap.SimpleEntry<>("api.mustache", ".java")
        );
//        Map<String, AbstractMap.SimpleEntry> nestedModelTemplate = Map.of(
//                NESTED_MODELS, new AbstractMap.SimpleEntry<>("models.mustache", "Model.java")
//        );
    }

    public void addApiTemplate(TwilioJavaGeneratorModern twilioJavaGenerator, java.util.List<CodegenOperation> operations) {
        clearApiTemplate(twilioJavaGenerator);
        for (CodegenOperation operation : operations) {
            String operationId = operation.operationId;
            if (operationId == null || operationId.isEmpty()) {
                throw new RuntimeException("Operation ID cannot be null or empty for path: " + operation.path);
            }
            if (Create.isCreate(operation)) {
                Create.add(twilioJavaGenerator, operation, apiOperationTemplate);
            } else if (List.isList(operation)) {
                List.add(twilioJavaGenerator, operation, apiOperationTemplate);
            } else if (Update.isUpdate(operation)) {
                Update.add(twilioJavaGenerator, operation, apiOperationTemplate);
            } else if (Delete.isDelete(operation)) {
                Delete.add(twilioJavaGenerator, operation, apiOperationTemplate);
            } else if (Fetch.isFetch(operation)) {
                Fetch.add(twilioJavaGenerator, operation, apiOperationTemplate);
            } else {
                throw new RuntimeException("Unsupported operation type for operationId: " + operationId);
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

class Create {
    public static void add(TwilioJavaGeneratorModern twilioJavaGenerator, CodegenOperation codegenOperation, Map<String, AbstractMap.SimpleEntry> apiOperationTemplate) {
        codegenOperation.vendorExtensions.put(EnumConstants.SupportedOperation.X_CREATE.getValue(), true);
        String key = (String) apiOperationTemplate.get("create").getKey();
        String value = (String) apiOperationTemplate.get("create").getValue();
        twilioJavaGenerator.apiTemplateFiles().put(key, value);

        codegenOperation.vendorExtensions.put(MustacheConstants.ACTION_TYPE, ActionType.CREATOR.getValue());
        codegenOperation.vendorExtensions.put(MustacheConstants.ACTION_METHOD, ActionMethod.CREATE.getValue());
    }
    public static boolean isCreate(CodegenOperation codegenOperation) {
        return codegenOperation.operationId.toLowerCase().startsWith("create");
    }
}

class List {
    public static void add(TwilioJavaGeneratorModern twilioJavaGenerator, CodegenOperation codegenOperation, Map<String, AbstractMap.SimpleEntry> apiOperationTemplate) {
        codegenOperation.vendorExtensions.put(EnumConstants.SupportedOperation.X_LIST.getValue(), true);
        String key = (String) apiOperationTemplate.get("list").getKey();
        String value = (String) apiOperationTemplate.get("list").getValue();
        twilioJavaGenerator.apiTemplateFiles().put(key, value);
        codegenOperation.vendorExtensions.put(MustacheConstants.X_IS_LIST_OP, true);

        codegenOperation.vendorExtensions.put(MustacheConstants.ACTION_TYPE, ActionType.READER.getValue());
        codegenOperation.vendorExtensions.put(MustacheConstants.ACTION_METHOD, ActionMethod.READ.getValue());
    }
    public static boolean isList(CodegenOperation codegenOperation) {
        return codegenOperation.operationId.toLowerCase().startsWith("list");
    }
}

class Update {
    public static void add(TwilioJavaGeneratorModern twilioJavaGenerator, CodegenOperation codegenOperation, Map<String, AbstractMap.SimpleEntry> apiOperationTemplate) {
        codegenOperation.vendorExtensions.put(EnumConstants.SupportedOperation.X_UPDATE.getValue(), true);
        String key = (String) apiOperationTemplate.get("update").getKey();
        String value = (String) apiOperationTemplate.get("update").getValue();
        twilioJavaGenerator.apiTemplateFiles().put(key, value);

        codegenOperation.vendorExtensions.put(MustacheConstants.ACTION_TYPE, ActionType.UPDATER.getValue());
        codegenOperation.vendorExtensions.put(MustacheConstants.ACTION_METHOD, ActionMethod.UPDATE.getValue());
    }
    public static boolean isUpdate(CodegenOperation codegenOperation) {
        return codegenOperation.operationId.toLowerCase().startsWith("update");
    }
}

class Delete {
    public static void add(TwilioJavaGeneratorModern twilioJavaGenerator, CodegenOperation codegenOperation, Map<String, AbstractMap.SimpleEntry> apiOperationTemplate) {
        codegenOperation.vendorExtensions.put(EnumConstants.SupportedOperation.X_DELETE.getValue(), true);
        String key = (String) apiOperationTemplate.get("delete").getKey();
        String value = (String) apiOperationTemplate.get("delete").getValue();
        twilioJavaGenerator.apiTemplateFiles().put(key, value);

        codegenOperation.vendorExtensions.put(MustacheConstants.ACTION_TYPE, ActionType.DELETER.getValue());
        codegenOperation.vendorExtensions.put(MustacheConstants.ACTION_METHOD, ActionMethod.DELETE.getValue());
    }
    public static boolean isDelete(CodegenOperation codegenOperation) {
        return codegenOperation.operationId.toLowerCase().startsWith("delete");
    }
}

class Fetch {
    public static void add(TwilioJavaGeneratorModern twilioJavaGenerator, CodegenOperation codegenOperation, Map<String, AbstractMap.SimpleEntry> apiOperationTemplate) {
        codegenOperation.vendorExtensions.put(EnumConstants.SupportedOperation.X_FETCH.getValue(), true);
        String key = (String) apiOperationTemplate.get("fetch").getKey();
        String value = (String) apiOperationTemplate.get("fetch").getValue();
        twilioJavaGenerator.apiTemplateFiles().put(key, value);

        codegenOperation.vendorExtensions.put(MustacheConstants.ACTION_TYPE, ActionType.FETCHER.getValue());
        codegenOperation.vendorExtensions.put(MustacheConstants.ACTION_METHOD, ActionMethod.FETCH.getValue());
    }
    public static boolean isFetch(CodegenOperation codegenOperation) {
        return codegenOperation.operationId.toLowerCase().startsWith("fetch");
    }
}