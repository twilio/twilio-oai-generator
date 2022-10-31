package com.twilio.oai.resolver.php;

import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.utils.CamelizeOption;
import org.openapitools.codegen.utils.StringUtils;

public class PhpPropertyResolver implements IResolver<CodegenProperty> {
    private  IConventionMapper mapper ;
    public PhpPropertyResolver(IConventionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CodegenProperty resolve( CodegenProperty codegenProperty) {
        resolveObjectDataType(codegenProperty);
        resolveSerialize(codegenProperty);
        resolveDeSerialize(codegenProperty);
        resolvePrefixedMap(codegenProperty);
        codegenProperty.baseName = StringUtils.camelize(codegenProperty.baseName, CamelizeOption.LOWERCASE_FIRST_CHAR);
        return codegenProperty;
    }

    private void resolveObjectDataType(CodegenProperty codegenProperty) {
        if(codegenProperty.dataType.equalsIgnoreCase(LanguageConventionResolver.OBJECT) || codegenProperty.dataType.equals(LanguageConventionResolver.LIST_OBJECT)) {
            codegenProperty.dataType = "array";
        }
        if (codegenProperty.dataType.contains("Enum")) {
            codegenProperty.dataType = "string";
        }
    }

    private CodegenProperty resolveSerialize(CodegenProperty codegenProperty) {
        boolean hasProperty = mapper.serialize().containsKey(codegenProperty.dataFormat);
        if (hasProperty) {
            codegenProperty.vendorExtensions.put(PhpParameterResolver.SERIALIZE_VEND_EXT,(String)mapper.serialize().get(codegenProperty.dataFormat));
        }
        return codegenProperty;
    }

    private CodegenProperty resolveDeSerialize(CodegenProperty codegenProperty) {
        boolean hasProperty = mapper.deserialize().containsKey(codegenProperty.dataFormat);
        if (hasProperty) {
            codegenProperty.vendorExtensions.put(PhpParameterResolver.DESERIALIZE_VEND_EXT, (String)mapper.deserialize().get(codegenProperty.dataFormat));
        }
        return codegenProperty;
    }

    private CodegenProperty resolvePrefixedMap(CodegenProperty codegenProperty) {
        if (codegenProperty.dataFormat != null && codegenProperty.dataFormat.startsWith(LanguageConventionResolver.PREFIXED_COLLAPSIBLE_MAP)) {
            String[] split_format_array = codegenProperty.dataFormat.split(LanguageConventionResolver.HYPHEN);
            codegenProperty.vendorExtensions.put(LanguageConventionResolver.X_PREFIXED_COLLAPSIBLE_MAP, split_format_array[split_format_array.length - 1]);
            codegenProperty.dataType = (String)mapper.properties().get(LanguageConventionResolver.PREFIXED_COLLAPSIBLE_MAP);
        }
        return codegenProperty;
    }
}
