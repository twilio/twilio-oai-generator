package com.twilio.oai;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InflectorTest {
    private final Inflector inflector = new Inflector();

    @Test
    public void shouldReturnSingularWhenPluralIsProvided() {
        assertEquals("sweet", inflector.singular("sweets"));
        assertEquals("summary", inflector.singular("summaries"));
        assertEquals("address", inflector.singular("addresses"));
        assertEquals("MyShoe", inflector.singular("MyShoes"));
        assertEquals("Hero", inflector.singular("Heroes"));
        assertEquals("YourData", inflector.singular("YourData"));
        assertEquals("Ox", inflector.singular("Oxen"));
        assertEquals("Primitive", inflector.singular("Primitives"));

    }

    @Test
    public void shouldReturnSingularWhenSingularIsProvided() {
        assertEquals("FIFAS", inflector.singular("FIFAS"));
        assertEquals("Aws", inflector.singular("Aws"));
        assertEquals("BuildStatus", inflector.singular("BuildStatus"));
        assertEquals("media", inflector.singular("media"));
    }
}
