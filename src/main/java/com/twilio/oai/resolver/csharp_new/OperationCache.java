package com.twilio.oai.resolver.csharp_new;

import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.HashMap;

public final class OperationCache {
    public static void clear() {
        className = new String();
        store.clear();
        enums.clear();
        isEnumPresentInResource = false;
        isEnumPresentInOptions = false;
    }

    public static String className;

    public static boolean isEnumPresentInResource; // TODO: Used in import

    public static boolean isEnumPresentInOptions;
    public static HashMap<String, Object> store = new HashMap<>();

    public static HashMap<String, IJsonSchemaValidationProperties> enums = new HashMap<>();
}
