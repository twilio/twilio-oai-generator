package com.twilio.oai.csharp;

import com.twilio.oai.resolver.CaseResolver;

import org.openapitools.codegen.utils.StringUtils;

public class CSharpCaseResolver implements CaseResolver {
    @Override
    public String productOperation(final String product) {
        return StringUtils.camelize(product);
    }

    @Override
    public String pathOperation(final String pathPart) {
        return pathPart;
    }

    @Override
    public String filenameOperation(final String filename) {
        return filename;
    }
}
