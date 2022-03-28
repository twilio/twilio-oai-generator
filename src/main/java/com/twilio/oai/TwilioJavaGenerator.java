package com.twilio.oai;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.AllArgsConstructor;
import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.JavaClientCodegen;
import org.openapitools.codegen.utils.StringUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TwilioJavaGenerator extends JavaClientCodegen {

    // Unique string devoid of symbols.
    private static final String PATH_SEPARATOR_PLACEHOLDER = "1234567890";
    private static final String INCORRECT_DATE_RANGE_TYPE = "OffsetDateTime";
    private static final String CORRECT_DATE_RANGE_TYPE = "ZonedDateTime";
    private static final String BEFORE = "Before";
    private static final String AFTER = "After";
    private static final int OVERFLOW_CHECKER = 32;
    private static final int BASE_SIXTEEN = 16;
    private static final int BIG_INTEGER_CONSTANT = 1;
    private static final int SERIAL_UID_LENGTH = 12;
    private static final int LENGTH_OF_LESSERTHAN = 8;
    private static final int LENGTH_OF_GREATERTHAN = 11;
    private static final int LENGTH_OF_LESSER_THAN = 9;
    private static final int LENGTH_OF_GREATER_THAN = 12;
    private static final int LENGTH_OF_RELATIONAL_OPERATOR = 1;

    private final List<CodegenModel> allModels = new ArrayList<>();
    private final Inflector inflector = new Inflector();

    public TwilioJavaGenerator() {
        super();

        // Remove the "API" suffix from the API filenames.
        apiNameSuffix = "";

        // Find the templates in the local resources dir.
        embeddedTemplateDir = templateDir = getName();
        sourceFolder = "";
    }

    @Override
    public void processOpts() {
        super.processOpts();

        final String inputSpecPattern = ".+_(?<domain>.+)_(?<version>.+)\\..+";
        final String version = inputSpec.replaceAll(inputSpecPattern, "${version}");
        final String domain = inputSpec.replaceAll(inputSpecPattern, "${domain}");
        apiPackage = version; // Place the API files in the version folder.
        additionalProperties.put("apiVersion", version);
        additionalProperties.put("apiVersionClass", version.toUpperCase());
        additionalProperties.put("domain", StringUtils.camelize(domain));
        additionalProperties.put("domainPackage", domain.toLowerCase());

        supportingFiles.clear();
        apiTemplateFiles.put("api.mustache", ".java");
        apiTemplateFiles.put("creator.mustache", "Creator.java");
        apiTemplateFiles.put("deleter.mustache", "Deleter.java");
        apiTemplateFiles.put("fetcher.mustache", "Fetcher.java");
        apiTemplateFiles.put("reader.mustache", "Reader.java");
        apiTemplateFiles.put("updater.mustache", "Updater.java");

        supportingFiles.add(new SupportingFile("Domains.mustache", "Domains.java"));
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        openAPI.getPaths().forEach((name, path) -> path.readOperations().forEach(operation -> {
            // Group operations together by tag. This gives us one file/post-process per resource.
            final String tag = PathUtils.cleanPath(name).replace("/", PATH_SEPARATOR_PLACEHOLDER);
            operation.addTagsItem(tag);
        }));
    }


    @Override
    public void postProcessParameter(final CodegenParameter parameter) {
        super.postProcessParameter(parameter);

        // Make sure required non-path params get into the options block.
        parameter.paramName = StringUtils.camelize(parameter.paramName, false);
    }

    @Override
    public String toApiFilename(final String name) {
        // Replace the path separator placeholder with the actual separator and lowercase the first character of each
        // path part.
        return Arrays
                .stream(super.toApiFilename(name).split(PATH_SEPARATOR_PLACEHOLDER))
                .map(part -> StringUtils.camelize(part, false))
                .map(this::singularize)
                .collect(Collectors.joining(File.separator));
    }


    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> postProcessAllModels(final Map<String, Object> allModels) {
        final Map<String, Object> results = super.postProcessAllModels(allModels);

        for (final Object obj : results.values()) {
            final Map<String, Object> mods = (Map<String, Object>) obj;
            final ArrayList<Map<String, Object>> modList = (ArrayList<Map<String, Object>>) mods.get("models");

            // Add all the models to the local models list.
            modList
                    .stream()
                    .map(model -> model.get("model"))
                    .map(CodegenModel.class::cast)
                    .collect(Collectors.toCollection(() -> this.allModels));
        }

        // Return an empty collection so no model files get generated.
        return new HashMap<>();
    }


    @Override
    public Map<String, Object> postProcessOperationsWithModels(final Map<String, Object> objs,
                                                               final List<Object> allModels) {
        final Map<String, Object> results = super.postProcessOperationsWithModels(objs, allModels);

        final Map<String, Map<String, Object>> resources = new LinkedHashMap<>();

        final Map<String, Object> ops = getStringMap(results, "operations");
        final ArrayList<CodegenOperation> opList = (ArrayList<CodegenOperation>) ops.get("operation");

        // iterate over the operation and perhaps modify something
        for (final CodegenOperation co : opList) {
            // Group operations by resource.
            String path = co.path;
            // TODO: Nested Properties to be fixed in upcoming stories
            String resourceName = singularize(getResourceName(co.path));
            if (resourceName.equals("IncomingPhoneNumber")) continue;
            final Map<String, Object> resource = resources.computeIfAbsent(resourceName, k -> new LinkedHashMap<>());
            populateCrudOperations(resource, co);
            // TODO: Review this condition
            if (co.path.endsWith("}") || co.path.endsWith("}.json")) {
                if ("GET".equalsIgnoreCase(co.httpMethod)) {
                    resource.put("hasFetch", true);
                    resource.put("requiredParamsFetch", co.requiredParams);

                    co.vendorExtensions.put("x-is-fetch-operation", true);
                    addOperationName(co, "Fetch");
                } else if ("POST".equalsIgnoreCase(co.httpMethod)) {
                    resource.put("hasUpdate", true);
                    addOperationName(co, "Update");
                    resource.put("requiredParamsUpdate", co.requiredParams);
                } else if ("DELETE".equalsIgnoreCase(co.httpMethod)) {
                    resource.put("hasDelete", true);
                    addOperationName(co, "Remove");
                    resource.put("requiredParamsDelete", co.requiredParams);
                }
            } else {
                if ("POST".equalsIgnoreCase(co.httpMethod)) {
                    // TODO: set false for testing the code, Fix this bug later
                    resource.put("hasCreate", true);
                    co.vendorExtensions.put("x-is-create-operation", true);
                    addOperationName(co, "Create");
                    resource.put("requiredParamsCreate", co.requiredParams);
                } else if ("GET".equalsIgnoreCase(co.httpMethod)) {
                    resource.put("hasRead", true);
                    co.vendorExtensions.put("x-is-read-operation", true);
                    addOperationName(co, "Page");
                    resource.put("requiredParamsRead", co.requiredParams);
                }
            }

            final ArrayList<CodegenOperation> resourceOperationList =
                    (ArrayList<CodegenOperation>) resource.computeIfAbsent(
                            "operations",
                            k -> new ArrayList<>());

            resourceOperationList.add(co);
            resource.put("resourceName", resourceName);
            resource.put("path", path);
            // TODO: This is the issue, These values will be overridden multiple times
            resource.put("resourcePathParams", co.pathParams);
            resource.put("resourceRequiredParams", co.requiredParams);

            co.pathParams = null;
            co.hasParams = !co.allParams.isEmpty();
            co.hasRequiredParams = !co.requiredParams.isEmpty();

            if (co.bodyParam != null) {
                addModel(resource, co.bodyParam.dataType);
            }

            co.responses
                    .stream()
                    .map(response -> response.dataType)
                    .filter(Objects::nonNull)
                    .map(this::getModel)
                    .flatMap(Optional::stream)
                    .forEach(model -> {
                        if (co.path.endsWith("}") || co.path.endsWith("}.json")) {
                            resource.put("responseModel", model);
                        }
                        resource.put("serialVersionUID", calculateSerialVersionUid(model.vars));
                    });

            fixDateRange(co.allParams);

            results.put("apiFilename", getResourceName(co.path));
            results.put("packageName", getPackageName(co.path));
            results.put("recordKey", getFolderName(co.path));
            resource.put("packageSubPart", getPackageName(co.path).substring(0, getPackageName(co.path).lastIndexOf(".")));
        }

        for (final Object resource : resources.values()) {
            flattenStringMap((Map<String, Object>) resource, "models");
        }

        results.put("resources", resources.values());

        return results;
    }

    @AllArgsConstructor
    private enum Operation {
        CREATE("create"),
        FETCH("fetch"),
        UPDATE("update"),
        DELETE("delete");

        private final String prefix;
    }

    private void populateCrudOperations(final Map<String, Object> resource, final CodegenOperation operation) {
        if (operation.nickname.startsWith(Operation.CREATE.prefix)) {
            operation.vendorExtensions.put("x-is-create-operation", true);
            resource.put(Operation.CREATE.name(), operation);
        } else if (operation.nickname.startsWith(Operation.FETCH.prefix)) {
            operation.vendorExtensions.put("x-is-fetch-operation", true);
            resource.put(Operation.FETCH.name(), operation);
        } else if (operation.nickname.startsWith(Operation.UPDATE.prefix)) {
            operation.vendorExtensions.put("x-is-update-operation", true);
            resource.put(Operation.UPDATE.name(), operation);
        } else if (operation.nickname.startsWith(Operation.DELETE.prefix)) {
            operation.vendorExtensions.put("x-is-delete-operation", true);
            resource.put(Operation.DELETE.name(), operation);
        }
    }

    private void addModel(final Map<String, Object> resource, final String dataType) {
        getModel(dataType).ifPresent(model -> {
            if (getStringMap(resource, "models").putIfAbsent(model.getClassname(), model) == null) {
                model.getVars().forEach(property -> addModel(resource, property.dataType));
            }
        });
    }

    private Optional<CodegenModel> getModel(final String modelName) {
        return allModels.stream().filter(model -> model.getClassname().equals(modelName)).findFirst();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getStringMap(final Map<String, Object> resource, final String key) {
        return (Map<String, Object>) resource.computeIfAbsent(key, k -> new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    private void flattenStringMap(final Map<String, Object> resource, final String key) {
        resource.computeIfPresent(key, (k, dependents) -> ((Map<String, Object>) dependents).values());
    }

    private String getResourceName(final String path) {
        String lastPathPart = PathUtils.getLastPathPart(PathUtils.cleanPath(path));
        if (inflector.isAbbrevation(lastPathPart)) {
            return StringUtils.camelize(lastPathPart.toLowerCase(), false);
        }
        return lastPathPart;
    }

    private String getPackageName(final String path) {
        return Arrays
                .stream(PathUtils.cleanPath(path).split("/"))
                .map(this::singularize)
                .collect(Collectors.joining("."));
    }

    private String getFolderName(final String path) {
        var cleanPath = PathUtils.cleanPath(path).split("/");
        return StringUtils.camelize(cleanPath[cleanPath.length - 1], true);
    }

    private String singularize(final String plural) {
        return (inflector.singularize(plural));
    }

    private void addOperationName(final CodegenOperation operation, final String name) {
        operation.vendorExtensions.put("x-name", name);
        operation.vendorExtensions.put("x-name-lower", name.toLowerCase());
    }

    private long calculateSerialVersionUid(final List<CodegenProperty> modelProperties){

        String signature = calculateSignature(modelProperties);
        return Long.parseLong(getMd5(signature).substring(0,SERIAL_UID_LENGTH), BASE_SIXTEEN);
    }

    private String calculateSignature(final List<CodegenProperty> modelProperties){

        Map<String, String> propertyMap = new HashMap<>();
        for(CodegenProperty property : modelProperties){
            String key = property.name;
            String type = property.dataType; //concatenate the class name
            propertyMap.put(key,type);
        }

        ArrayList<String> sortedKeys = new ArrayList<String>(propertyMap.keySet());
        Collections.sort(sortedKeys);
        StringBuilder sb = new StringBuilder();
        for (String key : sortedKeys){
             sb.append("|");
             sb.append(key.toLowerCase());
             sb.append("|");
             sb.append(propertyMap.get(key).toLowerCase());
        }
        return sb.toString();

    }

    private String getMd5(String input){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger bigInteger = new BigInteger(BIG_INTEGER_CONSTANT, messageDigest);
            String hashtext = bigInteger.toString(BASE_SIXTEEN);
            while (hashtext.length() < OVERFLOW_CHECKER) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void fixDateRange(List<CodegenParameter> allParams){
        for(CodegenParameter param: allParams){
            if(param.dataType==INCORRECT_DATE_RANGE_TYPE){
                param.dataType=CORRECT_DATE_RANGE_TYPE;
                if(param.paramName.length() >=LENGTH_OF_LESSERTHAN && param.paramName.substring(param.paramName.length() - LENGTH_OF_LESSERTHAN ).equals("LessThan")){
                    param.paramName = param.paramName.substring(0, param.paramName.length() - LENGTH_OF_LESSERTHAN ) + BEFORE;
                }
                if(param.paramName.length() >=LENGTH_OF_GREATERTHAN &&  param.paramName.substring(param.paramName.length() - LENGTH_OF_GREATERTHAN ).equals("GreaterThan")){
                    param.paramName = param.paramName.substring(0, param.paramName.length() - LENGTH_OF_GREATERTHAN ) + AFTER;
                }
                if(param.getSchema()!=null){
                    if(param.getSchema().dataType.equals(INCORRECT_DATE_RANGE_TYPE)){
                        param.getSchema().dataType=CORRECT_DATE_RANGE_TYPE;
                        param.getSchema().complexType=CORRECT_DATE_RANGE_TYPE;
                        param.getSchema().datatypeWithEnum=CORRECT_DATE_RANGE_TYPE;
                        param.getSchema().baseType=CORRECT_DATE_RANGE_TYPE;
                    }
                    if(param.getSchema().baseName.length()>=LENGTH_OF_RELATIONAL_OPERATOR && param.getSchema().baseName.substring(param.getSchema().baseName.length() - LENGTH_OF_RELATIONAL_OPERATOR ).equals("<")){
                        if(param.getSchema().getter.length()>=LENGTH_OF_LESSERTHAN){
                            param.getSchema().getter = param.getSchema().getter.substring(0, param.getSchema().getter.length() - LENGTH_OF_LESSERTHAN ) + BEFORE;
                        }
                        if(param.getSchema().setter.length()>=LENGTH_OF_LESSERTHAN){
                            param.getSchema().setter = param.getSchema().setter.substring(0, param.getSchema().setter.length() - LENGTH_OF_LESSERTHAN ) + BEFORE;
                        }
                        if(param.getSchema().name.length()>=LENGTH_OF_LESSERTHAN){
                            param.getSchema().name = param.getSchema().name.substring(0, param.getSchema().name.length() - LENGTH_OF_LESSERTHAN ) + BEFORE;
                        }
                        if(param.getSchema().nameInCamelCase.length()>=LENGTH_OF_LESSERTHAN){
                            param.getSchema().nameInCamelCase= param.getSchema().nameInCamelCase.substring(0, param.getSchema().nameInCamelCase.length() - LENGTH_OF_LESSERTHAN ) + BEFORE;
                        }
                        if(param.getSchema().baseName.length()>=LENGTH_OF_LESSER_THAN){
                            param.getSchema().nameInSnakeCase= param.getSchema().nameInSnakeCase.substring(0, param.getSchema().nameInSnakeCase.length() - LENGTH_OF_LESSER_THAN ) + "BEFORE";
                        }

                    }
                    if(param.getSchema().baseName.length()>=LENGTH_OF_RELATIONAL_OPERATOR && param.getSchema().baseName.substring(param.getSchema().baseName.length() - LENGTH_OF_RELATIONAL_OPERATOR ).equals(">")){
                        if(param.getSchema().getter.length()>=LENGTH_OF_GREATERTHAN){
                            param.getSchema().getter = param.getSchema().getter.substring(0, param.getSchema().getter.length() - LENGTH_OF_GREATERTHAN ) + AFTER;
                        }
                        if(param.getSchema().setter.length()>=LENGTH_OF_GREATERTHAN){
                            param.getSchema().setter = param.getSchema().setter.substring(0, param.getSchema().setter.length() - LENGTH_OF_GREATERTHAN ) + AFTER;
                        }
                        if(param.getSchema().name.length()>=LENGTH_OF_GREATERTHAN){
                            param.getSchema().name = param.getSchema().name.substring(0, param.getSchema().name.length() - LENGTH_OF_GREATERTHAN ) + AFTER;
                        }
                        if(param.getSchema().nameInCamelCase.length()>=LENGTH_OF_GREATERTHAN){
                            param.getSchema().nameInCamelCase= param.getSchema().nameInCamelCase.substring(0, param.getSchema().nameInCamelCase.length() - LENGTH_OF_GREATERTHAN ) + AFTER;
                        }
                        if(param.getSchema().nameInSnakeCase.length()>=LENGTH_OF_GREATER_THAN){
                            param.getSchema().nameInSnakeCase= param.getSchema().nameInSnakeCase.substring(0, param.getSchema().nameInSnakeCase.length() - LENGTH_OF_GREATER_THAN ) + "AFTER";
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return "twilio-java";
    }

    @Override
    public String getHelp() {
        return "Generates the twilio-java helper library.";
    }
}
