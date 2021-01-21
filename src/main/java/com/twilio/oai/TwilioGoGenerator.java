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
        supportingFiles.add(new SupportingFile("response.mustache", "response.go"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> postProcessOperationsWithModels(final Map<String, Object> objs, final List<Object> allModels) {
        final Map<String, Object> results = super.postProcessOperationsWithModels(objs, allModels);

        final Map<String, Object> ops = (Map<String, Object>) results.get("operations");
        final ArrayList<CodegenOperation> opList = (ArrayList<CodegenOperation>) ops.get("operation");

        // iterate over the operation and perhaps modify something
        for (final CodegenOperation co : opList) {
            co.vendorExtensions.put("x-is-delete-operation", "DELETE".equalsIgnoreCase(co.httpMethod));
            System.out.println(co);
        }

        return results;
    }

    @Override
    public void postProcessParameter(final CodegenParameter parameter) {
        // Make sure required non-path params get into the options block.
        parameter.required = parameter.isPathParam;
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
