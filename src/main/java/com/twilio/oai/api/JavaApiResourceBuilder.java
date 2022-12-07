package com.twilio.oai.api;

import com.google.common.collect.Lists;
import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.ISchemaResolver;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.java.JavaConventionResolver;
import com.twilio.oai.template.IApiActionTemplate;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.twilio.oai.common.ApplicationConstants.*;
import static com.twilio.oai.template.JavaApiActionTemplate.API_TEMPLATE;

public class JavaApiResourceBuilder extends ApiResourceBuilder{

    public static final String SIGNATURE_LIST = "x-signature-list";
    private static final int SERIAL_UID_LENGTH = 12;
    protected CodegenModel responseModel;
    protected long serialVersionUID;

    private final DirectoryStructureService directoryStructureService;
    private final JavaConventionResolver conventionResolver;
    public JavaApiResourceBuilder(IApiActionTemplate template, List<CodegenOperation> codegenOperations, List<CodegenModel> allModels,
                                  DirectoryStructureService directoryStructureService, IConventionMapper mapper) {
        super(template, codegenOperations, allModels);
        this.directoryStructureService = directoryStructureService;
        this.recordKey = directoryStructureService.getRecordKey(codegenOperationList);
        this.conventionResolver = new JavaConventionResolver(mapper);
    }
    @Override
    public IApiResourceBuilder updateTemplate() {
        template.clean();
        codegenOperationList.stream().forEach(codegenOperation -> {

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

    @Override
    public ApiResourceBuilder updateOperations(ISchemaResolver<CodegenParameter> codegenParameterIResolver) {
        this.codegenOperationList.stream().forEach(co -> {
            List<String> filePathArray = new ArrayList<>(Arrays.asList(co.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
            String resourceName = filePathArray.remove(filePathArray.size()-1);
            co.allParams = co.allParams.stream()
                    .map(codegenParameterIResolver::resolve)
                    .map(item -> conventionResolver.resolveEnumParameter(item, resourceName))
                    .collect(Collectors.toList());
            co.pathParams = co.pathParams.stream()
                    .map(codegenParameterIResolver::resolve)
                    .map(item -> conventionResolver.resolveEnumParameter(item, resourceName))
                    .collect(Collectors.toList());
            co.pathParams.stream().
                map(codegenParameterIResolver::resolve)
                .map(item -> conventionResolver.resolveEnumParameter(item, resourceName))
                .forEach(param -> param.paramName = "path"+param.paramName);
            co.queryParams = co.queryParams.stream()
                    .map(codegenParameterIResolver::resolve)
                    .map(conventionResolver::prefixedCollapsibleMap)
                    .map(item -> conventionResolver.resolveEnumParameter(item, resourceName))
                    .collect(Collectors.toList());
            co.queryParams = preProcessQueryParameters(co);
            co.formParams = co.formParams.stream()
                    .map(codegenParameterIResolver::resolve)
                    .map(conventionResolver::prefixedCollapsibleMap)
                    .map(item -> conventionResolver.resolveEnumParameter(item, resourceName))
                    .collect(Collectors.toList());
            processDataTypesForParams(co.formParams);
            co.headerParams = co.headerParams.stream()
                    .map(codegenParameterIResolver::resolve)
                    .map(conventionResolver::prefixedCollapsibleMap)
                    .map(item -> conventionResolver.resolveEnumParameter(item, resourceName))
                    .collect(Collectors.toList());
            co.optionalParams = co.optionalParams
                    .stream()
                    .map(codegenParameterIResolver::resolve)
                    .map(item -> conventionResolver.resolveEnumParameter(item, resourceName))
                    .collect(Collectors.toList());
            co.requiredParams = co.requiredParams
                    .stream()
                    .map(codegenParameterIResolver::resolve)
                    .map(item -> conventionResolver.resolveEnumParameter(item, resourceName))
                    .collect(Collectors.toList());
            co.hasParams = !co.allParams.isEmpty();
            co.hasRequiredParams = !co.requiredParams.isEmpty();

            requiredPathParams.addAll(co.pathParams);
            co.vendorExtensions = mapOperation(co);
        });
        return this;
    }

    @Override
    public IApiResourceBuilder setImports(DirectoryStructureService directoryStructureService) {
        return null;
    }

    @Override
    public ApiResourceBuilder updateResponseModel(ISchemaResolver<CodegenProperty> codegenPropertyIResolver, Resolver<CodegenModel> codegenModelResolver) {
        List<CodegenModel> responseModels = new ArrayList<>();
        codegenOperationList.stream().forEach(co -> {
            List<String> filePathArray = new ArrayList<>(Arrays.asList(co.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
            String resourceName = filePathArray.remove(filePathArray.size()-1);
            co.responses
                    .stream()
                    .map(response -> response.dataType)
                    .filter(Objects::nonNull)
                    .map(modelName -> directoryStructureService.getModelCoPath(modelName, co, recordKey))
                    .flatMap(Optional::stream)
                    .forEach(item -> {
                        codegenModelResolver.resolve(item);
                        item.vars = item.vars.stream().map(v ->
                                codegenPropertyIResolver.resolve(v)).collect(Collectors.toList());
                        CodegenModel responseModel = processEnumProperty(item, co, resourceName);
                        responseModels.add(responseModel);
                        this.serialVersionUID = calculateSerialVersionUid(item.vars);
                    });
        });
        this.apiResponseModels = getDistinctResponseModel(responseModels);
        this.responseModel = getConcatenatedResponseModel(responseModels);
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
            } else if (cp.isArray && cp.baseType.equalsIgnoreCase("String")) {
                cp.vendorExtensions.put("x-is-string-array", true);
            }

        }
    }
    @Override
    protected Map<String, Object> mapOperation(CodegenOperation co) {
        Map<String, Object> operationMap = super.mapOperation(co);
        final EnumConstants.Operation method = Arrays
            .stream(EnumConstants.Operation.values())
            .filter(item -> co.nickname.toLowerCase().startsWith(item.getValue().toLowerCase()))
            .findFirst()
            .orElse(EnumConstants.Operation.READ);

        operationMap.put("x-is-" + method.name().toLowerCase() + "-operation", true);
        metaAPIProperties.put(method.name(), co);

        String summary = co.notes;
        if (summary == null || summary.isEmpty()) {
            summary = method.name().toLowerCase();
        }

        operationMap.put("x-generate-comment", summary);
        operationMap.put(SIGNATURE_LIST, generateSignatureList(co));
        operationMap.put("x-non-path-params", getNonPathParams(co.allParams));
        return operationMap;
    }

    private List<CodegenParameter> getNonPathParams(List<CodegenParameter> allParams) {
       return allParams.stream().filter(param -> !param.isPathParam).collect(Collectors.toList());
    }

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

    @Override
    public JavaApiResources build() {
        return new JavaApiResources(this);
    }
}
