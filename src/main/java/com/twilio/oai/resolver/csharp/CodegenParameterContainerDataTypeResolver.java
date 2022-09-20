package com.twilio.oai.resolver.csharp;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.Resolver;
import org.openapitools.codegen.CodegenParameter;

public class CodegenParameterContainerDataTypeResolver implements Resolver<CodegenParameter> {
    private CodegenParameterDataTypeResolver codegenParameterDataTypeResolver;

    public CodegenParameterContainerDataTypeResolver(CodegenParameterDataTypeResolver codegenParameterDataTypeResolver) {
        this.codegenParameterDataTypeResolver = codegenParameterDataTypeResolver;
    }

    public CodegenParameter resolve(CodegenParameter parameter){
        String unwrappedContainer = unwrapContainerType(parameter);
        codegenParameterDataTypeResolver.resolve(parameter);
        rewrapContainerType(parameter,unwrappedContainer);
        return parameter;
    }

    private String unwrapContainerType(CodegenParameter parameter){
        String unwrappedContainer = null;
        for (EnumConstants.CsharpDataTypes dataType: EnumConstants.CsharpDataTypes.values()) {
            if (parameter.dataType != null && parameter.dataType.startsWith(dataType.getValue())) {
                unwrappedContainer = dataType.getValue();
                break;
            }
        }
        parameter.dataType = parameter.dataType.replace(unwrappedContainer, "");
        parameter.dataType = parameter.dataType.substring(0, parameter.dataType.length()-1);

        return unwrappedContainer;
    }


    private void rewrapContainerType(CodegenParameter parameter,String unwrappedContainer){
        parameter.dataType = unwrappedContainer + parameter.dataType + ApplicationConstants.LIST_END;
    }

}
