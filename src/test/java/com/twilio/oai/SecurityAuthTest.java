package com.twilio.oai;

import com.twilio.oai.api.CsharpApiResourceBuilder;
import com.twilio.oai.api.JavaApiResourceBuilder;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.java.JavaConventionResolver;
import org.junit.Test;
import org.openapitools.codegen.CodegenOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.twilio.oai.resolver.java.JavaConventionResolver.AUTH_IMPORT_CLASS;
import static com.twilio.oai.resolver.java.JavaConventionResolver.HTTP_CLASS_PREFIX;
import static com.twilio.oai.resolver.java.JavaConventionResolver.NOAUTH_HTTP_CLASS_PREFIX;
import static com.twilio.oai.resolver.java.JavaConventionResolver.NOAUTH_IMPORT_CLASS;
import static org.junit.Assert.assertEquals;

public class SecurityAuthTest {

    @Test
    public void testJavaResolverTreatsEmptyAuthMethodsAsNoAuth() {
        CodegenOperation operation = operationWithEmptyAuthMethods();
        Map<String, Object> vendorExtensions = new JavaConventionResolver().populateSecurityAttributes(operation);

        @SuppressWarnings("unchecked")
        Map<String, String> authAttributes = (Map<String, String>) vendorExtensions.get("x-auth-attributes");

        assertEquals(NOAUTH_IMPORT_CLASS, authAttributes.get(AUTH_IMPORT_CLASS));
        assertEquals(NOAUTH_HTTP_CLASS_PREFIX, authAttributes.get(HTTP_CLASS_PREFIX));
    }

    @Test
    public void testJavaResourceBuilderTreatsEmptyAuthMethodsAsNoAuth() {
        CodegenOperation operation = operationWithEmptyAuthMethods();
        JavaApiResourceBuilder builder = new JavaApiResourceBuilder(null, List.of(operation), List.of());

        builder.processAuthMethods(List.of(operation));

        assertEquals(NOAUTH_IMPORT_CLASS, builder.authMethodPackage);
    }

    @Test
    public void testCsharpResourceBuilderTreatsEmptyAuthMethodsAsNoAuth() {
        CodegenOperation operation = operationWithEmptyAuthMethods();
        CsharpApiResourceBuilder builder = new CsharpApiResourceBuilder(null, List.of(operation), List.of());

        builder.processAuthMethods(List.of(operation));

        assertEquals(EnumConstants.AuthType.NOAUTH.getValue(), builder.authMethod);
    }

    private CodegenOperation operationWithEmptyAuthMethods() {
        CodegenOperation operation = new CodegenOperation();
        operation.authMethods = new ArrayList<>();
        return operation;
    }
}
