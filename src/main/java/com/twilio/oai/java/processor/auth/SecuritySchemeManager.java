package com.twilio.oai.java.processor.auth;

import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenSecurity;

import java.util.List;

public class SecuritySchemeManager {
    public static SecuritySchemeManager instance;
    private final List<SecuritySchemeProcessor> securitySchemeProcessors;

    public static synchronized SecuritySchemeManager getInstance() {
        if (instance == null) {
            synchronized (SecuritySchemeManager.class) {
                if (instance == null) {
                    instance = new SecuritySchemeManager();
                }
            }
        }
        return instance;
    }

    private SecuritySchemeManager() {
        this.securitySchemeProcessors = List.of(
                new NoAuth(),
                new BasicAuth(),
                new OrgsOAuth()
        );
    }
    
    public void applyProcessor(CodegenOperation codegenOperation) {

        for (SecuritySchemeProcessor securitySchemeProcessor : securitySchemeProcessors) {
            if (securitySchemeProcessor.shouldProcess(codegenOperation)) {
                securitySchemeProcessor.process(codegenOperation);
            }
        }
    }
}
/*
ArrayList<CodegenSecurity> authMethods = (ArrayList) co.authMethods;
HashMap<String,String> authAttributes = new HashMap<>();
        if(authMethods == null){
            authAttributes.put(AUTH_IMPORT_CLASS, NOAUTH_IMPORT_CLASS);
            authAttributes.put(HTTP_CLASS_PREFIX, NOAUTH_HTTP_CLASS_PREFIX);
        }else{
            for(CodegenSecurity c : authMethods){
                if(c.isOAuth == true){
                    authAttributes.put(AUTH_IMPORT_CLASS, BEARER_AUTH_IMPORT_CLASS);
                    authAttributes.put(HTTP_CLASS_PREFIX, BEARER_AUTH_HTTP_CLASS_PREFIX);
                }
                else{
                    authAttributes.put(AUTH_IMPORT_CLASS, EMPTY_STRING);
                    authAttributes.put(HTTP_CLASS_PREFIX, EMPTY_STRING);
                }
            }
        }
        co.vendorExtensions.put("x-auth-attributes", authAttributes);
 */