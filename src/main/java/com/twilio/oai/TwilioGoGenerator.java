package com.twilio.oai;

import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.SupportingFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.stream.Collectors;

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

        if (parameter.paramName.equals("AccountSid")) {
            parameter.required = false;
            parameter.vendorExtensions.put("x-is-account-sid", parameter.paramName.equals("AccountSid"));
        }
    }

    @Override
    public Map<String, Object> postProcessOperationsWithModels(final Map<String, Object> objs, final List<Object> allModels) {
        final Map<String, Object> results = super.postProcessOperationsWithModels(objs, allModels);
        final Map<String, Object> ops = (Map<String, Object>) results.get("operations");
        final ArrayList<CodegenOperation> opList = (ArrayList<CodegenOperation>) ops.get("operation");
        // iterate over the operation and perhaps modify something
        for (final CodegenOperation co : opList) {
            if (!co.path.startsWith("/2010-04-01/")) {
                continue;
            }

            // handle case where the AccountSid exists as both a path parameter that's marked optional and a form parameter (optional)
            HashSet<Object> seen = new HashSet<>();
            co.optionalParams = co.optionalParams.stream()
                        .filter(e -> seen.add(e.baseName))
                        .collect(Collectors.toList());
        }

        return results;
    }

    /**
     * Configures a friendly name for the generator. This will be used by the generator to select the library with the
     * -g flag.
     *
     * @return the friendly name for the generator
     */
    @Override
    public String getName() {
        return "twilio-go";
    }

    /**
     * Returns human-friendly help for the generator. Provide the consumer with help tips, parameters here
     *
     * @return A string value for the help message
     */
    @Override
    public String getHelp() {
        return "Generates a Go client library (beta).";
    }
}
