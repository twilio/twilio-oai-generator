package com.twilio.oai.template;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openapitools.codegen.CodegenConfig;
import org.openapitools.codegen.utils.StringUtils;

public class PythonApiActionTemplate extends AbstractApiActionTemplate {
    public static final String INIT_FILENAME = "__init__";
    private boolean isVersionLess = false;

    public PythonApiActionTemplate(final CodegenConfig codegenConfig) {
        super(codegenConfig);
    }

    public void setIsVersionLess(boolean isVLess){
        isVersionLess = isVLess;
    }

    @Override
    public Map<String, List<String>> mapping() {
        return Map.of(API_TEMPLATE,
                      Arrays.asList("api-single.mustache", ".py"),
                      VERSION_TEMPLATE,
                      Arrays.asList("version.mustache", ".py"));
    }

    @Override
    protected String getVersionFilename(final String apiVersionClass) {
        if (isVersionLess != true && apiVersionClass.startsWith("V")) {
            return INIT_FILENAME;
        }

        return StringUtils.underscore(apiVersionClass) + File.separator + INIT_FILENAME;
    }

    @Override
    protected String getDestinationFilename(final String apiVersionClass, final String fileExtension) {
        return INIT_FILENAME + fileExtension;
    }
}
