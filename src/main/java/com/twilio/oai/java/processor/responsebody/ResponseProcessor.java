package com.twilio.oai.java.processor.responsebody;

import org.openapitools.codegen.CodegenOperation;

public interface ResponseProcessor {
    void process(CodegenOperation codegenOperation);
    String getContentType();
    boolean shouldProcess(CodegenOperation codegenOperation);
}
