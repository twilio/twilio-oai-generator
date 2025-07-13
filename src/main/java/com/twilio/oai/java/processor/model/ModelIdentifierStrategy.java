package com.twilio.oai.java.processor.model;

import com.twilio.oai.common.EnumConstants;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

public interface ModelIdentifierStrategy {
    boolean identify(CodegenProperty codegenProperty);

    EnumConstants.ModelType getType();
}
