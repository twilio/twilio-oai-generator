package com.twilio.oai;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.config.CodegenConfigurator;

import static org.junit.Assert.assertFalse;

/**
 * This test allows you to easily launch your code generation software under a debugger. Then run this test under debug
 * mode.  You will be able to step through your java code and then see the results in the out directory.
 */
public class TwilioGeneratorTest {

    @Test
    public void launchCodeGenerator() {
        final CodegenConfigurator configurator = new CodegenConfigurator()
            .setGeneratorName("twilio-java")
            .setInputSpec("examples/twilio_api_v2010.yaml")
            .setOutputDir("codegen/twilio-java");

        final ClientOptInput clientOptInput = configurator.toClientOptInput();
        DefaultGenerator generator = new DefaultGenerator();
        final List<File> output = generator.opts(clientOptInput).generate();

        assertFalse(output.isEmpty());
        
    }
}
