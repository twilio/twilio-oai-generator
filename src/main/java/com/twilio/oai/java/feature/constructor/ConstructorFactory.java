package com.twilio.oai.java.feature.constructor;

import org.openapitools.codegen.CodegenOperation;

import java.util.List;

public class ConstructorFactory {
    public static ConstructorFactory instance;

    public static synchronized ConstructorFactory getInstance() {
        if (instance == null) {
            synchronized (ConstructorFactory.class) {
                if (instance == null) {
                    instance = new ConstructorFactory();
                }
            }
        }
        return instance;
    }
    
    private final List<ConstructorGenerator> constructorGenerators;
    
    private ConstructorFactory() {
        constructorGenerators = List.of(
                //new JsonConstructorGenerator(),
                new UrlencodedBodyConstructorGenerator()
        );
    }
    
    public void applyFeature(CodegenOperation codegenOperation) {
        for (ConstructorGenerator constructorGenerator: constructorGenerators) {
            if (constructorGenerator.shouldApply(codegenOperation)) {
                constructorGenerator.apply(codegenOperation);
                return; // Exit after the first processor that applies
            }
        }
    }
}
