package com.twilio.oai.resolver.csharp;


import com.twilio.oai.Segments;

import com.twilio.oai.resolver.Resolver;
import org.openapitools.codegen.CodegenProperty;

import java.util.HashMap;
import java.util.Map;

public class CodegenModelComplexResolver implements Resolver<CodegenProperty> {
    private  Map<String, String> modelFormatMap = new HashMap<>();
    private Map<String, Map<String, Object>> conventionMap;
    public CodegenProperty resolve(CodegenProperty codegenProperty){
        if (this.modelFormatMap.isEmpty()) {
            return codegenProperty;
        }
        if (modelFormatMap.containsKey(codegenProperty.complexType)) {
            Map<String, Object> propertyMap = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment());
            String complexType = modelFormatMap.get(codegenProperty.complexType);

            if (propertyMap.containsKey(complexType)) {
                final String resolvedDataType = (String)propertyMap.get(complexType);
                codegenProperty.dataType = resolvedDataType;
            }
        }
        return codegenProperty;
    }
    public void setModelFormatMap(final Map<String, String> modelFormatMap) {
        this.modelFormatMap = new HashMap<>(modelFormatMap);
    }
    public void setConventionalMap(Map<String, Map<String, Object>> conventionMap) {
        this.conventionMap = conventionMap;
    }

}
