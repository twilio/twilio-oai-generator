package com.twilio.oai.resolver.python;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.common.CodegenModelContainerDataTypeResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

public class PythonCodegenModelContainerDataTypeResolver extends CodegenModelContainerDataTypeResolver {
    private final PythonCodegenModelDataTypeResolver codegenModelDataTypeResolver;
    private final List<? extends LanguageDataType> languageDataTypes;

    public PythonCodegenModelContainerDataTypeResolver(PythonCodegenModelDataTypeResolver codegenModelDataTypeResolver, List<? extends LanguageDataType> languageDataTypes) {
        super(codegenModelDataTypeResolver, languageDataTypes);
        this.codegenModelDataTypeResolver = codegenModelDataTypeResolver;
        this.languageDataTypes = languageDataTypes;
    }

    public CodegenProperty resolve(CodegenProperty codegenProperty, ApiResourceBuilder apiResourceBuilder, PythonCodegenModelResolver codegenModelResolver) {
        Stack<String> containerTypes = new Stack<>();
        codegenProperty.dataType = unwrapContainerType(codegenProperty,containerTypes);
        CodegenModel nestedModel = codegenModelResolver.resolveNestedModel(codegenProperty, apiResourceBuilder);
        if (nestedModel == null) {
            codegenModelDataTypeResolver.resolve(codegenProperty, apiResourceBuilder);
        }
        rewrapContainerType(codegenProperty,containerTypes);

        return codegenProperty;
    }

    @Override
    protected String unwrapContainerType(CodegenProperty codegenProperty, Stack<String> containerTypes) {
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
                codegenPropertyDataType = codegenPropertyDataType.replaceFirst(Pattern.quote(currentContainerType), "");
                codegenPropertyDataType = codegenPropertyDataType.substring(0, codegenPropertyDataType.length()-1);
                isContainerType = false;
            }
            else
                return codegenPropertyDataType;
        }
        return codegenPropertyDataType;
    }

    @Override
    public void rewrapContainerType(CodegenProperty codegenProperty,Stack<String> containerTypes) {
        String currentContainerType = "";
        while(!containerTypes.empty()) {
            currentContainerType = containerTypes.pop();
            codegenProperty.dataType = currentContainerType + codegenProperty.dataType + ApplicationConstants.PYTHON_LIST_END;
        }
    }
}
