package com.twilio.oai.resolver.node;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.Segments;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.utils.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class NodeConventionResolver {
    final Map<String, Map<String, Object>> conventionMap;

    public static final String OBJECT = "object";
    final static String CONFIG_NODE_JSON_PATH = "config/node.json";

    public NodeConventionResolver(){
        conventionMap = getConventionalMap();
    }

    public Optional<CodegenParameter> resolveParameter(CodegenParameter codegenParameter) {
        boolean hasProperty = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).containsKey(codegenParameter.dataFormat);
        if (hasProperty) {
            codegenParameter.dataType = (String) conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(codegenParameter.dataFormat);
        }
        return Optional.of(codegenParameter);
    }

    public CodegenModel resolveModel(CodegenModel model){
        return model;
    }

    public Map<String, Map<String, Object>> getConventionalMap() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_NODE_JSON_PATH), new TypeReference<Map<String, Map<String, Object>>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
