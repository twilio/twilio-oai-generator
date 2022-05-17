package com.twilio.oai;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TwilioNodeGeneratorTest {
    private static final String SEPARATOR = TwilioNodeGenerator.PATH_SEPARATOR_PLACEHOLDER;

    @Test
    public void testGetRelativeRoot() {
        final TwilioNodeGenerator generator = new TwilioNodeGenerator();

        assertEquals("..", generator.getRelativeRoot(""));
        assertEquals("..", generator.getRelativeRoot("autopilot"));
        assertEquals("..", generator.getRelativeRoot("autopilot" + SEPARATOR));
        assertEquals("../..", generator.getRelativeRoot("autopilot" + SEPARATOR + "v1"));
        assertEquals("../../..", generator.getRelativeRoot("autopilot" + SEPARATOR + "v1" + SEPARATOR + "services"));
    }
}
