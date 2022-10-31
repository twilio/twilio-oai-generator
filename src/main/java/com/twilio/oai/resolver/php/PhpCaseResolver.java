package com.twilio.oai.resolver.php;

import com.twilio.oai.StringHelper;
import com.twilio.oai.resolver.CaseResolver;

public class PhpCaseResolver implements CaseResolver {
    @Override
    public String productOperation(final String product) {
        return StringHelper.camelize(product, true);
    }

    @Override
    public String pathOperation(final String pathPart) {
        return StringHelper.camelize(pathPart);
    }

    @Override
    public String filenameOperation(final String filename) {
        return pathOperation(filename);
    }
}
