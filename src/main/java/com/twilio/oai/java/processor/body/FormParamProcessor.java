package com.twilio.oai.java.processor.body;

import com.twilio.oai.java.processor.parameter.enumidentifcation.EnumIdentifierHandler;
import org.openapitools.codegen.CodegenOperation;

public class FormParamProcessor implements RequestBodyProcessor {
    final EnumIdentifierHandler enumIdentifierHandler = new EnumIdentifierHandler();
    @Override
    public void process(CodegenOperation codegenOperation) {
        codegenOperation.formParams.forEach(property -> enumIdentifierHandler.identify(property));
    }

    @Override
    public String getContentType() {
        return "application/x-www-form-urlencoded";
    }
}
