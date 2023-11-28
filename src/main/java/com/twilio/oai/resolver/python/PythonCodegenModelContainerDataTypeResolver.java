package com.twilio.oai.resolver.python;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.common.CodegenModelContainerDataTypeResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;
import java.util.Stack;

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

}
