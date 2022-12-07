package com.twilio.oai.common;

import java.io.File;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {
    public static final String ACCOUNT_SID_FORMAT = "^AC[0-9a-fA-F]{32}$";

    // Unique string devoid of symbols.
    public static final String PATH_SEPARATOR_PLACEHOLDER = "1234567890";

    public static final String CONFIG_PATH = "config";
    public static final String CONFIG_CSHARP_JSON_PATH = CONFIG_PATH + File.separator + "csharp.json";
    public static final String CONFIG_JAVA_JSON_PATH = CONFIG_PATH + File.separator + "java.json";
    public static final String CONFIG_NODE_JSON_PATH = CONFIG_PATH + File.separator + "node.json";
    public static final String CONFIG_PYTHON_JSON_PATH = CONFIG_PATH + File.separator + "python.json";

    public static final String LIST_START = "List<";

    public static final String LIST_END = ">";

    public static final String OBJECT = "Object";
    public static final String ARRAY = "array";
    public static final String ENUM_VARS = "enumVars";

    public static final String LIST_INSTANCE = "ListInstance";
    public static final String DEPENDENTS = "dependents";

    public static final String TWILIO_EXTENSION_NAME = "x-twilio";
    public static final String PATH_TYPE_EXTENSION_NAME = "x-path-type";
    public static final String IGNORE_EXTENSION_NAME = "x-ignore";
    public static final String SERIALIZE_EXTENSION_NAME = "x-serialize";
    public static final String DESERIALIZE_EXTENSION_NAME = "x-deserialize";
    public static final String REF_ENUM_EXTENSION_NAME = "x-ref-enum";
    public static final String IS_PARENT_PARAM_EXTENSION_NAME = "x-is-parent-param";
    public static final String PROMOTION_EXTENTION_NAME = "x-promotions";
    public static final String VENDOR_PREFIX = "x-";
    public final static String SERIALIZE_VEND_EXT = "x-serialize";
    public final static String DESERIALIZE_VEND_EXT = "x-deserialize";
}
