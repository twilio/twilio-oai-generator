package com.twilio.oai.java.processor.enums.property;

import org.openapitools.codegen.CodegenProperty;

import java.util.List;

public class PropertyEnumProcessorFactory {
    private final List<PropertyEnumProcessor> processors;
    
    public PropertyEnumProcessorFactory(final List<PropertyEnumProcessor> processors) {
        this.processors = List.of(
                new InlineListPropEnumProcessor(),
                new InlinePropEnumProcessor(),
                new ReusableListPropEnumProcessor(),
                new ReusablePropEnumProcessor()
        );
    }
    
    public void applyProcessor(CodegenProperty codegenProperty) {
        if (codegenProperty.dataFormat != null && codegenProperty.dataFormat.equals("http-method")) {
            return;
        }
        for (PropertyEnumProcessor propertyEnumProcessor: processors) {
            if (propertyEnumProcessor.shouldProcess(codegenProperty)) {
                propertyEnumProcessor.process(codegenProperty);
                return; // Exit after the first processor that applies
            }
        }
    }
}
