package com.twilio.oai.resolver.csharp;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.DataSanitizer;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParameterFormat implements DataSanitizer<CodegenParameter> {
    public void sanitize(CodegenParameter parameter) {
        if (parameter.dataFormat == null) return;
        if (parameter.isMap && StringUtils.isNotBlank(parameter.dataFormat)
                && Arrays.stream(EnumConstants.ParameterMaps.values())
                .anyMatch(value -> parameter.dataFormat.startsWith(value.getValue()))) {
            // Example: dataformat = prefixed-collapsible-map-AddOns
            List<String> splitDataFormat = new ArrayList<>(List.of(parameter.dataFormat.split("-")));
            parameter.vendorExtensions.put("x-map-value", splitDataFormat.get(splitDataFormat.size()-1));
            splitDataFormat.remove(splitDataFormat.size()-1);
            parameter.dataFormat = String.join("-", splitDataFormat);
            parameter.vendorExtensions.put("x-parameter-map", true);
        }
    }
}
