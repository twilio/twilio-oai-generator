package com.twilio.oai.java.strategy.enums.property;

import com.twilio.oai.common.EnumConstants;
import org.openapitools.codegen.CodegenProperty;

public interface PropertyEnumProcessingStrategy {
    boolean process(CodegenProperty codegenProperty);
    EnumConstants.OpenApiEnumType getType();

    boolean isStrategyApplicable(CodegenProperty codegenProperty);
}
