package com.twilio.oai;

import com.twilio.oai.common.ApplicationConstants;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DirectoryStructureServiceTest {
    private static final String SEPARATOR = ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;

    @Test
    public void testGetRelativeRoot() {
        final DirectoryStructureService dirService = new DirectoryStructureService(null, null, null);

        assertEquals("..", dirService.getRelativeRoot(""));
        assertEquals("..", dirService.getRelativeRoot("autopilot"));
        assertEquals("..", dirService.getRelativeRoot("autopilot" + SEPARATOR));
        assertEquals("../..", dirService.getRelativeRoot("autopilot" + SEPARATOR + "v1"));
        assertEquals("../../..", dirService.getRelativeRoot("autopilot" + SEPARATOR + "v1" + SEPARATOR + "services"));
    }
}
