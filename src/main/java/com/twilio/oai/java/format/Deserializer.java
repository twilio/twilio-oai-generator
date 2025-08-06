package com.twilio.oai.java.format;

import org.openapitools.codegen.CodegenProperty;

public class Deserializer {

    public static void addDeserializer(CodegenProperty codegenProperty) {
        if (codegenProperty.getFormat() != null && OpenApiSpecFormatFeatureConstants.DESERIALIZER.containsKey(codegenProperty.getFormat())) {
            String deserializer = OpenApiSpecFormatFeatureConstants.DESERIALIZER.get(codegenProperty.getFormat());
            codegenProperty.vendorExtensions.put("x-deserializer", deserializer);
            System.out.println(deserializer);
        }
    }
}