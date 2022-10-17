package com.twilio.oai.resolver.common;

import com.twilio.oai.Segments;
import com.twilio.oai.resolver.Resolver;
import org.openapitools.codegen.CodegenProperty;

import java.util.HashMap;
import java.util.Map;

public class CodegenModelDataTypeResolver implements Resolver<CodegenProperty> {

    private Map<String, Map<String, Object>> conventionMap;
    private CodegenModelComplexResolver codegenModelComplexResolver = new CodegenModelComplexResolver();
    private  Map<String, String> modelFormatMap = new HashMap<>();

    public CodegenProperty resolve(CodegenProperty property) {
        assignDataType(property);

        if (property.complexType != null && modelFormatMap.containsKey(property.complexType)) {
            codegenModelComplexResolver.setModelFormatMap(modelFormatMap);
            codegenModelComplexResolver.setConventionalMap(conventionMap);
            codegenModelComplexResolver.resolve(property);
        }

        return property;
    }

    private void assignDataType(CodegenProperty property) {
        String propertyFieldName = Segments.SEGMENT_PROPERTIES.getSegment();

        if (conventionMap.get(propertyFieldName).containsKey(property.dataFormat)) {
            property.dataType = (String)conventionMap.get(propertyFieldName).get(property.dataFormat);
        } else if (conventionMap.get(propertyFieldName).containsKey(property.dataType)) {
            property.dataType = (String)conventionMap.get(propertyFieldName).get(property.dataType);
        }
    }

    public void setConventionMap(Map<String, Map<String, Object>> conventionMap) {
        this.conventionMap = conventionMap;
    }

    public void setModelFormatMap(final Map<String, String> modelFormatMap) {
        this.modelFormatMap = new HashMap<>(modelFormatMap);
    }
}
