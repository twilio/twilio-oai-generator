package com.twilio.oai.java.feature.constructor;

import org.openapitools.codegen.CodegenOperation;

public interface ConstructorGenerator {
    void apply(CodegenOperation codegenOperation);
    boolean shouldApply(CodegenOperation codegenOperation);
}
