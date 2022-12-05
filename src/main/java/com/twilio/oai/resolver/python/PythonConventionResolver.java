package com.twilio.oai.resolver.python;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.resolver.FluentConventionResolver;

import org.openapitools.codegen.CodegenModel;

import java.util.Map;

import static com.twilio.oai.common.ApplicationConstants.CONFIG_PYTHON_JSON_PATH;

public class PythonConventionResolver extends FluentConventionResolver {
    private final Map<String, Map<String, Object>> conventionMap = getConventionalMap();

    public CodegenModel resolveModel(CodegenModel model) {
        return super.resolveModel(model, conventionMap);
    }

    public CodegenModel resolveComplexType(CodegenModel model, Map<String, String> modelFormatMap) {
        return super.resolveComplexType(model, modelFormatMap, conventionMap);
    }

    private Map<String, Map<String, Object>> getConventionalMap() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_PYTHON_JSON_PATH), new TypeReference<>() {});
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
