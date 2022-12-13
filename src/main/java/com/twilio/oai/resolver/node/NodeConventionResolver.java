package com.twilio.oai.resolver.node;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.resolver.FluentConventionResolver;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;

import static com.twilio.oai.common.ApplicationConstants.CONFIG_NODE_JSON_PATH;

public class NodeConventionResolver extends FluentConventionResolver {
    private final Map<String, Map<String, Object>> conventionMap = getConventionalMap();

    public void resolveParameter(final CodegenParameter codegenParameter) {
        super.resolveParameter(codegenParameter, conventionMap);
    }

    public CodegenModel resolveModel(CodegenModel model) {
        return super.resolveModel(model, conventionMap);
    }

    public CodegenModel resolveComplexType(CodegenModel model, Map<String, String> modelFormatMap) {
        return super.resolveComplexType(model, modelFormatMap, conventionMap);
    }

    private Map<String, Map<String, Object>> getConventionalMap() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_NODE_JSON_PATH), new TypeReference<>() {});
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
