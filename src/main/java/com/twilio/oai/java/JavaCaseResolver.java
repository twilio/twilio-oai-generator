package com.twilio.oai.java;

import com.twilio.oai.resolver.CaseResolver;

import org.openapitools.codegen.utils.StringUtils;

public class JavaCaseResolver implements CaseResolver {
    @Override
    public String productOperation(final String product) {
        return StringUtils.camelize(product, true);
    }

    @Override
    public String pathOperation(final String pathPart) {
        return pathPart.toLowerCase();
    }

    @Override
    public String filenameOperation(final String filename) {
        return filename;
    }
}
