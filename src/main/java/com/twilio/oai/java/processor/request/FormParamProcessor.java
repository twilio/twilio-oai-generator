package com.twilio.oai.java.processor.request;

import org.openapitools.codegen.CodegenOperation;

public class FormParamProcessor implements RequestBodyProcessor {
    
    @Override
    public void process(CodegenOperation codegenOperation) {
        // Process the form parameters from the request body
        // This is a placeholder implementation, actual logic will depend on requirements
        return;
    }

    @Override
    public String getContentType() {
        return "application/x-www-form-urlencoded";
    }
}
