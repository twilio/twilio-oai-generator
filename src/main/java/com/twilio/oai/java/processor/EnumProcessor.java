package com.twilio.oai.java.processor;

import com.twilio.oai.java.factory.EnumStrategyFactory;
import com.twilio.oai.java.strategy.enums.parameter.ParameterEnumProcessingStrategy;
import com.twilio.oai.java.strategy.enums.property.PropertyEnumProcessingStrategy;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public class EnumProcessor {
    private static EnumProcessor instance;
    private final EnumStrategyFactory strategyFactory;

    private EnumProcessor() {
        this.strategyFactory = new EnumStrategyFactory();
    }
    
    public static synchronized EnumProcessor getInstance() {
        if (instance == null) {
            instance = new EnumProcessor();
        }
        return instance;
    }

    public void process(CodegenParameter codegenParameter) {
        ParameterEnumProcessingStrategy strategy = strategyFactory.getStrategy(codegenParameter);
        strategy.process(codegenParameter);
    }
    
    public void process(CodegenProperty codegenProperty) {
        PropertyEnumProcessingStrategy strategy = strategyFactory.getStrategy(codegenProperty);
        strategy.process(codegenProperty);
    }
}
