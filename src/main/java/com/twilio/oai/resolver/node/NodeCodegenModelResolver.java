package com.twilio.oai.resolver.node;

import com.twilio.oai.CodegenUtils;
import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.common.CodegenModelResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NodeCodegenModelResolver extends CodegenModelResolver {
    private final NodeCodegenModelDataTypeResolver codegenModelDataTypeResolver;
    private final NodeCodegenModelContainerDataTypeResolver codegenModelContainerDataTypeResolver;
    private static final String X_IMPORT = "x-import";

    public NodeCodegenModelResolver(IConventionMapper mapper, Map<String, String> modelFormatMap,
                                List<? extends LanguageDataType> languageDataTypes) {
        this(languageDataTypes, new NodeCodegenModelDataTypeResolver(mapper, modelFormatMap));
    }

    public NodeCodegenModelResolver(List<? extends LanguageDataType> languageDataTypes,
                                    NodeCodegenModelDataTypeResolver codegenModelDataTypeResolver) {
        super(languageDataTypes, codegenModelDataTypeResolver);
        this.codegenModelDataTypeResolver = codegenModelDataTypeResolver;
        this.codegenModelContainerDataTypeResolver = new NodeCodegenModelContainerDataTypeResolver(codegenModelDataTypeResolver,
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
                return nestedModel;
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
            return derivedCodegenModel;
        }
        return null;
    }
}
