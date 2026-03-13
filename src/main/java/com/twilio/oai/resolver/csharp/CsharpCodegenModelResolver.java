package com.twilio.oai.resolver.csharp;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.common.CodegenModelOneOf;
import com.twilio.oai.resolver.common.CodegenModelResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;

public class CsharpCodegenModelResolver extends CodegenModelResolver {
    private final CsharpCodegenModelDataTypeResolver codegenModelDataTypeResolver;
    private final CsharpCodegenModelContainerDataTypeResolver codegenModelContainerDataTypeResolver;
    private final CodegenModelOneOf codegenModelOneOf = CodegenModelOneOf.getInstance();

    public CsharpCodegenModelResolver(List<? extends LanguageDataType> languageDataTypes,
                                      CsharpCodegenModelDataTypeResolver codegenModelDataTypeResolver) {
        super(languageDataTypes, codegenModelDataTypeResolver);
        this.codegenModelDataTypeResolver = codegenModelDataTypeResolver;
        this.codegenModelContainerDataTypeResolver = new CsharpCodegenModelContainerDataTypeResolver(
                codegenModelDataTypeResolver, languageDataTypes);
    }

    @Override
    public CodegenModel resolve(CodegenModel model, ApiResourceBuilder apiResourceBuilder) {
        if (model == null) {
            return null;
        }
        if (null != model.oneOf && !model.oneOf.isEmpty()) {
            codegenModelOneOf.resolve(model);
        }

        for (CodegenProperty property : model.vars) {
            if (property.isContainer) {
                codegenModelContainerDataTypeResolver.resolve(property, apiResourceBuilder);
            } else {
                codegenModelDataTypeResolver.resolve(property, apiResourceBuilder);
            }
        }

        return model;
    }
}
