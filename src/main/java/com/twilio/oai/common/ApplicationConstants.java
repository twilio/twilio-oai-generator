package com.twilio.oai.common;

import java.util.regex.Pattern;

public class ApplicationConstants {
    public static  String ACCOUNT_SID_FORMAT = "^AC[0-9a-fA-F]{32}$";

    public static final String PATH_SEPARATOR_PLACEHOLDER = "1234567890";
    // regex example : https://flex-api.twilio.com
    public static final Pattern serverUrlPattern = Pattern.compile("https:\\/\\/([a-zA-Z-]+)\\.twilio\\.com");
}
