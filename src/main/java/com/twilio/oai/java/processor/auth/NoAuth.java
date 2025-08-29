package com.twilio.oai.java.processor.auth;

import org.openapitools.codegen.CodegenOperation;

import java.util.HashMap;

import static com.twilio.oai.resolver.java.JavaConventionResolver.AUTH_IMPORT_CLASS;
import static com.twilio.oai.resolver.java.JavaConventionResolver.HTTP_CLASS_PREFIX;
import static com.twilio.oai.resolver.java.JavaConventionResolver.NOAUTH_HTTP_CLASS_PREFIX;
import static com.twilio.oai.resolver.java.JavaConventionResolver.NOAUTH_IMPORT_CLASS;

public class NoAuth implements SecuritySchemeProcessor {

    @Override
    public void process(CodegenOperation codegenOperation) {
        HashMap<String,String> authAttributes = new HashMap<>();
        authAttributes.put(AUTH_IMPORT_CLASS, NOAUTH_IMPORT_CLASS);
        authAttributes.put(HTTP_CLASS_PREFIX, NOAUTH_HTTP_CLASS_PREFIX);
        codegenOperation.vendorExtensions.put(authAttributesExtension, authAttributes);
    }

    @Override
    public boolean shouldProcess(CodegenOperation codegenOperation) {
        // This processor is for operations that do not require authentication
        return true;
    }
}
