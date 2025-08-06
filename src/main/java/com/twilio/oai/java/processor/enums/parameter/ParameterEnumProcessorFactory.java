package com.twilio.oai.java.processor.enums.parameter;

import com.twilio.oai.java.processor.enums.parameter.body.InlineBodyEnumProcessor;
import com.twilio.oai.java.processor.enums.parameter.body.InlineBodyListEnumProcessor;
import com.twilio.oai.java.processor.enums.parameter.body.ReusableBodyEnumProcessor;
import com.twilio.oai.java.processor.enums.parameter.body.ReusableBodyListEnumProcessor;
import com.twilio.oai.java.processor.enums.parameter.param.InlineListParamEnumProcessor;
import com.twilio.oai.java.processor.enums.parameter.param.InlineParamEnumProcessor;
import com.twilio.oai.java.processor.enums.parameter.param.ReusableListParamEnumProcessor;
import com.twilio.oai.java.processor.enums.parameter.param.ReusableParamEnumProcessor;
import org.openapitools.codegen.CodegenParameter;

import java.util.List;

public class ParameterEnumProcessorFactory {
    private final List<ParameterEnumProcessor> processors;

    public ParameterEnumProcessorFactory(final List<ParameterEnumProcessor> processors) {
        this.processors = List.of(
                new InlineBodyEnumProcessor(),
                new InlineBodyListEnumProcessor(),
                new ReusableBodyEnumProcessor(),
                new ReusableBodyListEnumProcessor(),
                
                new InlineListParamEnumProcessor(),
                new InlineParamEnumProcessor(),
                new ReusableListParamEnumProcessor(),
                new ReusableParamEnumProcessor()
        );
    }

    public void applyProcessor(CodegenParameter codegenParameter) {
        if (codegenParameter.dataFormat != null && codegenParameter.dataFormat.equals("http-method")) {
            return;
        }
        for (ParameterEnumProcessor parameterEnumProcessor: processors) {
            if (parameterEnumProcessor.shouldProcess(codegenParameter)) {
                parameterEnumProcessor.process(codegenParameter);
                return; // Exit after the first processor that applies
            }
        }
    }
}
