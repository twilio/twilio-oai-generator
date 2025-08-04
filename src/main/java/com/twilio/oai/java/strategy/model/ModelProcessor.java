package com.twilio.oai.java.strategy.model;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;

public class ModelProcessor {
    private final List<ModelIdentifierStrategy> modelIdentifierStrategies;
    public ModelProcessor() {
        modelIdentifierStrategies = List.of(
            new SingleModelStrategy(),
            new ListModelStrategy()
        );
    }
    
    // An enum code must not come to this point.
    public boolean process(CodegenProperty codegenProperty, CodegenModel codegenModel) {
        System.out.println(codegenProperty.baseName);
        for (ModelIdentifierStrategy strategy : modelIdentifierStrategies) {
            if (strategy.identify(codegenProperty, codegenModel)) {
                System.out.println("Strategy matched: " + strategy.getType() + " Enum Basename: " + codegenProperty.baseName);
                break;
            }
        }
        return false;
    }
}
