package com.twilio.oai.resolver.common;

import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.IConventionMapper;
import org.openapitools.codegen.CodegenParameter;

import java.util.List;

public class CodegenParameterResolver extends Resolver<CodegenParameter> {

    private final IConventionMapper mapper;
    private List<? extends LanguageDataType> languageDataTypes;
    private final CodegenParameterDataTypeResolver codegenParameterDataTypeResolver = new CodegenParameterDataTypeResolver();
    private final CodegenParameterContainerDataTypeResolver codegenParameterContainerDataTypeResolver;

    public CodegenParameterResolver(IConventionMapper mapper,
                                    List<? extends LanguageDataType> languageDataTypes) {
        this.mapper = mapper;
        this.languageDataTypes = languageDataTypes;
        codegenParameterContainerDataTypeResolver = new CodegenParameterContainerDataTypeResolver(codegenParameterDataTypeResolver,
                languageDataTypes);
    }

    public CodegenParameter resolve(CodegenParameter parameter) {
        if (parameter == null) {
            return null;
        }

        codegenParameterDataTypeResolver.setMapper(mapper);

        if (parameter.isContainer) {
            codegenParameterContainerDataTypeResolver.resolve(parameter);
        } else {
            codegenParameterDataTypeResolver.resolve(parameter);
        }

        return parameter;
    }
}
