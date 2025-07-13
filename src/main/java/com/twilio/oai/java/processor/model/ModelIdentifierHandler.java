package com.twilio.oai.java.processor.model;

import com.twilio.oai.java.processor.parameter.enumidentifcation.EnumIdentificationStrategy;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;

public class ModelIdentifierHandler {
    private final List<ModelIdentifierStrategy> modelIdentifierStrategies;
    public ModelIdentifierHandler() {
        modelIdentifierStrategies = List.of(
            new SingleModelStrategy(),
            new ListModelStrategy()
        );
    }
    
    // An enum code must not come to this point.
    public boolean identify(CodegenProperty codegenProperty) {
        System.out.println(codegenProperty.baseName);
        for (ModelIdentifierStrategy strategy : modelIdentifierStrategies) {
            if (strategy.identify(codegenProperty)) {
                System.out.println("Strategy matched: " + strategy.getType() + " Enum Basename: " + codegenProperty.baseName);
                break;
            }
        }
        return false;
    }
}
