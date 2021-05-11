package com.twilio.oai;

import java.util.List;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.SupportingFile;

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

        if (parameter.paramName.equals("PathAccountSid")) {
            parameter.required = false;
            parameter.vendorExtensions.put("x-is-account-sid", true);
        }
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        openAPI.getPaths().forEach((name, path) -> path.readOperations().forEach(operation -> {
            List<Parameter> parameters = operation.getParameters();
            if (parameters != null) {
                for (Parameter p : parameters) {
                    String in = p.getIn();
                    String paramName = p.getName();
                    if (in.equals("path") && paramName.equals("AccountSid")) {
                        p.setName("PathAccountSid");
                    }
                }
            }
        }));
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
