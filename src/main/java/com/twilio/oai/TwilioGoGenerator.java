package com.twilio.oai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.*;

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

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> postProcessOperationsWithModels(final Map<String, Object> objs,
                                                               final List<Object> allModels) {
        final Map<String, Object> results = super.postProcessOperationsWithModels(objs, allModels);
        final Map<String, Object> ops = (Map<String, Object>) results.get("operations");
        final ArrayList<CodegenOperation> opList = (ArrayList<CodegenOperation>) ops.get("operation");
        for (final CodegenOperation co : opList) {
            if (co.nickname.startsWith("List")) {
                // make sure the format matches the other methods
                co.vendorExtensions.put("x-domain-name", co.nickname.replaceFirst("List", ""));
                co.vendorExtensions.put("x-is-list-operation", true);

                Map<String, CodegenModel> models = new HashMap<>();

                // get all models for the operation
                allModels
                        .forEach(m -> {
                            CodegenModel model = (CodegenModel) ((Map<String, Object>) m).get("model");
                            models.put(model.name, model);
                        });

                CodegenModel returnModel = null;
                // get the model for the return type
                for (CodegenOperation op : opList) {
                    if (models.containsKey(op.returnType)) {
                        returnModel = models.get(op.returnType);
                    }
                }

                // filter the fields in the model and get only the array typed field
                if (returnModel != null) {
                    CodegenProperty field = returnModel.allVars
                            .stream()
                            .filter(v -> v.dataType.startsWith("[]"))
                            .collect(toSingleton());

                    co.vendorExtensions.put("x-payload-field-name", field.name);
                    co.vendorExtensions.put("x-payload-model-name", field.complexType);
                }
            }
        }

        return results;
    }

    private static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
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
