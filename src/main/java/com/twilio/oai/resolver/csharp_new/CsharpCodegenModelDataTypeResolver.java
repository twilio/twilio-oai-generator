package com.twilio.oai.resolver.csharp_new;

import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.common.CodegenModelDataTypeResolver;
import org.openapitools.codegen.CodegenProperty;

import java.util.Map;

// Overriding default behavior and adding "x-jsonConverter" to enum
public class CsharpCodegenModelDataTypeResolver extends CodegenModelDataTypeResolver {
    private final IConventionMapper mapper;

    public CsharpCodegenModelDataTypeResolver(IConventionMapper mapper, Map<String, String> modelFormatMap) {
        super(mapper, modelFormatMap);
        this.mapper = mapper;
    }

    public CodegenProperty resolve(CodegenProperty property) {
        super.resolve(property);
        mapper.deserialize()
                .getString(property.dataFormat)
                .ifPresent(dataType -> property.vendorExtensions.put("x-jsonConverter", dataType));

        return property;
    }
}
