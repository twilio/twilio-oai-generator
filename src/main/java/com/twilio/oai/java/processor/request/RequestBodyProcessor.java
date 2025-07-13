package com.twilio.oai.java.processor.request;

import org.openapitools.codegen.CodegenOperation;

public interface RequestBodyProcessor {
    void process(CodegenOperation codegenOperation);
    String getContentType();
}
