package com.twilio.oai.java.processor.oneOf;

import org.openapitools.codegen.CodegenModel;

public class OneOfProcessor {
    private static volatile OneOfProcessor instance;

    private OneOfProcessor() {
        // Private constructor to prevent instantiation
    }

    public static OneOfProcessor getInstance() {
        if (instance == null) {
            synchronized (OneOfProcessor.class) {
                if (instance == null) {
                    instance = new OneOfProcessor();
                }
            }
        }
        return instance;
    }
    
    void process(CodegenModel codegenModel) {
        
    }
}
