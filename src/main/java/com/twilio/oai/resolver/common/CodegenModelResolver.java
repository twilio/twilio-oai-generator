package com.twilio.oai.resolver.common;

import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.IConventionMapper;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;
import java.util.Map;

public class CodegenModelResolver extends Resolver<CodegenModel> {
    private final CodegenModelDataTypeResolver codegenModelDataTypeResolver;
    private final CodegenModelContainerDataTypeResolver codegenModelContainerDataTypeResolver;

    public CodegenModelResolver(IConventionMapper mapper, Map<String, String> modelFormatMap,
                                List<? extends LanguageDataType> languageDataTypes) {
        this(languageDataTypes, new CodegenModelDataTypeResolver(mapper, modelFormatMap));
    }

    public CodegenModelResolver(List<? extends LanguageDataType> languageDataTypes,
                                CodegenModelDataTypeResolver codegenModelDataTypeResolver) {
        this.codegenModelDataTypeResolver = codegenModelDataTypeResolver;
        this.codegenModelContainerDataTypeResolver = new CodegenModelContainerDataTypeResolver(codegenModelDataTypeResolver,
                languageDataTypes);
    }

    @Override
    public CodegenModel resolve(CodegenModel model) {
        if (model == null) {
            return null;
        }

        for (CodegenProperty property : model.vars) {
            if (property.isContainer) {
                codegenModelContainerDataTypeResolver.resolve(property);
            } else {
                codegenModelDataTypeResolver.resolve(property);
            }
        }

        return model;
    }
}
