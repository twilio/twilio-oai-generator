package com.twilio.oai.java;

import com.twilio.oai.TwilioJavaGeneratorModern;
import org.openapitools.codegen.CodegenOperation;

import java.util.List;

public class JavaApiResourceBuilder {
    TwilioJavaGeneratorModern twilioJavaGenerator;
    public JavaApiResourceBuilder(TwilioJavaGeneratorModern twilioJavaGenerator) {
        this.twilioJavaGenerator = twilioJavaGenerator;
    }
    public void process(List<CodegenOperation> operations) {
    }

    public JavaApiResource build() {
        return new JavaApiResource(this);
    }
}
