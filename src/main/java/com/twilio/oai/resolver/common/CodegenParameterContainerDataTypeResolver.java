package com.twilio.oai.resolver.common;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.Resolver;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.openapitools.codegen.CodegenParameter;

@RequiredArgsConstructor
public class CodegenParameterContainerDataTypeResolver extends Resolver<CodegenParameter> {
    private final CodegenParameterDataTypeResolver codegenParameterDataTypeResolver;
    private final List<? extends LanguageDataType> languageDataTypes;

    public CodegenParameter resolve(CodegenParameter parameter, ApiResourceBuilder apiResourceBuilder) {
        String unwrappedContainer = unwrapContainerType(parameter);
        codegenParameterDataTypeResolver.resolve(parameter, apiResourceBuilder);
        rewrapContainerType(parameter, unwrappedContainer);
        return parameter;
    }

    private String unwrapContainerType(CodegenParameter parameter) {
        String unwrappedContainer = null;
        for (LanguageDataType dataType : languageDataTypes) {
            if (parameter.dataType != null && parameter.dataType.startsWith(dataType.getValue())) {
                unwrappedContainer = dataType.getValue();
                break;
            }
        }
        parameter.dataType = parameter.dataType.replace(unwrappedContainer, "");
        parameter.dataType = parameter.dataType.substring(0, parameter.dataType.length() - 1);

        return unwrappedContainer;
    }

    private void rewrapContainerType(CodegenParameter parameter, String unwrappedContainer) {
        parameter.dataType = unwrappedContainer + parameter.dataType + ApplicationConstants.LIST_END;
    }
}
