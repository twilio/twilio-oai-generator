package com.twilio.oai.resolver;

import com.twilio.oai.resolver.php.IConventionMapper;
import com.twilio.oai.resolver.php.LanguageConventionResolver;
import com.twilio.oai.resolver.php.PhpParameterResolver;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.utils.CamelizeOption;
import org.openapitools.codegen.utils.StringUtils;

public class LanguagePropertyResolver implements IResolver<CodegenProperty> {
    private IConventionMapper mapper ;
    public LanguagePropertyResolver(IConventionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CodegenProperty resolve( CodegenProperty codegenProperty) {
        resolveProperties(codegenProperty);
        resolveSerialize(codegenProperty);
        resolveDeSerialize(codegenProperty);
        resolvePrefixedMap(codegenProperty);
        codegenProperty.baseName = StringUtils.camelize(codegenProperty.baseName, CamelizeOption.LOWERCASE_FIRST_CHAR);
        return codegenProperty;
    }

    @Override
    public void resolveProperties(CodegenProperty codegenProperty) {
        if (mapper.properties().containsKey(codegenProperty.dataFormat)) {
            codegenProperty.dataType = (String) mapper.properties().get(codegenProperty.dataFormat);
        }
    }

    @Override
    public void resolveSerialize(CodegenProperty codegenProperty) {
        boolean hasProperty = mapper.serialize().containsKey(codegenProperty.dataFormat);
        if (hasProperty) {
            codegenProperty.vendorExtensions.put(PhpParameterResolver.SERIALIZE_VEND_EXT,(String)mapper.serialize().get(codegenProperty.dataFormat));
        }
    }

    @Override
    public void resolveDeSerialize(CodegenProperty codegenProperty) {
        boolean hasProperty = mapper.deserialize().containsKey(codegenProperty.dataFormat);
        if (hasProperty) {
            codegenProperty.vendorExtensions.put(PhpParameterResolver.DESERIALIZE_VEND_EXT, (String)mapper.deserialize().get(codegenProperty.dataFormat));
        }
    }

    @Override
    public void resolvePrefixedMap(CodegenProperty codegenProperty) {
        if (codegenProperty.dataFormat != null && codegenProperty.dataFormat.startsWith(LanguageConventionResolver.PREFIXED_COLLAPSIBLE_MAP)) {
            String[] split_format_array = codegenProperty.dataFormat.split(LanguageConventionResolver.HYPHEN);
            codegenProperty.vendorExtensions.put(LanguageConventionResolver.X_PREFIXED_COLLAPSIBLE_MAP, split_format_array[split_format_array.length - 1]);
            codegenProperty.dataType = (String)mapper.properties().get(LanguageConventionResolver.PREFIXED_COLLAPSIBLE_MAP);
        }
    }
}
