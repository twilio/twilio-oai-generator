package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.EnumConstants.CsharpHttpMethod;
import com.twilio.oai.common.EnumConstants.CsharpDataTypes;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.csharp.OperationStore;
import com.twilio.oai.template.CsharpApiActionTemplate;
import com.twilio.oai.template.IApiActionTemplate;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenSecurity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.twilio.oai.common.ApplicationConstants.HTTP_METHOD;
import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;

public class CsharpApiResourceBuilder extends ApiResourceBuilder {

    public String authMethod = "";

    /**
     * List of C# primitive types that require a nullable marker (?) when nullable
     */
    private static final Set<String> CSHARP_PRIMITIVE_TYPES = new HashSet<>(Arrays.asList(
        "int", "long", "float", "double", "decimal", "bool", "char", "byte",
        "sbyte", "short", "ushort", "uint", "ulong", "DateTime"
    ));

    public CsharpApiResourceBuilder(IApiActionTemplate template, List<CodegenOperation> codegenOperations,
                                    List<CodegenModel> allModels) {
        super(template, codegenOperations, allModels);
        processAuthMethods(codegenOperations);
    }

    public IApiResourceBuilder updateTemplate() {
        template.clean();
        codegenOperationList.forEach(codegenOperation -> updateNamespaceSubPart(codegenOperation));
        template.add(CsharpApiActionTemplate.TEMPLATE_TYPE_OPTIONS);
        template.add(CsharpApiActionTemplate.TEMPLATE_TYPE_RESOURCE);
        return this;
    }

    public IApiResourceBuilder setImports(DirectoryStructureService directoryStructureService) {
        codegenOperationList.forEach(operation -> {
            // Check parameter used in instance variable of options class.
            boolean arrayExists = operation.allParams.stream()
                    .filter(parameter -> !parameter.required)
                    .filter(parameter -> parameter.isArray)
                    .count() > 0;
            // Check parameter used in getParams method of options class.
            arrayExists = arrayExists || ((List<CodegenParameter>)operation.vendorExtensions.get("x-getparams")).stream()
                    .filter(parameter -> parameter.isArray)
                    .count() > 0;
            if (arrayExists) {
                metaAPIProperties.put("array-exists-options", true);
            }
        });

        if(this.authMethod == EnumConstants.AuthType.BEARER_TOKEN.getValue()){
            metaAPIProperties.put("auth_method-bearer-token", true);
        }
        else if(this.authMethod == EnumConstants.AuthType.NOAUTH.getValue()){
            metaAPIProperties.put("auth_method-no-auth", true);
        }
        if (OperationStore.getInstance().isEnumPresentInOptions())
            metaAPIProperties.put("enum-exists-options", true);

        if (OperationStore.getInstance().isEnumPresentInResource())
            metaAPIProperties.put("enum-exists-resource", true);
        return this;
    }

    @Override
    public CsharpApiResources build() {
        return new CsharpApiResources(this);
    }

    @Override
    public ApiResourceBuilder updateOperations(Resolver<CodegenParameter> codegenParameterIResolver) { // CsharpParameterResolver
        super.updateOperations(codegenParameterIResolver);
        processAuthMethods(this.codegenOperationList);
        this.codegenOperationList.forEach(co -> {
            co.allParams.forEach(this::handleNullableParameter);
            co.headerParams.forEach(e -> codegenParameterIResolver.resolve(e, this));
            populateRequestBodyArgument(co);
            resolveIngressModel(co);
        });
        return this;
    }

    public void processAuthMethods(List<CodegenOperation> opList) {
        boolean isBasicAuthPresent = false;
        boolean isTokenAuthPresent = false;
        if(opList != null){
            List<CodegenSecurity> authMethods = opList.get(0).authMethods;
            if(authMethods != null){
                for(CodegenSecurity c : authMethods){
                    if(c.isOAuth == true){
                        isTokenAuthPresent = true;
                    }
                    if(c.isBasic == true){
                        isBasicAuthPresent = true;
                    }
                }
                if(isBasicAuthPresent != true && isTokenAuthPresent){
                    this.authMethod = EnumConstants.AuthType.BEARER_TOKEN.getValue();
                }
            }
            else{
                this.authMethod = EnumConstants.AuthType.NOAUTH.getValue();
            }
        }
    }

    @Override
    public void updateHttpMethod(CodegenOperation co) {
        switch (co.httpMethod) {
            case "GET":
                co.vendorExtensions.put(HTTP_METHOD, CsharpHttpMethod.GET.getValue());
                break;
            case "POST":
                co.vendorExtensions.put(HTTP_METHOD, CsharpHttpMethod.POST.getValue());
                break;
            case "PUT":
                co.vendorExtensions.put(HTTP_METHOD, CsharpHttpMethod.PUT.getValue());
                break;
            case "PATCH":
                co.vendorExtensions.put(HTTP_METHOD, CsharpHttpMethod.PATCH.getValue());
                break;
            case "DELETE":
                co.vendorExtensions.put(HTTP_METHOD, CsharpHttpMethod.DELETE.getValue());
                break;
        }
    }

    private void setDataType(CodegenParameter codegenParameter) {
        for (CodegenModel codegenModel : getAllModels())
            if (codegenModel.classname.equals(codegenParameter.dataType))
                codegenParameter.dataType = getApiName() + "Resource" + ApplicationConstants.DOT + codegenParameter.dataType;
    }

    private void resolveIngressModel(CodegenOperation codegenOperation) {
        // Required params are used in parameters in C#, Check Params.mustache.
        for (CodegenParameter codegenParameter: codegenOperation.requiredParams)
            setDataType(codegenParameter);

        for (CodegenParameter codegenParameter: codegenOperation.optionalParams) {
            setDataType(codegenParameter);
        }
    }

    // Here operation specific variables can be set.
    @Override
    protected Map<String, Object> mapOperation(CodegenOperation co) {
        Map<String, Object> operationMap = super.mapOperation(co);
        operationMap.put("x-non-path-params", getNonPathParams(co.allParams));
        operationMap.put("x-required-param-exists", !co.requiredParams.isEmpty()); // TODO: Rename this
        operationMap.put("x-header-param-exists", !co.headerParams.isEmpty());
        operationMap.put("x-header-params", co.headerParams);
        operationMap.put("x-getparams", generateGetParams(co));
        return operationMap;
    }

    private LinkedList<CodegenParameter> generateGetParams(CodegenOperation co) {
        LinkedHashMap<String, CodegenParameter> getParams = new LinkedHashMap<>();
        return co.allParams.stream()
                .filter(Objects::nonNull)
                .filter(parameter -> maintainOrder(parameter, getParams))
                .filter(parameter -> !parameter.isPathParam && !parameter.isHeaderParam)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    // Expected order for variable Date: Date, DateBefore, DateAfter (used in GetParams method)
    private boolean maintainOrder(CodegenParameter parameter, LinkedHashMap<String, CodegenParameter> getParams) {
        if (parameter.paramName.endsWith("Before")) {
            CodegenParameter parent = getParams.get(StringUtils.chomp(parameter.paramName, "Before"));
            if (parent != null) {
                parent.vendorExtensions.put("x-before", parameter);
                parent.vendorExtensions.put("x-before-or-after", true);
                // Skip Variable ending with Before and if its prefix exists
                return false;
            }
        } else if (parameter.paramName.endsWith("After")) {
            CodegenParameter parent = getParams.get(StringUtils.chomp(parameter.paramName, "After"));
            if (parent != null) {
                parent.vendorExtensions.put("x-after", parameter);
                parent.vendorExtensions.put("x-before-or-after", true);
                // Skip Variable ending with After and if its prefix exists
                return false;
            }
        } else {
            getParams.put(parameter.paramName, parameter);
        }
        return true;
    }

    private List<CodegenParameter> getNonPathParams(List<CodegenParameter> allParams) {
        return allParams.stream().filter(param -> !param.isPathParam).collect(Collectors.toList());
    }

    private String handleContainerDatatype(String dataType) {
        for(CsharpDataTypes csharpDataType: CsharpDataTypes.values()) {
            String containerDatatype = csharpDataType.getValue();
            if(dataType.startsWith(containerDatatype))
            {
                // removing the List<> or Dictionary<> wrapper
                String subStringWithoutContainerDatatype = dataType.substring(containerDatatype.length(), dataType.length()-1);
                if(csharpDataType == CsharpDataTypes.MAP) {
                    // For example, if dataType = "Dictionary<string, int>"
                    // subStringWithoutContainerDatatype will be "string, int"
                    // splitting the string to get key and value separately
                    String[] keyValueList = subStringWithoutContainerDatatype.split(", ");
                    String key = keyValueList[0];
                    String value = keyValueList[1];
                    return handleContainerDatatype(value); // key is not being processed since open api standard does not provide that feature
                }
                return handleContainerDatatype(subStringWithoutContainerDatatype);
            }
        }
        return dataType;
    }

    private void recursivelyResolve(CodegenProperty codegenProperty, CodegenOperation codegenOperation) {
        String modelName = codegenProperty.dataType;
        // extract the baseType for the modelName
        modelName = handleContainerDatatype(modelName);
        Optional<CodegenModel> model = Utility.getModelByClassname(allModels, modelName);
        if ((model == null) || model.isEmpty()) {
            return;
        }

        CodegenModel codegenModel = model.get();
        for(CodegenProperty property: codegenModel.vars) {
            // recursively resolve each var, since each var is itself a CodegenProperty
            recursivelyResolve(property, codegenOperation);
        }
        // these nested response models must also be generated as classes, so adding them in nestedModels
        // same nestedModels variable is used for request body nested class generation
        nestedModels.add(codegenModel);
    }

    @Override
    public ApiResourceBuilder updateResponseModel(Resolver<CodegenProperty> codegenPropertyIResolver, Resolver<CodegenModel> codegenModelResolver) {
        List<CodegenModel> responseModels = new ArrayList<>();
        codegenOperationList.forEach(codegenOperation -> {
            codegenOperation.responses.forEach(response -> {
                String modelName = response.dataType;
                if (response.dataType != null && response.dataType.startsWith(EnumConstants.CsharpDataTypes.LIST.getValue())){
                    modelName = response.baseType;
                }
                Optional<CodegenModel> responseModel = Utility.getModel(allModels, modelName, recordKey, codegenOperation);
                if ((responseModel == null) || responseModel.isEmpty() || (Integer.parseInt(response.code) >= 400)) {
                    return;
                }
                CodegenModel codegenModel = responseModel.get();
                for(CodegenProperty property: codegenModel.vars) {
                    // resolving response model recursively for nested objects
                    recursivelyResolve(property, codegenOperation);
                }
                codegenModelResolver.resolve(codegenModel, this);
                responseModels.add(codegenModel);
            });
        });
        this.apiResponseModels = getDistinctResponseModel(responseModels);
        return this;
    }

    public Set<CodegenProperty> getDistinctResponseModel(List<CodegenModel> responseModels) {
        HashMap<String, CodegenProperty> propertyMap = new HashMap<>();
        Set<CodegenProperty> distinctResponseModels = new LinkedHashSet<>();

        for (CodegenModel codegenModel: responseModels) {
            for (CodegenProperty property: codegenModel.vars) {
                property.nameInCamelCase = StringHelper.camelize(property.nameInSnakeCase);
                handleNullableProperty(property);
                // Check for operation name conflicts or nested model name conflicts
                if (Arrays.stream(EnumConstants.Operation.values())
                        .anyMatch(value -> value.getValue().equals(property.nameInCamelCase))
                        || isNestedModelPresentWithPropertyName(property)) {
                    property.nameInCamelCase = "_" + property.nameInCamelCase;
                }

                // Check if property with same name already exists, but has different metadata
                // This handles cases where properties have the same name but different types or model flags
                String propertyKey = property.name;
                if (propertyMap.containsKey(propertyKey)) {
                    CodegenProperty existingProperty = propertyMap.get(propertyKey);

                    // Check if the properties are functionally different enough to warrant duplication
                    // Only consider properties truly different if they have different data types
                    // Differences in isModel or isOverridden flags alone should not cause duplication
                    if (!Objects.equals(existingProperty.dataType, property.dataType)) {
                        property.nameInCamelCase = "_" + property.nameInCamelCase;
                    } else {
                        // If they're functionally the same (same data type), merge any metadata if needed
                        // and skip adding this property
                        continue;
                    }
                }

                // Store the property in the map for future duplicate checks
                propertyMap.put(propertyKey, property);
                distinctResponseModels.add(property);
            }
        }

        // Final check for name collisions in camelCase names (which are used in C# code)
        HashSet<String> modelVars = new HashSet<>();
        for (CodegenProperty property : distinctResponseModels) {
            if (modelVars.contains(property.nameInCamelCase)) {
                property.nameInCamelCase = "_" + property.nameInCamelCase;
            } else {
                modelVars.add(property.nameInCamelCase);
            }
        }

        return distinctResponseModels;
    }

    private void updateNamespaceSubPart(CodegenOperation codegenOperation) {
        List<String> filePathArray = new ArrayList<>(Arrays.asList(codegenOperation.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
        OperationStore.getInstance().setClassName(filePathArray.remove(filePathArray.size()-1));
        if (!filePathArray.isEmpty()) {
            final String namespacePath = filePathArray
                    .stream()
                    .collect(Collectors.joining("."));
            namespaceSubPart = "." + namespacePath;
        }
    }

    // Request Body arguments are used as arguments to following methods Create/Read/Update/Delete/Fetch(...) in resource file.
    // Order for request body arguments is: requiredParams + pathParams + conditionalParameters + formParams +
    // headerParams + optionalParameters(in case of Read or Fetch operation)
    @SuppressWarnings("unchecked")
    private void populateRequestBodyArgument(final CodegenOperation co) {
        List<CodegenParameter> conditionalParameters = new ArrayList<>();
        List<CodegenParameter> optionalParameters = new ArrayList<>();

        HashMap<String, CodegenParameter> optionalParamMap = new HashMap<>();
        for (CodegenParameter codegenParameter: co.optionalParams) {
            optionalParamMap.put(codegenParameter.paramName, codegenParameter);
        }

        /*
         * Conditional parameter are present in key: x-twilio in open api specs.
         * Populate conditionalParameters list with conditional parameters
         */
        if (co.vendorExtensions.containsKey(ApplicationConstants.TWILIO_EXTENSION_NAME)) {
            HashMap<String, Object> twilioVendorExtension = (HashMap<String, Object>) co.vendorExtensions.get(ApplicationConstants.TWILIO_EXTENSION_NAME);
            if (twilioVendorExtension != null && twilioVendorExtension.containsKey("conditional")) {
                List<List<String>> conditionalParamList = (List<List<String>>) twilioVendorExtension.get("conditional");
                // As of now conditional parameters are stored as list of list in open api specs.
                for (List<String> conditionalParams: conditionalParamList) {
                    for (String conditionalParam: conditionalParams) {
                        if (optionalParamMap.containsKey(StringHelper.camelize(conditionalParam))) {
                            conditionalParameters.add(optionalParamMap.get(StringHelper.camelize(conditionalParam)));
                            optionalParamMap.remove(StringHelper.camelize(conditionalParam));
                        }
                    }
                }
            }
        }

        // Remove path param from Optional Parameters as they appear before conditional and optional Parameter
        for (CodegenParameter pathParam: co.pathParams) {
            optionalParamMap.remove(pathParam.paramName);
        }

        // Populate optionalParameters only with Optional Parameters.
        for (CodegenParameter optionalParam: co.optionalParams) {
            if (optionalParamMap.containsKey(optionalParam.paramName)) {
                optionalParameters.add(optionalParam);
            }
        }

        rearrangeBeforeAfter(co.requiredParams);
        rearrangeBeforeAfter(co.pathParams);
        rearrangeBeforeAfter(conditionalParameters);
        rearrangeBeforeAfter(optionalParameters);
        co.optionalParams = co.optionalParams.stream().filter(parameter -> !parameter.paramName.equals("PageSize")).collect(Collectors.toList());

        // Add to vendor extension
        LinkedHashMap<String, CodegenParameter> requestBodyArgument = new LinkedHashMap<>();
        co.requiredParams.forEach(parameter -> requestBodyArgument.put(parameter.paramName, parameter));
        co.pathParams.forEach(parameter -> requestBodyArgument.put(parameter.paramName, parameter));
        conditionalParameters.forEach(parameter -> requestBodyArgument.put(parameter.paramName, parameter));
        co.formParams.forEach(parameter -> requestBodyArgument.put(parameter.paramName, parameter));
        co.headerParams.forEach(parameter -> requestBodyArgument.put(parameter.paramName, parameter));
        if (co.operationId.startsWith("List") || co.operationId.startsWith("Fetch")) {
            optionalParameters.forEach(parameter -> requestBodyArgument.putIfAbsent(parameter.paramName, parameter));
        }

        co.vendorExtensions.put("x-request-body-param", new ArrayList<>(requestBodyArgument.values()));
    }

    /*
     * Swap "Before" parameter with Normal parameter to keep following order: NameBefore, Name, NameAfter
     * Input Example: startTime, startTimeBefore, startTimeAfter
     * Output Example: startTimeBefore, startTime, startTimeAfter
     */
    private void rearrangeBeforeAfter(final List<CodegenParameter> parameters) {
        for (int index = 0; index < parameters.size(); index++) {
            CodegenParameter codegenParameter = parameters.get(index);
            if (!StringUtils.isBlank(codegenParameter.paramName) && codegenParameter.paramName.endsWith("Before")) {
                String paramName = codegenParameter.paramName.replace("Before", "");
                if (index > 0 && paramName.equals(parameters.get(index-1).paramName)) {
                    CodegenParameter codegenParameterToRearrange = parameters.get(index-1);
                    parameters.set(index, codegenParameterToRearrange);
                    parameters.set(index-1, codegenParameter);
                }
            }
        }
    }

    public boolean isNestedModelPresentWithPropertyName(final CodegenProperty property) {
        if (nestedModels == null || nestedModels.size() < 1) return false;
        Optional<CodegenModel> foundModel = nestedModels.stream()
                .filter(model -> model.classname.equals(property.name))
                .findFirst();
        return foundModel.isPresent();
    }

    /**
     * Handle nullable property in C# by appending "?" to primitive types when nullable.
     * Reference types (arrays, objects, custom classes) are already nullable in C#.
     *
     * @param property The CodegenProperty to process
     */
    protected void handleNullableProperty(CodegenProperty property) {
        if (property.isNullable && !property.dataType.endsWith("?")) {
                // Only add nullable marker to primitive types
                if (CSHARP_PRIMITIVE_TYPES.contains(property.dataType)) {
                    property.dataType = property.dataType + "?";
                }
        }
    }

    protected void handleNullableParameter(CodegenParameter parameter) {
        CodegenModel model = getModel(parameter.dataType);
        if(model != null) {
            for(CodegenProperty property: model.vars) {
                handleNullableProperty(property);
            }
        }
        else {
            if (parameter.isNullable && !parameter.dataType.endsWith("?")) {
                // Only add nullable marker to primitive types
                if (CSHARP_PRIMITIVE_TYPES.contains(parameter.dataType)) {
                    parameter.dataType = parameter.dataType + "?";
                }
            }
        }
    }
}
