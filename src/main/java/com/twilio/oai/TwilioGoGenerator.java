package com.twilio.oai;

import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.SupportingFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TwilioGoGenerator extends AbstractTwilioGoGenerator {

    @Override
    public void processOpts() {
        super.processOpts();

        supportingFiles.add(new SupportingFile("README.mustache", "README.md"));
    }

    @Override
    public void postProcessParameter(final CodegenParameter parameter) {
        // Make sure required non-path params get into the options block.
        parameter.required = parameter.isPathParam;
        parameter.vendorExtensions.put("x-is-account-sid", this.toLowerCase(parameter.baseName).equals("accountsid"));
    }

    private String toLowerCase(String paramName) {
        // pre-process name
        paramName = paramName.replaceAll("[^a-zA-Z]", "");
        return paramName.toLowerCase();
    }

    /**
     * Configures a friendly name for the generator.  This will be used by the generator
     * to select the library with the -g flag.
     *
     * @return the friendly name for the generator
     */
    @Override
    public String getName() {
        return "twilio-go";
    }

    /**
     * Returns human-friendly help for the generator.  Provide the consumer with help
     * tips, parameters here
     *
     * @return A string value for the help message
     */
    @Override
    public String getHelp() {
        return "Generates a Go client library (beta).";
    }

}
