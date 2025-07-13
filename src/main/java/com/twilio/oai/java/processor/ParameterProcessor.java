package com.twilio.oai.java.processor;

import com.twilio.oai.java.processor.parameter.enumidentifcation.EnumIdentifierHandler;
import org.openapitools.codegen.CodegenOperation;

public class ParameterProcessor implements Processor {
    private final EnumIdentifierHandler enumIdentifierHandler = new EnumIdentifierHandler();
    @Override
    public void process(final CodegenOperation codegenOperation) {
        codegenOperation.queryParams.forEach(param -> enumIdentifierHandler.identify(param));
        codegenOperation.pathParams.forEach(param -> enumIdentifierHandler.identify(param));
        codegenOperation.headerParams.forEach(param -> enumIdentifierHandler.identify(param));
    }
}
