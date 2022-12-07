package com.twilio.oai.resolver.common;

import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.IConventionMapper;
import org.openapitools.codegen.CodegenProperty;

import java.util.HashMap;
import java.util.Map;

public class CodegenModelComplexResolver implements Resolver<CodegenProperty> {

    private IConventionMapper mapper;
    private Map<String, String> modelFormatMap = new HashMap<>();

    public CodegenModelComplexResolver() {}

    public CodegenProperty resolve(CodegenProperty property){
        if (this.modelFormatMap.isEmpty()) {
            return property;
        }

        if (modelFormatMap.containsKey(property.complexType)) {
            Map<String, Object> propertyMap = mapper.properties();
            String complexType = modelFormatMap.get(property.complexType);

            if (propertyMap.containsKey(complexType)) {
                property.dataType = (String)propertyMap.get(complexType);
            }
        }
        return property;
    }

    public void setMapper(IConventionMapper mapper) {
        this.mapper = mapper;
    }

    public void setModelFormatMap(final Map<String, String> modelFormatMap) {
        this.modelFormatMap = new HashMap<>(modelFormatMap);
    }
}
