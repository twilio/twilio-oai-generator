package com.twilio.oai.resolver.ruby;

import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageParamResolver;
import org.openapitools.codegen.CodegenParameter;

import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_VEND_EXT;

public class RubyParameterResolver extends LanguageParamResolver {
    public RubyParameterResolver(final IConventionMapper mapper) {
        super(mapper);
    }

    @Override
    public CodegenParameter resolve(final CodegenParameter codegenParameter) {
        super.resolve(codegenParameter);

        if (codegenParameter.isArray) {
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT, "Twilio.serialize_list");
        }
        if (codegenParameter.dataType.equals("Time")) {
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT, " Twilio.serialize_iso8601_datetime");
        }
        if (codegenParameter.paramName.equals("end")) {
            codegenParameter.paramName = "end_";
        }
        if (codegenParameter.dataType.equals("Integer")) {
            codegenParameter.dataType = "String";
        }
        return codegenParameter;
    }
}
