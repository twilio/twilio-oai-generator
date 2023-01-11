package com.twilio.oai.template;

import com.twilio.oai.StringHelper;
import org.openapitools.codegen.CodegenConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RubyApiActionTemplate extends AbstractApiActionTemplate {

    public RubyApiActionTemplate(final CodegenConfig defaultCodegen) {
        super(defaultCodegen);
    }

    @Override
    protected Map<String, List<String>> mapping() {
        return Map.of(API_TEMPLATE, Arrays.asList("api.mustache", ".rb"), VERSION_TEMPLATE, Arrays.asList("version.mustache", ".rb"));
    }

    @Override
    protected String getVersionFilename(final String apiVersionClass) {
        return StringHelper.toSnakeCase(apiVersionClass);
    }
}
