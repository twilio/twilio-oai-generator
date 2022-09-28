package com.twilio.oai.common;

import lombok.AllArgsConstructor;

public class EnumConstants {

    public enum Generator {
        TWILIO_JAVA("twilio-java"),
        TWILIO_CSHARP("twilio-csharp");

        private final String value;

        public String getValue() {
            return value;
        }
        private Generator(String value) {
            this.value = value;
        }
    }

    public enum Operation {
        CREATE("Create"),
        FETCH("Fetch"),
        UPDATE("Update"),

        DELETE("Delete"),

        READ("Read");

        private final String value;

        public String getValue() {
            return value;
        }
        private Operation(String value) {
            this.value = value;
        }
    }

    public enum CsharpDataTypes {
        LIST("List<");

        private final String value;

        public String getValue() {
            return value;
        }
        private CsharpDataTypes(String value) {
            this.value = value;
        }
    }
}
