package com.twilio.oai.java.processor.enums;

import com.twilio.oai.java.processor.enums.parameter.ParameterEnumProcessor;
import com.twilio.oai.java.processor.enums.parameter.body.InlineBodyEnumProcessor;
import com.twilio.oai.java.processor.enums.parameter.body.InlineBodyListEnumProcessor;
import com.twilio.oai.java.processor.enums.parameter.body.ReusableBodyEnumProcessor;
import com.twilio.oai.java.processor.enums.parameter.body.ReusableBodyListEnumProcessor;
import com.twilio.oai.java.processor.enums.parameter.param.InlineListParamEnumProcessor;
import com.twilio.oai.java.processor.enums.parameter.param.InlineParamEnumProcessor;
import com.twilio.oai.java.processor.enums.parameter.param.ReusableListParamEnumProcessor;
import com.twilio.oai.java.processor.enums.parameter.param.ReusableParamEnumProcessor;
import com.twilio.oai.java.processor.enums.property.InlineListPropEnumProcessor;
import com.twilio.oai.java.processor.enums.property.InlinePropEnumProcessor;
import com.twilio.oai.java.processor.enums.property.PropertyEnumProcessor;
import com.twilio.oai.java.processor.enums.property.ReusableListPropEnumProcessor;
import com.twilio.oai.java.processor.enums.property.ReusablePropEnumProcessor;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;

import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;

/*
 * Factory class to manage and apply enum processors for CodegenParameter and CodegenProperty.
 * 
 * This class is implemented as a singleton to ensure that only one instance of the factory exists
 * throughout the application.
 * 
 * The processors (e.g., `InlineBodyEnumProcessor`, `ReusablePropEnumProcessor`) are instantiated
 * during the initialization of the factory. Since the factory is a singleton, these processor
 * instances effectively behave as singletons within the context of the factory. There is no need
 * to make these processor classes singleton themselves, as their lifecycle is already managed
 * by the factory.
 * 
 */
public class EnumProcessorFactory {

    public static EnumProcessorFactory instance;
    private final List<ParameterEnumProcessor> parameterEnumProcessors;
    private final List<PropertyEnumProcessor> propertyEnumProcessors;

    public static synchronized EnumProcessorFactory getInstance() {
        if (instance == null) {
            synchronized (EnumProcessorFactory.class) {
                if (instance == null) {
                    instance = new EnumProcessorFactory();
                }
            }
        }
        return instance;
    }
    
    private EnumProcessorFactory() {
        this.parameterEnumProcessors = List.of(
                new InlineBodyEnumProcessor(),
                new InlineBodyListEnumProcessor(),
                new ReusableBodyEnumProcessor(),
                new ReusableBodyListEnumProcessor(),

                new InlineListParamEnumProcessor(),
                new InlineParamEnumProcessor(),
                new ReusableListParamEnumProcessor(),
                new ReusableParamEnumProcessor()
        );

        this.propertyEnumProcessors = List.of(
                new InlineListPropEnumProcessor(),
                new InlinePropEnumProcessor(),
                new ReusableListPropEnumProcessor(),
                new ReusablePropEnumProcessor()
        );
    }

    public void applyProcessor(CodegenParameter codegenParameter) {
        if (codegenParameter.dataFormat != null && codegenParameter.dataFormat.equals("http-method")) {
            return;
        }
        for (ParameterEnumProcessor parameterEnumProcessor: parameterEnumProcessors) {
            if (parameterEnumProcessor.shouldProcess(codegenParameter)) {
                parameterEnumProcessor.process(codegenParameter);
                // TODO: to be deleted later.
                addDataType(codegenParameter);
                return; // Exit after the first processor that applies
            }
        }
    }

    // TODO: to be deleted later.
    private void addDataType(CodegenParameter codegenParameter) {
        codegenParameter.dataType = (String)codegenParameter.vendorExtensions.get(X_DATATYPE);
    }

    // TODO: to be deleted later.
    private void addDataType(CodegenProperty codegenProperty) {
        codegenProperty.dataType = (String)codegenProperty.vendorExtensions.get(X_DATATYPE);
    }

    public void applyProcessor(CodegenProperty codegenProperty) {
        if (codegenProperty.dataFormat != null && codegenProperty.dataFormat.equals("http-method")) {
            return;
        }
        for (PropertyEnumProcessor propertyEnumProcessor: propertyEnumProcessors) {
            if (propertyEnumProcessor.shouldProcess(codegenProperty)) {
                propertyEnumProcessor.process(codegenProperty);
                addDataType(codegenProperty);
                return; // Exit after the first processor that applies
            }
        }
    }
    
    public boolean isEnum(final CodegenParameter codegenParameter) {
        for (ParameterEnumProcessor parameterEnumProcessor: parameterEnumProcessors) {
            if (parameterEnumProcessor.shouldProcess(codegenParameter)) {
                return true; // If any processor can process, it's considered an enum
            }
        }
        return false;
    }

    public boolean isEnum(final CodegenProperty codegenProperty) {
        for (PropertyEnumProcessor propertyEnumProcessor : propertyEnumProcessors) {
            if (propertyEnumProcessor.shouldProcess(codegenProperty)) {
                return true; // If any processor can process, it's considered an enum
            }
        }
        return false;
    }
}