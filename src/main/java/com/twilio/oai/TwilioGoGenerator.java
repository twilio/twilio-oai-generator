package com.twilio.oai;

import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.SupportingFile;

public class TwilioGoGenerator extends AbstractTwilioGoGenerator {

    @Override
    public void processOpts() {
        super.processOpts();

        supportingFiles.add(new SupportingFile("api_service.mustache", "api_service.go"));
        supportingFiles.add(new SupportingFile("README.mustache", "README.md"));
    }

    @Override
    public String toApiFilename(String name) {
        // Drop the "api_" prefix.
        return super.toApiFilename(name).replaceAll("^api_", "");
    }

    @Override
    public void postProcessParameter(final CodegenParameter parameter) {
        super.postProcessParameter(parameter);

        // Make sure required non-path params get into the options block.
        parameter.required = parameter.isPathParam;
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        super.processOpenAPI(openAPI);

        // Group operations together by tag. This gives us one file/post-process per resource.
        openAPI
            .getPaths()
            .forEach((name, path) -> path
                .readOperations()
                .forEach(operation -> operation.addTagsItem(PathUtils.cleanPath(name))));
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
