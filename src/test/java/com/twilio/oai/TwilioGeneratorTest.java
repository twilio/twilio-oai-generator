package com.twilio.oai;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.twilio.oai.common.EnumConstants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.config.CodegenConfigurator;

import static org.junit.Assert.assertFalse;

/**
 * This test allows you to easily launch your code generation software under a debugger. Then run this test under debug
 * mode. You will be able to step through your java code and then see the results in the out directory.
 */
@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class TwilioGeneratorTest {
    @Parameterized.Parameters
    public static Collection<String> generators() {
        return Arrays.asList(EnumConstants.Generator.TWILIO_JAVA.getValue(),
                EnumConstants.Generator.TWILIO_GO.getValue(),
                EnumConstants.Generator.TWILIO_CSHARP.getValue(),
                EnumConstants.Generator.TWILIO_TERRAFORM.getValue(),
                EnumConstants.Generator.TWILIO_PHP.getValue(),
                EnumConstants.Generator.TWILIO_PYTHON.getValue(),
                EnumConstants.Generator.TWILIO_NODE.getValue());
    }

    private final String generatorName;

    @BeforeClass
    public static void setUp() {
        FileUtils.deleteQuietly(new File("codegen"));
    }

    @Test
    public void launchGenerator() {
        final CodegenConfigurator configurator = new CodegenConfigurator()
            .setGeneratorName(generatorName)
            .setInputSpec("examples/spec/twilio_api_v2010.yaml")
            .setOutputDir("codegen/" + generatorName);

        final ClientOptInput clientOptInput = configurator.toClientOptInput();
        DefaultGenerator generator = new DefaultGenerator();
        final List<File> output = generator.opts(clientOptInput).generate();

        assertFalse(output.isEmpty());
    }
}
