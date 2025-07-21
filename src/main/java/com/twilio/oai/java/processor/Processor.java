package com.twilio.oai.java.processor;

import org.openapitools.codegen.CodegenOperation;

/*
    * This interface and classes implementing it are used to process CodegenOperation objects.
    * It allows for custom processing of operations in the OpenAPI code generation process.
    * Implementations can modify the operation, add additional information, or perform any other necessary processing.
    * Example implementations could include processing parameters, request bodies, or responses.
 */
public interface Processor {
    int DEFAULT_PRIORITY = 100;

    void process(CodegenOperation codegenOperation);

    int getPriority();
}
