package com.twilio.oai.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {
    public static final String ACCOUNT_SID_FORMAT = "^AC[0-9a-fA-F]{32}$";

    // Unique string devoid of symbols.
    public static final String PATH_SEPARATOR_PLACEHOLDER = "1234567890";

    public static final String CONFIG_JAVA_JSON_PATH = "config/java.json";
    public static final String CONFIG_CSHARP_JSON_PATH = "config/csharp.json";

    public static final String LIST_START = "List<";

    public static final String LIST_END = ">";

    public static final String OBJECT = "Object";
}
