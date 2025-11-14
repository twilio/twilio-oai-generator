package com.twilio.oai.java.processor.model;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

public interface ModelProcessor {
    boolean shouldProcess(CodegenProperty codegenProperty, CodegenModel codegenModel);
    void process(CodegenProperty codegenProperty, CodegenModel codegenModel);
}
