package com.twilio.oai.resolver.ruby;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageParamResolver;
import com.twilio.oai.resolver.common.CodegenModelResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;

import java.util.List;

import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_VEND_EXT;
import static com.twilio.oai.common.ApplicationConstants.IS_SERIALIZE_LIST_EXT;

public class RubyParameterResolver extends LanguageParamResolver {
    public static final String ARRAY_OF_OBJECT = "Array[Object]";

    public RubyParameterResolver(final IConventionMapper mapper, final CodegenModelResolver codegenModelResolver) {
        super(mapper, codegenModelResolver);
    }

    @Override
    public CodegenParameter resolve(final CodegenParameter codegenParameter, ApiResourceBuilder apiResourceBuilder) {
        super.resolve(codegenParameter, apiResourceBuilder);

        if (codegenParameter.isArray) {
            codegenParameter.dataType = codegenParameter.dataType.replaceAll("<", "[");
            codegenParameter.dataType = codegenParameter.dataType.replaceAll(">", "]");
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT, "Twilio.serialize_list");
            codegenParameter.vendorExtensions.put(IS_SERIALIZE_LIST_EXT, "true");
        }
        if (codegenParameter.dataType.equals("Time")) {
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT, "Twilio.serialize_iso8601_datetime");
        }
        if (codegenParameter.paramName.equals("end")) {
            codegenParameter.paramName = "end_";
        }
        if (codegenParameter.dataType.equals("Integer")) {
            codegenParameter.dataType = "String";
        }
        if (codegenParameter.dataType.equals(ARRAY_OF_OBJECT)) {
            codegenParameter.dataType = "Array[Hash]";
            codegenParameter.vendorExtensions.put("x-is-array-of-objects", "Twilio.serialize_object(e)");
        }
        if(codegenParameter.dataType.equals("phone-number")){
            codegenParameter.vendorExtensions.put("x-is-phone-number", true);
        }
        return codegenParameter;
    }
}
