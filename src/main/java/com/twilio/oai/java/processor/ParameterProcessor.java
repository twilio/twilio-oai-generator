package com.twilio.oai.java.processor;

import com.twilio.oai.java.feature.Inequality;
import com.twilio.oai.java.strategy.enums.parameter.EnumIdentifierHandler;
import org.openapitools.codegen.CodegenOperation;

public class ParameterProcessor implements Processor {

    private Inequality inequality = new Inequality();
    
    public static ParameterProcessor instance;
    EnumProcessor enumProcessor;
    private ParameterProcessor () {
        this.enumProcessor = EnumProcessor.getInstance();
    }
    
    public static synchronized ParameterProcessor getInstance() {
        if (instance == null) {
            instance = new ParameterProcessor();
        }
        return instance;
    }

    @Override
    public void process(final CodegenOperation codegenOperation) {
        codegenOperation.queryParams.forEach(param -> enumProcessor.process(param));
        codegenOperation.pathParams.forEach(param -> enumProcessor.process(param));
        codegenOperation.headerParams.forEach(param -> enumProcessor.process(param));
        codegenOperation.requiredParams.forEach(param -> enumProcessor.process(param));
        inequality.process(codegenOperation);
        
    }

    @Override
    public int getPriority() {
        return Processor.DEFAULT_PRIORITY;
    }
}
