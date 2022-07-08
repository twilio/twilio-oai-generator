package com.twilio.oai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.SupportingFile;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;

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
    public String toModel(String name, boolean doUnderscore) {
        String prunedName = name;
        // Keep the domain, version and the last part of the schema name
        String[] words = name.split("\\.");
        if (words.length > 3) {
            // api.v2010.account.sip.sip_domain.sip_auth.sip_auth_registrations.sip_auth_registrations_credential_list_mapping
            // becomes api.v2010.sip_auth_registrations_credential_list_mapping
            prunedName = String.format("%s.%s.%s", words[0], words[1], words[words.length - 1]);
        }

        return super.toModel(prunedName, doUnderscore);
    }

    @Override
    public ModelsMap postProcessModels(final ModelsMap objs) {
        final ModelsMap results = super.postProcessModels(objs);
        final List<ModelMap> models = results.getModels();

        for (final ModelMap m : models) {
            final CodegenModel model = m.getModel();

            model.allVars.forEach(v -> v.setIsNumber(v.isNumber || v.isFloat));
            model.vendorExtensions.put("x-has-numbers-vars", model.allVars.stream().anyMatch(v -> v.isNumber));
        }
        return results;
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        final OperationMap ops = results.getOperations();
        final List<CodegenOperation> opList = ops.getOperation();
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

                // filter the fields in the model and get only the array typed field. Also, make sure there is only one field of type list/array
                if (returnModel != null) {
                    CodegenProperty field = returnModel.allVars
                            .stream()
                            .filter(v -> v.dataType.startsWith("[]"))
                            .collect(toSingleton());

                    co.returnContainer = co.returnType;
                    co.returnType = field.dataType;
                    co.returnBaseType = field.complexType;

                    co.vendorExtensions.put("x-payload-field-name", field.name);
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
        parameter.vendorExtensions.put("x-custom", parameter.baseName.equals("limit"));

        // Parameters (and their items) need to be marshalled to a string for inclusion in the request payload when
        // they are either free-form objects (type: object) or any type objects (type is absent).
        if (parameter.isFreeFormObject || parameter.isAnyType) {
            parameter.vendorExtensions.put("x-marshal", true);
        }

        if (parameter.isArray && (parameter.items.isFreeFormObject || parameter.items.isAnyType)) {
            parameter.items.vendorExtensions.put("x-marshal", true);
        }
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        super.processOpenAPI(openAPI);

        openAPI
                .getPaths()
                .forEach((name, path) -> path
                        .readOperations()
                        .forEach(operation -> {
                            // Group operations together by tag. This gives us one file/post-process per resource.
                            operation.addTagsItem(PathUtils.cleanPathAndRemoveFirstElement(name));
                            // Add a parameter called limit for list and stream operations
                            if (operation.getOperationId().startsWith("List")) {
                                operation.addParametersItem(new Parameter().name("limit").description("Max number of records to return.").required(false).schema(new IntegerSchema()));
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
