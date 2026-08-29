package com.twilio.oai;

import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;

public class PhpGeneratedPaginationSyntaxTest {
    private static final Path PHP_EXAMPLES = Paths.get("examples/php/src");
    private static final Path LIST_READ_TEMPLATE = Paths.get("src/main/resources/twilio-php/listRead.mustache");

    @Test
    public void phpPaginationTemplateDoesNotEmitApiV1TrailingCommas() throws IOException {
        String template = Files.readString(LIST_READ_TEMPLATE);

        assertFalse(template.contains("$pageSize = Values::NONE,\n        {{^isApiV1}}"));
        assertFalse(template.contains("$this->_page({{#vendorExtensions.x-has-non-pagination-params}}$options,{{/vendorExtensions.x-has-non-pagination-params}} $pageSize,{{^isApiV1}}"));
    }

    @Test
    public void generatedPhpPaginationMethodsDoNotUseTrailingCommas() throws IOException {
        assertNoGeneratedPhpMatches(Pattern.compile("function\\s+\\w+\\s*\\([^)]*,\\s*\\):", Pattern.DOTALL));
        assertNoGeneratedPhpMatches(Pattern.compile("_page\\([^;{}]*,\\s*\\);", Pattern.DOTALL));
    }

    private void assertNoGeneratedPhpMatches(Pattern pattern) throws IOException {
        try (Stream<Path> paths = Files.walk(PHP_EXAMPLES)) {
            paths.filter(path -> path.toString().endsWith(".php"))
                .forEach(path -> assertNoMatch(pattern, path));
        }
    }

    private void assertNoMatch(Pattern pattern, Path path) {
        try {
            String content = Files.readString(path);
            assertFalse(path.toString(), pattern.matcher(content).find());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
