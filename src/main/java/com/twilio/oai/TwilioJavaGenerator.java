package com.twilio.oai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.twilio.oai.resource.IResourceTree;
import com.twilio.oai.resource.ResourceMap;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import lombok.AllArgsConstructor;
import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.JavaClientCodegen;
import org.openapitools.codegen.utils.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TwilioJavaGenerator extends JavaClientCodegen {

    // Unique string devoid of symbols.
    public static final String PATH_SEPARATOR_PLACEHOLDER = "1234567890";

    public static final String ACCOUNT_SID_FORMAT = "^AC[0-9a-fA-F]{32}$";
    private static final int OVERFLOW_CHECKER = 32;
    private static final int BASE_SIXTEEN = 16;
    private static final int BIG_INTEGER_CONSTANT = 1;
    private static final int SERIAL_UID_LENGTH = 12;
    public static final String URI = "uri";

    private final List<CodegenModel> allModels = new ArrayList<>();
    private  Map<String, String> modelFormatMap = new HashMap<>();
    private final Inflector inflector = new Inflector();
    private IResourceTree resourceTree;

    public TwilioJavaGenerator() {
        super();

        // Remove the "API" suffix from the API filenames.
        apiNameSuffix = "";

        // Find the templates in the local resources dir.
        embeddedTemplateDir = templateDir = getName();
        sourceFolder = "";

        // Skip automated api test and doc generation
        apiTestTemplateFiles.clear();
        apiDocTemplateFiles.clear();
    }

    @Override
    public void processOpts() {
        super.processOpts();
        String[] inputSpecs = inputSpec.split("_");
        final String version = inputSpecs[inputSpecs.length-1].replaceAll("\\.[^/]+$", "");
        final String domain = String.join("", Arrays.copyOfRange(inputSpecs, 1, inputSpecs.length-1));
        apiPackage = version; // Place the API files in the version folder.
        additionalProperties.put("apiVersion", version);
        additionalProperties.put("apiVersionClass", version.toUpperCase());
        additionalProperties.put("domain", StringUtils.camelize(domain));
        additionalProperties.put("domainPackage", domain.toLowerCase());

        supportingFiles.clear();
        apiTemplateFiles.put("api.mustache", ".java");
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        resourceTree = new ResourceMap(inflector, PATH_SEPARATOR_PLACEHOLDER);
        // regex example : https://flex-api.twilio.com
        Pattern serverUrlPattern = Pattern.compile("https:\\/\\/([a-zA-Z-]+)\\.twilio\\.com");
        Map<String, Object> extensionMapAtResourceLevel = new HashMap<String, Object>();
        openAPI.getPaths().forEach((name, path) -> {
            resourceTree.addResource(name, path);
        });
        openAPI.getPaths().forEach((name, path) -> {
            updateAccountSidParam(name, path);
            path.readOperations().forEach(operation -> {
                // Group operations together by tag. This gives us one file/post-process per resource.
                String tag = String.join(PATH_SEPARATOR_PLACEHOLDER, resourceTree.ancestors("/"+name.replaceFirst("/[^/]+/", "")));
                operation.addTagsItem(tag);
                extensionMapAtResourceLevel.put(tag, path.getExtensions());
            });
            Matcher m = serverUrlPattern.matcher(path.getServers().get(0).getUrl());
            if(m.find()){
                additionalProperties.put("domainName", StringUtils.camelize(m.group(1)));
            }
            additionalProperties.put("extensionMapAtResourceLevel", extensionMapAtResourceLevel);
        });
    }
    /**
     * make accountSid an optional param
     * @param path
     * @param pathMap
     */
    private void updateAccountSidParam(final String path, final PathItem pathMap) {
        pathMap.readOperations().stream().map(io.swagger.v3.oas.models.Operation::getParameters)
        .filter(Objects::nonNull)
        .flatMap(Collection::stream)
        .filter(param -> param.getIn().equals("path") && ( ACCOUNT_SID_FORMAT.equals(param.getSchema().getPattern())))
        .forEach(param -> {
            param.required(false);
            param.addExtension("x-is-account-sid", true);
        });
    }

    @Override
    public String toParamName(String name) {
        name = name.replace("<", "Before");
        name = name.replace(">", "After");
        name = super.toVarName(name);
        return name;
    }


    @Override
    public void postProcessParameter(final CodegenParameter parameter) {
        super.postProcessParameter(parameter);

        // Make sure required non-path params get into the options block.
        parameter.paramName = StringUtils.camelize(parameter.paramName, false);
    }

    @Override
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);
        property.isEnum =  property.isEnum && property.dataFormat == null;
    }

    @Override
    public String toApiFilename(final String name) {
        String[] split = super.toApiFilename(name).split(PATH_SEPARATOR_PLACEHOLDER);
        return Arrays.stream(Arrays.copyOfRange(split, 0, split.length - 1))
                .map(String::toLowerCase)
                .collect(Collectors.joining("/")) + "/"+split[split.length-1];
    }

    /**
     * Different data types need different formatting and conversion mechanisms while they are being added as parameters to the request
     * This special handling will be done in mustache files
     * This function sets different flags for processing in mustache files
     * */
    private void processDataTypesForParams(List<CodegenParameter> finalQueryParamList) {
        //Date types needing special processing
        List<String> specialTypes = Arrays.asList("String", "ZonedDateTime", "LocalDate");

        for(CodegenParameter e : finalQueryParamList){

            if(!specialTypes.contains(e.dataType) && !e.vendorExtensions.containsKey("x-prefixed-collapsible-map") && !e.isArray){
                e.vendorExtensions.put("x-is-other-data-type", true);
            } else if (e.isArray && e.baseType.equalsIgnoreCase("String")) {
                e.vendorExtensions.put("x-is-string-array", true);
            }

        }
    }


    /**
     * Function to pre process query parameters
     * There are some combination of query parameters, if present needs to be treated different
     * This function identifies and label them and remove some query params from the original list
     * returns finalQueryParamList - Modified query parameters list
     */
    public List<CodegenParameter> preProcessQueryParameters(CodegenOperation co){

        List<String> queryParamNames = new ArrayList<>();
        for(CodegenParameter e : co.queryParams){
            queryParamNames.add(e.paramName);
        }
        Collections.sort(queryParamNames, Collections.reverseOrder());
        for(CodegenParameter e : co.queryParams){
            String afterName = e.paramName + "After";
            String beforeName = e.paramName + "Before";
            if(queryParamNames.contains(afterName) && queryParamNames.contains(beforeName)){
                e.vendorExtensions.put("x-has-before-and-after", true);
                queryParamNames.remove(afterName);
                queryParamNames.remove(beforeName);
            }
        }
        List<CodegenParameter> finalQueryParamList = new ArrayList<CodegenParameter>();
        for (CodegenParameter e : co.queryParams) {
            if (queryParamNames.contains(e.paramName)) {
                finalQueryParamList.add(e);
            }
        }
        processDataTypesForParams(finalQueryParamList);
        return finalQueryParamList;
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
        setObjectFormatMap(this.allModels);
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
        final Map<String, String> enumAliasDict = (Map<String, String>) getEnumAliasDict(this.additionalProperties.get("extensionMapAtResourceLevel"), ops);
        String recordKey = getRecordKey(opList, this.allModels);
        List<CodegenModel> responseModels = new ArrayList<CodegenModel>();
        boolean isVersionV2010 = objs.get("package").equals("v2010");
        apiTemplateFiles.remove("updater.mustache");
        apiTemplateFiles.remove("creator.mustache");
        apiTemplateFiles.remove("deleter.mustache");
        apiTemplateFiles.remove("reader.mustache");
        apiTemplateFiles.remove("fetcher.mustache");

        // iterate over the operation and perhaps modify something
        for (final CodegenOperation co : opList) {
            // Group operations by resource.
            String path = co.path;
            co.vendorExtensions.put("x-is-version-v2010", isVersionV2010);
            String[] filePathArray = co.baseName.split(PATH_SEPARATOR_PLACEHOLDER);
            String resourceName = filePathArray[filePathArray.length-1];
            final Map<String, Object> resource = resources.computeIfAbsent(resourceName, k -> new LinkedHashMap<>());
            populateCrudOperations(resource, co);
            updateCodeOperationParams(co);
            if (co.nickname.startsWith("update")) {
                resource.put("hasUpdate", true);
                addOperationName(co, "Update");
                co.vendorExtensions.put("x-is-update-operation", true);
                resource.put("signatureListUpdate", generateSignatureList(resource, co, isVersionV2010));
                apiTemplateFiles.put("updater.mustache", "Updater.java");
            } else if (co.nickname.startsWith("delete")) {
                resource.put("hasDelete", true);
                addOperationName(co, "Remove");
                co.vendorExtensions.put("x-is-delete-operation", true);
                resource.put("signatureListDelete", generateSignatureList(resource, co, isVersionV2010));
                apiTemplateFiles.put("deleter.mustache", "Deleter.java");
            } else if (co.nickname.startsWith("create")) {
                resource.put("hasCreate", true);
                co.vendorExtensions.put("x-is-create-operation", true);
                addOperationName(co, "Create");
                resource.put("signatureListCreate", generateSignatureList(resource, co, isVersionV2010));
                apiTemplateFiles.put("creator.mustache", "Creator.java");
            } else if (co.nickname.startsWith("fetch")) {
                resource.put("hasFetch", true);
                resource.put("signatureListFetch", generateSignatureList(resource, co, isVersionV2010));
                co.vendorExtensions.put("x-is-fetch-operation", true);
                addOperationName(co, "Fetch");
                apiTemplateFiles.put("fetcher.mustache", "Fetcher.java");
            } else {
                resource.put("hasRead", true);
                co.vendorExtensions.put("x-is-read-operation", true);
                addOperationName(co, "Page");
                resource.put("signatureListRead", generateSignatureList(resource, co, isVersionV2010));
                apiTemplateFiles.put("reader.mustache", "Reader.java");

            }

            final ArrayList<CodegenOperation> resourceOperationList =
                    (ArrayList<CodegenOperation>) resource.computeIfAbsent(
                            "operations",
                            k -> new ArrayList<>());
            resourceOperationList.add(co);
            resource.put("path", path);
            resource.put("resourceName", resourceName);
            updateCodeOperationParams(co);
            co.queryParams = preProcessQueryParameters(co);
            co.pathParams = null;
            co.hasParams = !co.allParams.isEmpty();
            co.allParams.stream().map(ConventionResolver::resolveParamTypes).map(item -> StringUtils.camelize(item.paramName)).collect(Collectors.toList());
            co.hasRequiredParams = !co.requiredParams.isEmpty();
            resource.put("resourcePathParams", co.pathParams);
            resource.put("resourceRequiredParams", co.requiredParams);
            resource.put("serialVersionUID",1);
            co.vendorExtensions.put("x-non-path-params", getNonPathParams(co.allParams));

            if (co.bodyParam != null) {
                addModel(resource, co.bodyParam.dataType);
            }
            Map<String, String> finalEnumAliasDict = enumAliasDict;
            co.responses
              .stream()
              .map(response -> response.dataType)
              .filter(Objects::nonNull)
              .map(modelName -> this.getModelCoPath(modelName, co, recordKey))
              .map(ConventionResolver::resolve)
              .map(item -> ConventionResolver.resolveComplexType(item, modelFormatMap))
              .flatMap(Optional::stream)
              .forEach(model -> {

                  resource.put("serialVersionUID", calculateSerialVersionUid(model.vars));
                  processEnumVarsForAll(responseModels, co, model, resourceName, finalEnumAliasDict);

              });

            results.put("recordKey", getRecordKey(opList, this.allModels));
            List<String> packagePaths = Arrays.asList(Arrays.copyOfRange(filePathArray,0 , filePathArray.length-1))
                    .stream().map(String::toLowerCase).collect(Collectors.toList());
            if (packagePaths.isEmpty()) {
                resource.put("packageSubPart", "");
            } else {
                String packagePath = packagePaths.stream().map(String::toLowerCase).collect(Collectors.joining("."));
                resource.put("packageSubPart", "."+packagePath);
            }
        }

        for (final Map<String, Object> resource : resources.values()) {
            resource.put("responseModel", getConcatenatedResponseModel(responseModels));
            flattenStringMap(resource, "models");
        }

        results.put("resources", resources.values());

        return results;
    }

    private Object getEnumAliasDict(Object extensionMapAtResourceLevelPassed, Map<String, Object> ops) {
        final Map<String, Object> extensionMapAtResourceLevel = (Map<String, Object>) extensionMapAtResourceLevelPassed;
        Map<String, String> enumAliasDict = new HashMap<String, String>(){};
        LinkedHashMap<String, Object> extensions = null;
        Object keyNametoFetchExtensionsAtResourceLevel = ops.get("classname");

        if(!extensionMapAtResourceLevel.containsKey(keyNametoFetchExtensionsAtResourceLevel)){
            keyNametoFetchExtensionsAtResourceLevel = ops.get("pathPrefix");
        }
        extensions = (LinkedHashMap<String, Object>) extensionMapAtResourceLevel.get(keyNametoFetchExtensionsAtResourceLevel);
        LinkedHashMap<String, Object> xtwilioExtensions = null;
        if(extensions != null)
            xtwilioExtensions = (LinkedHashMap<String, Object>) extensions.get("x-twilio");

        if(xtwilioExtensions != null && xtwilioExtensions.containsKey("enumAliases") ){
            enumAliasDict = (Map<String, String>) xtwilioExtensions.get("enumAliases");
        }

        return enumAliasDict;
    }

    private void processEnumVarsForAll(List<CodegenModel> responseModels, CodegenOperation co, CodegenModel model, String resourceName, Map<String, String> enumAliasDict) {
        model.vars.forEach(item -> {
            if(item.isEnum){
                if(enumAliasDict.containsKey(item.baseName)){
                    item = renameItem(item, enumAliasDict);
                }
            }

        });
        for (CodegenParameter param : co.allParams) {
                if(param.isEnum){
                    String aliasName = getAliasNameForEnum(enumAliasDict, param.paramName);
                    final String camelizedAliasName = StringUtils.camelize(aliasName, false);
                    Optional<CodegenProperty> alreadyExisting = model.vars.stream().filter(item -> item.name.equalsIgnoreCase(camelizedAliasName)).findFirst();
                    if(!alreadyExisting.isPresent()){
                        model.vars.add(createCodeGenPropertyFromParameter(param));
                    }
                }
            }

        model.vars.forEach(item -> {
            if(item.isEnum){
                    item.dataType = generateDataType(resourceName, item.nameInCamelCase, enumAliasDict);
                    item.vendorExtensions.put("x-is-other-data-type", true);
            }

        });
        responseModels.add(model);
        co.queryParams.forEach(param -> processEnumVars(param, model, resourceName, enumAliasDict));
        co.formParams.forEach(param -> processEnumVars(param, model, resourceName, enumAliasDict));
        co.allParams.forEach(param -> processEnumVars(param, model, resourceName, enumAliasDict));
        co.headerParams.forEach(param -> processEnumVars(param, model, resourceName, enumAliasDict));
        co.requiredParams.forEach(param -> processEnumVars(param, model, resourceName, enumAliasDict));
    }

    private CodegenProperty renameItem(CodegenProperty item, Map<String, String> enumAliasDict){
        String camelizedName = StringUtils.camelize(enumAliasDict.get(item.baseName), false);
        item.getter = "get"+ camelizedName;
        item.setter = "set"+ camelizedName;
        item.datatypeWithEnum = camelizedName + "Enum";
        item.name = StringUtils.camelize(enumAliasDict.get(item.baseName),true);
        item.nameInCamelCase = camelizedName;
        item.nameInSnakeCase = camelizedName;
        item.enumName = camelizedName + "Enum";
        item.baseName = enumAliasDict.get(item.baseName);

        return item;
    }

    private CodegenProperty createCodeGenPropertyFromParameter(CodegenParameter co) {
        CodegenProperty property = new CodegenProperty();
        property.isEnum = co.isEnum;
        property.baseName = co.baseName;
        property.allowableValues = co.allowableValues;
        property.dataType = co.dataType;
        property.vendorExtensions = co.vendorExtensions;
        property.datatypeWithEnum = co.datatypeWithEnum;
        property.complexType = co.getComplexType();


        property.name = co.paramName;
        property.nameInCamelCase = co.baseName.replaceAll("-","");
        property.nameInSnakeCase = co.baseName.replaceAll("-","_").toLowerCase();
        return property;
    }

    private List<CodegenParameter> getNonPathParams(List<CodegenParameter> allParams) {
       return allParams.stream().filter(param -> !param.isPathParam).collect(Collectors.toList());
    }

    private CodegenModel getConcatenatedResponseModel(List<CodegenModel> responseModels) {
        CodegenModel codegenModel = new CodegenModel();
        codegenModel.allowableValues = new HashMap<>();
        List<CodegenProperty> codegenProperties = new ArrayList<>();
        if(responseModels.isEmpty()) {
            return null;
        }
        for (CodegenModel resModel : responseModels) {
                codegenModel.hasEnums = codegenModel.hasEnums || resModel.hasEnums;
                codegenModel.isEnum = codegenModel.isEnum || ( resModel.isEnum );
                if (resModel.allowableValues != null) {
                    resModel.allowableValues.forEach(
                            (key, value) -> codegenModel.allowableValues.merge(key, value, (oldValue, newValue) -> newValue));
                }
                for (CodegenProperty modelProp : resModel.vars) {
                        boolean contains = false;
                        for (CodegenProperty property : codegenProperties) {
                                if (Arrays.stream(modelProp.baseName.split("_")).
                                        map(StringUtils::camelize).collect(Collectors.joining()).equals(property.baseName)) {
                                        contains = true;
                                    }
                            }
                        if (!contains) {
                                modelProp.baseName = Arrays.stream(modelProp.baseName.split("_")).
                                        map(StringUtils::camelize).collect(Collectors.joining());
                                codegenProperties.add(modelProp);
                            }
                    }
            }

        codegenModel.setVars(codegenProperties);
        return codegenModel;
    }

    private Optional<CodegenModel> getModelCoPath(final String modelName, CodegenOperation codegenOperation, String recordKey) {
        if (codegenOperation.vendorExtensions.containsKey("x-is-read-operation") && (boolean)codegenOperation.vendorExtensions.get("x-is-read-operation")) {
                Optional<CodegenModel> coModel = allModels.stream().filter(model -> model.getClassname().equals(modelName)).findFirst();
                if (!coModel.isPresent()) {
                        return Optional.empty();
                    }
                CodegenProperty property = coModel.get().vars.stream().filter(prop -> prop.baseName.equals(recordKey)).findFirst().get();
                Optional<CodegenModel> complexModel = allModels.stream().filter(model -> model.getClassname().equals(property.complexType)).findFirst();
                return complexModel;
            }
        return allModels.stream().filter(model -> model.getClassname().equals(modelName)).findFirst();
    }

    private String getAliasNameForEnum(Map<String, String> enumAliasDict, String name){
        String result = name;
        String processedItemName = StringUtils.camelize(name, false);
        for (Map.Entry<String,String> entry : enumAliasDict.entrySet()){
            if(StringUtils.camelize(entry.getKey(), false).equalsIgnoreCase(processedItemName)){
                result = entry.getValue();
            }
        }
        return result;
    }

    private String generateDataType(String resourceName, String nameInCamelCase, Map<String,String> enumAliases){
        String name = nameInCamelCase;
        String dataType = "";
        String nameArr[] = name.split("[.]", 0);
        if(nameArr.length > 1){
            String itemName = String.join("", nameArr);
            itemName = getAliasNameForEnum(enumAliases, itemName);
            dataType = resourceName + "." + StringUtils.camelize(itemName, false);
        }
        else{
            name = getAliasNameForEnum(enumAliases, name);
            dataType = resourceName + "." + StringUtils.camelize(name, false);

        }
        return dataType;
    }

    private CodegenParameter processEnumVars(CodegenParameter param, CodegenModel model, String resourceName, Map<String, String> enumAliases) {
        if(param.isEnum){
            model.vars.forEach(item -> {
                if(param.paramName.equalsIgnoreCase(item.nameInCamelCase) && item.isEnum == true){
                    String baseType = generateDataType(resourceName, item.nameInCamelCase, enumAliases);
                    if(param.isArray){
                        param.dataType = "List<"+ baseType +">";
                        param.baseType = baseType;
                    }
                    else{
                        param.dataType = baseType;
                    }
                    param.vendorExtensions.put("x-is-other-data-type", true);
                }
            });
        }
        return param;
    }

    private String getRecordKey(List<CodegenOperation> opList, List<CodegenModel> models) {
        String recordKey =  "";
        for (CodegenOperation co: opList) {
            for(CodegenModel model: models) {
                if(model.name.equals(co.returnType)) {
                    recordKey = model.allVars
                            .stream()
                            .filter(v -> v.openApiType.equals("array"))
                            .collect(Collectors.toList()).get(0).baseName;
                }
            }
        }
        return recordKey;
    }

    @AllArgsConstructor
    private enum Operation {
        CREATE("create"),
        FETCH("fetch"),
        UPDATE("update"),
        DELETE("delete");

        private final String prefix;
    }

    /**
     * keep track of signature list that would contain different combinations of signatures for constructor generation (since Account sid is optional, so different constructor are needed)
     * @param resource
     * @param co
     * @param isVersionV2010
     * @return
     */
    private ArrayList<List<CodegenParameter>> generateSignatureList(final Map<String, Object> resource, final CodegenOperation co, boolean isVersionV2010) {
        CodegenParameter accountSidParam = null;
        List<List<CodegenParameter>> conditionalCodegenParam = new ArrayList<>();
        if (isVersionV2010) {
            Optional<CodegenParameter> optionalParam = co.allParams.stream()
            .filter(param -> param.vendorExtensions.containsKey("x-is-account-sid")).findAny();
            if(optionalParam.isPresent()){
                accountSidParam = optionalParam.get();
            }
            /**
             * structure for vendorExtensions
             * @<code> x-twilio:
             *          conditional:
             *           - - from
             *             - messaging_service_sid
             *           - - body
             *             - media_url</code>
             */
            if(co.vendorExtensions.containsKey("x-twilio")) {
                HashMap<String, Object> twilioVendorExtension = (HashMap<String, Object>) co.vendorExtensions.get("x-twilio");
                if(twilioVendorExtension.containsKey("conditional")) {
                    List<List<String>> conditionalParams = (List<List<String>>) twilioVendorExtension.get("conditional");
                    // map the conditional param names with the codegenParameter added in optional params
                    conditionalCodegenParam = conditionalParams.stream().map(
                            paramList -> paramList.stream().map(
                                    cp -> co.optionalParams.stream().filter(
                                            op -> op.paramName.equals(StringUtils.camelize(cp, true))
                                    ).findAny().get()
                            ).collect(Collectors.toList())).collect(Collectors.toList());
                    // added filter to prevent same signature types
                    conditionalCodegenParam = conditionalCodegenParam.stream().filter(cpList -> (cpList.size() <=1 || !cpList.get(0).dataType.equals(cpList.get(1).dataType))).collect(Collectors.toList());
                }
            }
        }
        conditionalCodegenParam = Lists.cartesianProduct(conditionalCodegenParam);
        ArrayList<List<CodegenParameter>> signatureList = new ArrayList<>();
        for(List<CodegenParameter> paramList : conditionalCodegenParam){
            signatureList.add(addAllToList(co.requiredParams, paramList));
            if( accountSidParam != null) {
                signatureList.add(addAllToList(List.of(accountSidParam), co.requiredParams, paramList));
            }
        }
        return signatureList;
    }

    private <T> List<T> addAllToList(List<T> ... list) {
        return Arrays.stream(list).flatMap(List<T>::stream).collect(Collectors.toList());
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

    private void setObjectFormatMap(final List<CodegenModel> allModels) {
        allModels.forEach(item -> {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(item.modelJson);
                if (jsonNode.get("type").textValue().equals("object") && jsonNode.has("format")) {
                    modelFormatMap.put(item.classFilename, jsonNode.get("format").textValue());
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void updateCodeOperationParams(final CodegenOperation co) {
        co.allParams = co.allParams
                .stream()
                .map(ConventionResolver::resolveParameter)
                .map(Optional::get)
                .collect(Collectors.toList());
        co.pathParams = co.pathParams
                .stream()
                .map(ConventionResolver::resolveParameter)
                .map(Optional::get)
                .collect(Collectors.toList());
        co.pathParams.stream().
                map(ConventionResolver::resolveParamTypes)
                .forEach(param -> param.paramName = "path"+param.paramName);
        co.queryParams = co.queryParams.stream().map(ConventionResolver::resolveParamTypes)
                .map(ConventionResolver::prefixedCollapsibleMap)
                .collect(Collectors.toList());
        co.queryParams = preProcessQueryParameters(co);
        co.formParams = co.formParams.stream().map(ConventionResolver::resolveParamTypes)
                .map(ConventionResolver::prefixedCollapsibleMap)
                .collect(Collectors.toList());
        co.formParams = preProcessFormParams(co);
        co.headerParams = co.headerParams.stream().map(ConventionResolver::resolveParamTypes)
                .map(ConventionResolver::prefixedCollapsibleMap)
                .collect(Collectors.toList());
        co.optionalParams = co.optionalParams
                .stream()
                .map(ConventionResolver::resolveParameter)
                .map(Optional::get)
                .collect(Collectors.toList());
        co.requiredParams = co.requiredParams
                .stream()
                .map(ConventionResolver::resolveParameter)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private List<CodegenParameter> preProcessFormParams(CodegenOperation co) {
        processDataTypesForParams(co.formParams);
        for(CodegenParameter e : co.formParams){
            if(e.dataType.equalsIgnoreCase(URI)) {
                e.vendorExtensions.put("x-is-uri-param",true);
            }
        }
        return co.formParams;
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
