package com.twilio.oai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.mlambdas.ReplaceHyphenLambda;
import com.twilio.oai.resolver.java.JavaCaseResolver;
import com.twilio.oai.resolver.java.JavaConventionResolver;
import com.twilio.oai.resource.ResourceMap;

import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.JavaClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.google.common.collect.ImmutableMap;
import com.samskivert.mustache.Mustache.Lambda;

import static com.twilio.oai.common.ApplicationConstants.*;

public class TwilioJavaGenerator extends JavaClientCodegen {

    private static final int OVERFLOW_CHECKER = 32;
    private static final int BASE_SIXTEEN = 16;
    private static final int BIG_INTEGER_CONSTANT = 1;
    private static final int SERIAL_UID_LENGTH = 12;
    private static final String URI = "uri";
    private static final String ENUM_VARS = "enumVars";
    private static final String VALUES = "values";

    private final TwilioCodegenAdapter twilioCodegen;
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
        additionalProperties,
        new ResourceMap(new Inflector()),
        new JavaCaseResolver());
    private final List<CodegenModel> allModels = new ArrayList<>();
    private final Map<String, String> modelFormatMap = new HashMap<>();
    private final JavaConventionResolver conventionResolver = new JavaConventionResolver(getConventionMap());

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

        directoryStructureService.configure(openAPI);
    }

    @Override
    public String toParamName(final String name) {
        return super.toVarName(twilioCodegen.toParamName(name));
    }

    @Override
    public void postProcessParameter(final CodegenParameter parameter) {
        super.postProcessParameter(parameter);
        if (parameter.dataType.startsWith(LIST_START) && parameter.dataType.contains("Enum")) {
            parameter.vendorExtensions.put(REF_ENUM_EXTENSION_NAME, true);
            String[] value = parameter.dataType.split("Enum");
            String lastValue = value[value.length-1];
            parameter.dataType = LIST_START+lastValue;
            parameter.baseType = lastValue.substring(0, lastValue.length()-1);
        } else if(parameter.dataType.contains("Enum")) {
             parameter.vendorExtensions.put(REF_ENUM_EXTENSION_NAME, true);
            String[] value = parameter.dataType.split("Enum");
            parameter.dataType = value[value.length-1];
            parameter.baseType = value[value.length-1];
        }
        else if (parameter.isEnum) {
            parameter.enumName = parameter.paramName;
        } else {
            if (parameter.isPathParam) {
                parameter.paramName = "Path" + parameter.paramName.substring(0, 1).toUpperCase() + parameter.paramName.substring(1);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);
        if (property.dataType.startsWith(LIST_START) && property.dataType.contains("Enum")) {
            property.vendorExtensions.put(REF_ENUM_EXTENSION_NAME, true);
            String[] value = property.dataType.split("Enum");
            String lastValue = value[value.length-1];
            property.dataType = LIST_START + lastValue;
            property.complexType = lastValue.substring(0, lastValue.length()-1);
            property.baseType = lastValue.substring(0, lastValue.length()-1);
             property.isEnum = true;
            property.allowableValues = property.items.allowableValues;
            property._enum = (List<String>) property.items.allowableValues.get(VALUES);
        } else if (property.dataType.contains("Enum")) {
            property.vendorExtensions.put(REF_ENUM_EXTENSION_NAME, true);
            String[] value = property.dataType.split("Enum");
            property.dataType = value[value.length - 1];
            property.complexType = property.dataType;
            property.baseType = property.dataType;
            property.isEnum = true;
            property._enum = (List<String>) property.allowableValues.get(VALUES);
        } else if (property.isEnum ) {
            property.enumName = property.baseName;
        }
        property.isEnum = property.isEnum && property.dataFormat == null;
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
     * Function to pre-process query parameters
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

        Utility.addModelsToLocalModelList(results, this.allModels);
        Utility.setComplexDataMapping(this.allModels, this.modelFormatMap);
        // Return an empty collection so no model files get generated.
        return new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        final List<CodegenOperation> opList = directoryStructureService.processOperations(results);
        final String recordKey = directoryStructureService.getRecordKey(opList, this.allModels);
        results.put("recordKey", recordKey);

        final Map<String, Map<String, Object>> resources = new LinkedHashMap<>();

        List<CodegenModel> responseModels = new ArrayList<>();
        apiTemplateFiles.remove("updater.mustache");
        apiTemplateFiles.remove("creator.mustache");
        apiTemplateFiles.remove("deleter.mustache");
        apiTemplateFiles.remove("reader.mustache");
        apiTemplateFiles.remove("fetcher.mustache");
        resetAllModelVendorExtensions();

        String tempName = opList.get(0).baseName;
        // iterate over the operation and perhaps modify something
        for (final CodegenOperation co : opList) {
            // Group operations by resource.
            String path = co.path;
            List<String> filePathArray = new ArrayList<>(Arrays.asList(co.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
            String resourceName = filePathArray.remove(filePathArray.size()-1);
            final Map<String, Object> resource = resources.computeIfAbsent(resourceName, k -> new LinkedHashMap<>());
            twilioCodegen.populateCrudOperations(resource, co);
            updateCodeOperationParams(co, resourceName);
            if (co.nickname.startsWith("update")) {
                resource.put("hasUpdate", true);
                addOperationName(co, "Update");
                resource.put("signatureListUpdate", generateSignatureList(co));
                apiTemplateFiles.put("updater.mustache", "Updater.java");
            } else if (co.nickname.startsWith("delete")) {
                resource.put("hasDelete", true);
                addOperationName(co, "Remove");
                resource.put("signatureListDelete", generateSignatureList(co));
                apiTemplateFiles.put("deleter.mustache", "Deleter.java");
                addDeleteHeaderEnums(co, responseModels);
            } else if (co.nickname.startsWith("create")) {
                resource.put("hasCreate", true);
                addOperationName(co, "Create");
                resource.put("signatureListCreate", generateSignatureList(co));
                apiTemplateFiles.put("creator.mustache", "Creator.java");
            } else if (co.nickname.startsWith("fetch")) {
                resource.put("hasFetch", true);
                resource.put("signatureListFetch", generateSignatureList(co));
                addOperationName(co, "Fetch");
                apiTemplateFiles.put("fetcher.mustache", "Fetcher.java");
            } else {
                resource.put("hasRead", true);
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
            co.allParams.forEach(conventionResolver::resolveParamTypes);
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
              .map(modelName -> directoryStructureService.getModelCoPath(modelName, co, recordKey, this.allModels))
              .filter(Optional::isPresent)
              .map(item -> conventionResolver.resolve(item.get()))
              .map(item -> conventionResolver.resolveComplexType(item, modelFormatMap))
              .forEach(model -> {
                  resource.put("serialVersionUID", calculateSerialVersionUid(model.vars));
                  CodegenModel responseModel = processEnumVarsForAll(model, co, resourceName);
                  responseModels.add(responseModel);
              });

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

    private Map<String, Map<String, Object>> getConventionMap() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(ApplicationConstants.CONFIG_JAVA_JSON_PATH), new TypeReference<Map<String, Map<String, Object>>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    private void resetAllModelVendorExtensions() {
        allModels.forEach(item -> item.getVendorExtensions().remove(ENUM_VARS));
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
            codegenModel.vendorExtensions.put(ENUM_VARS, codegenProperties);
            responseModels.add(codegenModel);
        }
    }

    @SuppressWarnings("unchecked")
    private CodegenModel processEnumVarsForAll(CodegenModel model, CodegenOperation co,  String resourceName) {
        List<CodegenProperty> enumProperties = new ArrayList<>();
        model.vars.forEach(item -> {
            if (item.isEnum && item.dataFormat == null && !item.dataType.contains(resourceName+ ".")) {
                if (item.vendorExtensions.containsKey(REF_ENUM_EXTENSION_NAME)) {
                    if (item.containerType != null && item.containerType.equals(ARRAY)) {
                        item.enumName = item.baseType;
                        item.dataType = LIST_START+resourceName +"." + item.complexType+">";
                    } else {
                        item.enumName = item.baseType;
                        String[] values = item.dataType.split("\\.");
                        item.dataType = resourceName + "." + values[values.length -1];
                    }
                } else {
                    String baseName = StringHelper.camelize(item.baseName, true);
                    item.enumName = baseName;
                    if (item.containerType != null && item.containerType.equals(ARRAY)) {
                        item.dataType = LIST_START+ resourceName + "." + baseName + ">";
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
        if (model.getVendorExtensions().get(ENUM_VARS) != null) {
            List<CodegenProperty> codegenProperties = (List<CodegenProperty>) model.getVendorExtensions().get(ENUM_VARS);
            ts.addAll(codegenProperties);
        }
        model.vendorExtensions.put(ENUM_VARS,new ArrayList<>(ts));
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

    @SuppressWarnings("unchecked")
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
                    List<CodegenProperty> resEnum1 = (List<CodegenProperty>)resModel.vendorExtensions.get(ENUM_VARS);
                    List<CodegenProperty> codeModelEnums = (List<CodegenProperty>)codegenModel.vendorExtensions.get(ENUM_VARS);
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
                        resModel.vendorExtensions.put(ENUM_VARS, codeModelEnums);
                    }
                    resModel.vendorExtensions.forEach(
                            (key, value) -> codegenModel.vendorExtensions.merge(key, value, (oldValue, newValue) -> newValue));
                }

            resModel.vars.stream().filter(Predicate.not(codegenProperties::contains)).forEach(codegenProperties::add);
        }

        codegenProperties.forEach(prop -> prop.baseName = StringHelper.camelize(prop.baseName, true));

        codegenModel.setVars(codegenProperties);
        return codegenModel;
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
                                        op -> op.paramName.equals(StringHelper.camelize(cp, true))
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
            String hashText = bigInteger.toString(BASE_SIXTEEN);
            while (hashText.length() < OVERFLOW_CHECKER) {
                hashText = "0" + hashText;
            }
            return hashText;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateCodeOperationParams(final CodegenOperation co, String resourceName) {
        co.allParams = co.allParams
                .stream()
                .map(conventionResolver::resolveParameter)
                .map(item -> this.resolveEnumParameter(item, resourceName))
                .collect(Collectors.toList());
        co.pathParams = co.pathParams
                .stream()
                .map(conventionResolver::resolveParameter)
                .map(item -> this.resolveEnumParameter(item, resourceName))
                .collect(Collectors.toList());
        co.pathParams.stream().
                map(conventionResolver::resolveParamTypes)
                .map(item -> this.resolveEnumParameter(item, resourceName))
                .forEach(param -> param.paramName = "path"+param.paramName);
        co.queryParams = co.queryParams.stream().map(conventionResolver::resolveParameter)
                .map(conventionResolver::prefixedCollapsibleMap)
                .map(item -> this.resolveEnumParameter(item, resourceName))
                .collect(Collectors.toList());
        co.queryParams = preProcessQueryParameters(co);
        co.formParams = co.formParams.stream().map(conventionResolver::resolveParameter)
                .map(conventionResolver::prefixedCollapsibleMap)
                .map(item -> this.resolveEnumParameter(item, resourceName))
                .collect(Collectors.toList());
        co.formParams = preProcessFormParams(co);
        co.headerParams = co.headerParams.stream().map(conventionResolver::resolveParameter)
                .map(conventionResolver::prefixedCollapsibleMap)
                .map(item -> this.resolveEnumParameter(item, resourceName))
                .collect(Collectors.toList());
        co.optionalParams = co.optionalParams
                .stream()
                .map(conventionResolver::resolveParameter)
                .map(item -> this.resolveEnumParameter(item, resourceName))
                .collect(Collectors.toList());
        co.requiredParams = co.requiredParams
                .stream()
                .map(conventionResolver::resolveParameter)
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

    @SuppressWarnings("unchecked")
    private CodegenParameter resolveEnumParameter(CodegenParameter parameter, String resourceName) {
        if( parameter.isEnum && !parameter.vendorExtensions.containsKey(REF_ENUM_EXTENSION_NAME)) {
            parameter.enumName = StringHelper.camelize(parameter.enumName);
            if (parameter.items != null && parameter.items.allowableValues != null && parameter.items.allowableValues.containsKey(VALUES)) {
                parameter.dataType = LIST_START + resourceName+"."+ parameter.enumName + ">";
                parameter.baseType = resourceName + "." + parameter.enumName;
            } else {
                parameter.dataType = resourceName+"."+ parameter.enumName;
            }

            return parameter;
        }
        if (parameter.items != null && parameter.items.allowableValues != null && parameter.items.allowableValues.containsKey(VALUES) ) {
            parameter.isEnum = true;
            parameter.enumName = parameter.baseType;
            parameter._enum = (List<String>) parameter.items.allowableValues.get(VALUES);
            parameter.dataType = LIST_START + resourceName + "." + parameter.baseType + ">";
            parameter.baseType = resourceName + "." + parameter.baseType;
            parameter.allowableValues = parameter.items.allowableValues;
        }
        if (parameter.allowableValues != null && parameter.allowableValues.containsKey(ENUM_VARS)) {
            parameter.isEnum = true;
            parameter._enum = (List<String>) parameter.allowableValues.get(VALUES);
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
