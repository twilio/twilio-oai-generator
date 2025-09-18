package com.twilio.oai.java.processor.auth;

import org.openapitools.codegen.CodegenOperation;

// Orgs OAuth and Basic Auth has different RestClients.
// Set "x-auth-attributes" for different type of authentication
public interface SecuritySchemeProcessor {
    String authAttributesExtension = "x-auth-attributes";
    String noAuth = "x-no-auth";
    void process(CodegenOperation codegenOperation);
    boolean shouldProcess(CodegenOperation codegenOperation);
}
