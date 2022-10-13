package com.twilio.oai.resolver.java;

import com.twilio.oai.StringHelper;
import com.twilio.oai.resolver.CaseResolver;

public class JavaCaseResolver implements CaseResolver {
    @Override
    public String productOperation(final String product) {
        return StringHelper.camelize(product, true);
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
