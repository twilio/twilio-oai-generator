package com.twilio.oai.resolver.common;

import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.Resolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodegenModelResolver implements Resolver<CodegenModel> {

    private Map<String, Map<String, Object>> conventionMap;
    private List<? extends LanguageDataType> languageDataTypes;
    private String className;
    private Map<String, String> modelFormatMap = new HashMap<>();
    private CodegenModelDataTypeResolver codegenModelDataTypeResolver;
    private CodegenModelContainerDataTypeResolver codegenModelContainerDataTypeResolver;

    public CodegenModelResolver(Map<String, Map<String, Object>> conventionMap,
                                List<? extends LanguageDataType> languageDataTypes,
                                String listStart,
                                String listEnd) {
        this.conventionMap = conventionMap;
        this.languageDataTypes = languageDataTypes;
        codegenModelDataTypeResolver = new CodegenModelDataTypeResolver(listStart, listEnd);
        codegenModelContainerDataTypeResolver = new CodegenModelContainerDataTypeResolver(codegenModelDataTypeResolver,
                languageDataTypes);
    }

    @Override
    public CodegenModel resolve(CodegenModel model) {
        if (model == null) {
            return null;
        }

        for (CodegenProperty property : model.vars) {
            codegenModelDataTypeResolver.setModelFormatMap(modelFormatMap);
            codegenModelDataTypeResolver.setConventionMap(conventionMap);

            if (isContainerType(property)) {
                codegenModelContainerDataTypeResolver.resolve(property);
            } else {
                codegenModelDataTypeResolver.resolve(property);
            }
        }

        return model;
    }

    private boolean isContainerType(CodegenProperty codegenProperty) {
        for (LanguageDataType dataType : languageDataTypes) {
            if (codegenProperty.dataType != null && codegenProperty.dataType.startsWith(dataType.getValue())) {
                return true;
            }
        }
        return false;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setModelFormatMap(Map<String, String> modelFormatMap) {
        this.modelFormatMap = modelFormatMap;
    }
}
