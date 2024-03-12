package com.twilio.oai.resolver.php;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.java.ContainerResolver;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;
import java.util.Stack;

public class PhpContainerResolver extends ContainerResolver {
    private final List<? extends LanguageDataType> languageDataTypes;
    public PhpContainerResolver(List<? extends LanguageDataType> languageDataTypes) {
        super(languageDataTypes);
        this.languageDataTypes = languageDataTypes;
    }

    @Override
    public CodegenProperty resolve(CodegenProperty codegenProperty, ApiResourceBuilder apiResourceBuilder, Resolver<CodegenProperty> resolver) {
        return super.resolve(codegenProperty, apiResourceBuilder, resolver);
    }

    @Override
    public String unwrapContainerType(CodegenProperty codegenProperty, Stack<String> containerTypes) {
        String codegenPropertyDataType = "";
        codegenPropertyDataType = codegenProperty.dataType;

        String currentContainerType = "";
        boolean isContainerType = false;

        while(codegenPropertyDataType != null && !codegenPropertyDataType.isEmpty()) {
            for (LanguageDataType dataType : languageDataTypes) {
                if (codegenPropertyDataType.endsWith(dataType.getValue())) {
                    isContainerType = true;
                    currentContainerType = dataType.getValue();
                }
            }
            if(isContainerType) {
                containerTypes.push(currentContainerType);
                codegenPropertyDataType = codegenPropertyDataType.substring(0, codegenPropertyDataType.length()-2);
                isContainerType = false;
            }
            else
                return codegenPropertyDataType;
        }
        return codegenPropertyDataType;
    }

    @Override
    public void rewrapContainerType(CodegenProperty codegenProperty, Stack<String> containerTypes) {
        String currentContainerType = "";
        while(!containerTypes.empty()) {
            currentContainerType = containerTypes.pop();
            codegenProperty.dataType = codegenProperty.dataType + currentContainerType;
        }
    }

    @Override
    public String unwrapContainerType(CodegenParameter codegenParameter, Stack<String> containerTypes) {
        return super.unwrapContainerType(codegenParameter, containerTypes);
    }

    @Override
    public void rewrapContainerType(CodegenParameter codegenParameter, Stack<String> containerTypes) {
        String currentContainerType = "";
        while(!containerTypes.empty()) {
            currentContainerType = containerTypes.pop();
            codegenParameter.dataType = currentContainerType + codegenParameter.dataType + ApplicationConstants.LIST_END;
        }
    }
}
