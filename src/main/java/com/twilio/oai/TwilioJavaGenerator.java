package com.twilio.oai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.java.JavaCaseResolver;
import com.twilio.oai.mlambdas.ReplaceHyphenLambda;
import com.twilio.oai.resource.ResourceMap;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.AllArgsConstructor;
import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.JavaClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;
import org.openapitools.codegen.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.google.common.collect.ImmutableMap;
import com.samskivert.mustache.Mustache.Lambda;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;
import static com.twilio.oai.resource.Resource.TWILIO_EXTENSION_NAME;

public class TwilioJavaGenerator extends JavaClientCodegen {

    private static final int OVERFLOW_CHECKER = 32;
    private static final int BASE_SIXTEEN = 16;
    private static final int BIG_INTEGER_CONSTANT = 1;
    private static final int SERIAL_UID_LENGTH = 12;
    public static final String URI = "uri";

    private final TwilioCodegenAdapter twilioCodegen;
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(new ResourceMap(
        new Inflector()), new JavaCaseResolver());
    private final List<CodegenModel> allModels = new ArrayList<>();
    private final Map<String, String> modelFormatMap = new HashMap<>();

    public TwilioJavaGenerator() {
        super();

        twilioCodegen = new TwilioCodegenAdapter(this, getName());

        sourceFolder = "";

        // Skip automated api test and doc generation
        apiTestTemplateFiles.clear();
        apiDocTemplateFiles.clear();
    }

    @Override
    public void processOpts() {
        super.processOpts();

        twilioCodegen.processOpts();

        apiTemplateFiles.put("api.mustache", ".java");
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        final String domain = twilioCodegen.getDomainFromOpenAPI(openAPI);
        twilioCodegen.setDomain(domain);

        directoryStructureService.configure(openAPI, additionalProperties);
    }

    @Override
    public String toParamName(final String name) {
        return super.toVarName(twilioCodegen.toParamName(name));
    }

    @Override
    public void postProcessParameter(final CodegenParameter parameter) {
        super.postProcessParameter(parameter);
        if (parameter.dataType.startsWith("List<") && parameter.dataType.contains("Enum")) {
            parameter.vendorExtensions.put("refEnum", true);
            String[] value = parameter.dataType.split("Enum");
            String lastValue = value[value.length-1];
            parameter.dataType = "List<"+lastValue;
            parameter.baseType = lastValue.substring(0, lastValue.length()-1);
        } else if(parameter.dataType.contains("Enum")) {
             parameter.vendorExtensions.put("refEnum", true);
            String[] value = parameter.dataType.split("Enum");
            parameter.dataType = value[value.length-1];
            parameter.baseType = value[value.length-1];
        }
        else if (parameter.isEnum) {
            parameter.enumName = parameter.paramName;
        }
        // Make sure required non-path params get into the options block.
        parameter.paramName = StringUtils.camelize(parameter.paramName, true);
    }

    @Override
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);
        if (property.dataType.startsWith("List<") && property.dataType.contains("Enum")) {
            property.vendorExtensions.put("refEnum", true);
            String[] value = property.dataType.split("Enum");
            String lastValue = value[value.length-1];
            property.dataType = "List<" + lastValue;
            property.complexType = lastValue.substring(0, lastValue.length()-1);
            property.baseType = lastValue.substring(0, lastValue.length()-1);
             property.isEnum = true;
            property.allowableValues = property.items.allowableValues;
            property._enum = (List<String>) property.items.allowableValues.get("values");
        } else if (property.dataType.contains("Enum")) {
            property.vendorExtensions.put("refEnum", true);
            String[] value = property.dataType.split("Enum");
            property.dataType = value[value.length - 1];
            property.complexType = property.dataType;
            property.baseType = property.dataType;
            property.isEnum = true;
            property._enum = (List<String>) property.allowableValues.get("values");
        } else if (property.isEnum ) {
            property.enumName = property.baseName;
        }
        property.isEnum =  property.isEnum && property.dataFormat == null;
    }

    @Override
    public String toApiFilename(final String name) {
        return directoryStructureService.toApiFilename(super.toApiFilename(name));
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
        queryParamNames.sort(Collections.reverseOrder());
        for(CodegenParameter e : co.queryParams){
            String afterName = e.paramName + "After";
            String beforeName = e.paramName + "Before";
            if(queryParamNames.contains(afterName) && queryParamNames.contains(beforeName)){
                e.vendorExtensions.put("x-has-before-and-after", true);
                queryParamNames.remove(afterName);
                queryParamNames.remove(beforeName);
            }
        }
        List<CodegenParameter> finalQueryParamList = new ArrayList<>();
        for (CodegenParameter e : co.queryParams) {
            if (queryParamNames.contains(e.paramName)) {
                finalQueryParamList.add(e);
            }
        }
        processDataTypesForParams(finalQueryParamList);
        return finalQueryParamList;
    }


    @Override
    public Map<String, ModelsMap> postProcessAllModels(final Map<String, ModelsMap> allModels) {
        final Map<String, ModelsMap> results = super.postProcessAllModels(allModels);

        for (final ModelsMap mods : results.values()) {
            final List<ModelMap> modList = mods.getModels();

            // Add all the models to the local models list.
            modList
                    .stream()
                    .map(ModelMap::getModel)
                    .map(CodegenModel.class::cast)
                    .collect(Collectors.toCollection(() -> this.allModels));
        }
        setObjectFormatMap(this.allModels);
        // Return an empty collection so no model files get generated.
        return new HashMap<>();
    }


    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);

        final Map<String, Map<String, Object>> resources = new LinkedHashMap<>();

        final OperationMap ops = results.getOperations();
        final List<CodegenOperation> opList = ops.getOperation();
        String recordKey = getRecordKey(opList, this.allModels);
        List<CodegenModel> responseModels = new ArrayList<>();
        apiTemplateFiles.remove("updater.mustache");
        apiTemplateFiles.remove("creator.mustache");
        apiTemplateFiles.remove("deleter.mustache");
        apiTemplateFiles.remove("reader.mustache");
        apiTemplateFiles.remove("fetcher.mustache");
        resetAllModelVendorExtensions();

        // iterate over the operation and perhaps modify something
        for (final CodegenOperation co : opList) {
            // Group operations by resource.
            String path = co.path;
            List<String> filePathArray = new ArrayList<>(Arrays.asList(co.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
            String resourceName = filePathArray.remove(filePathArray.size()-1);
            final Map<String, Object> resource = resources.computeIfAbsent(resourceName, k -> new LinkedHashMap<>());
            populateCrudOperations(resource, co);
            updateCodeOperationParams(co, resourceName);
            if (co.nickname.startsWith("update")) {
                resource.put("hasUpdate", true);
                addOperationName(co, "Update");
                co.vendorExtensions.put("x-is-update-operation", true);
                resource.put("signatureListUpdate", generateSignatureList(co));
                apiTemplateFiles.put("updater.mustache", "Updater.java");
            } else if (co.nickname.startsWith("delete")) {
                resource.put("hasDelete", true);
                addOperationName(co, "Remove");
                co.vendorExtensions.put("x-is-delete-operation", true);
                resource.put("signatureListDelete", generateSignatureList(co));
                apiTemplateFiles.put("deleter.mustache", "Deleter.java");
                addDeleteHeaderEnums(co, responseModels);
            } else if (co.nickname.startsWith("create")) {
                resource.put("hasCreate", true);
                co.vendorExtensions.put("x-is-create-operation", true);
                addOperationName(co, "Create");
                resource.put("signatureListCreate", generateSignatureList(co));
                apiTemplateFiles.put("creator.mustache", "Creator.java");
            } else if (co.nickname.startsWith("fetch")) {
                resource.put("hasFetch", true);
                resource.put("signatureListFetch", generateSignatureList(co));
                co.vendorExtensions.put("x-is-fetch-operation", true);
                addOperationName(co, "Fetch");
                apiTemplateFiles.put("fetcher.mustache", "Fetcher.java");
            } else {
                resource.put("hasRead", true);
                co.vendorExtensions.put("x-is-read-operation", true);
                addOperationName(co, "Page");
                resource.put("signatureListRead", generateSignatureList(co));
                apiTemplateFiles.put("reader.mustache", "Reader.java");

            }

            final ArrayList<CodegenOperation> resourceOperationList =
                    (ArrayList<CodegenOperation>) resource.computeIfAbsent(
                            "operations",
                            k -> new ArrayList<>());
            resourceOperationList.add(co);
            resource.put("path", path);
            resource.put("resourceName", resourceName);
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
                  CodegenModel responseModel = processEnumVarsForAll(model, co, resourceName);
                  responseModels.add(responseModel);
              });

            results.put("recordKey", getRecordKey(opList, this.allModels));

            if (directoryStructureService.isVersionLess(additionalProperties)) {
                resource.put("apiVersion", StringUtils.camelize(PathUtils.getFirstPathPart(co.path), true));
            }
            if (!filePathArray.isEmpty()) {
                final String packagePath = filePathArray
                    .stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.joining("."));
                resource.put("packageSubPart", "." + packagePath);
            }
        }

        for (final Map<String, Object> resource : resources.values()) {
            resource.put("responseModel", getConcatenatedResponseModel(responseModels));
            PathUtils.flattenStringMap(resource, "models");
        }

        results.put("resources", resources.values());

        return results;
    }

    private void resetAllModelVendorExtensions() {
        allModels.forEach(item -> item.getVendorExtensions().remove("enumVars"));
    }

    private void addDeleteHeaderEnums(CodegenOperation co, List<CodegenModel> responseModels) {
        List<CodegenProperty> codegenProperties = new ArrayList<>();
        for (CodegenParameter cp: co.allParams) {
            if (cp.isEnum && cp.isHeaderParam) {
                codegenProperties.add(createCodeGenPropertyFromParameter(cp));
            }
        }
        if (!codegenProperties.isEmpty()) {
            CodegenModel codegenModel = new CodegenModel();
            codegenModel.vendorExtensions.put("enumVars", codegenProperties);
            responseModels.add(codegenModel);
        }
    }

    private CodegenModel processEnumVarsForAll(CodegenModel model, CodegenOperation co,  String resourceName) {
        List<CodegenProperty> enumProperties = new ArrayList<>();
        model.vars.forEach(item -> {
            if (item.isEnum && item.dataFormat == null && !item.dataType.contains(resourceName+ ".")) {
                if (item.vendorExtensions.containsKey("refEnum")) {
                    if (item.containerType != null && item.containerType.equals("array")) {
                        item.enumName = item.baseType;
                        item.dataType = "List<"+resourceName +"." + item.complexType+">";
                    } else {
                        item.enumName = item.baseType;
                        String[] values = item.dataType.split("\\.");
                        item.dataType = resourceName + "." + values[values.length -1];
                    }
                } else {
                    String baseName = Arrays.stream(item.baseName.split("_")).map(StringUtils::camelize)
                            .collect(Collectors.joining());
                    item.enumName = baseName;
                    if (item.containerType != null && item.containerType.equals("array")) {
                        item.dataType = "List<"+ resourceName + "." + baseName + ">";
                        item.baseType = resourceName + "." + baseName;
                    } else {
                        item.dataType = resourceName + "." + baseName;
                    }

                }
                item.vendorExtensions.put("x-is-other-data-type", true);
            }
        });
        for (CodegenProperty codegenProperty: model.vars) {
            if (codegenProperty.isEnum && codegenProperty.dataFormat == null) {
                enumProperties.add(codegenProperty);
            }
        }
        for (CodegenParameter param : co.allParams) {
            if (param.isEnum) {
                Optional<CodegenProperty> alreadyAdded = enumProperties.stream().
                        filter(item -> item.enumName.equalsIgnoreCase(param.enumName)).findFirst();
                if (alreadyAdded.isEmpty() && param.dataFormat == null) {
                    enumProperties.add(createCodeGenPropertyFromParameter(param));
                }
            }
        }
        TreeSet<CodegenProperty> ts = getEnumPropertyComparator();
        ts.addAll(enumProperties);
        if (model.getVendorExtensions().get("enumVars") != null) {
            List<CodegenProperty> codegenProperties = (List<CodegenProperty>) model.getVendorExtensions().get("enumVars");
            ts.addAll(codegenProperties);
        }
        model.vendorExtensions.put("enumVars",new ArrayList<>(ts));
        return model;
    }

    private TreeSet<CodegenProperty> getEnumPropertyComparator() {
        return new TreeSet<>((cp1, cp2) -> cp1.enumName.compareTo(cp2.getEnumName()));
    }

    private CodegenProperty createCodeGenPropertyFromParameter(CodegenParameter co) {
        CodegenProperty property = new CodegenProperty();
        property.isEnum = co.isEnum;
        property.baseName = co.baseName;
        property.enumName = co.enumName;
        property.allowableValues = co.allowableValues;
        property.dataType = co.dataType;
        property.vendorExtensions = co.vendorExtensions;
        property.datatypeWithEnum = co.datatypeWithEnum;
        property.complexType = co.getComplexType();
        property.name = co.paramName;
        property.nameInCamelCase = co.baseName.replace("-","");
        property.nameInSnakeCase = co.baseName.replace("-","_").toLowerCase();
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
                if (resModel.vendorExtensions != null && resModel.vendorExtensions.size() > 0) {
                    List<CodegenProperty> resEnum1 = (List<CodegenProperty>)resModel.vendorExtensions.get("enumVars");
                    List<CodegenProperty> codeModelEnums = (List<CodegenProperty>)codegenModel.vendorExtensions.get("enumVars");
                    if (codeModelEnums != null) {
                        for (CodegenProperty resCodegenProperty: resEnum1) {
                            boolean found = false;
                            for (CodegenProperty codeEnumProperty: codeModelEnums) {
                                if (codeEnumProperty.enumName.equals(resCodegenProperty.getEnumName())) {
                                    found = true;
                                }
                            }
                            if (!found) {
                                codeModelEnums.add(resCodegenProperty);
                            }
                        }
                        resModel.vendorExtensions.put("enumVars", codeModelEnums);
                    }
                    resModel.vendorExtensions.forEach(
                            (key, value) -> codegenModel.vendorExtensions.merge(key, value, (oldValue, newValue) -> newValue));
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
                if (coModel.isEmpty()) {
                        return Optional.empty();
                    }
                CodegenProperty property = coModel.get().vars.stream().filter(prop -> prop.baseName.equals(recordKey)).findFirst().get();
            return allModels.stream().filter(model -> model.getClassname().equals(property.complexType)).findFirst();
            }
        return allModels.stream().filter(model -> model.getClassname().equals(modelName)).findFirst();
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
     */
    @SuppressWarnings("unchecked")
    private ArrayList<List<CodegenParameter>> generateSignatureList(final CodegenOperation co) {
        CodegenParameter accountSidParam = null;
        List<List<CodegenParameter>> conditionalCodegenParam = new ArrayList<>();
        Optional<CodegenParameter> optionalParam = co.allParams.stream()
                .filter(param -> param.vendorExtensions.containsKey("x-is-account-sid")).findAny();
        if(optionalParam.isPresent()){
            accountSidParam = optionalParam.get();
        }
        /*
         * structure for vendorExtensions
         * @<code> x-twilio:
         *          conditional:
         *           - - from
         *             - messaging_service_sid
         *           - - body
         *             - media_url</code>
         */
        if (co.vendorExtensions.containsKey(TWILIO_EXTENSION_NAME)) {
            HashMap<String, Object> twilioVendorExtension = (HashMap<String, Object>) co.vendorExtensions.get(TWILIO_EXTENSION_NAME);
            if (twilioVendorExtension.containsKey("conditional")) {
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
            if (PathUtils.getStringMap(resource, "models").putIfAbsent(model.getClassname(), model) == null) {
                model.getVars().forEach(property -> addModel(resource, property.dataType));
            }
        });
    }

    private Optional<CodegenModel> getModel(final String modelName) {
        return allModels.stream().filter(model -> model.getClassname().equals(modelName)).findFirst();
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

        ArrayList<String> sortedKeys = new ArrayList<>(propertyMap.keySet());
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

    private void updateCodeOperationParams(final CodegenOperation co, String resourceName) {
        co.allParams = co.allParams
                .stream()
                .map(ConventionResolver::resolveParameter)
                .map(Optional::get)
                .map(item -> this.resolveEnumParameter(item, resourceName))
                .collect(Collectors.toList());
        co.pathParams = co.pathParams
                .stream()
                .map(ConventionResolver::resolveParameter)
                .map(Optional::get)
                .map(item -> this.resolveEnumParameter(item, resourceName))
                .collect(Collectors.toList());
        co.pathParams.stream().
                map(ConventionResolver::resolveParamTypes)
                .map(item -> this.resolveEnumParameter(item, resourceName))
                .forEach(param -> param.paramName = "path"+param.paramName);
        co.queryParams = co.queryParams.stream().map(ConventionResolver::resolveParameter)
                .map(Optional::get)
                .map(ConventionResolver::prefixedCollapsibleMap)
                .map(item -> this.resolveEnumParameter(item, resourceName))
                .collect(Collectors.toList());
        co.queryParams = preProcessQueryParameters(co);
        co.formParams = co.formParams.stream().map(ConventionResolver::resolveParameter)
                .map(Optional::get)
                .map(ConventionResolver::prefixedCollapsibleMap)
                .map(item -> this.resolveEnumParameter(item, resourceName))
                .collect(Collectors.toList());
        co.formParams = preProcessFormParams(co);
        co.headerParams = co.headerParams.stream().map(ConventionResolver::resolveParameter)
                .map(Optional::get)
                .map(ConventionResolver::prefixedCollapsibleMap)
                .map(item -> this.resolveEnumParameter(item, resourceName))
                .collect(Collectors.toList());
        co.optionalParams = co.optionalParams
                .stream()
                .map(ConventionResolver::resolveParameter)
                .map(Optional::get)
                .map(item -> this.resolveEnumParameter(item, resourceName))
                .collect(Collectors.toList());
        co.requiredParams = co.requiredParams
                .stream()
                .map(ConventionResolver::resolveParameter)
                .map(Optional::get)
                .map(item -> this.resolveEnumParameter(item, resourceName))
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
    protected ImmutableMap.Builder<String, Lambda> addMustacheLambdas() {
        ImmutableMap.Builder<String, Lambda> lambdaBuilder = super.addMustacheLambdas();
        lambdaBuilder.put("replacehyphen", new ReplaceHyphenLambda());
        return lambdaBuilder;
    }

    private CodegenParameter resolveEnumParameter(CodegenParameter parameter, String resourceName) {
        if( parameter.isEnum && !parameter.vendorExtensions.containsKey("refEnum")) {
            parameter.enumName = Arrays.stream(parameter.enumName.split("_")).map(StringUtils::camelize)
                    .collect(Collectors.joining());
            if (parameter.items != null && parameter.items.allowableValues != null && parameter.items.allowableValues.containsKey("values")) {
                parameter.dataType = "List<" + resourceName+"."+ parameter.enumName + ">";
                parameter.baseType = resourceName + "." + parameter.enumName;
            } else {
                parameter.dataType = resourceName+"."+ parameter.enumName;
            }

            return parameter;
        }
        if (parameter.items != null && parameter.items.allowableValues != null && parameter.items.allowableValues.containsKey("values") ) {
            parameter.isEnum = true;
            parameter.enumName = parameter.baseType;
            parameter._enum = (List<String>) parameter.items.allowableValues.get("values");
            parameter.dataType = "List<" + resourceName + "." + parameter.baseType + ">";
            parameter.baseType = resourceName + "." + parameter.baseType;
            parameter.allowableValues = parameter.items.allowableValues;
        }
        if (parameter.allowableValues != null && parameter.allowableValues.containsKey("enumVars")) {
            parameter.isEnum = true;
            parameter._enum = (List<String>) parameter.allowableValues.get("values");
            parameter.enumName = parameter.dataType;
            parameter.dataType=resourceName+"."+parameter.dataType;
        }
        return parameter;
    }

    @Override
    public String getName() {
        return EnumConstants.Generator.TWILIO_JAVA.getValue();
    }

    @Override
    public String getHelp() {
        return "Generates the twilio-java helper library.";
    }
}
