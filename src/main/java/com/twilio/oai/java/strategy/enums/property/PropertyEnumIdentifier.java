package com.twilio.oai.java.strategy.enums.property;

import org.openapitools.codegen.CodegenProperty;

import java.util.List;

public class PropertyEnumIdentifier {
    private final List<PropertyEnumProcessingStrategy> strategies;
    public PropertyEnumIdentifier() {
        strategies = List.of(
                new InlinePropStrategy(),
                new RefPropStrategy(),
                new InlineListPropStrategy(),
                new RefListPropStrategy()
        );
    }

    public void identify(CodegenProperty codegenProperty) {
        if ("http-method".equals(codegenProperty.dataFormat)) {
            return;
        }
        for (PropertyEnumProcessingStrategy strategy : strategies) {
            if (strategy.process(codegenProperty)) {
                System.out.println("Strategy matched: " + strategy.getType() + " Enum Basename: " + codegenProperty.baseName);
                break;
            }
        }
    }
}
