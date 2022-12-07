package com.twilio.oai.resolver.common;

import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.IConventionMapper;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodegenModelResolver implements Resolver<CodegenModel> {

    private IConventionMapper mapper;
    private List<? extends LanguageDataType> languageDataTypes;
    private Map<String, String> modelFormatMap = new HashMap<>();
    private CodegenModelDataTypeResolver codegenModelDataTypeResolver;
    private CodegenModelContainerDataTypeResolver codegenModelContainerDataTypeResolver;

    public CodegenModelResolver(IConventionMapper mapper,
                                List<? extends LanguageDataType> languageDataTypes) {
        this.mapper = mapper;
        this.languageDataTypes = languageDataTypes;
        codegenModelDataTypeResolver = new CodegenModelDataTypeResolver();
        codegenModelContainerDataTypeResolver = new CodegenModelContainerDataTypeResolver(codegenModelDataTypeResolver,
                languageDataTypes);
    }

    @Override
    public CodegenModel resolve(CodegenModel model) {
        if (model == null) {
            return null;
        }

        codegenModelDataTypeResolver.setModelFormatMap(modelFormatMap);
        codegenModelDataTypeResolver.setMapper(mapper);
        for (CodegenProperty property : model.vars) {
            if (property.isContainer) {
                codegenModelContainerDataTypeResolver.resolve(property);
            } else {
                codegenModelDataTypeResolver.resolve(property);
            }
        }

        return model;
    }

    public void setModelFormatMap(Map<String, String> modelFormatMap) {
        this.modelFormatMap = modelFormatMap;
    }
}
