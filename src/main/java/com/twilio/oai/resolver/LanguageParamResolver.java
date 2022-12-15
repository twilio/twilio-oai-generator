package com.twilio.oai.resolver;

import com.twilio.oai.StringHelper;
import com.twilio.oai.common.Utility;

import lombok.AllArgsConstructor;
import org.openapitools.codegen.CodegenParameter;

import static com.twilio.oai.common.ApplicationConstants.DESERIALIZE_VEND_EXT;
import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_VEND_EXT;

@AllArgsConstructor
public class LanguageParamResolver extends Resolver<CodegenParameter> {
    protected IConventionMapper mapper;

    @Override
    public CodegenParameter resolve(CodegenParameter codegenParameter) {
        resolveProperties(codegenParameter);
        resolvePrefixedMap(codegenParameter);
        resolveSerialize(codegenParameter);
        resolveDeSerialize(codegenParameter);

        codegenParameter.dataType = Utility.removeEnumName(codegenParameter.dataType);
        codegenParameter.baseType = Utility.removeEnumName(codegenParameter.baseType);

        return codegenParameter;
    }

    protected void resolveProperties(CodegenParameter codegenParameter) {
        if (codegenParameter.dataFormat != null) {
            getMapperValue(codegenParameter.dataFormat, mapper.properties()).ifPresent(dataType -> codegenParameter.dataType = dataType);
        }
    }

    protected void resolveSerialize(CodegenParameter codegenParameter) {
        getMapperValue(getDataType(codegenParameter),
                       mapper.serialize()).ifPresent(serialize -> codegenParameter.vendorExtensions.put(
            SERIALIZE_VEND_EXT,
            serialize.split("\\(")[0]));
    }

    protected void resolveDeSerialize(CodegenParameter codegenParameter) {
        getMapperValue(getDataType(codegenParameter),
                       mapper.deserialize()).ifPresent(deserialize -> codegenParameter.vendorExtensions.put(
            DESERIALIZE_VEND_EXT,
            deserialize.split("\\(")[0]));
    }

    protected void resolvePrefixedMap(CodegenParameter codegenParameter) {
        if (codegenParameter.dataFormat != null && codegenParameter.dataFormat
                .startsWith(LanguageConventionResolver.PREFIXED_COLLAPSIBLE_MAP)) {
            String[] split_format_array = codegenParameter.dataFormat.split(LanguageConventionResolver.HYPHEN);
            codegenParameter.vendorExtensions.put(LanguageConventionResolver.X_PREFIXED_COLLAPSIBLE_MAP,
                    split_format_array[split_format_array.length - 1]);
            codegenParameter.dataType = (String) mapper.properties()
                    .get(LanguageConventionResolver.PREFIXED_COLLAPSIBLE_MAP);
        }
        codegenParameter.paramName = StringHelper.toFirstLetterLower(codegenParameter.paramName);
    }
}
