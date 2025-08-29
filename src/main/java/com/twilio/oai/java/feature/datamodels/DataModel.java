package com.twilio.oai.java.feature.datamodels;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;

public interface DataModel {
    void apply(CodegenModel codegenModel);
    boolean shouldApply(CodegenModel codegenModel);
}
