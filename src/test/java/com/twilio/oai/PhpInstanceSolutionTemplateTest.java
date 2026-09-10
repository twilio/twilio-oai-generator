package com.twilio.oai;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhpInstanceSolutionTemplateTest {
    private static final Path INSTANCE_TEMPLATE = Paths.get("src/main/resources/twilio-php/instance.mustache");
    private static final Path INSTANCE_CLASSES_TEMPLATE = Paths.get("src/main/resources/twilio-php/instanceClasses.mustache");
    private static final List<Path> STRING_OUTPUT_TEMPLATES = Arrays.asList(
        INSTANCE_TEMPLATE,
        INSTANCE_CLASSES_TEMPLATE,
        Paths.get("src/main/resources/twilio-php/instanceClass.mustache"),
        Paths.get("src/main/resources/twilio-php/context.mustache")
    );

    @Test
    public void phpInstanceSolutionFallbacksAreNullSafe() throws IOException {
        assertFalse(Files.readString(INSTANCE_TEMPLATE).contains(" ?: $this->properties['"));
        assertFalse(Files.readString(INSTANCE_CLASSES_TEMPLATE).contains(" ?: $this->properties['"));
    }

    @Test
    public void phpStringOutputFormatsDateTimeSolutionValues() throws IOException {
        for (Path template : STRING_OUTPUT_TEMPLATES) {
            String content = Files.readString(template);
            assertTrue(template.toString(), content.contains("if ($value instanceof \\DateTimeInterface)"));
        }
    }
}
