package com.twilio.oai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.twilio.oai.mlambdas.ReplaceHyphenLambda;
import com.twilio.oai.resource.IResourceTree;
import com.twilio.oai.resource.ResourceMap;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import lombok.AllArgsConstructor;
import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.JavaClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;
import org.openapitools.codegen.utils.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.google.common.collect.ImmutableMap;
import com.samskivert.mustache.Mustache.Lambda;

public class TwilioJavaGenerator extends JavaClientCodegen {

    // Unique string devoid of symbols.
    public static final String PATH_SEPARATOR_PLACEHOLDER = "1234567890";
    public static final String PREVIEW_STRING = "Preview";

    public static final String ACCOUNT_SID_FORMAT = "^AC[0-9a-fA-F]{32}$";
    private static final int OVERFLOW_CHECKER = 32;
    private static final int BASE_SIXTEEN = 16;
    private static final int BIG_INTEGER_CONSTANT = 1;
    private static final int SERIAL_UID_LENGTH = 12;
    public static final String URI = "uri";

    private final List<CodegenModel> allModels = new ArrayList<>();
    private  Map<String, String> modelFormatMap = new HashMap<>();
    private  Map<String, String> subDomainMap = new HashMap<>();
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
        String version = inputSpecs[inputSpecs.length-1].replaceAll("\\.[^/]+$", "");
        String domain = String.join("", Arrays.copyOfRange(inputSpecs, 1, inputSpecs.length-1));

        if(inputSpecs.length <3){   // version is missing
            version = "";
            domain = inputSpecs[inputSpecs.length-1].replaceAll("\\.[^/]+$", "");
        }

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
        openAPI.getPaths().forEach((name, path) -> {
            resourceTree.addResource(name, path);
        });
        openAPI.getPaths().forEach((name, path) -> {
            updateAccountSidParam(name, path);
            path.readOperations().forEach(operation -> {
                // Group operations together by tag. This gives us one file/post-process per resource.
                String tag = String.join(PATH_SEPARATOR_PLACEHOLDER, resourceTree.ancestors(name, operation));
                if(isPreviewDomain()){
                    String subDomainName = extractSubDomainName(name);
                    subDomainMap.put(tag, subDomainName);
                }

                operation.addTagsItem(tag);
            });
            Matcher m = serverUrlPattern.matcher(path.getServers().get(0).getUrl());
            if(m.find()){
                additionalProperties.put("domainName", StringUtils.camelize(m.group(1)));
                additionalProperties.put("domainPackage", m.group(1).replaceAll("-",""));
            }
        });
    }

    private String extractSubDomainName(String name) {
        String[] split = name.split("/");
        if(split.length > 1 && split[1] != null) {
            String result = split[1];
            result = result.substring(0, 1).toLowerCase() + result.substring(1);
            return result;
        }
        return null;
    }

    private void resetOperationMethodCalls(PathItem pathItemOperatorClassVendExt, PathItem pathItemClassVendExt) {
        pathItemClassVendExt.put(null);
        pathItemClassVendExt.get(null);
        pathItemClassVendExt.post(null);
        pathItemClassVendExt.delete(null);
        pathItemClassVendExt.patch(null);
        pathItemOperatorClassVendExt.put(null);
        pathItemOperatorClassVendExt.get(null);
        pathItemOperatorClassVendExt.post(null);
        pathItemOperatorClassVendExt.delete(null);
        pathItemOperatorClassVendExt.patch(null);
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
        String[] split = super.toApiFilename(name).split(PATH_SEPARATOR_PLACEHOLDER);
        if(isPreviewDomain()){
            return getSubDomainName(subDomainMap, name) + "/" +Arrays.stream(Arrays.copyOfRange(split, 0, split.length - 1)).map(String::toLowerCase).collect(Collectors.joining("/")) + "/"+split[split.length-1];
        }

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
        boolean isVersionV2010 = objs.get("package").equals("v2010");
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
            String[] filePathArray = co.baseName.split(PATH_SEPARATOR_PLACEHOLDER);
            String resourceName = filePathArray[filePathArray.length-1];
            final Map<String, Object> resource = resources.computeIfAbsent(resourceName, k -> new LinkedHashMap<>());
            populateCrudOperations(resource, co);
            updateCodeOperationParams(co, resourceName);
            if (co.nickname.startsWith("update")) {
                resource.put("hasUpdate", true);
                addOperationName(co, "Update");
                co.vendorExtensions.put("x-is-update-operation", true);
                resource.put("signatureListUpdate", generateSignatureList(resource, co));
                apiTemplateFiles.put("updater.mustache", "Updater.java");
            } else if (co.nickname.startsWith("delete")) {
                resource.put("hasDelete", true);
                addOperationName(co, "Remove");
                co.vendorExtensions.put("x-is-delete-operation", true);
                resource.put("signatureListDelete", generateSignatureList(resource, co));
                apiTemplateFiles.put("deleter.mustache", "Deleter.java");
                addDeleteHeaderEnums(co, responseModels);
            } else if (co.nickname.startsWith("create")) {
                resource.put("hasCreate", true);
                co.vendorExtensions.put("x-is-create-operation", true);
                addOperationName(co, "Create");
                resource.put("signatureListCreate", generateSignatureList(resource, co));
                apiTemplateFiles.put("creator.mustache", "Creator.java");
            } else if (co.nickname.startsWith("fetch")) {
                resource.put("hasFetch", true);
                resource.put("signatureListFetch", generateSignatureList(resource, co));
                co.vendorExtensions.put("x-is-fetch-operation", true);
                addOperationName(co, "Fetch");
                apiTemplateFiles.put("fetcher.mustache", "Fetcher.java");
            } else {
                resource.put("hasRead", true);
                co.vendorExtensions.put("x-is-read-operation", true);
                addOperationName(co, "Page");
                resource.put("signatureListRead", generateSignatureList(resource, co));
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
            List<String> packagePaths = Arrays.asList(Arrays.copyOfRange(filePathArray,0 , filePathArray.length-1))
                    .stream().map(String::toLowerCase).collect(Collectors.toList());
            if(isPreviewDomain()){
                String tag = ops.getClassname();
                String subDomainName = getSubDomainName(subDomainMap, tag);
                resource.put("package", subDomainName);
            }
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

    private boolean isPreviewDomain(){
       return this.additionalProperties.get("domain").equals(PREVIEW_STRING);
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
        if (codegenProperties.size() > 0) {
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
                if (!alreadyAdded.isPresent() && param.dataFormat == null) {
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
        return new TreeSet<CodegenProperty>(new Comparator<CodegenProperty>() {
            public int compare(CodegenProperty cp1, CodegenProperty cp2) {
                return cp1.enumName.compareTo(cp2.getEnumName());
            }
        });
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
                if (!coModel.isPresent()) {
                        return Optional.empty();
                    }
                CodegenProperty property = coModel.get().vars.stream().filter(prop -> prop.baseName.equals(recordKey)).findFirst().get();
                Optional<CodegenModel> complexModel = allModels.stream().filter(model -> model.getClassname().equals(property.complexType)).findFirst();
                return complexModel;
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
     * @param resource
     * @param co
     * @return
     */
    private ArrayList<List<CodegenParameter>> generateSignatureList(final Map<String, Object> resource, final CodegenOperation co) {
        CodegenParameter accountSidParam = null;
        List<List<CodegenParameter>> conditionalCodegenParam = new ArrayList<>();
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

    private String getSubDomainName(Map<String, String> subDomainMap, String name) {
        return subDomainMap.entrySet().stream().filter(x -> x.getKey().equalsIgnoreCase(name)).findFirst().get().getValue();
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
