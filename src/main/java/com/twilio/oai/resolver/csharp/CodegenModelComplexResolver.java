package com.twilio.oai.resolver.csharp;

import com.twilio.oai.Segments;
import com.twilio.oai.StringHelper;
import com.twilio.oai.resolver.Resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.Setter;
import org.openapitools.codegen.CodegenProperty;

@Setter
public class CodegenModelComplexResolver extends Resolver<CodegenProperty> {
    private Map<String, String> modelFormatMap = new HashMap<>();
    private Map<String, Map<String, Object>> conventionMap;
    private Set<String> enumsDict;

    public CodegenProperty resolve(CodegenProperty codegenProperty){
        if (this.modelFormatMap.isEmpty()) {
            return codegenProperty;
        }
        if (modelFormatMap.containsKey(codegenProperty.complexType)) {
            Map<String, Object> propertyMap = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment());
            String complexType = modelFormatMap.get(codegenProperty.complexType);

            if (propertyMap.containsKey(complexType)) {
                codegenProperty.dataType = (String)propertyMap.get(complexType);
                if(StringHelper.existInSetIgnoreCase(codegenProperty.dataType, enumsDict)){
                    codegenProperty.vendorExtensions.put("x-has-enum-params", true);
                }
            }
        }
        return codegenProperty;
    }
}
