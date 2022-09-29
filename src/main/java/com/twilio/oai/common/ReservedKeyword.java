package com.twilio.oai.common;

public class ReservedKeyword {

    public enum Csharp {
        CREATE("Create"),
        READ("Read"),
        UPDATE("Update"),
        DELETE("Delete"),
        FETCH("Fetch");

        private final String value;

        public String getValue() {
            return value;
        }
        private Csharp(String value) {
            this.value = value;
        }
    }
}
