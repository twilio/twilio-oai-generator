package com.twilio.oai.resolver;

import com.twilio.oai.common.Utility;

import lombok.AllArgsConstructor;
import org.openapitools.codegen.CodegenProperty;

import static com.twilio.oai.common.ApplicationConstants.DESERIALIZE_VEND_EXT;
import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_VEND_EXT;

@AllArgsConstructor
public class LanguagePropertyResolver extends Resolver<CodegenProperty> {
    protected IConventionMapper mapper ;

    @Override
    public CodegenProperty resolve( CodegenProperty codegenProperty) {
        resolveProperties(codegenProperty);
        resolveSerialize(codegenProperty);
        resolveDeSerialize(codegenProperty);
        resolvePrefixedMap(codegenProperty);

        codegenProperty.dataType = Utility.removeEnumName(codegenProperty.dataType);
        codegenProperty.complexType = Utility.removeEnumName(codegenProperty.complexType);

        return codegenProperty;
    }

    protected void resolveProperties(CodegenProperty codegenProperty) {
        if (codegenProperty.dataFormat != null) {
            getMapperValue(codegenProperty.dataFormat, mapper.properties()).ifPresent(dataType -> codegenProperty.dataType = dataType);
        }
    }

    protected void resolveSerialize(CodegenProperty codegenProperty) {
        getMapperValue(getDataType(codegenProperty),
                       mapper.serialize()).ifPresent(serialize -> codegenProperty.vendorExtensions.put(
            SERIALIZE_VEND_EXT,
            serialize.split("\\(")[0]));
    }

    protected void resolveDeSerialize(CodegenProperty codegenProperty) {
        getMapperValue(getDataType(codegenProperty),
                       mapper.deserialize()).ifPresent(deserialize -> codegenProperty.vendorExtensions.put(
            DESERIALIZE_VEND_EXT,
            deserialize.split("\\(")[0]));
    }

    protected void resolvePrefixedMap(CodegenProperty codegenProperty) {
        if (codegenProperty.dataFormat != null && codegenProperty.dataFormat.startsWith(LanguageConventionResolver.PREFIXED_COLLAPSIBLE_MAP)) {
            String[] split_format_array = codegenProperty.dataFormat.split(LanguageConventionResolver.HYPHEN);
            codegenProperty.vendorExtensions.put(LanguageConventionResolver.X_PREFIXED_COLLAPSIBLE_MAP, split_format_array[split_format_array.length - 1]);
            codegenProperty.dataType = (String)mapper.properties().get(LanguageConventionResolver.PREFIXED_COLLAPSIBLE_MAP);
        }
    }
}
