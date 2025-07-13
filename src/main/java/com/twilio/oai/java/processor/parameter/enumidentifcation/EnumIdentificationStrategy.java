package com.twilio.oai.java.processor.parameter.enumidentifcation;

import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
import org.openapitools.codegen.CodegenParameter;

public interface EnumIdentificationStrategy {
    boolean identify(CodegenParameter codegenParameter);
    OpenApiEnumType getType();
}
