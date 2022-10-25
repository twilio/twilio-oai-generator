package com.twilio.oai.resolver.common;

import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.Resolver;
import org.openapitools.codegen.CodegenParameter;

import java.util.List;
import java.util.Map;

public class CodegenParameterResolver implements Resolver<CodegenParameter> {

    private final Map<String, Map<String, Object>> conventionMap;
    private List<? extends LanguageDataType> languageDataTypes;
    private CodegenParameterDataTypeResolver codegenParameterDataTypeResolver = new CodegenParameterDataTypeResolver();
    private CodegenParameterContainerDataTypeResolver codegenParameterContainerDataTypeResolver;

    public CodegenParameterResolver(Map<String, Map<String, Object>> conventionMap,
                                    List<? extends LanguageDataType> languageDataTypes) {
        this.conventionMap = conventionMap;
        this.languageDataTypes = languageDataTypes;
        codegenParameterContainerDataTypeResolver = new CodegenParameterContainerDataTypeResolver(codegenParameterDataTypeResolver,
                languageDataTypes);
    }

    public CodegenParameter resolve(CodegenParameter parameter) {
        if (parameter == null) {
            return null;
        }

        codegenParameterDataTypeResolver.setConventionMap(conventionMap);

        if (parameter.isContainer) {
            codegenParameterContainerDataTypeResolver.resolve(parameter);
        } else {
            codegenParameterDataTypeResolver.resolve(parameter);
        }

        return parameter;
    }
}
