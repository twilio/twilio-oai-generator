package com.twilio.oai.resolver.csharp;

import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.Resolver;

import java.util.Set;
import java.util.Stack;

import org.openapitools.codegen.CodegenProperty;

public class CodegenModelContainerDataTypeResolver extends Resolver<CodegenProperty> {
    private final CodegenModelDataTypeResolver codegenModelDataTypeResolver;

    public CodegenModelContainerDataTypeResolver(CodegenModelDataTypeResolver codegenModelDataTypeResolver) {
        this.codegenModelDataTypeResolver = codegenModelDataTypeResolver;
    }

    public CodegenProperty resolve(CodegenProperty codegenProperty){
        Stack<String> containerTypes = new Stack<>();

        codegenProperty.dataType = unwrapContainerType(codegenProperty,containerTypes);

        codegenModelDataTypeResolver.resolve(codegenProperty);
        setHasEnumParamsVendorExtension(codegenProperty);
        rewrapContainerType(codegenProperty,containerTypes);

        return codegenProperty;
    }

    public void setHasEnumParamsVendorExtension(CodegenProperty codegenProperty){
        Set<String> enumsDict = codegenModelDataTypeResolver.getEnumsDict();
        if(StringHelper.existInSetIgnoreCase(codegenProperty.dataType, enumsDict)){//List of enums present
            codegenProperty.vendorExtensions.put("x-has-enum-params", true);
        }
    }

    private static String unwrapContainerType(CodegenProperty codegenProperty,Stack<String> containerTypes){
        String codegenPropertyDataType = "";
        codegenPropertyDataType = codegenProperty.dataType;

        String currentContainerType = "";
        boolean isContainerType = false;

        while(codegenPropertyDataType != null && !codegenPropertyDataType.isEmpty()){
            for (EnumConstants.CsharpDataTypes dataType: EnumConstants.CsharpDataTypes.values()){
                if (codegenPropertyDataType.startsWith(dataType.getValue())){
                    isContainerType = true;
                    currentContainerType = dataType.getValue();
                }
            }
            if(isContainerType){
                containerTypes.push(currentContainerType);
                codegenPropertyDataType = codegenPropertyDataType.replaceFirst(currentContainerType, "");
                codegenPropertyDataType = codegenPropertyDataType.substring(0, codegenPropertyDataType.length()-1);
                isContainerType = false;
            }
            else
                return codegenPropertyDataType;
        }
        return codegenPropertyDataType;
    }

    private static void rewrapContainerType(CodegenProperty codegenProperty,Stack<String> containerTypes){
        String currentContainerType = "";
        while(!containerTypes.empty()){
            currentContainerType = containerTypes.pop();
            codegenProperty.dataType = currentContainerType + codegenProperty.dataType + ApplicationConstants.LIST_END;
        }
    }
}
