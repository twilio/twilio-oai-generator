package com.twilio.oai;

import java.util.NoSuchElementException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HttpMethodTest {

    @Test
    public void testFromString() {
        assertEquals(HttpMethod.GET, HttpMethod.fromString("Get"));
        assertEquals(HttpMethod.POST, HttpMethod.fromString("POST"));
        assertEquals(HttpMethod.DELETE, HttpMethod.fromString("delete"));
    }

    @Test(expected = NoSuchElementException.class)
    public void testFromStringThrows() {
        HttpMethod.fromString("YEET");
    }
}
