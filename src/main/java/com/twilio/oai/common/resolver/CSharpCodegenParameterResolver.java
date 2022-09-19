package com.twilio.oai.common.resolver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.Segments;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSharpCodegenParameterResolver implements  Resolver<CodegenParameter> {
    private final Map<String, Map<String, Object>> conventionMap;
    private String className;
    private Map<String, IJsonSchemaValidationProperties> enums;
    public CSharpCodegenParameterResolver() {
        conventionMap = getConventionalMap();
    }

    public CodegenParameter resolve(CodegenParameter parameter){
        if (parameter == null) {
            return null;
        }

        String existsInDataType = null;
        for (EnumConstants.CsharpDataTypes dataType: EnumConstants.CsharpDataTypes.values()) {
            if (parameter.dataType != null && parameter.dataType.startsWith(dataType.getValue())) {
                existsInDataType = dataType.getValue();
                break;
            }
        }
        if (existsInDataType != null) {
            resolveContainerDataType(parameter, existsInDataType);
        } else {
            resolveDataType(parameter);
        }
        return parameter;
    }

    private CodegenParameter resolveDataType(CodegenParameter parameter) {
        String property = Segments.SEGMENT_PROPERTIES.getSegment();

        CSharpDataSanitizer.sanitizeParameterFormat(parameter);
        if (parameter.isPathParam) {
            parameter.paramName = "Path"+parameter.paramName;
        }

        if (StringUtils.isBlank(parameter.dataFormat)) {
            if (parameter.dataType != null && parameter.dataType.equals("Object")) {
                parameter.dataType = "object";
                parameter.isMap = false;
            }
        }


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

        return parameter;
    }
    private CodegenParameter resolveContainerDataType(CodegenParameter parameter, String existsInDataType)  {
        parameter.dataType = parameter.dataType.replace(existsInDataType, "");
        parameter.dataType = parameter.dataType.substring(0, parameter.dataType.length()-1);
        /// Can be re-used
        resolveDataType(parameter);
        ////
        parameter.dataType = existsInDataType + parameter.dataType + ApplicationConstants.LIST_END;
        return parameter;
    }

    public List<CodegenParameter> resolveParameters(List<CodegenParameter> parameters) {
        for (CodegenParameter parameter: parameters) {
            // Adding reserved keyword for backward compatibility
            if (ApplicationConstants._CONFIGURATION.equals(parameter.paramName)) {
                parameter.paramName = "Configuration";
            }
            resolve(parameter);
        }
        return parameters;
    }

    public static Map<String, Map<String, Object>> getConventionalMap() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(ApplicationConstants.CONFIG_CSHARP_JSON_PATH), new TypeReference<Map<String, Map<String, Object>>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, IJsonSchemaValidationProperties> getEnums() {
        return enums;
    }

    public void setEnums(Map<String, IJsonSchemaValidationProperties> enums) {
        this.enums = enums;
    }

}
