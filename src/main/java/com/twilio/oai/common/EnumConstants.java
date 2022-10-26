package com.twilio.oai.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class EnumConstants {

    @Getter
    @RequiredArgsConstructor
    public enum Generator {
        TWILIO_CSHARP("twilio-csharp"),
        TWILIO_JAVA("twilio-java"),
        TWILIO_NODE("twilio-node");

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
}
