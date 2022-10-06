package com.twilio.oai.csharp;

import com.twilio.oai.resolver.CaseResolver;

public class CSharpCaseResolver implements CaseResolver {
    @Override
    public String productOperation(final String product) {
        return product;
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
