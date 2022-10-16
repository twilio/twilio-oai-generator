package com.twilio.oai;

import org.openapitools.codegen.languages.PhpClientCodegen;

public class TwilioPhpGenerator extends PhpClientCodegen {
    private final TwilioCodegenAdapter twilioCodegen;

    public TwilioPhpGenerator() {
        super();

        twilioCodegen = new TwilioCodegenAdapter(this, getName());

        // Skip automated api test and doc generation
        apiTestTemplateFiles.clear();
        apiDocTemplateFiles.clear();
    }

    @Override
    public String getName() {
        return "twilio-php";
    }
}
