package com.twilio.oai.java.processor;

import com.twilio.oai.java.feature.SetterMethodGenerator;
import com.twilio.oai.java.feature.constructor.ConstructorFactory;
import com.twilio.oai.java.processor.parameter.ParameterProcessor;
import com.twilio.oai.java.processor.requestbody.RequestBodyProcessorFactory;
import org.openapitools.codegen.CodegenOperation;

public class JavaOperationProcessor {
    
    public static JavaOperationProcessor instance;
    public static synchronized JavaOperationProcessor getInstance() {
        if (instance == null) {
            synchronized (JavaOperationProcessor.class) {
                if (instance == null) {
                    instance = new JavaOperationProcessor();
                }
            }
        }
        return instance;
    }
    
    private JavaOperationProcessor() {
        
    }
    
    public void process(final CodegenOperation codegenOperation) {
        ParameterProcessor.getInstance().process(codegenOperation);
        RequestBodyProcessorFactory.getInstance().process(codegenOperation);


        
        // All Features should be applied after processors are completed
        ConstructorFactory.getInstance().applyFeature(codegenOperation);
        SetterMethodGenerator.getInstance().apply(codegenOperation);
    }
}
