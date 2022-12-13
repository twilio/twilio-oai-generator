package com.twilio.oai;

import java.util.Arrays;

public enum HttpMethod {
    DELETE,
    GET,
    PATCH,
    POST,
    PUT;

    public static HttpMethod fromString(final String httpMethodString) {
        return Arrays
            .stream(HttpMethod.values())
            .filter(httpMethod -> httpMethod.name().equalsIgnoreCase(httpMethodString))
            .findFirst()
            .orElseThrow();
    }
}
