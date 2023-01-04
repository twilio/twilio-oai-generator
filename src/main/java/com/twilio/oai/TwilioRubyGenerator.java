package com.twilio.oai;

import org.openapitools.codegen.languages.RubyClientCodegen;
import org.openapitools.codegen.model.ModelsMap;

import java.util.HashMap;
import java.util.Map;

public class TwilioRubyGenerator extends RubyClientCodegen {
    private final TwilioCodegenAdapter twilioCodegen;

    public TwilioRubyGenerator() {
        super();
        twilioCodegen = new TwilioCodegenAdapter(this, getName());
    }

    @Override
    public void processOpts() {
        super.processOpts();

        twilioCodegen.processOpts();
    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(Map<String, ModelsMap> objs) {
        return new HashMap<>();
    }


    @Override
    public String getName() {
        return "twilio-ruby";
    }
}

