package com.twilio.oai.resolver.csharp;

import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.common.CodegenModelContainerDataTypeResolver;
import com.twilio.oai.resolver.common.CodegenModelDataTypeResolver;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;
import java.util.Stack;

public class CsharpCodegenModelContainerDataTypeResolver extends CodegenModelContainerDataTypeResolver {

    public CsharpCodegenModelContainerDataTypeResolver(CodegenModelDataTypeResolver codegenModelDataTypeResolver,
                                                       List<? extends LanguageDataType> languageDataTypes) {
        super(codegenModelDataTypeResolver, languageDataTypes);
    }

    @Override
    protected String unwrapContainerType(CodegenProperty codegenProperty, Stack<String> containerTypes) {
        String dataType = super.unwrapContainerType(codegenProperty, containerTypes);

        // For map types (e.g. Dictionary<string, float?>), the base unwrap strips the container
        // prefix and closing bracket, leaving "string, float?" as the inner content. The resolver
        // then replaces the entire dataType based on dataFormat, losing the key type.
        // Fix: detect map types by checking for a comma in the unwrapped content, then move the
        // key portion into the container prefix on the stack so only the value type gets resolved.
        if (!containerTypes.isEmpty() && dataType.contains(",")) {
            int commaIndex = dataType.indexOf(",");
            String keyAndSeparator = dataType.substring(0, commaIndex + 1) + " ";
            String containerPrefix = containerTypes.pop();
            containerTypes.push(containerPrefix + keyAndSeparator);
            dataType = dataType.substring(commaIndex + 1).trim();
        }

        return dataType;
    }
}
