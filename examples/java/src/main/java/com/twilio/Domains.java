package com.twilio.rest;

public enum Domains {
    API("api"),
    FLEXAPI("flex-api"),
    VERSIONLESS("versionless"),
    MESSAGING("messaging"),
    PREVIEWIAM("preview-iam");

    private final String value;

    private Domains(final String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
