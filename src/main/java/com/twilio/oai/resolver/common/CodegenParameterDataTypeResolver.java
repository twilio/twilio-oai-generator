package com.twilio.oai.resolver.common;

import com.twilio.oai.Segments;
import com.twilio.oai.resolver.Resolver;
import org.openapitools.codegen.CodegenParameter;

import java.util.Map;

public class CodegenParameterDataTypeResolver implements Resolver<CodegenParameter> {

    private Map<String, Map<String, Object>> conventionMap;

    public CodegenParameter resolve(CodegenParameter parameter) {
        assignDataType(parameter);
        return parameter;
    }

    private void assignDataType(CodegenParameter parameter){
        String propertyFieldName = Segments.SEGMENT_PROPERTIES.getSegment();

        if (conventionMap.get(propertyFieldName).containsKey(parameter.dataFormat)) {
            parameter.dataType = (String) conventionMap.get(propertyFieldName).get(parameter.dataFormat);
        } else if (conventionMap.get(propertyFieldName).containsKey(parameter.dataType)) {
            parameter.dataType = (String) conventionMap.get(propertyFieldName).get(parameter.dataType);
        }
    }

    public void setConventionMap(Map<String, Map<String, Object>> conventionMap) {
        this.conventionMap = conventionMap;
    }
}
