package com.twilio.oai.resolver.csharp_new;

import com.twilio.oai.api.CsharpApiResourceBuilder;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageParamResolver;
import com.twilio.oai.resolver.common.CodegenParameterResolver;
import org.openapitools.codegen.CodegenParameter;

import java.util.Arrays;

public class CsharpParameterResolver extends LanguageParamResolver {

    private final CodegenParameterResolver codegenParameterResolver;

    // Resolve Enums here
    private final CsharpEnumResolver csharpEnumResolver;

    public CsharpParameterResolver(IConventionMapper mapper) {
        super(mapper);
        codegenParameterResolver = new CodegenParameterResolver(mapper, Arrays.asList(EnumConstants.CsharpDataTypes.values()));
        csharpEnumResolver = new CsharpEnumResolver();
    }

    @Override
    public void resolveProperties(CodegenParameter parameter) {
        codegenParameterResolver.resolve(parameter);
        csharpEnumResolver.resolve(parameter, OperationCache.className);
    }



}
