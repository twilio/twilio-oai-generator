package com.twilio.oai.resolver.csharp;

import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.HashMap;

public final class OperationStore {
    public static void clear() {
        className = "";
        enums.clear();
        isEnumPresentInResource = false;
        isEnumPresentInOptions = false;
    }

    public static String className;
    public static boolean isEnumPresentInResource;
    public static boolean isEnumPresentInOptions;
    public static HashMap<String, IJsonSchemaValidationProperties> enums = new HashMap<>();
}
