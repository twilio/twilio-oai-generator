package com.twilio.oai;

import org.openapitools.codegen.CodegenConstants;
import org.openapitools.codegen.languages.GoClientCodegen;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTwilioGoGenerator extends GoClientCodegen {

    public AbstractTwilioGoGenerator() {
        super();

        embeddedTemplateDir = templateDir = getName();
    }

    @Override
    public void processOpts() {
        super.processOpts();

        additionalProperties.put(CodegenConstants.IS_GO_SUBMODULE, true);

        supportingFiles.clear();
    }

    @Override
    public Map<String, String> createMapping(final String key, final String value) {
        // Optional dependency not needed.
        if (value.equals("github.com/antihax/optional")) {
            return new HashMap<>();
        }

        return super.createMapping(key, value);
    }
}
