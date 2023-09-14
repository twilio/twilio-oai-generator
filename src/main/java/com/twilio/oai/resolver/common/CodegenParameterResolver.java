package com.twilio.oai.resolver.common;

import com.twilio.oai.CodegenUtils;
import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.IConventionMapper;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;

import java.util.List;

public class CodegenParameterResolver extends Resolver<CodegenParameter> {

    private final CodegenParameterDataTypeResolver codegenParameterDataTypeResolver;
    private final CodegenParameterContainerDataTypeResolver codegenParameterContainerDataTypeResolver;
    private final CodegenModelResolver codegenModelResolver;

    public CodegenParameterResolver(IConventionMapper mapper,
                                    List<? extends LanguageDataType> languageDataTypes) {
        this(languageDataTypes, new CodegenParameterDataTypeResolver(mapper), null);
    }

    public CodegenParameterResolver(List<? extends LanguageDataType> languageDataTypes,
                                    CodegenParameterDataTypeResolver codegenParameterDataTypeResolver,
                                    CodegenModelResolver codegenModelResolver) {
        this.codegenParameterDataTypeResolver = codegenParameterDataTypeResolver;
        codegenParameterContainerDataTypeResolver = new CodegenParameterContainerDataTypeResolver(codegenParameterDataTypeResolver,
                languageDataTypes);
        this.codegenModelResolver = codegenModelResolver;
    }

    public CodegenParameter resolve(CodegenParameter parameter, ApiResourceBuilder apiResourceBuilder) {
        if (parameter == null) {
            return null;
        }
        
        CodegenModel codegenModel = apiResourceBuilder.getModel(parameter.dataType);
        if (codegenModel != null && codegenModelResolver != null && !CodegenUtils.isParameterSchemaEnum(parameter)) { // Request Body is json
            codegenModelResolver.resolve(codegenModel, apiResourceBuilder);
            apiResourceBuilder.addNestedModel(codegenModel);
        } else {
            if (parameter.isContainer) {
                codegenParameterContainerDataTypeResolver.resolve(parameter, apiResourceBuilder);
            } else {
                codegenParameterDataTypeResolver.resolve(parameter, apiResourceBuilder);
            } 
        }

        return parameter;
    }
}
