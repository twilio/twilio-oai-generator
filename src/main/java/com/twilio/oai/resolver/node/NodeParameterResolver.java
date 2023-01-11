package com.twilio.oai.resolver.node;

import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageParamResolver;

import org.openapitools.codegen.CodegenParameter;

import static com.twilio.oai.common.ApplicationConstants.OBJECT;
import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_VEND_EXT;

public class NodeParameterResolver extends LanguageParamResolver {
    public NodeParameterResolver(final IConventionMapper mapper) {
        super(mapper);
    }

    @Override
    public CodegenParameter resolve(final CodegenParameter codegenParameter) {
        super.resolve(codegenParameter);

        if (codegenParameter.isArray) {
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT, "serialize.map");
            final String transform = codegenParameter.baseType.equals("any") ? mapper
                .serialize()
                .getString(OBJECT)
                .map(serialize -> serialize.replaceAll("\\{.*}", "e"))
                .orElseThrow() : "(e)";
            codegenParameter.vendorExtensions.put("x-transform", transform);
        }

        if (mapper.libraries().getMap(codegenParameter.dataFormat).isPresent()){
            codegenParameter.vendorExtensions.put("x-import", mapper.libraries().get(codegenParameter.dataFormat).orElseThrow());
        }

        return codegenParameter;
    }
}
