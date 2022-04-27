package com.twilio.oai;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InflectorTest  {
    @Test
    public void shouldReturnSingularWhenPluralIsProvided() {
        Inflector inflector = new Inflector();
        String actual = inflector.singular("sweets");
        assertEquals("sweet", actual);
    }

    @Test
    public void shouldReturnNotSingularizeWhenAbbrevatedStringIsProvided() {
        Inflector inflector = new Inflector();
        String actual = inflector.singular("FIFAS");
        assertEquals("FIFAS", actual);
    }

    @Test
    public void shouldReturnNotSingularizeWhenIrregularStringIsProvided() {
        Inflector inflector = new Inflector();
        String actual = inflector.singular("Aws");
        assertEquals("Aws", actual);
    }
}
