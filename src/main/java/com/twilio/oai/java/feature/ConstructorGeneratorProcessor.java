package com.twilio.oai.java.feature;

import com.twilio.oai.java.factory.ConstructorFactory;
import com.twilio.oai.java.processor.Processor;
import org.openapitools.codegen.CodegenOperation;

public class ConstructorGeneratorProcessor implements Processor {
    public static ConstructorGeneratorProcessor instance;
    ConstructorFactory constructorFactory;

    private ConstructorGeneratorProcessor () {
        constructorFactory = new ConstructorFactory();
    }

    public static synchronized ConstructorGeneratorProcessor getInstance() {
        if (instance == null) {
            instance = new ConstructorGeneratorProcessor();
        }
        return instance;
    }
    
    @Override
    public void process(CodegenOperation codegenOperation) {
        Processor constructorGenerator = constructorFactory.getConstructorGenerator(codegenOperation);
        if (constructorGenerator != null) {
            constructorGenerator.process(codegenOperation);
        }
    }

    @Override
    public int getPriority() {
        return Processor.DEFAULT_PRIORITY + 100;
    }
}
