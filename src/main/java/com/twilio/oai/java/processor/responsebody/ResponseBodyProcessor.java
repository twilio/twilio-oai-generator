package com.twilio.oai.java.processor.responsebody;

import org.openapitools.codegen.CodegenOperation;

public interface ResponseBodyProcessor {
    void process(CodegenOperation codegenOperation);
    String getContentType();
}
