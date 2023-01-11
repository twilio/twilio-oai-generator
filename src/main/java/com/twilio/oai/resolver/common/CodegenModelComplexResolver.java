package com.twilio.oai.resolver.common;

import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.Resolver;

import java.util.Map;

import org.openapitools.codegen.CodegenProperty;

public class CodegenModelComplexResolver extends Resolver<CodegenProperty> {

    private final IConventionMapper mapper;
    private final Map<String, String> modelFormatMap;

    public CodegenModelComplexResolver(final IConventionMapper mapper, final Map<String, String> modelFormatMap) {
        this.mapper = mapper;
        this.modelFormatMap = modelFormatMap;
    }

    public CodegenProperty resolve(CodegenProperty property) {
        if (this.modelFormatMap.isEmpty()) {
            return property;
        }

        if (modelFormatMap.containsKey(property.complexType)) {
            String complexType = modelFormatMap.get(property.complexType);

            mapper.properties().getString(complexType).ifPresent(dataType -> {
                property.dataType = dataType;
                property.complexType = null;

                property.vendorExtensions.put("x-import", mapper.libraries().get(complexType).orElseThrow());
            });
        }
        return property;
    }
}
