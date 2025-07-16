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
        if (codegenParameter.dataFormat != null && codegenParameter.dataFormat.equals("http-method")) {
            return;
        }
        ParameterEnumProcessingStrategy strategy = strategyFactory.getStrategy(codegenParameter);
        if (strategy != null)
            strategy.process(codegenParameter);
    }
    
    public void process(CodegenProperty codegenProperty) {
        if (codegenProperty.dataFormat != null && codegenProperty.dataFormat.equals("http-method")) {
            return;
        }
        PropertyEnumProcessingStrategy strategy = strategyFactory.getStrategy(codegenProperty);
        if (strategy != null)
            strategy.process(codegenProperty);
    }
}
