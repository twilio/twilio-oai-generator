package com.twilio.oai.resolver.csharp;

import com.twilio.oai.Segments;
import com.twilio.oai.StringHelper;
import com.twilio.oai.resolver.Resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import static com.twilio.oai.common.ApplicationConstants.OBJECT;

@Setter
public class CodegenParameterDataTypeResolver extends Resolver<CodegenParameter> {
    private Map<String, Map<String, Object>> conventionMap;
    private Map<String, IJsonSchemaValidationProperties> enums;
    private String className;
    @Getter
    private Set<String> enumsDict;

    public CodegenParameter resolve(CodegenParameter parameter) {
        new ParameterFormat().sanitize(parameter);
        assignDataTypeForObject(parameter);
        if(StringHelper.existInSetIgnoreCase(parameter.dataType, enumsDict) || StringHelper.existInSetIgnoreCase(parameter.dataFormat, enumsDict)){
            parameter.vendorExtensions.put("x-has-enum-params", true);
        }
        assignDataType(parameter);
        handleEnums(parameter);
        return parameter;
    }

    private void assignDataTypeForObject(CodegenParameter parameter){
        if (StringUtils.isBlank(parameter.dataFormat)) {
            if (parameter.dataType != null && parameter.dataType.equals(OBJECT)) {
                parameter.dataType = "object";
                parameter.isMap = false;
            }
        }
    }

    private void handleEnums(CodegenParameter parameter) {
        String property = Segments.SEGMENT_PROPERTIES.getSegment();
        if (conventionMap.get(property).containsKey(parameter.dataFormat)) {
            parameter.dataType = (String) conventionMap.get(property).get(parameter.dataFormat);
        } else if (conventionMap.get(property).containsKey(parameter.dataType)) {
            parameter.dataType = (String) conventionMap.get(property).get(parameter.dataType);
        } else if (parameter.dataType.contains("Enum")) { // parameter.dataType.contains(className) &&
            String[] value = parameter.dataType.split("Enum");
            parameter.enumName = value[value.length-1] + "Enum";
            if (parameter.items != null) {
                parameter.items.enumName = value[value.length-1] + "Enum";
            }
            if (enums == null) {
                enums = new HashMap<>();
            }
            parameter.dataType = className + "Resource." + parameter.enumName;
            enums.putIfAbsent(parameter.enumName, parameter);
        } else if (parameter.isEnum) {
            parameter.dataType = className + "Resource." + parameter.enumName;
            if (parameter.items != null) {
                parameter.items.enumName = parameter.enumName;
            }
            enums.putIfAbsent(parameter.enumName, parameter);
        }
    }

    private void assignDataType(CodegenParameter parameter){
        String property = Segments.SEGMENT_PROPERTIES.getSegment();
        if (conventionMap.get(property).containsKey(parameter.dataFormat)) {
            parameter.dataType = (String) conventionMap.get(property).get(parameter.dataFormat);
        } else if (conventionMap.get(property).containsKey(parameter.dataType)) {
            parameter.dataType = (String) conventionMap.get(property).get(parameter.dataType);
        }
    }
}
