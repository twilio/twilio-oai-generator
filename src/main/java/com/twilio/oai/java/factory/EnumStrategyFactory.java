package com.twilio.oai.java.factory;

import com.twilio.oai.java.strategy.enums.parameter.ParameterEnumProcessingStrategy;
import com.twilio.oai.java.strategy.enums.parameter.FormParamInlineStrategy;
import com.twilio.oai.java.strategy.enums.parameter.FormParamListStrategy;
import com.twilio.oai.java.strategy.enums.parameter.FormParamListRefStrategy;
import com.twilio.oai.java.strategy.enums.parameter.FormParamRefStrategy;
import com.twilio.oai.java.strategy.enums.parameter.InlineParamStrategy;
import com.twilio.oai.java.strategy.enums.parameter.InlineListParamStrategy;
import com.twilio.oai.java.strategy.enums.parameter.RefParamStrategy;
import com.twilio.oai.java.strategy.enums.parameter.RefListParamStrategy;
import com.twilio.oai.java.strategy.enums.property.InlineListPropStrategy;
import com.twilio.oai.java.strategy.enums.property.InlinePropStrategy;
import com.twilio.oai.java.strategy.enums.property.PropertyEnumProcessingStrategy;
import com.twilio.oai.java.strategy.enums.property.RefListPropStrategy;
import com.twilio.oai.java.strategy.enums.property.RefPropStrategy;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;

public class EnumStrategyFactory {
    List<ParameterEnumProcessingStrategy> parameterStrategies = List.of(
            new InlineParamStrategy(),
            new InlineListParamStrategy(),
            new RefParamStrategy(),
            new RefListParamStrategy(),
            new FormParamInlineStrategy(),
            new FormParamListStrategy(),
            new FormParamListRefStrategy(),
            new FormParamRefStrategy()
    );

    List<PropertyEnumProcessingStrategy> propertyStrategies = List.of(
            new InlinePropStrategy(),
            new InlineListPropStrategy(),
            new RefPropStrategy(),
            new RefListPropStrategy()
    );

    public ParameterEnumProcessingStrategy getStrategy(CodegenParameter codegenParameter) {
        for (ParameterEnumProcessingStrategy strategy: parameterStrategies) {
            if (strategy.isStrategyApplicable(codegenParameter)) {
                codegenParameter.vendorExtensions.put(X_ENUM_TYPE, strategy.getType());
                return strategy;
            }
        }
        return null;
    }

    public PropertyEnumProcessingStrategy getStrategy(CodegenProperty codegenProperty) {
        for (PropertyEnumProcessingStrategy strategy: propertyStrategies) {
            if (strategy.isStrategyApplicable(codegenProperty)) {
                codegenProperty.vendorExtensions.put(X_ENUM_TYPE, strategy.getType());
                return strategy;
            }
        }
        return null;
    }
    
}
