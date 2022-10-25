package com.twilio.oai.resolver.common;

import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.Resolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodegenModelResolver implements Resolver<CodegenModel> {

    private Map<String, Map<String, Object>> conventionMap;
    private List<? extends LanguageDataType> languageDataTypes;
    private Map<String, String> modelFormatMap = new HashMap<>();
    private CodegenModelDataTypeResolver codegenModelDataTypeResolver;
    private CodegenModelContainerDataTypeResolver codegenModelContainerDataTypeResolver;

    public CodegenModelResolver(Map<String, Map<String, Object>> conventionMap,
                                List<? extends LanguageDataType> languageDataTypes) {
        this.conventionMap = conventionMap;
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
        codegenModelDataTypeResolver.setConventionMap(conventionMap);
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
