package com.twilio.oai.resolver;

import com.twilio.oai.StringHelper;
import com.twilio.oai.resolver.php.IConventionMapper;
import com.twilio.oai.resolver.php.LanguageConventionResolver;
import org.openapitools.codegen.CodegenParameter;

public class LanguageParamResolver implements IResolver<CodegenParameter> {
    protected IConventionMapper mapper;
    public final static String SERIALIZE_VEND_EXT = "x-serialize";
    public final static String DESERIALIZE_VEND_EXT = "x-deserialize";

    public LanguageParamResolver(IConventionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CodegenParameter resolve(CodegenParameter codegenParameter) {
        resolveProperties(codegenParameter);
        resolvePrefixedMap(codegenParameter);
        resolveSerialize(codegenParameter);
        resolveDeSerialize(codegenParameter);
        return codegenParameter;
    }

    @Override
    public void resolveProperties(CodegenParameter codegenParameter) {
        if (mapper.properties().containsKey(codegenParameter.dataFormat)) {
            codegenParameter.dataType = (String) mapper.properties().get(codegenParameter.dataFormat);
        }
    }

    @Override
    public void resolveSerialize(CodegenParameter codegenParameter) {
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

    @Override
    public void resolveDeSerialize(CodegenParameter codegenParameter) {
        if (mapper.deserialize().containsKey(codegenParameter.dataFormat)) {
            codegenParameter.vendorExtensions.put(DESERIALIZE_VEND_EXT, (String) mapper.deserialize().get(codegenParameter.dataFormat));
            codegenParameter.vendorExtensions.put(DESERIALIZE_VEND_EXT + LanguageConventionResolver.HYPHEN + codegenParameter.dataType, true);
        }

        if (mapper.deserialize().containsKey(codegenParameter.dataType)) {
            codegenParameter.vendorExtensions.put(DESERIALIZE_VEND_EXT, (String) mapper.serialize().get(codegenParameter.dataType));
            codegenParameter.vendorExtensions.put(DESERIALIZE_VEND_EXT + LanguageConventionResolver.HYPHEN + codegenParameter.dataType, true);
        }
    }



    @Override
    public void resolvePrefixedMap(CodegenParameter codegenParameter) {
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
