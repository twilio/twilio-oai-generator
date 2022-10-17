package com.twilio.oai.resolver.common;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.Resolver;
import org.openapitools.codegen.CodegenParameter;

import java.util.List;

public class CodegenParameterContainerDataTypeResolver implements Resolver<CodegenParameter> {

    private CodegenParameterDataTypeResolver codegenParameterDataTypeResolver;
    private List<? extends LanguageDataType> languageDataTypes;

    public CodegenParameterContainerDataTypeResolver(CodegenParameterDataTypeResolver codegenParameterDataTypeResolver,
                                                     List<? extends LanguageDataType> languageDataTypes) {
        this.codegenParameterDataTypeResolver = codegenParameterDataTypeResolver;
        this.languageDataTypes = languageDataTypes;
    }

    public CodegenParameter resolve(CodegenParameter parameter) {
        String unwrappedContainer = unwrapContainerType(parameter);
        codegenParameterDataTypeResolver.resolve(parameter);
        rewrapContainerType(parameter,unwrappedContainer);
        return parameter;
    }

    private String unwrapContainerType(CodegenParameter parameter) {
        String unwrappedContainer = null;
        for (LanguageDataType dataType: languageDataTypes) {
            if (parameter.dataType != null && parameter.dataType.startsWith(dataType.getValue())) {
                unwrappedContainer = dataType.getValue();
                break;
            }
        }
        parameter.dataType = parameter.dataType.replace(unwrappedContainer, "");
        parameter.dataType = parameter.dataType.substring(0, parameter.dataType.length()-1);

        return unwrappedContainer;
    }

    private void rewrapContainerType(CodegenParameter parameter,String unwrappedContainer) {
        parameter.dataType = unwrappedContainer + parameter.dataType + ApplicationConstants.LIST_END;
    }
}
