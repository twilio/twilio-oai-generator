package com.twilio.oai.resolver.python;

import com.twilio.oai.resolver.CaseResolver;

import org.openapitools.codegen.utils.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PythonCaseResolver implements CaseResolver {
    // Python reserved keywords that cannot be used as filenames
    private static final Set<String> PYTHON_RESERVED_KEYWORDS = new HashSet<>(Arrays.asList(
        "import", "from", "class", "def", "return", "if", "else", "elif", "for", "while",
        "break", "continue", "pass", "try", "except", "finally", "raise", "with", "as",
        "assert", "del", "global", "lambda", "yield", "None", "True", "False", "and",
        "or", "not", "in", "is"
    ));

    @Override //not sure
    public String productOperation(final String product) {
        return StringUtils.underscore(product);
    }

    @Override //flex_flow.py
    public String pathOperation(final String pathPart) {
        String result = StringUtils.underscore(pathPart);
        // If the result is a Python reserved keyword, append underscore
        if (PYTHON_RESERVED_KEYWORDS.contains(result.toLowerCase())) {
            return result + "_";
        }
        return result;
    }

    @Override
    public String filenameOperation(final String filename) {
        String result = pathOperation(filename);
        // If the filename (without extension) is a Python reserved keyword, append underscore
        if (PYTHON_RESERVED_KEYWORDS.contains(result.toLowerCase())) {
            return result + "_";
        }
        return result;
    }
}
