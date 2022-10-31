package com.twilio.oai.resolver.php;

import com.twilio.oai.StringHelper;
import org.openapitools.codegen.CodegenParameter;

public class PhpParameterResolver implements IResolver<CodegenParameter> {
    private IConventionMapper mapper;
    public final static String SERIALIZE_VEND_EXT = "x-serialize";
    public final static String DESERIALIZE_VEND_EXT = "x-deserialize";

    public PhpParameterResolver(IConventionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CodegenParameter resolve(CodegenParameter codegenParameter) {
        resolveObjectDataType(codegenParameter);
        resolvePrefixedMap(codegenParameter);
        resolveSerialize(codegenParameter);
        resolveDeSerialize(codegenParameter);
        return codegenParameter;
    }

    private void resolveObjectDataType(CodegenParameter codegenParameter) {
        if(codegenParameter.dataType.equalsIgnoreCase(LanguageConventionResolver.OBJECT) ||
                codegenParameter.dataType.equals(LanguageConventionResolver.LIST_OBJECT)) {
            codegenParameter.dataType = "array";
        }
        if (codegenParameter.dataType.contains("Enum")) {
            codegenParameter.dataType = "string";
        }
    }

    private CodegenParameter resolveSerialize(CodegenParameter codegenParameter) {
        boolean hasProperty = mapper.serialize().containsKey(codegenParameter.dataFormat);
        if (hasProperty) {
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT, (String)mapper.serialize().get(codegenParameter.dataFormat));
        }
        return codegenParameter;
    }

    private CodegenParameter resolveDeSerialize(CodegenParameter codegenParameter) {
        boolean hasProperty = mapper.deserialize().containsKey(codegenParameter.dataFormat);
        if (hasProperty) {
            codegenParameter.vendorExtensions.put(DESERIALIZE_VEND_EXT, (String)mapper.deserialize().get(codegenParameter.dataFormat));
        }
        return codegenParameter;
    }

    private CodegenParameter resolvePrefixedMap(CodegenParameter codegenParameter) {
        if (codegenParameter.dataFormat != null && codegenParameter.dataFormat
                .startsWith(LanguageConventionResolver.PREFIXED_COLLAPSIBLE_MAP)) {
            String[] split_format_array = codegenParameter.dataFormat.split(LanguageConventionResolver.HYPHEN);
            codegenParameter.vendorExtensions.put(LanguageConventionResolver.X_PREFIXED_COLLAPSIBLE_MAP,
                    split_format_array[split_format_array.length - 1]);
            codegenParameter.dataType = (String)mapper.properties()
                    .get(LanguageConventionResolver.PREFIXED_COLLAPSIBLE_MAP);
        }
        codegenParameter.paramName = StringHelper.toFirstLetterLower(codegenParameter.paramName);
        return codegenParameter;
    }
}
