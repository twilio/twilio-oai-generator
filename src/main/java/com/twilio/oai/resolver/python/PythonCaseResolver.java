package com.twilio.oai.resolver.python;

import com.twilio.oai.resolver.CaseResolver;

import org.openapitools.codegen.utils.StringUtils;

public class PythonCaseResolver implements CaseResolver {
    @Override //not sure
    public String productOperation(final String product) {
        return StringUtils.underscore(product);
    }

    @Override //flex_flow.py
    public String pathOperation(final String pathPart) {
        String processedName = StringUtils.underscore(pathPart);
        // Handle Python reserved keyword "import"
        return processedName.equalsIgnoreCase("import") ? "import_" : processedName;
    }

    @Override
    public String filenameOperation(final String filename) {
        // pathOperation already handles the "import" keyword
        return pathOperation(filename);
    }
}
