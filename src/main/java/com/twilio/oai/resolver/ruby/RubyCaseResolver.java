package com.twilio.oai.resolver.ruby;

import com.twilio.oai.StringHelper;
import com.twilio.oai.resolver.CaseResolver;

public class RubyCaseResolver implements CaseResolver {
    @Override
    public String productOperation(final String product) {
        return StringHelper.toSnakeCase(product);
    }

    @Override
    public String pathOperation(final String pathPart) {
        return StringHelper.toSnakeCase(pathPart);
    }

    @Override
    public String filenameOperation(final String filename) {
        return pathOperation(filename);
    }
}
