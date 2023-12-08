package com.twilio.oai.resolver.python;

import com.twilio.oai.CodegenUtils;
import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.common.CodegenModelResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PythonCodegenModelResolver extends CodegenModelResolver {
    private final PythonCodegenModelDataTypeResolver codegenModelDataTypeResolver;
    private final PythonCodegenModelContainerDataTypeResolver codegenModelContainerDataTypeResolver;
    private static final String X_IMPORT = "x-import";

    public PythonCodegenModelResolver(IConventionMapper mapper, Map<String, String> modelFormatMap,
                                      List<? extends LanguageDataType> languageDataTypes) {
        this(languageDataTypes, new PythonCodegenModelDataTypeResolver(mapper, modelFormatMap));
    }

    public PythonCodegenModelResolver(List<? extends LanguageDataType> languageDataTypes,
                                      PythonCodegenModelDataTypeResolver codegenModelDataTypeResolver) {
        super(languageDataTypes, codegenModelDataTypeResolver);
        this.codegenModelDataTypeResolver = codegenModelDataTypeResolver;
        this.codegenModelContainerDataTypeResolver = new PythonCodegenModelContainerDataTypeResolver(codegenModelDataTypeResolver,
                languageDataTypes);
        this.codegenModelDataTypeResolver.setCodegenModel(this);
    }

    /**
     * Resolves the model using its nestedModel and resolving recursively. If the nestedModel is not found, then if
     * it is a container, the container is resolved else the dataType is resolved straightaway.
     * @param model the codegenModel to be resolved
     * @param apiResourceBuilder the PythonApiResourceBuilder
     * @return resolved model
     */
    @Override
    public CodegenModel resolve(CodegenModel model, ApiResourceBuilder apiResourceBuilder) {
        if (model == null) {
            return null;
        }

        for (CodegenProperty property : model.vars) {
            CodegenModel nestedModel = resolveNestedModel(property, apiResourceBuilder);
            if(nestedModel != null) {
                continue;
            }
            if (property.isContainer) {
                codegenModelContainerDataTypeResolver.resolve(property, apiResourceBuilder, this);
            } else {
                codegenModelDataTypeResolver.resolve(property, apiResourceBuilder);
            }
            property.getVendorExtensions().computeIfPresent(X_IMPORT, (key, value) -> {
                if(model.vendorExtensions.containsKey(key)) {
                    ((HashMap) model.vendorExtensions.get(key)).putAll((Map) value);
                }
                else {
                    model.vendorExtensions.put(key, value);
                }
                return value;
            });
        }
        return model;
    }

    /**
     * Sets Json name in the vendorExtensions of the property
     * @param property the dataType of the variable
     */
    private void setJsonName(CodegenProperty property) {
        if (property.name.contains("from") && property.dataFormat != null &&
                property.dataFormat.equals(ApplicationConstants.PHONE_NUMBER)) {
            property.name = "from_";
            property.vendorExtensions.put("json-name", "from");
        } else {
            property.vendorExtensions.put("json-name", property.name);
        }
    }

    /**
     * Resolves the response model using parent method
     * @param model the CodegenModel to be resolved
     * @param apiResourceBuilder the PythonApiResourceBuilder to access getApiName()
     */
    public void resolveResponseModel(CodegenModel model, ApiResourceBuilder apiResourceBuilder) {
        if (model == null) {
            return;
        }

        for (CodegenProperty property : model.vars) {
            if (property.isContainer) {
                codegenModelContainerDataTypeResolver.resolveResponseModel(property, apiResourceBuilder);
            } else {
                codegenModelDataTypeResolver.resolveResponseModel(property, apiResourceBuilder);
            }
        }

    }

    /**
     * Fetches the model for the property and resolves it. Further updates the dataType, baseType and datatypeWithEnum
     * according to the nested structure.
     * @param property the CodegenProperty to be resolved
     * @param apiResourceBuilder the PythonApiResourceBuilder to access getApiName()
     * @return resolved model
     */
    public CodegenModel resolveNestedModel(CodegenProperty property, ApiResourceBuilder apiResourceBuilder) {
        CodegenModel derivedCodegenModel = apiResourceBuilder.getModel(property.dataType);
        if(derivedCodegenModel != null && !CodegenUtils.isPropertySchemaEnum(property)) {
            this.resolve(derivedCodegenModel, apiResourceBuilder);
            CodegenUtils.mergeVendorExtensionProperty(property.vendorExtensions,(LinkedHashMap) derivedCodegenModel.getVendorExtensions().get(X_IMPORT), X_IMPORT);
            updateDataType(property, apiResourceBuilder);
            setJsonName(property);
            return derivedCodegenModel;
        }
        return null;
    }

    private void updateDataType(CodegenProperty property, ApiResourceBuilder apiResourceBuilder) {
        property.dataType = apiResourceBuilder.getApiName() + ApplicationConstants.LIST + ApplicationConstants.DOT + property.dataType;
        property.baseType = property.dataType;
        property.datatypeWithEnum = property.dataType;
    }
}
