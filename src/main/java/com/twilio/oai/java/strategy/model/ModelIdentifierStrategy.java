package com.twilio.oai.java.strategy.model;

import com.twilio.oai.common.EnumConstants;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

public interface ModelIdentifierStrategy {
    boolean identify(CodegenProperty codegenProperty, CodegenModel codegenModel);

    EnumConstants.ModelType getType();
}
