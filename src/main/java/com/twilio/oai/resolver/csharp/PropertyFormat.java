package com.twilio.oai.resolver.csharp;

import com.twilio.oai.resolver.DataSanitizer;
import org.openapitools.codegen.CodegenProperty;

import java.util.ArrayList;
import java.util.List;

public class PropertyFormat implements DataSanitizer<CodegenProperty> {
    public void sanitize(CodegenProperty property) {
        if (property.dataFormat == null) return;
        if (property.isMap) {
            List<String> splitDataFormat = new ArrayList<>(List.of(property.dataFormat.split("-")));
            property.vendorExtensions.put("x-map-value", splitDataFormat.get(splitDataFormat.size()-1));
            splitDataFormat.remove(splitDataFormat.size()-1);
            property.dataFormat = String.join("-", splitDataFormat);
        }
    }
}
