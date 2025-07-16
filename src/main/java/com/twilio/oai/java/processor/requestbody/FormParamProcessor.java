package com.twilio.oai.java.processor.requestbody;

import com.twilio.oai.java.processor.EnumProcessor;
import com.twilio.oai.java.processor.Processor;
import com.twilio.oai.java.strategy.enums.parameter.EnumIdentifierHandler;
import org.openapitools.codegen.CodegenOperation;

import static com.twilio.oai.common.ApplicationConstants.X_REQUEST_CONTENT_TYPE;

public class FormParamProcessor implements RequestBodyProcessor, Processor {
    final EnumProcessor enumProcessor = EnumProcessor.getInstance();
    @Override
    public void process(CodegenOperation codegenOperation) {
        codegenOperation.vendorExtensions.put(X_REQUEST_CONTENT_TYPE, getContentType());
        codegenOperation.formParams.forEach(parameter -> enumProcessor.process(parameter));
    }

    @Override
    public String getContentType() {
        return "application/x-www-form-urlencoded";
    }
}