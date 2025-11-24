package com.twilio.oai.java.processor.model.parameter;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;

public interface ModelProcessor {
    boolean shouldProcess(CodegenParameter codegenParameter, CodegenModel codegenModel);
    void process(CodegenParameter codegenParameter, CodegenModel codegenModel);
}
