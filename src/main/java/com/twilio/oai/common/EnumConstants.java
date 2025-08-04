package com.twilio.oai.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class EnumConstants {

    @Getter
    @RequiredArgsConstructor
    public enum Generator {
        TWILIO_CSHARP("twilio-csharp"),
        TWILIO_JAVA("twilio-java"),
        TWILIO_JAVA_MODERN("twilio-java-modern"),
        TWILIO_NODE("twilio-node"),
        TWILIO_PHP("twilio-php"),
        TWILIO_PYTHON("twilio-python"),
        TWILIO_GO("twilio-go"),
        TWILIO_TERRAFORM("terraform-provider-twilio"),
        TWILIO_RUBY("twilio-ruby");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Operation {
        CREATE("Create"),
        FETCH("Fetch"),
        UPDATE("Update"),

        DELETE("Delete"),

        READ("Read");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum PathType {
        LIST("list"),
        INSTANCE("instance");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum AuthType {
        BEARER_TOKEN("BearerToken"),
        NOAUTH("NoAuth");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum CsharpDataTypes implements LanguageDataType {
        LIST("List<");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum JavaDataTypes implements LanguageDataType {
        LIST("List<");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum NodeDataTypes implements LanguageDataType {
        LIST("Array<");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum PythonDataTypes implements LanguageDataType {
        LIST("List[");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum RubyDataTypes implements LanguageDataType {
        LIST("Array<");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum PredefinedJavaEnumsFormat {
        HTTP_METHOD("http-method");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum PhpDataTypes implements LanguageDataType {
        LIST("[]");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum JavaHttpMethod {
        GET("HttpMethod.GET"),
        POST("HttpMethod.POST"),
        PUT("HttpMethod.PUT"),
        PATCH("HttpMethod.PATCH"),
        DELETE("HttpMethod.DELETE"),
        HEAD("HttpMethod.HEAD"),
        OPTIONS("HttpMethod.OPTIONS");

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum CsharpHttpMethod {
        GET("HttpMethod.Get"),
        POST("HttpMethod.Post"),
        PUT("HttpMethod.Put"),
        DELETE("HttpMethod.Delete");

        private final String value;
    }
}
