package com.twilio.oai;

import org.junit.Test;
import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.config.CodegenConfigurator;

/**
 * This test allows you to easily launch your code generation software under a debugger. Then run this test under debug
 * mode.  You will be able to step through your java code and then see the results in the out directory.
 */
public class TwilioTerraformGeneratorTest {

    @Test
    public void launchCodeGenerator() {
        final CodegenConfigurator configurator = new CodegenConfigurator()
            .setGeneratorName("terraform-provider-twilio")
            .setInputSpec("examples/twilio_api_v2010.yaml")
            .setOutputDir("examples/terraform/resources");

        final ClientOptInput clientOptInput = configurator.toClientOptInput();
        DefaultGenerator generator = new DefaultGenerator();
        generator.opts(clientOptInput).generate();
    }
}
