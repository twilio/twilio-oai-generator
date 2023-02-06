package com.twilio.oai.resolver.csharp;

import com.twilio.oai.resolver.IConventionMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenParameter;

import java.util.Optional;

// Resolves datatype to string and stores to "x-param-to-string" in vendorExtensions
@RequiredArgsConstructor
public class CsharpSerializer {

    private final ParameterFormat parameterFormat;

    private final IConventionMapper mapper;

    public CsharpSerializer(IConventionMapper mapper) {
        parameterFormat = new ParameterFormat();
        this.mapper = mapper;
    }

    public CodegenParameter serialize(CodegenParameter parameter) {
        parameterFormat.sanitize(parameter);

        // Look for dataformat or datatype in csharp.json (serialize).
        Optional<Object> optionalSerializedFormat = StringUtils.isNotBlank(parameter.dataFormat) ? mapper.serialize().get(parameter.dataFormat)
                : mapper.serialize().get(parameter.dataType);

        // If not found, set to default format
        String serializedFormat = (String) optionalSerializedFormat.orElse(mapper.serialize().get("-").get());

        // Replace "%s" with paramName
        serializedFormat = parameter.isMap ? String.format(serializedFormat, parameter.paramName, parameter.vendorExtensions.get("x-map-value"))
                    : String.format(serializedFormat, parameter.paramName);

        parameter.vendorExtensions.put("x-param-to-string", serializedFormat);
        return parameter;
    }
}
