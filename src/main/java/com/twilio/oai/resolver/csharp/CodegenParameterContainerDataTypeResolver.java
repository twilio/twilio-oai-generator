package com.twilio.oai.resolver.csharp;

import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.Resolver;

import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.openapitools.codegen.CodegenParameter;

@RequiredArgsConstructor
public class CodegenParameterContainerDataTypeResolver extends Resolver<CodegenParameter> {
    private final CodegenParameterDataTypeResolver codegenParameterDataTypeResolver;

    public CodegenParameter resolve(CodegenParameter parameter){
        String unwrappedContainer = unwrapContainerType(parameter);
        codegenParameterDataTypeResolver.resolve(parameter);
        setHasEnumParamsVendorExtension(parameter);
        rewrapContainerType(parameter,unwrappedContainer);
        return parameter;
    }

    private void setHasEnumParamsVendorExtension(CodegenParameter parameter){
        Set<String> enumsDict = codegenParameterDataTypeResolver.getEnumsDict();
        if(StringHelper.existInSetIgnoreCase(parameter.dataType, enumsDict)){//List of enums present
            parameter.vendorExtensions.put("x-has-enum-params", true);
        }
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
