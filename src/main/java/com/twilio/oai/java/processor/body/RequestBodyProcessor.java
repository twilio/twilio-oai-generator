package com.twilio.oai.java.processor.body;

import org.openapitools.codegen.CodegenOperation;

public interface RequestBodyProcessor {
    void process(CodegenOperation codegenOperation);
    String getContentType();
}
