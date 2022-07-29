package com.twilio.oai;

public enum Segments {
    SEGMENT_HYDRATE("hydrate"),
    SEGMENT_SERIALIZE("serialize"),
    SEGMENT_DESERIALIZE("deserialize"),
    SEGMENT_PROPERTIES("properties"),
    SEGMENT_LIBRARY("library"),
    //promotions is uses to provide support for different datatypes of same parameter
    SEGMENT_PROMOTIONS("promotions");

    private final String segment;

    Segments(String segment) {
        this.segment = segment;
    }

    public String getSegment() {
        return segment;
    }
}
