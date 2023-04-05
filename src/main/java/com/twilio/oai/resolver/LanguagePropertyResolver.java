package com.twilio.oai.resolver;

import com.twilio.oai.common.ApplicationConstants;

import lombok.AllArgsConstructor;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;

import static com.twilio.oai.common.ApplicationConstants.DESERIALIZE_VEND_EXT;

@AllArgsConstructor
public class LanguagePropertyResolver extends Resolver<CodegenProperty> {
    protected IConventionMapper mapper;
    protected List<CodegenModel> allModels;

    @Override
    public CodegenProperty resolve(CodegenProperty codegenProperty) {
        resolveProperties(codegenProperty);
        resolveDeSerialize(codegenProperty);
        resolvePrefixedMap(codegenProperty);
        return codegenProperty;
    }

    protected void resolveProperties(CodegenProperty codegenProperty) {
        mapper
            .properties()
            .getString(codegenProperty.dataFormat)
            .ifPresent(dataType -> codegenProperty.dataType = dataType);
    }

    protected void resolveDeSerialize(CodegenProperty codegenProperty) {
        mapper
            .deserialize()
            .getString(getDataType(codegenProperty))
            .ifPresent(deserialize -> codegenProperty.vendorExtensions.put(DESERIALIZE_VEND_EXT,
                                                                           deserialize.split("\\(")[0]));
    }

    protected void resolvePrefixedMap(CodegenProperty codegenProperty) {
        if (codegenProperty.dataFormat != null && codegenProperty.dataFormat.startsWith(ApplicationConstants.PREFIXED_COLLAPSIBLE_MAP)) {
            String[] split_format_array = codegenProperty.dataFormat.split(LanguageConventionResolver.HYPHEN);
            codegenProperty.vendorExtensions.put(LanguageConventionResolver.X_PREFIXED_COLLAPSIBLE_MAP, split_format_array[split_format_array.length - 1]);
            codegenProperty.dataType = mapper.properties().getString(ApplicationConstants.PREFIXED_COLLAPSIBLE_MAP).orElseThrow();
        }
    }
}
