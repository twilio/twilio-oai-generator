package com.twilio.oai;

import java.util.Map;

import io.swagger.v3.oas.models.PathItem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PathUtilsTest {

    @Test
    public void testGetTwilioExtension() {
        final String extensionKey = "that-one";
        final PathItem pathItem = new PathItem();

        assertFalse(PathUtils.getTwilioExtension(pathItem, extensionKey).isPresent());

        pathItem.addExtension("x-twilio", null);
        assertFalse(PathUtils.getTwilioExtension(pathItem, extensionKey).isPresent());

        pathItem.addExtension("x-twilio", Map.of("other-one", "other-value"));
        assertFalse(PathUtils.getTwilioExtension(pathItem, extensionKey).isPresent());

        pathItem.addExtension("x-twilio", Map.of("that-one", "that-value"));
        assertTrue(PathUtils.getTwilioExtension(pathItem, extensionKey).isPresent());
        assertEquals("that-value", PathUtils.getTwilioExtension(pathItem, extensionKey).orElseThrow());
    }
}
