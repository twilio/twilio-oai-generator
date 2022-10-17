package com.twilio.oai.resolver.common;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.Resolver;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;
import java.util.Stack;

public class CodegenModelContainerDataTypeResolver implements Resolver<CodegenProperty> {
    private CodegenModelDataTypeResolver codegenModelDataTypeResolver;
    private List<? extends LanguageDataType> languageDataTypes;

    public CodegenModelContainerDataTypeResolver(CodegenModelDataTypeResolver codegenModelDataTypeResolver,
                                                 List<? extends LanguageDataType> languageDataTypes) {
        this.codegenModelDataTypeResolver = codegenModelDataTypeResolver;
        this.languageDataTypes = languageDataTypes;
    }

    public CodegenProperty resolve(CodegenProperty codegenProperty) {
        Stack<String> containerTypes = new Stack<>();

        codegenProperty.dataType = unwrapContainerType(codegenProperty,containerTypes);
        codegenModelDataTypeResolver.resolve(codegenProperty);
        rewrapContainerType(codegenProperty,containerTypes);

        return codegenProperty;
    }

    private String unwrapContainerType(CodegenProperty codegenProperty,Stack<String> containerTypes) {
        String codegenPropertyDataType = "";
        codegenPropertyDataType = codegenProperty.dataType;

        String currentContainerType = "";
        boolean isContainerType = false;

        while(codegenPropertyDataType != null && !codegenPropertyDataType.isEmpty()) {
            for (LanguageDataType dataType : languageDataTypes) {
                if (codegenPropertyDataType.startsWith(dataType.getValue())) {
                    isContainerType = true;
                    currentContainerType = dataType.getValue();
                }
            }
            if(isContainerType) {
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

    private static void rewrapContainerType(CodegenProperty codegenProperty,Stack<String> containerTypes) {
        String currentContainerType = "";
        while(!containerTypes.empty()) {
            currentContainerType = containerTypes.pop();
            codegenProperty.dataType = currentContainerType + codegenProperty.dataType + ApplicationConstants.LIST_END;
        }
    }
}
