package com.twilio.oai.java.processor.auth;

import org.openapitools.codegen.CodegenOperation;

// Placeholder class, OAuth is not added to security schemes.
public class OAuth implements SecuritySchemeProcessor {

    @Override
    public void process(CodegenOperation codegenOperation) {
        // Implement the logic to process the CodegenOperation for OAuth
        // This could include setting up OAuth-specific parameters, headers, etc.
    }

    // Currently we are using same classes for OAuth and Basic Authentication, Thus no differentiation required.
    @Override
    public boolean shouldProcess(CodegenOperation codegenOperation) {
        return false;
    }
}
