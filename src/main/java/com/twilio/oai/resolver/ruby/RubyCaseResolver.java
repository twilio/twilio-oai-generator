package com.twilio.oai.resolver.ruby;

import com.twilio.oai.resolver.CaseResolver;
import org.openapitools.codegen.utils.StringUtils;

public class RubyCaseResolver implements CaseResolver {
    @Override
    public String productOperation(final String product) {
        return StringUtils.underscore(product);
    }

    @Override
    public String pathOperation(final String pathPart) {
        return StringUtils.underscore(pathPart);
    }

    @Override
    public String filenameOperation(final String filename) {
        return pathOperation(filename);
    }
}
