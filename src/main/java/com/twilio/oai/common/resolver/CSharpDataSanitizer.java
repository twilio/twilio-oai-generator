package com.twilio.oai.common.resolver;

import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.ArrayList;
import java.util.List;

public class CSharpDataSanitizer {
    public static void sanitizeParameterFormat(CodegenParameter parameter) {
        if (parameter.dataFormat == null) return;
        if (parameter.isMap) {
            List<String> splitDataFormat = new ArrayList<>(List.of(parameter.dataFormat.split("-")));
            parameter.vendorExtensions.put("x-map-value", splitDataFormat.get(splitDataFormat.size()-1));
            splitDataFormat.remove(splitDataFormat.size()-1);
            parameter.dataFormat = String.join("-", splitDataFormat);
        }
    }

    public static void sanitizePropertyFormat(CodegenProperty property) {
        if (property.dataFormat == null) return;
        if (property.isMap) {
            List<String> splitDataFormat = new ArrayList<>(List.of(property.dataFormat.split("-")));
            splitDataFormat.remove(splitDataFormat.size()-1);
            property.dataFormat = String.join("-", splitDataFormat);
        }

    }
}
