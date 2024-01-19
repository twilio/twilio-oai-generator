package com.twilio.oai.resolver.ruby;

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

public class RubyCodegenModelResolver extends CodegenModelResolver {
    private final RubyCodegenModelDataTypeResolver codegenModelDataTypeResolver;
    private final RubyCodegenModelContainerDataTypeResolver codegenModelContainerDataTypeResolver;
    private static final String X_IMPORT = "x-import";

    public RubyCodegenModelResolver(IConventionMapper mapper, Map<String, String> modelFormatMap,
                                      List<? extends LanguageDataType> languageDataTypes) {
        this(languageDataTypes, new RubyCodegenModelDataTypeResolver(mapper, modelFormatMap));
    }

    public RubyCodegenModelResolver(List<? extends LanguageDataType> languageDataTypes,
                                      RubyCodegenModelDataTypeResolver codegenModelDataTypeResolver) {
        super(languageDataTypes, codegenModelDataTypeResolver);
        this.codegenModelDataTypeResolver = codegenModelDataTypeResolver;
        this.codegenModelContainerDataTypeResolver = new RubyCodegenModelContainerDataTypeResolver(codegenModelDataTypeResolver,
                languageDataTypes);
        this.codegenModelDataTypeResolver.setCodegenModel(this);
    }

    /**
     * Resolves the model using its nestedModel and resolving recursively. If the nestedModel is not found, then if
     * it is a container, the container is resolved else the dataType is resolved straightaway.
     * @param model the codegenModel to be resolved
     * @param apiResourceBuilder the RubyApiResourceBuilder
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
     * Resolves the response model using parent method
     * @param model the CodegenModel to be resolved
     * @param apiResourceBuilder the RubyApiResourceBuilder to access getApiName()
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
     * @param apiResourceBuilder the RubyApiResourceBuilder to access getApiName()
     * @return resolved model
     */
    public CodegenModel resolveNestedModel(CodegenProperty property, ApiResourceBuilder apiResourceBuilder) {
        CodegenModel derivedCodegenModel = apiResourceBuilder.getModel(property.dataType);
        if(derivedCodegenModel != null && !CodegenUtils.isPropertySchemaEnum(property)) {
            this.resolve(derivedCodegenModel, apiResourceBuilder);
            CodegenUtils.mergeVendorExtensionProperty(property.vendorExtensions,(LinkedHashMap) derivedCodegenModel.getVendorExtensions().get(X_IMPORT), X_IMPORT);
            updateDataType(property, apiResourceBuilder);
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
