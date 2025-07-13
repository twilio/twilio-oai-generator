package com.twilio.oai.java.processor.property;

import com.twilio.oai.common.EnumConstants;
import org.openapitools.codegen.CodegenProperty;

public interface PropertyIdentificationStrategy {
    boolean identify(CodegenProperty codegenProperty);
    EnumConstants.OpenApiEnumType getType();
}
