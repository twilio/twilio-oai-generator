package com.twilio.oai;

import com.twilio.oai.common.Utility;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
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
import org.openapitools.codegen.utils.StringUtils;

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

    boolean containsAllOf(String modelName) {
        return modelName.contains("allOf");
    }

    boolean containsStatusCode(String modelName) {
        return Pattern.compile("_\\d{3}_").matcher(modelName).find();
    }

    String removeStatusCode(String modelName) {
        if(modelName == null || modelName.isEmpty())
            return modelName;
        return modelName.replaceFirst("_\\d{3}", "");
    }

    String removeDigits(String modelName) {
        if(modelName == null || modelName.isEmpty() || modelName.contains("2010"))
            return modelName;
        return modelName.replaceFirst("\\d{3}", "");
    }

    String modelNameWithoutStatusCode(String modelName) {
        if(modelName == null || modelName.isEmpty())
            return modelName;
        String newModelName = removeStatusCode(modelName);
        if(Objects.equals(newModelName, modelName))
            newModelName = removeDigits(newModelName);
        return StringUtils.camelize(newModelName);
    }

    private boolean contains2xxStatusCode(String modelName) {
        return Pattern.compile("2\\d{2}_").matcher(modelName).find();
    }

    @Override
    public Map<String, ModelsMap> updateAllModels(Map<String, ModelsMap> objs) {
        objs = super.updateAllModels(objs);

        Set<String> modelNames = objs.keySet()
                .stream()
                .filter(key -> (containsStatusCode(key) || containsAllOf(key)))
            .filter(this::contains2xxStatusCode)
                .map(this::modelNameWithoutStatusCode)
                .collect(Collectors.toSet());

        objs.entrySet().removeIf(entry -> containsAllOf(entry.getKey()) || modelNames.contains(entry.getKey()));
        Map<String, ModelsMap> updatedObjs = new HashMap<>();

        objs.forEach((key, value) -> {
            if (containsStatusCode(key)) {
                ModelMap modelMap = value.getModels().get(0);
                String importPath = (String) modelMap.get("importPath");
                CodegenModel model = modelMap.getModel();
                key = modelNameWithoutStatusCode(key);
                model.setName(modelNameWithoutStatusCode(model.name));
                model.setClassname(modelNameWithoutStatusCode(model.classname));
                model.setClassVarName(modelNameWithoutStatusCode(model.classVarName));
                model.setClassFilename(removeStatusCode(model.classFilename));
                modelMap.setModel(model);
                modelMap.put("importPath", removeDigits(importPath));
                value.put("classname", removeDigits((String) value.get("classname")));
            }
            updatedObjs.put(key, value);
        });

        return updatedObjs;
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

        final List<CodegenModel> modelByOperation = opList
            .stream()
            .filter(op -> models.containsKey(op.returnType))
            .filter(op -> ( op.returnType != null && ( op.returnType.contains("Page") || op.returnType.contains("page") ) ))
            .map(op -> models.get(op.returnType)).collect(Collectors.toList());

        final Map<String, CodegenModel> modelNameToCodegenModel = modelByOperation
            .stream()
            .collect(Collectors.toMap(CodegenModel::getName, Function.identity(), (existing, replacement) -> existing));

        // get the model for the return type
        Optional<CodegenModel> returnModel = opList.stream()
            .filter(op -> ( op.returnType != null && ( op.returnType.contains("Page") || op.returnType.contains("page") ) ) && models.containsKey(op.returnType))
            .map(op -> models.get(op.returnType))
            .findFirst();

        if (modelNameToCodegenModel.size() > 1 && returnModel.isEmpty()) {
            returnModel = opList
                    .stream()
                    .filter(op -> models.containsKey(op.returnType))
                    .map(op -> models.get(op.returnType))
                .findFirst();
        }

        for (final CodegenOperation co : opList) {
            Utility.populateCrudOperations(co);
            Utility.resolveContentType(co);
            co.returnType = modelNameWithoutStatusCode(co.returnType);
            if (co.nickname.startsWith("List")) {
                // make sure the format matches the other methods
                co.vendorExtensions.put("x-domain-name", co.nickname.replaceFirst("List", ""));
                co.allParams.forEach( p -> {
                    if( p.dataType.equals("int64") && ( p.baseName.equals("PageSize") || p.baseName.equals("limit") ) ){
                        p.dataType = "int";
                    }
                });

                co.optionalParams.forEach( p -> {
                    if( p.dataType.equals("int64") && ( p.baseName.equals("PageSize") || p.baseName.equals("limit") ) ){
                        p.dataType = "int";
                    }
                });

                // filter the fields in the model and get only the array typed field. Also, make sure there is only one field of type list/array
                if (modelNameToCodegenModel.size() == 1 && returnModel.isPresent()) {
                    CodegenProperty field = returnModel.get().allVars
                            .stream()
                            .filter( v -> !v.baseName.contains("schemas"))
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
                            if(Objects.equals(openAPI.getServers().get(0).getUrl(), "/"))
                                openAPI.servers(path.getServers());
                            // Group operations together by tag. This gives us one file/post-process per resource.
                            if (!PathUtils.cleanPathAndRemoveFirstElement(name).equals("")) operation.addTagsItem(PathUtils.cleanPathAndRemoveFirstElement(name));
                            if(name.equals("/Organizations/{OrganizationSid}"))
                                operation.addTagsItem("organization");
                            // Add a parameter called limit for list and stream operations
                            if (operation.getOperationId().startsWith("List")) {
                                operation.addParametersItem(new Parameter().name("limit").description("Max number of records to return.").required(false).schema(new IntegerSchema()));
                                if ( operation.getParameters().stream().noneMatch(op -> op.getName().equalsIgnoreCase("pagesize")))
                                    operation.addParametersItem(new Parameter().name("PageSize").description("Max number of records to return in a page").required(false).schema(new IntegerSchema()));
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
