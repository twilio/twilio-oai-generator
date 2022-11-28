package com.twilio.oai.resolver.node;

import com.twilio.oai.Segments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.utils.StringUtils;

import static com.twilio.oai.common.ApplicationConstants.CONFIG_NODE_JSON_PATH;

public class NodeConventionResolver {
    private final Map<String, Map<String, Object>> conventionMap = getConventionalMap();

    public void resolveParameter(final CodegenParameter codegenParameter) {
        if (codegenParameter.dataFormat != null) {
            boolean hasProperty = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).containsKey(StringUtils.underscore(codegenParameter.dataFormat));
            if (hasProperty) {
                codegenParameter.dataType = (String) conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(StringUtils.underscore(codegenParameter.dataFormat));
            }
        }
    }

    public CodegenModel resolveModel(CodegenModel model) {
        for (CodegenProperty property : model.vars) {
            if (property.dataFormat != null) {
                boolean hasProperty = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).containsKey(StringUtils.underscore(property.dataFormat));
                if (hasProperty) {
                    property.dataType = (String)conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(StringUtils.underscore(property.dataFormat));
                }
            }
        }
        return model;
    }

    @SuppressWarnings("unchecked")
    public CodegenModel resolveComplexType(CodegenModel model, Map<String, String> modelFormatMap) {
        for (CodegenProperty prop: model.vars) {
            if (modelFormatMap.containsKey(prop.complexType)) {
                final String propertyName = StringUtils.underscore(modelFormatMap.get(prop.complexType));
                boolean hasProperty = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).
                        containsKey(propertyName);
                if (hasProperty) {
                    prop.dataType = (String) conventionMap
                        .get(Segments.SEGMENT_PROPERTIES.getSegment())
                        .get(propertyName);
                    prop.complexType = null;

                    if (prop.containerType != null && prop.containerType.equals("array")) {
                        prop.dataType = "Array<" + prop.dataType + ">";
                    }

                    // Add custom object import path
                    Map<String, String> propMap = (Map<String, String>) conventionMap.get(Segments.SEGMENT_LIBRARY.getSegment()).
                            get(propertyName);
                    ArrayList<Map<String, String>> imports =
                        (ArrayList<Map<String, String>>) model.vendorExtensions.computeIfAbsent(
                        "x-imports",
                        k -> new ArrayList<>());
                    if (imports.stream().noneMatch(item -> prop.dataType.equals(item.get("name")))) {
                        Map<String, String> customObjectImport = new HashMap<>();
                        customObjectImport.put("name", prop.dataType);
                        customObjectImport.put("path", propMap.get(prop.dataType));
                        imports.add(customObjectImport);
                    }
                }
            }
        }
        return model;
    }

    private Map<String, Map<String, Object>> getConventionalMap() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_NODE_JSON_PATH), new TypeReference<>() {});
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
