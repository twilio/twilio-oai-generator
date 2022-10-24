package com.twilio.oai.resolver.common;

import com.twilio.oai.Segments;
import com.twilio.oai.resolver.Resolver;
import org.openapitools.codegen.CodegenProperty;

import java.util.HashMap;
import java.util.Map;

public class CodegenModelComplexResolver implements Resolver<CodegenProperty> {

    private Map<String, Map<String, Object>> conventionMap;
    private Map<String, String> modelFormatMap = new HashMap<>();
    private String listStart;
    private String listEnd;

    public CodegenModelComplexResolver(final String listStart, final String listEnd) {
        this.listStart = listStart;
        this.listEnd = listEnd;
    }

    public CodegenProperty resolve(CodegenProperty property){
        if (this.modelFormatMap.isEmpty()) {
            return property;
        }

        if (modelFormatMap.containsKey(property.complexType)) {
            Map<String, Object> propertyMap = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment());
            String complexType = modelFormatMap.get(property.complexType);

            if (propertyMap.containsKey(complexType)) {
                property.dataType = (String)propertyMap.get(complexType);
            }
        }
        return property;
    }

    public void setConventionalMap(Map<String, Map<String, Object>> conventionMap) {
        this.conventionMap = conventionMap;
    }

    public void setModelFormatMap(final Map<String, String> modelFormatMap) {
        this.modelFormatMap = new HashMap<>(modelFormatMap);
    }
}
