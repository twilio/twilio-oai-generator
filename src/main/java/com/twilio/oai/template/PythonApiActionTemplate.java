package com.twilio.oai.template;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openapitools.codegen.CodegenConfig;

public class PythonApiActionTemplate extends AbstractApiActionTemplate {
    public PythonApiActionTemplate(final CodegenConfig codegenConfig) {
        super(codegenConfig);
    }

    @Override
    public Map<String, List<String>> mapping() {
        return Map.of(API_TEMPLATE,
                      Arrays.asList("api-single.mustache", ".py"),
                      VERSION_TEMPLATE,
                      Arrays.asList("version.mustache", ".py"));
    }

    @Override
    protected String getVersionFilename(final String apiVersionClass) {
        return "__init__";
    }
}
