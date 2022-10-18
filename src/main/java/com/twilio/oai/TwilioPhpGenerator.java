package com.twilio.oai;

import org.openapitools.codegen.languages.PhpClientCodegen;

import java.util.HashMap;
import java.util.Map;

public class TwilioPhpGenerator extends PhpClientCodegen {
    private final TwilioCodegenAdapter twilioCodegen;

    public TwilioPhpGenerator() {
        super();

        twilioCodegen = new TwilioCodegenAdapter(this, getName());
    }

    @Override
    public void processOpts() {
        super.processOpts();

        apiTestTemplateFiles.clear();
        modelTestTemplateFiles.clear();
        apiDocTemplateFiles.clear();
        modelDocTemplateFiles.clear();
        twilioCodegen.processOpts();
    }


    @Override
    public String getName() {
        return "twilio-php";
    }
}
