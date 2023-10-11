package com.twilio.oai.resolver.common;

import com.twilio.oai.CodegenUtils;
import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.IConventionMapper;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CodegenModelResolver extends Resolver<CodegenModel> {
    private final CodegenModelDataTypeResolver codegenModelDataTypeResolver;
    private final CodegenModelContainerDataTypeResolver codegenModelContainerDataTypeResolver;

    public CodegenModelResolver(IConventionMapper mapper, Map<String, String> modelFormatMap,
                                List<? extends LanguageDataType> languageDataTypes) {
        this(languageDataTypes, new CodegenModelDataTypeResolver(mapper, modelFormatMap));
    }

    public CodegenModelResolver(List<? extends LanguageDataType> languageDataTypes,
                                CodegenModelDataTypeResolver codegenModelDataTypeResolver) {
        this.codegenModelDataTypeResolver = codegenModelDataTypeResolver;
        this.codegenModelContainerDataTypeResolver = new CodegenModelContainerDataTypeResolver(codegenModelDataTypeResolver,
                languageDataTypes);
    }

    @Override
    public CodegenModel resolve(CodegenModel model, ApiResourceBuilder apiResourceBuilder) {
        if (model == null) {
            return null;
        }

        for (CodegenProperty property : model.vars) {
            CodegenModel nestedModel = resolveNestedModel(property, apiResourceBuilder);
            if(nestedModel != null) {
                return nestedModel;
            }
            if (property.isContainer) {
                codegenModelContainerDataTypeResolver.resolve(property, apiResourceBuilder, this);
            } else {
                codegenModelDataTypeResolver.resolve(property, apiResourceBuilder);
            }
            property.getVendorExtensions().computeIfPresent("x-import", (key, value) -> {
                if( model.vendorExtensions.containsKey(key)) {
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
    public CodegenModel resolveNestedModel(CodegenProperty property, ApiResourceBuilder apiResourceBuilder) {
        CodegenModel derivedCodegenModel = apiResourceBuilder.getModel(property.dataType);
            if(derivedCodegenModel != null && !CodegenUtils.isPropertySchemaEnum(property) &&
                    !codegenModelDataTypeResolver.modelFormatMap.containsKey(property.dataType)) {
                this.resolve(derivedCodegenModel, apiResourceBuilder);
                CodegenUtils.mergeVendorExtensionProperty(property.vendorExtensions,(LinkedHashMap) derivedCodegenModel.getVendorExtensions().get("x-import"), "x-import");
                return derivedCodegenModel;
            }
            return null;
    }
}
