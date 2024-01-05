package com.twilio.oai;

import com.twilio.oai.common.Utility;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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

import static com.twilio.oai.common.ApplicationConstants.STRING;

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
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);
        if (property.dataType.startsWith("[]") && property.dataType.contains("Enum")) {
            property._enum = (List<String>) property.items.allowableValues.get("values");
            property.allowableValues = property.items.allowableValues;
            property.dataType = "[]" + STRING;
            property.isEnum = property.dataFormat == null;
            property.isNullable = true;
        } else if (property.dataType.contains("Enum")) {
            property.datatypeWithEnum = Utility.removeEnumName(property.dataType);
            property.dataType = STRING;
            property.isEnum = property.dataFormat == null;
            property.isNullable = true;
        }
    }

    private void processEnumParameters(final CodegenParameter parameter) {
        if (parameter.dataType.startsWith("[]") && parameter.dataType.contains("Enum")) {
            parameter._enum = (List<String>) parameter.items.allowableValues.get("values");
            parameter.allowableValues = parameter.items.allowableValues;
            parameter.dataType = "[]" + STRING;
            parameter.isEnum = parameter.dataFormat == null;
            parameter.isNullable = true;
        } else if (parameter.dataType.contains("Enum")) {
            parameter.datatypeWithEnum = Utility.removeEnumName(parameter.dataType);
            parameter.dataType = STRING;
            parameter.isEnum = parameter.dataFormat == null;
            parameter.isNullable = true;
        }
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        final OperationMap ops = results.getOperations();
        final List<CodegenOperation> opList = ops.getOperation();

        final Map<String, CodegenModel> models = allModels
            .stream()
            .map(m -> m.get("model"))
            .map(CodegenModel.class::cast)
            .collect(Collectors.toMap(CodegenModel::getName, Function.identity()));

        // get the model for the return type
        final Optional<CodegenModel> returnModel = opList
            .stream()
            .filter(op -> models.containsKey(op.returnType))
            .map(op -> models.get(op.returnType))
            .findFirst();

        for (final CodegenOperation co : opList) {
            Utility.populateCrudOperations(co);
            Utility.resolveContentType(co);

            if (co.nickname.startsWith("List")) {
                // make sure the format matches the other methods
                co.vendorExtensions.put("x-domain-name", co.nickname.replaceFirst("List", ""));

                // filter the fields in the model and get only the array typed field. Also, make sure there is only one field of type list/array
                if (returnModel.isPresent()) {
                    CodegenProperty field = returnModel.get().allVars
                        .stream()
                        .filter(v -> v.dataType.startsWith("[]"))
                        .collect(toSingleton());

                    co.returnContainer = co.returnType;
                    co.returnType = field.dataType;
                    co.returnBaseType = field.complexType;

                    co.vendorExtensions.put("x-payload-field-name", field.name);
                }
                //For handling the cases where the schema contains allOf
                else{
                    ModelMap modelMap = allModels.stream().filter(map1 -> map1.getModel().getClassname().equals(co.returnType)).collect(toSingleton());
                    CodegenModel model = (CodegenModel) modelMap.get("model");
                    final Optional<CodegenModel> returnModelOther = Optional.ofNullable(model);
                    CodegenProperty field = returnModelOther.get().allVars
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
        processEnumParameters(parameter);
        // Make sure required non-path params get into the options block.
        parameter.required = parameter.isPathParam;
        parameter.vendorExtensions.put("x-custom", parameter.baseName.equals("limit"));

        // Parameters (and their items) need to be marshalled to a string for inclusion in the request payload when
        // they are either free-form objects (type: object) or any type objects (type is absent).
        if(parameter.isBodyParam){
            parameter.vendorExtensions.put("x-is-body-param",true);
        }
        if(parameter.isQueryParam){
            parameter.vendorExtensions.put("x-is-query-param",true);
        }
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
