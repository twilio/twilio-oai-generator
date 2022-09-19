package com.twilio.oai.common.resolver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.Segments;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.HashMap;
import java.util.Map;

public class CSharpCodegenModelResolver implements  Resolver<CodegenModel>{

    private final Map<String, Map<String, Object>> conventionMap;
    private String className;
    private Map<String, IJsonSchemaValidationProperties> enums;
    private  Map<String, String> modelFormatMap = new HashMap<>();

    public CSharpCodegenModelResolver(){
        conventionMap = getConventionalMap();
    }

    @Override
    public CodegenModel resolve(CodegenModel codegenModel){
        if (codegenModel == null) {
            return null;
        }
        for (CodegenProperty codegenProperty : codegenModel.vars) {
            CSharpDataSanitizer.sanitizePropertyFormat(codegenProperty);
            String existsInDataType = null;
            for (EnumConstants.CsharpDataTypes dataType: EnumConstants.CsharpDataTypes.values()) {
                if (codegenProperty.dataType != null && codegenProperty.dataType.startsWith(dataType.getValue())) {
                    existsInDataType = dataType.getValue();
                    break;
                }
            }
            if (existsInDataType != null) {
                resolveContainerDataType(codegenProperty, existsInDataType);
            } else {
                resolveDataType(codegenProperty);
            }
        }

        return codegenModel;
    }

    private CodegenProperty resolveContainerDataType(CodegenProperty codegenProperty, String existsInDataType) {
        codegenProperty.dataType = codegenProperty.dataType.replaceFirst(existsInDataType, "");
        codegenProperty.dataType = codegenProperty.dataType.substring(0, codegenProperty.dataType.length()-1);
        // Added to handle case List<List<string>>
        for (EnumConstants.CsharpDataTypes dataType: EnumConstants.CsharpDataTypes.values()) {
            if (codegenProperty.dataType != null && codegenProperty.dataType.startsWith(dataType.getValue())) {
                existsInDataType = dataType.getValue();
                resolveContainerDataType(codegenProperty, existsInDataType);
            }
        }

        resolveDataType(codegenProperty);

        codegenProperty.dataType = existsInDataType + codegenProperty.dataType + ApplicationConstants.LIST_END;
        return codegenProperty;
    }

    private CodegenProperty resolveDataType(CodegenProperty codegenProperty) {
        String property = Segments.SEGMENT_PROPERTIES.getSegment();
        String deserialize = Segments.SEGMENT_DESERIALIZE.getSegment();
        if (conventionMap.get(property).containsKey(codegenProperty.dataFormat)) {
            codegenProperty.dataType = (String)conventionMap.get(property).get(codegenProperty.dataFormat);
        } else if (conventionMap.get(property).containsKey(codegenProperty.dataType)) {
            codegenProperty.dataType = (String)conventionMap.get(property).get(codegenProperty.dataType);
        }

        if (conventionMap.get(deserialize).containsKey(codegenProperty.dataFormat)) {
            codegenProperty.vendorExtensions.put("x-jsonConverter", conventionMap.get(deserialize).get(codegenProperty.dataFormat));
        }

        // Exceptional case, data format does not exist for Object type.
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
                codegenProperty.items.enumName = value[value.length-1] + "Enum";
            }
            if (enums == null) {
                enums = new HashMap<>();
            }
            codegenProperty.dataType = className + "Resource." + codegenProperty.enumName;
            codegenProperty.vendorExtensions.put("x-jsonConverter", "StringEnumConverter");
            enums.putIfAbsent(codegenProperty.enumName, codegenProperty);
        } else if (codegenProperty.complexType != null && modelFormatMap.containsKey(codegenProperty.complexType)) {
            resolveComplex(codegenProperty);
        }

        return codegenProperty;
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
                codegenProperty.dataType = resolvedDataType;
            }
        }
        return codegenProperty;
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

    public void setModelFormatMap(final Map<String, String> modelFormatMap) {
        this.modelFormatMap = new HashMap<>(modelFormatMap);
    }

}
