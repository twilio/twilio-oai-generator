package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.csharp_new.CsharpEnumResolver;
import com.twilio.oai.resolver.csharp_new.OperationCache;
import com.twilio.oai.template.IApiActionTemplate;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenResponse;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;
import static com.twilio.oai.template.AbstractApiActionTemplate.API_TEMPLATE;

public class CsharpApiResourceBuilder extends ApiResourceBuilder {

    public CodegenModel responseModel;

    public List<CodegenParameter> requestBodyParams;

    private final CsharpEnumResolver csharpEnumResolver;

    public static Map<String, Object> operationCache = new HashMap<>();

    public CsharpApiResourceBuilder(IApiActionTemplate template, List<CodegenOperation> codegenOperations, List<CodegenModel> allModels) {
        super(template, codegenOperations, allModels);
        this.csharpEnumResolver = new CsharpEnumResolver();
    }

    @Override
    public IApiResourceBuilder updateTemplate() {
        template.clean();
        codegenOperationList.forEach(codegenOperation -> {
            updateNamespaceSubPart(codegenOperation);

        });
        return this;
    }

    @Override
    public IApiResourceBuilder setImports(DirectoryStructureService directoryStructureService) {
        return null;
    }

    @Override
    public CsharpApiResources build() {
        return new CsharpApiResources(this);
    }

    @Override
    public ApiResourceBuilder updateOperations(Resolver<CodegenParameter> codegenParameterIResolver) { // CsharpParameterResolver
        OperationCache.clear();
        this.codegenOperationList.forEach(co -> {
            List<String> filePathArray = new ArrayList<>(Arrays.asList(co.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
            String className = filePathArray.remove(filePathArray.size() - 1);
//            operationCache.put("className", className);
//            operationCache.put("enums", new HashMap<String, IJsonSchemaValidationProperties>());
            OperationCache.className = className;

            co.allParams = co.allParams.stream()
                    .map(codegenParameterIResolver::resolve)
                    .collect(Collectors.toList());

            co.pathParams = co.pathParams.stream()
                    .map(codegenParameterIResolver::resolve)
                    .collect(Collectors.toList());

            co.pathParams = co.pathParams.stream()
                    .map(codegenParameterIResolver::resolve)
                    .collect(Collectors.toList());

            co.pathParams.stream()
                    .map(codegenParameterIResolver::resolve)
                    .forEach(param -> param.paramName = "Path"+param.paramName);

            co.queryParams = co.queryParams.stream()
                    .map(codegenParameterIResolver::resolve)
                    .collect(Collectors.toList());

            co.formParams = co.formParams.stream()
                    .map(codegenParameterIResolver::resolve)
                    .collect(Collectors.toList());

            co.optionalParams = co.optionalParams.stream()
                    .map(codegenParameterIResolver::resolve)
                    .collect(Collectors.toList());

            co.requiredParams = co.requiredParams.stream()
                    .map(codegenParameterIResolver::resolve)
                    .collect(Collectors.toList());

            requiredPathParams.addAll(co.pathParams);
            co.vendorExtensions = mapOperation(co);
            requestBodyArgument(co);
        });

        return this;
    }

    @Override
    protected Map<String, Object> mapOperation(CodegenOperation co) {
        Map<String, Object> operationMap = super.mapOperation(co);
        operationMap.put("x-non-path-params", getNonPathParams(co.allParams));
        return operationMap;
    }

    private List<CodegenParameter> getNonPathParams(List<CodegenParameter> allParams) {
        return allParams.stream().filter(param -> !param.isPathParam).collect(Collectors.toList());
    }

    @Override
    public ApiResourceBuilder updateResponseModel(Resolver<CodegenProperty> codegenPropertyIResolver, Resolver<CodegenModel> codegenModelResolver) {
        CsharpEnumResolver csharpEnumResolver =  new CsharpEnumResolver();
        List<CodegenModel> responseModels = new ArrayList<>();
        codegenOperationList.forEach(co -> {
            for (CodegenResponse response : co.responses) {
                String modelName = response.dataType;
                Optional<CodegenModel> responseModel = Utility.getModel(allModels, modelName, recordKey, co);
                if (responseModel.isEmpty()) {
                    continue;
                }
                codegenModelResolver.resolve(responseModel.get());
                csharpEnumResolver.resolve(responseModel.get(), OperationCache.className);
                responseModels.add(responseModel.get()); // Check for DeleteCall (delete operation)
            }
        });

        this.apiResponseModels = getDistinctResponseModel(responseModels);
        return this;
    }

    public Set<CodegenProperty> getDistinctResponseModel(List<CodegenModel> responseModels) {
        Set<CodegenProperty> distinctResponseModels = new LinkedHashSet<>();
        for (CodegenModel codegenModel: responseModels) {
            for (CodegenProperty property: codegenModel.vars) {
                property.nameInCamelCase = StringHelper.camelize(property.nameInSnakeCase);
                if (Arrays
                        .stream(EnumConstants.Operation.values())
                        .anyMatch(value -> value.getValue().equals(property.nameInCamelCase))) {
                    property.nameInCamelCase = "_" + property.nameInCamelCase;
                }
                distinctResponseModels.add(property);
            }
        }
        return distinctResponseModels;
    }

    private void updateNamespaceSubPart(CodegenOperation codegenOperation) {
        List<String> filePathArray = new ArrayList<>(Arrays.asList(codegenOperation.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
        filePathArray.remove(filePathArray.size()-1);
        if (!filePathArray.isEmpty()) {
            final String namespacePath = filePathArray
                    .stream()
                    .collect(Collectors.joining("."));
            namespaceSubPart = "." + namespacePath;
        }
    }

    @SuppressWarnings("unchecked")
    private void requestBodyArgument(final CodegenOperation co) {
        List<CodegenParameter> conditionalParameters = new ArrayList<>();
        List<CodegenParameter> optionalParameters = new ArrayList<>();

        HashMap<String, CodegenParameter> optionalParamMap = new HashMap<>();
        for (CodegenParameter codegenParameter: co.optionalParams) {
            optionalParamMap.put(codegenParameter.paramName, codegenParameter);
        }

        // Process Conditional Parameters
        if (co.vendorExtensions.containsKey("x-twilio")) {
            HashMap<String, Object> twilioVendorExtension = (HashMap<String, Object>) co.vendorExtensions.get("x-twilio");
            if (twilioVendorExtension != null && twilioVendorExtension.containsKey("conditional")) {
                List<List<String>> conditionalParamList = (List<List<String>>) twilioVendorExtension.get("conditional");
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

        // Process Optional Parameters
        for (CodegenParameter optionalParam: co.optionalParams) {
            if (optionalParamMap.containsKey(optionalParam.paramName)) {
                optionalParameters.add(optionalParam);
            }
        }

        rearrangeBeforeAfter(co.requiredParams);
        rearrangeBeforeAfter(co.pathParams);
        rearrangeBeforeAfter(conditionalParameters);
        rearrangeBeforeAfter(optionalParameters);

        // Add to vendor extension
        LinkedHashMap<String, CodegenParameter> requestBodyArgument = new LinkedHashMap<>();
        co.requiredParams.forEach(parameter -> requestBodyArgument.put(parameter.paramName, parameter));
        co.pathParams.forEach(parameter -> requestBodyArgument.put(parameter.paramName, parameter));
        conditionalParameters.forEach(parameter -> requestBodyArgument.put(parameter.paramName, parameter));
        co.formParams.forEach(parameter -> requestBodyArgument.put(parameter.paramName, parameter));
        co.headerParams.forEach(parameter -> requestBodyArgument.put(parameter.paramName, parameter));
        if (co.operationId.startsWith("List") || co.operationId.startsWith("Fetch")) {
            optionalParameters.forEach(parameter -> requestBodyArgument.put(parameter.paramName, parameter));
        }
        //co.vendorExtensions.put("x-request-body-param", new ArrayList<>(requestBodyArgument.values()));
        this.requestBodyParams = new ArrayList<>(requestBodyArgument.values());
    }

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
}
