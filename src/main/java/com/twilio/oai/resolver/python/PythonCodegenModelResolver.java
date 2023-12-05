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
            setJsonName(property);
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

    private void setJsonName(CodegenProperty property) {
        if (property.name.contains("from") && property.dataFormat != null &&
                property.dataFormat.equals(ApplicationConstants.PHONE_NUMBER)) {
            property.name = "from_";
            property.vendorExtensions.put("json-name", "from");
        } else {
            property.vendorExtensions.put("json-name", property.name);
        }
    }

    public void resolveResponseModel(CodegenModel model, ApiResourceBuilder apiResourceBuilder) {
        if (model == null) {
            return;
        }

        for (CodegenProperty property : model.vars) {
            if (property.isContainer) {
                codegenModelContainerDataTypeResolver.resolve(property, apiResourceBuilder);
            } else {
                codegenModelDataTypeResolver.resolveResponseModel(property, apiResourceBuilder);
            }
        }

    }

    public CodegenModel resolveNestedModel(CodegenProperty property, ApiResourceBuilder apiResourceBuilder) {
        CodegenModel derivedCodegenModel = apiResourceBuilder.getModel(property.dataType);
        if(derivedCodegenModel != null && !CodegenUtils.isPropertySchemaEnum(property) &&
                !codegenModelDataTypeResolver.modelFormatMap.containsKey(property.dataType)) {
            this.resolve(derivedCodegenModel, apiResourceBuilder);
            CodegenUtils.mergeVendorExtensionProperty(property.vendorExtensions,(LinkedHashMap) derivedCodegenModel.getVendorExtensions().get(X_IMPORT), X_IMPORT);
            property.dataType = apiResourceBuilder.getApiName() + ApplicationConstants.LIST + ApplicationConstants.DOT + property.dataType;
            property.baseType = property.dataType;
            property.datatypeWithEnum = property.dataType;
            return derivedCodegenModel;
        }
        return null;
    }
}
