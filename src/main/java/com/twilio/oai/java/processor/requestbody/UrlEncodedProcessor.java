package com.twilio.oai.java.processor.requestbody;

import com.twilio.oai.java.processor.enums.EnumProcessorFactory;
import org.openapitools.codegen.CodegenOperation;

import static com.twilio.oai.common.ApplicationConstants.X_REQUEST_CONTENT_TYPE;
import static com.twilio.oai.java.constants.MustacheConstants.X_REQUEST_LANGUAGE_CONTENT_TYPE_CONSTANT;

public class UrlEncodedProcessor implements RequestBodyProcessor {
    final EnumProcessorFactory enumFactory = EnumProcessorFactory.getInstance();
    @Override
    public void process(CodegenOperation codegenOperation) {
        codegenOperation.vendorExtensions.put(X_REQUEST_CONTENT_TYPE, getContentType());
        codegenOperation.vendorExtensions.put(X_REQUEST_LANGUAGE_CONTENT_TYPE_CONSTANT, "FORM_URLENCODED");
        codegenOperation.formParams.forEach(parameter -> {
            enumFactory.applyProcessor(parameter);
            //Promoter.addPromoter(parameter);
        });
    }

    @Override
    public String getContentType() {
        return "application/x-www-form-urlencoded";
    }

    @Override
    public boolean shouldProcess(final CodegenOperation codegenOperation) {
        if (codegenOperation.formParams != null && !codegenOperation.formParams.isEmpty()) return true;
        return false;
    }
}
