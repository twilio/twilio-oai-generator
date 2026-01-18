package com.twilio.oai.common;

import java.io.File;
import java.util.function.Predicate;

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
    public static final String CONFIG_RUBY_JSON_PATH = CONFIG_PATH + File.separator + "ruby.json";

    public static final String LIST_START = "List<";

    public static final String LIST_END = ">";
    public static final String PYTHON_LIST_END = "]";

    public static final String OBJECT = "Object";
    public static final String STRING = "string";
    public static final String ARRAY = "array";
    public static final String OBJECT_ARRAY = "object[]";
    public static final String ENUM_VARS = "enumVars";

    public static final String LIST_INSTANCE = "ListInstance";

    public static final String LIST = "List";

    public static final String TWILIO_EXTENSION_NAME = "x-twilio";
    public static final String PATH_TYPE_EXTENSION_NAME = "x-path-type";
    public static final String IGNORE_EXTENSION_NAME = "x-ignore";
    public static final String REF_ENUM_EXTENSION_NAME = "x-ref-enum";
    public static final String IS_PARENT_PARAM_EXTENSION_NAME = "x-is-parent-param";
    public static final String PROMOTION_EXTENSION_NAME = "x-promotions";
    public static final String VENDOR_PREFIX = "x-";
    public static final String SERIALIZE_VEND_EXT = "x-serialize";
    public static final String IS_SERIALIZE_LIST_EXT = "isList";
    public static final String DESERIALIZE_VEND_EXT = "x-deserialize";
    public static final String ENUM = "Enum";
    public static final String RESOURCE = "Resource";
    public static final String DOT = ".";
    public static final String ACCOUNT_SID_VEND_EXT = "x-is-account-sid";
    public static final String HTTP_METHOD = "x-http-method";
    public static final String DEPENDENT_PROPERTIES = "dependentProperties";

    public static final String PREFIXED_COLLAPSIBLE_MAP = "prefixed-collapsible-map";
    public static final String PHONE_NUMBER = "phone-number";

    public static final Predicate<Integer> SUCCESS = i -> i != null && i >= 200 && i < 400;

    public static final String[] VERSION_LESS_SPECS = {"organizations"};
    
    public static final String X_DATATYPE = "x-var-datatype";
    public static final String X_VARIABLE_NAME = "x-var-name";
    public static final String X_ENUM_TYPE = "x-enum-type"; // EnumConstants.OpenApiEnumType
    public static final String X_MODEL_TYPE = "x-model-type"; // EnumConstants.OpenApiEnumType
    public static final String X_IS_MODEL = "x-is-model";
    public static final String X_REQUEST_CONTENT_TYPE = "x-request-content-type";
    public static final String GENERATOR_NAME = "generator-name";
    public static final Predicate<String> isV1 = input -> input != null && input.startsWith("v1"); // Usage: isV1.test("v1.0");

}
