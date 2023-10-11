package com.twilio.oai.resolver.node;

import com.twilio.oai.CodegenUtils;
import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageParamResolver;

import com.twilio.oai.resolver.common.CodegenModelResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;

import java.util.List;

import static com.twilio.oai.common.ApplicationConstants.OBJECT;
import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_VEND_EXT;

public class NodeParameterResolver extends LanguageParamResolver {

    public NodeParameterResolver(final IConventionMapper mapper, final CodegenModelResolver codegenModelResolver) {
        super(mapper, codegenModelResolver);
    }

    @Override
    public CodegenParameter resolve(final CodegenParameter codegenParameter, ApiResourceBuilder apiResourceBuilder) {
        super.resolve(codegenParameter, apiResourceBuilder);

        if (codegenParameter.isArray) {
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT, "serialize.map");
            final String transform = codegenParameter.baseType.equals("any") ? mapper
                .serialize()
                .getString(OBJECT)
                .map(serialize -> serialize.replaceAll("\\{.*}", "e"))
                .orElseThrow() : "(e)";
            codegenParameter.vendorExtensions.put("x-transform", transform);
        }

        mapper.libraries().getMap(codegenParameter.dataFormat).ifPresent(imports -> codegenParameter.vendorExtensions.put("x-import", imports));
        return codegenParameter;
    }
}
