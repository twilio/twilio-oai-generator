package com.twilio.oai.resolver.python;

import com.twilio.oai.StringHelper;
import com.twilio.oai.resolver.CaseResolver;
import org.openapitools.codegen.utils.StringUtils;

public class PythonCaseResolver implements CaseResolver {
    @Override //not sure
    public String productOperation(final String product) {
        return StringUtils.underscore(product);
    }

    @Override //flex_flow.py
    public String pathOperation(final String pathPart) {
        return StringUtils.underscore(pathPart);
    }

    @Override
    public String filenameOperation(final String filename) {
        return pathOperation(filename);
    }
}
