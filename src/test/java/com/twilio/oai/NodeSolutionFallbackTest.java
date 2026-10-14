package com.twilio.oai;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.config.CodegenConfigurator;

import static com.twilio.oai.common.EnumConstants.Generator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Guards the `_solution` identifier plumbing in the generated Node Instance constructors.
 *
 * <p>An Instance receives its path params positionally, but list/page/create operations don't know
 * them up front and omit the argument. Where the response body echoes a param, the Instance falls
 * back to the parsed property, so a resource obtained from `.list()` can still build a context and
 * serve `.update()`/`.fetch()`/`.remove()`. Without that fallback `_solution.sid` is undefined and
 * the context constructor throws "Parameter 'sid' is not valid." before any request is made.
 *
 * <p>The fallback must be emitted only where a response property actually backs the param --
 * referencing a property that doesn't exist does not compile. Both directions are asserted here
 * against real specs, because each has regressed once:
 * <ul>
 *   <li>frontline v1 -- `Sid` is echoed as `sid`, so the fallback is required.</li>
 *   <li>voice v2 -- `Type` and `IdOrUniqueName` are absent from the response, so it must be omitted.</li>
 * </ul>
 */
public class NodeSolutionFallbackTest {
    private static final Path OUT = Path.of("codegen-solution-fallback");

    @BeforeClass
    public static void generate() {
        FileUtils.deleteQuietly(OUT.toFile());
        for (final String spec : List.of("twilio_frontline_v1.yaml", "twilio_voice_v2.yaml")) {
            final CodegenConfigurator configurator = new CodegenConfigurator()
                    .setGeneratorName(Generator.TWILIO_NODE.getValue())
                    .setInputSpec("src/test/resources/spec/" + spec)
                    .setOutputDir(OUT.toString())
                    .addGlobalProperty("apiTests", "false")
                    .addGlobalProperty("apiDocs", "false");
            final ClientOptInput clientOptInput = configurator.toClientOptInput();
            assertFalse(new DefaultGenerator().opts(clientOptInput).generate().isEmpty());
        }
    }

    @AfterClass
    public static void cleanup() {
        FileUtils.deleteQuietly(OUT.toFile());
    }

    /** Returns every `this._solution = { ... }` line emitted for the named resource file. */
    private static List<String> solutionLines(final String fileName) throws Exception {
        try (Stream<Path> paths = Files.walk(OUT)) {
            final Path file = paths
                    .filter(p -> p.getFileName().toString().equals(fileName))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("generator did not emit " + fileName
                            + " -- the spec fixture or its resource naming changed"));
            return Files.readAllLines(file)
                    .stream()
                    .map(String::trim)
                    .filter(line -> line.startsWith("this._solution = {"))
                    .collect(Collectors.toList());
        }
    }

    @Test
    public void paramEchoedInResponseGetsFallback() throws Exception {
        final List<String> lines = solutionLines("user.ts");

        // frontline v1 User returns `sid`, so an Instance built from a payload can recover it.
        assertTrue("expected a _solution falling back to this.sid, got " + lines,
                lines.stream().anyMatch(line -> line.contains("sid: sid || this.sid")));
    }

    @Test
    public void paramAbsentFromResponseGetsNoFallback() throws Exception {
        // voice v2 Configuration's path param `Type` is not a response property; emitting
        // `type || this.type` would reference a property that does not exist.
        for (final String line : solutionLines("configuration.ts")) {
            assertFalse("_solution must not fall back to a property absent from the response: " + line,
                    line.contains("|| this.type"));
        }

        // Same for `IdOrUniqueName`, whose identity arrives as `id`/`unique_name`.
        for (final String line : solutionLines("recording.ts")) {
            assertFalse("_solution must not fall back to a property absent from the response: " + line,
                    line.contains("|| this.idOrUniqueName"));
        }
    }

    @Test
    public void contextSolutionNeverFallsBack() throws Exception {
        // The Context constructor validates its params, so it has no payload to recover from --
        // only the Instance constructor may fall back.
        final List<String> contextLines = solutionLines("user.ts")
                .stream()
                .filter(line -> !line.contains("||"))
                .collect(Collectors.toList());
        assertFalse("expected a plain Context _solution alongside the Instance one",
                contextLines.isEmpty());
    }
}
