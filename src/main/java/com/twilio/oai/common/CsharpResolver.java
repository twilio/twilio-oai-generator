package com.twilio.oai.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.Segments;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Data Type can be of 2 types
// Direct = example: string, bool, PhoneNumberPrice(user defined), Enum
// Encapsulating: List<string>, Dictionary<string, string>. These are encapsulating Direct data types.
public class CsharpResolver extends Resolver {

    private EnumConstants.Generator generator;

    private String className;

    private Map<String, IJsonSchemaValidationProperties> enums;

    private final Map<String, Map<String, Object>> conventionMap;

    private  Map<String, String> modelFormatMap = new HashMap<>();


    public CsharpResolver(EnumConstants.Generator generator) {
        this.generator = generator;
        conventionMap = getConventionalMap();

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

    public void setModelFormatMap(final Map<String, String> modelFormatMap) {
        this.modelFormatMap = new HashMap<>(modelFormatMap);
    }
    public static Map<String, Map<String, Object>> getConventionalMap() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(ApplicationConstants.CONFIG_CSHARP_JSON_PATH), new TypeReference<Map<String, Map<String, Object>>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CodegenModel resolveParameter(CodegenModel codegenModel) {
        if (codegenModel == null) {
            return null;
        }
        for (CodegenProperty codegenProperty : codegenModel.vars) {
            if (codegenProperty.complexType != null) {
                System.out.println("Complex data types: " + codegenProperty.complexType);
                System.out.println("Complex data base name: " + codegenProperty.baseName);
            }
            sanitizeDataFormat(codegenProperty);
            String existsInDataType = null;
            for (EnumConstants.CsharpDataTypes dataType: EnumConstants.CsharpDataTypes.values()) {
                if (codegenProperty.dataType != null && codegenProperty.dataType.startsWith(dataType.getValue())) {
                    existsInDataType = dataType.getValue();
                    break;
                }
            }
            if (existsInDataType != null) {
                resolveInDirect(codegenProperty, existsInDataType);
            } else {
                resolveDirect(codegenProperty);
            }
            resolveComplex(codegenProperty);
        }

        return codegenModel;
    }

    private CodegenProperty resolveComplex(CodegenProperty codegenProperty) {
        if (this.modelFormatMap.isEmpty()) {
            return codegenProperty;
        }
        if (modelFormatMap.containsKey(codegenProperty.complexType)) {
            Map<String, Object> propertyMap = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment());
            String complexType = modelFormatMap.get(codegenProperty.complexType);

            if (propertyMap.containsKey(complexType)) {
                final String resolvedDataType = (String)propertyMap.get(complexType);
                if (codegenProperty.isArray) {
                    codegenProperty.dataType = "List<" + resolvedDataType + ">";
                } else {
                    codegenProperty.dataType = resolvedDataType;
                }
            }
        }
        return codegenProperty;
    }

    @Override
    public CodegenParameter resolveParameter(CodegenParameter parameter) {
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
            resolveInDirect(parameter, existsInDataType);
        } else {
            resolveDirect(parameter);
        }
        return parameter;
    }

    private CodegenProperty resolveDirect(CodegenProperty codegenProperty) {
        String property = Segments.SEGMENT_PROPERTIES.getSegment();
        if (conventionMap.get(property).containsKey(codegenProperty.dataFormat)) {
            codegenProperty.dataType = (String)conventionMap.get(property).get(codegenProperty.dataFormat);
        }

        // Exceptional case, data format does not exists for Object type.
        if (codegenProperty.dataFormat == null) {
            if (codegenProperty.dataType == "Object") {
                codegenProperty.dataType = "object";
            } else if (codegenProperty.dataType == "List<Object" ) {
                codegenProperty.dataType = "List<object";
            }
        }
        if (codegenProperty.complexType != null && codegenProperty.complexType.contains("Enum")) { // codegenProperty.dataType.contains(className) &&
            String[] value = codegenProperty.complexType.split("Enum");
            codegenProperty.enumName = value[value.length-1] + "Enum";
            if (codegenProperty.items != null) {
                codegenProperty.items.enumName = value[value.length-1];
            }
            if (enums == null) {
                enums = new HashMap<>();
            }
            codegenProperty.dataType = String.join("Resource.", value) + "Enum";
            enums.putIfAbsent(codegenProperty.enumName, codegenProperty);
        }
        return codegenProperty;
    }

    private CodegenProperty resolveInDirect(CodegenProperty codegenProperty, String existsInDataType) {
        codegenProperty.dataType = codegenProperty.dataType.replace(existsInDataType, "");
        codegenProperty.dataType = codegenProperty.dataType.substring(0, codegenProperty.dataType.length()-1);
        /// Can be re-used
        resolveDirect(codegenProperty);
        ////
        codegenProperty.dataType = existsInDataType + codegenProperty.dataType + ApplicationConstants.LIST_END;
        return codegenProperty;
    }

    private CodegenParameter resolveDirect(CodegenParameter parameter) {
        String property = Segments.SEGMENT_PROPERTIES.getSegment();

        sanitizeDataFormat(parameter);
        if (parameter.isPathParam) {
            parameter.paramName = "Path"+parameter.paramName;
        }
        if (conventionMap.get(property).containsKey(parameter.dataFormat)) {
            parameter.dataType = (String) conventionMap.get(property).get(parameter.dataFormat);
        }

        if (parameter.dataFormat == null) {
            if (parameter.dataType == "Object") {
                parameter.dataType = "object";
            } else if (parameter.dataType == "List<Object" ) {
                parameter.dataType = "List<object";
            }
        }

        if (parameter.dataType.contains("Enum")) { // parameter.dataType.contains(className) &&
            String[] value = parameter.dataType.split("Enum");
            parameter.enumName = value[value.length-1] + "Enum";
            if (enums == null) {
                enums = new HashMap<>();
            }
            parameter.dataType = String.join("Resource.", value) + "Enum";
            enums.putIfAbsent(parameter.enumName, parameter);
        }

        return parameter;
    }

    private void sanitizeDataFormat(CodegenParameter parameter) {
        if (parameter.dataFormat == null) return;
        if (parameter.isMap) {
            List<String> splitDataFormat = new ArrayList<>(List.of(parameter.dataFormat.split("-")));
            splitDataFormat.remove(splitDataFormat.size()-1);
            parameter.dataFormat = String.join("-", splitDataFormat);
        }
    }

    private void sanitizeDataFormat(CodegenProperty property) {
        if (property.dataFormat == null) return;
        if (property.isMap) {
            List<String> splitDataFormat = new ArrayList<>(List.of(property.dataFormat.split("-")));
            splitDataFormat.remove(splitDataFormat.size()-1);
            property.dataFormat = String.join("-", splitDataFormat);
        }

    }

    private CodegenParameter resolveInDirect(CodegenParameter parameter, String existsInDataType)  {
        parameter.dataType = parameter.dataType.replace(existsInDataType, "");
        parameter.dataType = parameter.dataType.substring(0, parameter.dataType.length()-1);
        /// Can be re-used
        resolveDirect(parameter);
        ////
        parameter.dataType = existsInDataType + parameter.dataType + ApplicationConstants.LIST_END;
        return parameter;
    }

    @Override
    public List<CodegenParameter> resolveParameter(List<CodegenParameter> parameters) {
        for (CodegenParameter parameter: parameters) {
            resolveParameter(parameter);
        }
        return parameters;
    }
}
