package com.twilio.oai.resolver.java;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.common.CodegenModelDataTypeResolver;
import lombok.RequiredArgsConstructor;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;
import java.util.Stack;

@RequiredArgsConstructor
public class ContainerResolver {
    private final List<? extends LanguageDataType> languageDataTypes;

    public CodegenProperty resolve(CodegenProperty codegenProperty, ApiResourceBuilder apiResourceBuilder, Resolver<CodegenProperty> resolver) {
        Stack<String> containerTypes = new Stack<>();

        codegenProperty.dataType = unwrapContainerType(codegenProperty,containerTypes);
        resolver.resolve(codegenProperty, apiResourceBuilder);
        rewrapContainerType(codegenProperty,containerTypes);

        return codegenProperty;
    }

    /**
     * Unwraps the container type(s) from the underlying property datatype and adds the container type(s) to the given
     * containerTypes stack. Returns the underlying property datatype (i.e. "List<IceServer>" -> "IceServer").
     * @param codegenProperty
     * @param containerTypes
     * @return
     */
    public String unwrapContainerType(CodegenProperty codegenProperty, Stack<String> containerTypes) {
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

    /**
     * Rewraps the property dataType with the container types in the given stack. Sets the property dataType to the
     * rewrapped value (i.e. "IceServer" -> "List<IceServer>").
     * @param codegenProperty
     * @param containerTypes
     */
    public static void rewrapContainerType(CodegenProperty codegenProperty, Stack<String> containerTypes) {
        String currentContainerType = "";
        while(!containerTypes.empty()) {
            currentContainerType = containerTypes.pop();
            codegenProperty.dataType = currentContainerType + codegenProperty.dataType + ApplicationConstants.LIST_END;
        }
    }

    public String unwrapContainerType(CodegenParameter codegenParameter, Stack<String> containerTypes) {
        String codegenPropertyDataType = "";
        codegenPropertyDataType = codegenParameter.dataType;

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

    public static void rewrapContainerType(CodegenParameter codegenParameter, Stack<String> containerTypes) {
        String currentContainerType = "";
        while(!containerTypes.empty()) {
            currentContainerType = containerTypes.pop();
            codegenParameter.dataType = currentContainerType + codegenParameter.dataType + ApplicationConstants.LIST_END;
        }
    }
}
