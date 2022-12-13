package com.twilio.oai.resolver;

import com.twilio.oai.StringHelper;
import lombok.AllArgsConstructor;
import org.openapitools.codegen.CodegenParameter;

import static com.twilio.oai.common.ApplicationConstants.DESERIALIZE_VEND_EXT;
import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_VEND_EXT;

@AllArgsConstructor
public class LanguageParamResolver implements ISchemaResolver<CodegenParameter> {
    protected IConventionMapper mapper;


    @Override
    public CodegenParameter resolve(CodegenParameter codegenParameter) {
        resolveProperties(codegenParameter);
        resolvePrefixedMap(codegenParameter);
        resolveSerialize(codegenParameter);
        resolveDeSerialize(codegenParameter);
        return codegenParameter;
    }

    protected void resolveProperties(CodegenParameter codegenParameter) {
        if (mapper.properties().containsKey(codegenParameter.dataFormat)) {
            codegenParameter.dataType = (String) mapper.properties().get(codegenParameter.dataFormat);
        }
    }

    protected void resolveSerialize(CodegenParameter codegenParameter) {
        boolean hasProperty = mapper.serialize().containsKey(codegenParameter.dataFormat);
        if (hasProperty) {
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT, (String) mapper.serialize().get(codegenParameter.dataFormat));
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT + LanguageConventionResolver.HYPHEN + codegenParameter.dataFormat, true);
        }
        boolean hasType = mapper.serialize().containsKey(codegenParameter.dataType);
        if (hasType) {
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT, (String) mapper.serialize().get(codegenParameter.dataType));
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT + LanguageConventionResolver.HYPHEN + codegenParameter.dataType, true);
        }
    }

    protected void resolveDeSerialize(CodegenParameter codegenParameter) {
        if (mapper.deserialize().containsKey(codegenParameter.dataFormat)) {
            codegenParameter.vendorExtensions.put(DESERIALIZE_VEND_EXT, mapper.deserialize().get(codegenParameter.dataFormat));
            codegenParameter.vendorExtensions.put(DESERIALIZE_VEND_EXT + LanguageConventionResolver.HYPHEN + codegenParameter.dataType, true);
        }

        if (mapper.deserialize().containsKey(codegenParameter.dataType)) {
            codegenParameter.vendorExtensions.put(DESERIALIZE_VEND_EXT, mapper.serialize().get(codegenParameter.dataType));
            codegenParameter.vendorExtensions.put(DESERIALIZE_VEND_EXT + LanguageConventionResolver.HYPHEN + codegenParameter.dataType, true);
        }
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
