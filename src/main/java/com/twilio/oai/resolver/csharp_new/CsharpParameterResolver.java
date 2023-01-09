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

    public CsharpParameterResolver(IConventionMapper mapper) {
        super(mapper);
        codegenParameterResolver = new CodegenParameterResolver(mapper, Arrays.asList(EnumConstants.CsharpDataTypes.values()));
    }

    @Override
    public CodegenParameter resolve(CodegenParameter codegenParameter) {
        resolveProperties(codegenParameter);
        return codegenParameter;
    }

    @Override
    public void resolveProperties(CodegenParameter parameter) {
        codegenParameterResolver.resolve(parameter);
    }

    protected void resolvePrefixedMap(CodegenParameter codegenParameter) {

    }



}
