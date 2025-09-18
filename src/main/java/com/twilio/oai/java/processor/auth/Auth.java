package com.twilio.oai.java.processor.auth;

import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenSecurity;

import java.util.HashMap;
import java.util.List;

import static com.twilio.oai.resolver.java.JavaConventionResolver.AUTH_IMPORT_CLASS;
import static com.twilio.oai.resolver.java.JavaConventionResolver.HTTP_CLASS_PREFIX;

public class Auth implements SecuritySchemeProcessor {
    @Override
    public void process(CodegenOperation codegenOperation) {
    }

    @Override
    public boolean shouldProcess(CodegenOperation codegenOperation) {
        List<CodegenSecurity> securities = codegenOperation.authMethods;
        if (securities == null || securities.isEmpty()) return false; // No Authentication
        return true;
//        if (securities.size() > 1) {
//            throw new RuntimeException("Multiple auth methods are not supported. Please use only one auth method per operation.");
//        }
    }
}
