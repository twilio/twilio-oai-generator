package com.twilio.oai.resolver;

import com.twilio.oai.StringHelper;
import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.ApplicationConstants;

import lombok.AllArgsConstructor;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;

import java.util.List;

import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_VEND_EXT;

@AllArgsConstructor
public class LanguageParamResolver extends Resolver<CodegenParameter> {
    protected IConventionMapper mapper;

    @Override
    public CodegenParameter resolve(CodegenParameter codegenParameter, ApiResourceBuilder apiResourceBuilder) {
        resolveProperties(codegenParameter, apiResourceBuilder);
        resolvePrefixedMap(codegenParameter);
        resolveSerialize(codegenParameter);
        return codegenParameter;
    }

    protected void resolveProperties(CodegenParameter codegenParameter, ApiResourceBuilder apiResourceBuilder) {
        mapper
            .properties()
            .getString(codegenParameter.dataFormat)
            .ifPresent(dataType -> codegenParameter.dataType = dataType);
    }

    protected void resolveSerialize(CodegenParameter codegenParameter) {
        mapper
            .serialize()
            .getString(getDataType(codegenParameter))
            .ifPresent(serialize -> codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT,
                                                                          serialize.split("\\(")[0]));
    }

    protected void resolvePrefixedMap(CodegenParameter codegenParameter) {
        if (codegenParameter.dataFormat != null && codegenParameter.dataFormat
                .startsWith(ApplicationConstants.PREFIXED_COLLAPSIBLE_MAP)) {
            String[] split_format_array = codegenParameter.dataFormat.split(LanguageConventionResolver.HYPHEN);
            codegenParameter.vendorExtensions.put(LanguageConventionResolver.X_PREFIXED_COLLAPSIBLE_MAP,
                    split_format_array[split_format_array.length - 1]);
            codegenParameter.dataType = mapper.properties()
                    .getString(ApplicationConstants.PREFIXED_COLLAPSIBLE_MAP).orElse(null);
        }
        codegenParameter.paramName = StringHelper.toFirstLetterLower(codegenParameter.paramName);
    }
}
