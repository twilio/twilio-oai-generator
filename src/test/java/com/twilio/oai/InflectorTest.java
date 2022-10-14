package com.twilio.oai;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InflectorTest {
    @Test
    public void shouldReturnSingularWhenPluralIsProvided() {
        Inflector inflector = new Inflector();
        assertEquals("sweet", inflector.singular("sweets"));
        assertEquals("FIFAS", inflector.singular("FIFAS"));
        assertEquals("Aws", inflector.singular("Aws"));
        assertEquals("summary", inflector.singular("summaries"));
        assertEquals("address", inflector.singular("addresses"));
    }
}
