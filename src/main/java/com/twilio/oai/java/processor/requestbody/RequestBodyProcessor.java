package com.twilio.oai.java.processor.requestbody;

import org.openapitools.codegen.CodegenOperation;

public interface RequestBodyProcessor {
    void process(CodegenOperation codegenOperation);
    String getContentType();
    boolean shouldProcess(CodegenOperation codegenOperation);
}
