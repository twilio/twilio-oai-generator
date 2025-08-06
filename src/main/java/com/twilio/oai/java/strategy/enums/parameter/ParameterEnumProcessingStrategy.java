package com.twilio.oai.java.strategy.enums.parameter;

import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
import org.openapitools.codegen.CodegenParameter;

public interface ParameterEnumProcessingStrategy {
    boolean process(CodegenParameter codegenParameter);
    OpenApiEnumType getType();
    
    boolean isStrategyApplicable(CodegenParameter codegenParameter);
}
