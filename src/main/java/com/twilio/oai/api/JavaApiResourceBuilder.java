package com.twilio.oai.api;

import com.google.common.collect.Lists;
import com.twilio.oai.CodegenUtils;
import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.JsonRequestBodyResolver;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.EnumConstants.JavaHttpMethod;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.java.JavaConventionResolver;
import com.twilio.oai.template.IApiActionTemplate;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenSecurity;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.twilio.oai.common.ApplicationConstants.ACCOUNT_SID_VEND_EXT;
import static com.twilio.oai.common.ApplicationConstants.ARRAY;
import static com.twilio.oai.common.ApplicationConstants.ENUM_VARS;
import static com.twilio.oai.common.ApplicationConstants.HTTP_METHOD;
import static com.twilio.oai.common.ApplicationConstants.LIST_END;
import static com.twilio.oai.common.ApplicationConstants.LIST_START;
import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;
import static com.twilio.oai.common.ApplicationConstants.REF_ENUM_EXTENSION_NAME;
import static com.twilio.oai.common.ApplicationConstants.STRING;
import static com.twilio.oai.common.ApplicationConstants.SUCCESS;
import static com.twilio.oai.common.ApplicationConstants.TWILIO_EXTENSION_NAME;
import static com.twilio.oai.template.AbstractApiActionTemplate.NESTED_MODELS;
import static com.twilio.oai.template.JavaApiActionTemplate.API_TEMPLATE;

public class JavaApiResourceBuilder extends ApiResourceBuilder{

    public static final String SIGNATURE_LIST = "x-signature-list";
    private static final int SERIAL_UID_LENGTH = 12;
    protected CodegenModel responseModel;
    protected long serialVersionUID;
    private Set<CodegenModel> headerParamModelList;

    private final JavaConventionResolver conventionResolver;

    private Resolver<CodegenProperty> codegenPropertyIResolver;

    public Set<IJsonSchemaValidationProperties> enums = new HashSet<>();
    public String authMethodPackage = "";

    public ArrayList<List<CodegenProperty>> modelParameters;

    public JavaApiResourceBuilder(IApiActionTemplate template, List<CodegenOperation> codegenOperations,
                                  List<CodegenModel> allModels) {
        super(template, codegenOperations, allModels);
        this.conventionResolver = new JavaConventionResolver();
    }

    public JavaApiResourceBuilder(IApiActionTemplate apiActionTemplate, List<CodegenOperation> opList,
                                  List<CodegenModel> allModels, Map<String, Boolean> toggleMap,
                                  Resolver<CodegenProperty> codegenPropertyIResolver) {
        this(apiActionTemplate, opList, allModels);
        this.toggleMap = toggleMap;
        this.codegenPropertyIResolver = codegenPropertyIResolver;
        processAuthMethods(opList);
    }

    @Override
    public IApiResourceBuilder updateTemplate() {
        template.clean();
        codegenOperationList.forEach(codegenOperation -> {
            updateNamespaceSubPart(codegenOperation);
            EnumConstants.Operation operationsName = Arrays
            .stream(EnumConstants.Operation.values())
                   .filter(item -> codegenOperation.nickname.toLowerCase().startsWith(item.getValue().toLowerCase()))
                    //default to read operation if no operation match
                    .findAny().orElse(EnumConstants.Operation.READ);
            // make sure the codegenOperation.nickname is expected operation
            template.add(operationsName.getValue().toLowerCase(Locale.ROOT));
        });
        template.add(API_TEMPLATE);
        return this;
    }

    private void populateContentType(CodegenOperation co) {
        if(co.consumes != null && !co.consumes.isEmpty())
            co.consumes.forEach(consume -> {
                if(consume.getOrDefault("mediaType", "").equals(CONTENT_TYPE_JSON))
                    co.vendorExtensions.put("x-is-json", true);
                else if( consume.getOrDefault("mediaType", "").equals(CONTENT_TYPE_MULTIPART_FORM_DATA))
                    co.vendorExtensions.put("x-is-multipart-form-data", true);
            });
    }

    @Override
    public ApiResourceBuilder updateOperations(Resolver<CodegenParameter> codegenParameterIResolver) {
        headerParamModelList = new HashSet<>();
        JsonRequestBodyResolver jsonRequestBodyResolver = new JsonRequestBodyResolver(this, codegenPropertyIResolver);
        this.codegenOperationList.forEach(co -> {
            updateNestedContent(co);
            populateContentType(co);
            updateHttpMethod(co);
            List<String> filePathArray = new ArrayList<>(Arrays.asList(co.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
            String resourceName = filePathArray.remove(filePathArray.size()-1);

            co.allParams.stream()
                    .filter(item -> !(item.getContent() != null && item.getContent().get("application/json") != null))
                    .map(item -> codegenParameterIResolver.resolve(item, this))
                    .map(item -> conventionResolver.resolveEnumParameter(item, resourceName))
                    .collect(Collectors.toList());

            co.allParams.forEach(parameter -> {
                if (CodegenUtils.isParameterSchemaEnumJava(parameter)) {
                    addEnums(parameter);
                }
            });

            co.vendorExtensions = conventionResolver.populateSecurityAttributes(co);

            jsonRequestBodyResolver.setResourceName(resourceName);
            co.allParams.stream()
                    .filter(item -> (item.getContent() != null && item.getContent().get("application/json") != null))
                    .forEach(item -> {
                        CodegenModel model = getModel(item.dataType);
                        // currently supporting required and conditional parameters only for request body object
                        if (model != null) {
                            model.vendorExtensions.put("x-constructor-required", true);
                            model.vendorExtensions.put("x-model-parameters", generateSignatureListBody(co));
                            jsonRequestBodyResolver.resolve(item, codegenParameterIResolver);
                        }
                    });
            co.allParams.forEach(this::updateHeaderParamsList);
            co.pathParams = co.pathParams.stream()
                    .map(item -> codegenParameterIResolver.resolve(item, this))
                    .map(item -> conventionResolver.resolveEnumParameter(item, resourceName))
                    .collect(Collectors.toList());
            co.pathParams.stream().
                map(item -> codegenParameterIResolver.resolve(item, this))
                .map(item -> conventionResolver.resolveEnumParameter(item, resourceName))
                .forEach(param -> param.paramName = "path"+param.paramName);
            co.queryParams = co.queryParams.stream()
                    .map(item -> codegenParameterIResolver.resolve(item, this))
                    .map(item -> conventionResolver.resolveEnumParameter(item, resourceName))
                    .collect(Collectors.toList());
            co.queryParams = preProcessQueryParameters(co);
            co.formParams = co.formParams.stream()
                    .map(item -> codegenParameterIResolver.resolve(item, this))
                    .map(item -> conventionResolver.resolveEnumParameter(item, resourceName))
                    .collect(Collectors.toList());
            processDataTypesForParams(co.formParams);
            co.headerParams = co.headerParams.stream()
                    .map(item -> codegenParameterIResolver.resolve(item, this))
                    .map(item -> conventionResolver.resolveEnumParameter(item, resourceName))
                    .collect(Collectors.toList());
            processDataTypesForParams(co.headerParams);
            co.optionalParams = co.optionalParams
                    .stream()
                    .map(item -> codegenParameterIResolver.resolve(item, this))
                    .map(item -> conventionResolver.resolveEnumParameter(item, resourceName))
                    .collect(Collectors.toList());
            co.requiredParams = co.requiredParams
                    .stream()
                    .map(item -> codegenParameterIResolver.resolve(item, this))
                    .map(item -> conventionResolver.resolveEnumParameter(item, resourceName))
                    .collect(Collectors.toList());
            co.hasParams = !co.allParams.isEmpty();
            co.hasRequiredParams = !co.requiredParams.isEmpty();
            if(!co.formParams.isEmpty())co.vendorExtensions.put("x-has-form-params", true);
            if(!co.headerParams.isEmpty())co.vendorExtensions.put("x-has-header-params", true);
            if(!co.bodyParams.isEmpty())co.vendorExtensions.put("x-has-body-params", true);
            if(!co.queryParams.isEmpty())co.vendorExtensions.put("x-has-query-params", true);

            requiredPathParams.addAll(co.pathParams);
            co.vendorExtensions = mapOperation(co);
        });
        return this;
    }


    public void processAuthMethods(List<CodegenOperation> opList) {
        if(opList != null){
            List<CodegenSecurity> authMethods = opList.get(0).authMethods;
            if(authMethods != null){
                for(CodegenSecurity c : authMethods){
                    if(c.isOAuth == true){
                        this.authMethodPackage = ".bearertoken";
                    }
                }
            }
            else this.authMethodPackage = ".noauth";
        }
    }

    @Override
    public void updateHttpMethod(CodegenOperation co) {
        switch (co.httpMethod) {
            case "GET":
                co.vendorExtensions.put(HTTP_METHOD, JavaHttpMethod.GET.getValue());
                break;
            case "POST":
                co.vendorExtensions.put(HTTP_METHOD, JavaHttpMethod.POST.getValue());
                break;
            case "PUT":
                co.vendorExtensions.put(HTTP_METHOD, JavaHttpMethod.PUT.getValue());
                break;
            case "PATCH":
                co.vendorExtensions.put(HTTP_METHOD, JavaHttpMethod.PATCH.getValue());
                break;
            case "DELETE":
                co.vendorExtensions.put(HTTP_METHOD, JavaHttpMethod.DELETE.getValue());
                break;
        }
    }

    private void updateHeaderParamsList(CodegenParameter cp) {
        List<CodegenProperty> codegenProperties = new ArrayList<>();
        if (cp.isEnum && cp.isHeaderParam) {
            codegenProperties.add(createCodeGenPropertyFromParameter(cp));
        }
        if (!codegenProperties.isEmpty()) {
            CodegenModel codegenModel = new CodegenModel();
            addUniqueResponseModelEnums(codegenProperties, codegenModel);
            headerParamModelList.add(codegenModel);
        }
    }
    @Override
    public IApiResourceBuilder setImports(DirectoryStructureService directoryStructureService) {
        return null;
    }

    @Override
    public ApiResourceBuilder updateResponseModel(Resolver<CodegenProperty> codegenPropertyIResolver, Resolver<CodegenModel> codegenModelResolver) {
        List<CodegenModel> responseModels = new ArrayList<>();
        JsonRequestBodyResolver jsonRequestBodyResolver = new JsonRequestBodyResolver(this, codegenPropertyIResolver);
        codegenOperationList.forEach(co -> {
            List<String> filePathArray = new ArrayList<>(Arrays.asList(co.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
            String resourceName = filePathArray.remove(filePathArray.size()-1);
            jsonRequestBodyResolver.setResourceName(resourceName);
            co.responses
                    .stream()
                    .filter(response -> SUCCESS.test(Integer.parseInt(response.code.trim())))
                    .map(response -> {
                        if (response.dataType != null && response.dataType.startsWith(EnumConstants.JavaDataTypes.LIST.getValue())) {
                            return response.baseType;
                        }
                        return response.dataType;
                    })
                    .filter(Objects::nonNull)
                    .map(modelName -> getModel(modelName, co))
                    .flatMap(Optional::stream)
                    .forEach(item -> {
                        // Here use json body resolver
                        codegenModelResolver.resolve(item, this);
                        item.vars.forEach(property ->
                                jsonRequestBodyResolver.resolve(property));
                        responseModels.add(processEnumProperty(item, co, resourceName));
                        responseModels.addAll(headerParamModelList);
                        this.serialVersionUID = calculateSerialVersionUid(item.vars);
                    });
        });
        this.apiResponseModels = getDistinctResponseModel(responseModels);
        this.responseModel = getConcatenatedResponseModel(responseModels);
        return this;
    }

    @Override
    public ApiResourceBuilder updateModel(Resolver<CodegenModel> codegenModelResolver) {
        super.updateModel(codegenModelResolver);
        if (nestedModels.size() >= 1) {
            template.add(NESTED_MODELS);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    private CodegenModel processEnumProperty(CodegenModel model, CodegenOperation co,  String resourceName) {
        List<CodegenProperty> enumProperties = new ArrayList<>();
        model.vars.forEach(item -> {
            if (item.isEnum && item.dataFormat == null && !item.dataType.contains(resourceName+ ".")) {
                if (item.vendorExtensions.containsKey(REF_ENUM_EXTENSION_NAME)) {
                    if (item.containerType != null && item.containerType.equals(ARRAY)) {
                        item.enumName = item.baseType;
                        item.dataType = LIST_START + resourceName +"." + item.complexType + LIST_END;
                    } else {
                        item.enumName = item.baseType;
                        String[] values = item.dataType.split("\\.");
                        item.dataType = resourceName + "." + values[values.length -1];
                    }
                } else {
                    String baseName = StringHelper.camelize(item.baseName, true);
                    item.enumName = baseName;
                    if (item.containerType != null && item.containerType.equals(ARRAY)) {
                        item.dataType = LIST_START+ resourceName + "." + baseName + LIST_END;
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
       List<CodegenProperty> codegenProperties = new ArrayList<>(ts);
        addUniqueResponseModelEnums(codegenProperties, model);
        return model;
    }

    private TreeSet<CodegenProperty> getEnumPropertyComparator() {
        return new TreeSet<>((cp1, cp2) -> cp1.enumName.compareTo(cp2.getEnumName()));
    }

       public CodegenProperty createCodeGenPropertyFromParameter(CodegenParameter co) {
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

    private long calculateSerialVersionUid(final List<CodegenProperty> modelProperties){
        String signature = calculateSignature(modelProperties);
        return Long.parseLong(Utility.getMd5(signature).substring(0,SERIAL_UID_LENGTH), Utility.BASE_SIXTEEN);
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

       /**
     * Different data types need different formatting and conversion mechanisms while they are being added as parameters to the request
     * This special handling will be done in mustache files
     * This function sets different flags for processing in mustache files
     * */
    private void processDataTypesForParams(List<CodegenParameter> finalQueryParamList) {
        //Date types needing special processing
        List<String> specialTypes = Arrays.asList("String", "ZonedDateTime", "LocalDate");

        for(CodegenParameter cp : finalQueryParamList){

            if(!specialTypes.contains(cp.dataType) && !cp.vendorExtensions.containsKey("x-prefixed-collapsible-map") && !cp.isArray){
                cp.vendorExtensions.put("x-is-other-data-type", true);
            } else if (cp.isArray && cp.baseType.equalsIgnoreCase(STRING)) {
                cp.vendorExtensions.put("x-is-string-array", true);
            } else if (cp.isArray && cp.items != null && cp.items.isAnyType) {
                cp.vendorExtensions.put("x-is-anytype-array", true);
            }

        }
    }
    @Override
    protected Map<String, Object> mapOperation(CodegenOperation co) {
        Map<String, Object> operationMap = super.mapOperation(co);
        if (co.bodyParam !=null && co.bodyParam.getContent() != null && co.bodyParam.getContent().containsKey(CONTENT_TYPE_JSON)) {
            operationMap.put(SIGNATURE_LIST, generateSignatureListJson(co));
            modelParameters = generateSignatureListBody(co);
        } else {
            operationMap.put(SIGNATURE_LIST, generateSignatureList(co));
        }

        operationMap.put("x-non-path-params", getNonPathParams(co.allParams));
        return operationMap;
    }

    private List<CodegenParameter> getNonPathParams(List<CodegenParameter> allParams) {
       return allParams.stream().filter(param -> !param.isPathParam).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private ArrayList<List<CodegenParameter>> generateSignatureList(final CodegenOperation co) {
        CodegenParameter accountSidParam = null;
        List<List<CodegenParameter>> conditionalCodegenParam = new ArrayList<>();
        Optional<CodegenParameter> optionalParam = co.allParams.stream()
                .filter(param -> param.vendorExtensions.containsKey(ACCOUNT_SID_VEND_EXT)).findAny();
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
        List<List<CodegenParameter>> filteredConditionalCodegenParam = new ArrayList<>();
        //Remove duplicates from signatureList
        HashSet<List<String>> signatureHashSet = new HashSet<>();
        for(List<CodegenParameter> paramList : conditionalCodegenParam){
            List<String> orderedParamList = paramList.stream().map(p -> p.dataType).collect(Collectors.toList());
            if(!signatureHashSet.contains(orderedParamList)){
                filteredConditionalCodegenParam.add(paramList);
                signatureHashSet.add(orderedParamList);
            }
        }
        ArrayList<List<CodegenParameter>> signatureList = new ArrayList<>();
        for(List<CodegenParameter> paramList : filteredConditionalCodegenParam){
            signatureList.add(addAllToList(co.requiredParams, paramList));
            if( accountSidParam != null) {
                signatureList.add(addAllToList(List.of(accountSidParam), co.requiredParams, paramList));
            }
        }
        return signatureList;
    }

    private ArrayList<List<CodegenParameter>> generateSignatureListJson(final CodegenOperation co) {
        CodegenParameter accountSidParam = null;
        Optional<CodegenParameter> optionalParam = co.allParams.stream()
                .filter(param -> param.vendorExtensions.containsKey(ACCOUNT_SID_VEND_EXT)).findAny();
        if(optionalParam.isPresent()){
            accountSidParam = optionalParam.get();
        }

        ArrayList<List<CodegenParameter>> signatureList = new ArrayList<>();
        List<List<CodegenParameter>> conditionalCodegenParam = Lists.cartesianProduct(new ArrayList<>());
        for(List<CodegenParameter> paramList : conditionalCodegenParam){
            signatureList.add(addAllToList(co.requiredParams, paramList));
            if( accountSidParam != null) {
                signatureList.add(addAllToList(List.of(accountSidParam), co.requiredParams, paramList));
            }
        }
        return signatureList;
    }

    private ArrayList<List<CodegenProperty>> generateSignatureListBody(final CodegenOperation co) {
        List<List<CodegenProperty>> conditionalCodegenParam = new ArrayList<>();

        if (co.vendorExtensions.containsKey(TWILIO_EXTENSION_NAME)) {
            HashMap<String, Object> twilioVendorExtension = (HashMap<String, Object>) co.vendorExtensions.get(TWILIO_EXTENSION_NAME);
            if (twilioVendorExtension.containsKey("conditional")) {
                List<List<String>> conditionalParams = (List<List<String>>) twilioVendorExtension.get("conditional");
                // map the conditional param names with the codegenParameter added in optional params
                conditionalCodegenParam = conditionalParams.stream().map(
                        paramList -> paramList.stream().map(
                                cp -> co.bodyParams.get(0).vars.stream().filter(
                                        op -> op.name.equals(StringHelper.camelize(cp, true))
                                ).findAny().get()
                        ).collect(Collectors.toList())).collect(Collectors.toList());
                // added filter to prevent same signature types
                conditionalCodegenParam = conditionalCodegenParam.stream().filter(cpList -> (cpList.size() <=1 || !cpList.get(0).dataType.equals(cpList.get(1).dataType))).collect(Collectors.toList());
            }
        }
        conditionalCodegenParam = Lists.cartesianProduct(conditionalCodegenParam);
        List<List<CodegenProperty>> filteredConditionalCodegenParam = new ArrayList<>();
        //Remove duplicates from signatureList
        HashSet<List<String>> signatureHashSet = new HashSet<>();
        for(List<CodegenProperty> paramList : conditionalCodegenParam){
            List<String> orderedParamList = paramList.stream().map(p -> p.dataType).collect(Collectors.toList());
            if(!signatureHashSet.contains(orderedParamList)){
                filteredConditionalCodegenParam.add(paramList);
                signatureHashSet.add(orderedParamList);
            }
        }
        ArrayList<List<CodegenProperty>> signatureList = new ArrayList<>();
        for(List<CodegenProperty> paramList : filteredConditionalCodegenParam){
            if (co.bodyParams != null && co.bodyParams.size() != 0 && co.bodyParams.get(0) != null)
                signatureList.add(addAllToList(co.bodyParams.get(0).requiredVars, paramList));
        }
        return signatureList;
    }
    private <T> List<T> addAllToList(List<T> ... list) {
        return Arrays.stream(list).flatMap(List<T>::stream).collect(Collectors.toList());
    }

    private void updateNamespaceSubPart(CodegenOperation codegenOperation) {
        List<String> filePathArray = new ArrayList<>(Arrays.asList(codegenOperation.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
        filePathArray.remove(filePathArray.size()-1);
        if (!filePathArray.isEmpty()) {
            final String namespacePath = filePathArray
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.joining("."));
            namespaceSubPart = "." + namespacePath;
        }
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
                        addUniqueResponseModelEnums(codeModelEnums, resModel);
                    }
                    resModel.vendorExtensions.forEach(
                            (key, value) -> codegenModel.vendorExtensions.merge(key, value, (oldValue, newValue) -> newValue));
                }
                resModel.vars.forEach(var -> {
                    Boolean isOverridden = var.isOverridden;
                    if(isOverridden != null && isOverridden == false)
                        var.isOverridden = null;
                    var.baseName = StringHelper.camelize(var.baseName, true);
                    if(!codegenProperties.contains(var))
                        codegenProperties.add(var);
                    var.isOverridden = isOverridden;
                });
//            resModel.vars.stream().filter(Predicate.not(codegenProperties::contains)).forEach(codegenProperties::add);
        }

        codegenProperties.forEach(prop -> prop.baseName = StringHelper.camelize(prop.baseName, true));

        codegenModel.setVars(codegenProperties);
        return codegenModel;
    }

    // Enums can be present either at enums or in responseModel
    public void addEnums(IJsonSchemaValidationProperties item) {
        boolean isDuplicate = false;
        String newItemEnumName = null;

        if (item instanceof CodegenParameter) {
            newItemEnumName = ((CodegenParameter) item).enumName;
        } else if (item instanceof CodegenProperty) {
            newItemEnumName = ((CodegenProperty) item).enumName;
        }

        if (newItemEnumName != null) {
            for (IJsonSchemaValidationProperties enumItem : enums) {
                String enumName = "";
                if (enumItem instanceof CodegenParameter) {
                    enumName = ((CodegenParameter) enumItem).enumName;
                } else {
                    enumName = ((CodegenProperty) enumItem).enumName;
                }

                if (enumName.equals(newItemEnumName)) {
                    isDuplicate = true;
                    break; // No need to continue checking duplicates
                }
            }
        }

        if (!isDuplicate) {
            enums.add(item);
        }
    }

    // Enums can be added either via Response(vendorExtensions) or via request body(Enums).
    // It will only add those enums to Response vendorExtensions which are not present in Enums.
    private void addUniqueResponseModelEnums(List<CodegenProperty> codegenProperties, CodegenModel model) {
        boolean isDuplicate = false;
        List<CodegenProperty> uniqueEnums = new ArrayList<>();
        for (CodegenProperty codegenProperty: codegenProperties) {
            for (IJsonSchemaValidationProperties enumItem : enums) {
                if (enumItem instanceof CodegenProperty
                        && ((CodegenProperty) enumItem).enumName.equals(codegenProperty.enumName)) {
                    isDuplicate = true;
                }
            }
            if (!isDuplicate) {
                uniqueEnums.add(codegenProperty);
            }
            isDuplicate = false;
        }
        model.vendorExtensions.put(ENUM_VARS, uniqueEnums);
    }
    @Override
    public JavaApiResources build() {
        return new JavaApiResources(this);
    }
}
