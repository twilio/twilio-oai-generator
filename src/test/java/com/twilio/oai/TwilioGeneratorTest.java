package com.twilio.oai;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.config.CodegenConfigurator;

import static com.twilio.oai.common.EnumConstants.Generator;
import static org.junit.Assert.assertFalse;

/**
 * This test allows you to easily launch your code generation software under a debugger. Then run this test under debug
 * mode. You will be able to step through your java code and then see the results in the out directory.
 */
@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class TwilioGeneratorTest {
    @Parameterized.Parameters
    public static Collection<Generator> generators() {
        return Arrays.asList(Generator.TWILIO_CSHARP,
                Generator.TWILIO_GO,
                Generator.TWILIO_JAVA,
                Generator.TWILIO_NODE,
                Generator.TWILIO_PHP,
                Generator.TWILIO_PYTHON,
                Generator.TWILIO_RUBY,
                Generator.TWILIO_TERRAFORM);
    }

    private final Generator generator;

    @BeforeClass
    public static void setUp() {
        FileUtils.deleteQuietly(new File("codegen"));
    }

    @Test
    public void launchGenerator() {
        final String pathname = "examples/spec/twilio_api_v2010.yaml";
//        final String pathname = "examples/twilio_messaging_bulk_v1.yaml";
        File filesList[] ;
        File directoryPath = new File(pathname);
        if (directoryPath.isDirectory()) {
            filesList = directoryPath.listFiles();
        } else {
            filesList = new File[]{directoryPath};
        }
        for (File file: filesList) {
            final CodegenConfigurator configurator = new CodegenConfigurator()
                    .setGeneratorName(generator.getValue())
                    .setInputSpec(file.getPath())
                    .setOutputDir("codegen/" + generator.getValue())
                    .setInlineSchemaNameDefaults(Map.of("arrayItemSuffix", ""))
                    .addGlobalProperty("apiTests", "false")
                    .addGlobalProperty("apiDocs", "false");
            final ClientOptInput clientOptInput = configurator.toClientOptInput();
            DefaultGenerator generator = new DefaultGenerator();
            final List<File> output = generator.opts(clientOptInput).generate();
            assertFalse(output.isEmpty());
        }




    }
}
