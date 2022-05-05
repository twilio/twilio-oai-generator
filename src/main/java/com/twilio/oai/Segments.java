package com.twilio.oai;

enum Segments {
    SEGMENT_HYDRATE("hydrate"),
    SEGMENT_SERIALIZE("serialize"),
    SEGMENT_DESERIALIZE("deserialize"),
    SEGMENT_PROPERTIES("properties"),
    SEGMENT_LIBRARY("library");

    private final String segment;

    Segments(String segment) {
        this.segment = segment;
    }

    public String getSegment() {
        return segment;
    }
}
