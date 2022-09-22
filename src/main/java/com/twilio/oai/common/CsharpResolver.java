package com.twilio.oai.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.Segments;
import org.apache.commons.lang3.StringUtils;
import com.twilio.oai.StringHelper;
import com.twilio.oai.EnumsResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


// Data Type can be of 2 types
// Direct = example: string, bool, PhoneNumberPrice(user defined), Enum
// Encapsulating: List<string>, Dictionary<string, string>. These are encapsulating Direct data types.
public class CsharpResolver extends Resolver {

    private EnumConstants.Generator generator;

    private String className;

    private Map<String, IJsonSchemaValidationProperties> enums;
    private boolean hasEnumsInResource = false;
    private boolean hasEnumsInOptions = false;
    private EnumsResolver enumsResolver;

    private HashSet<String> enumsDict;

    private final Map<String, Map<String, Object>> conventionMap;

    private  Map<String, String> modelFormatMap = new HashMap<>();


    public CsharpResolver(EnumConstants.Generator generator) {
        this.generator = generator;
        conventionMap = getConventionalMap();
        enumsResolver = new EnumsResolver(conventionMap);
        enumsDict = enumsResolver.getEnumsDict();

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

    public boolean isHasEnumsInResource() {
        return hasEnumsInResource;
    }

    public void setHasEnumsInResource(boolean hasEnumsInResource) {
        this.hasEnumsInResource = hasEnumsInResource;
    }

    public boolean isHasEnumsInOptions() {
        return hasEnumsInOptions;
    }

    public void setHasEnumsInOptions(boolean hasEnumsInOptions) {
        this.hasEnumsInOptions = hasEnumsInOptions;
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
            sanitizeDataFormat(codegenProperty);
            if(codegenProperty.isEnum && StringHelper.existInSetIgnoreCase(codegenProperty.dataType, enumsDict)){
                hasEnumsInResource = true;
            }

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
            if(codegenProperty.vendorExtensions.containsKey("x-has-enum-params")){
                hasEnumsInResource = true;
            }
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
                codegenProperty.dataType = resolvedDataType;
            }
        }
        return codegenProperty;
    }

    @Override
    public CodegenParameter resolveParameter(CodegenParameter parameter) {
        if (parameter == null) {
            return null;
        }
        if(parameter.isEnum  && StringHelper.existInSetIgnoreCase(parameter.dataType, enumsDict)){
            hasEnumsInOptions = true;
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
        if(parameter.vendorExtensions.containsKey("x-has-enum-params")){
            hasEnumsInOptions = true;
        }
        return parameter;
    }

    private CodegenProperty resolveDataType(CodegenProperty codegenProperty) {
        String property = Segments.SEGMENT_PROPERTIES.getSegment();
        String deserialize = Segments.SEGMENT_DESERIALIZE.getSegment();
        if(StringHelper.existInSetIgnoreCase(codegenProperty.dataType, enumsDict) || StringHelper.existInSetIgnoreCase(codegenProperty.dataFormat, enumsDict)){
            codegenProperty.vendorExtensions.put("x-has-enum-params", true);
        }
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
        if(StringHelper.existInSetIgnoreCase(codegenProperty.dataType, enumsDict)){//List of enums present
            codegenProperty.vendorExtensions.put("x-has-enum-params", true);
        }

        codegenProperty.dataType = existsInDataType + codegenProperty.dataType + ApplicationConstants.LIST_END;
        return codegenProperty;
    }

    private CodegenParameter resolveDataType(CodegenParameter parameter) {
        String property = Segments.SEGMENT_PROPERTIES.getSegment();

        sanitizeDataFormat(parameter);
        if (parameter.isPathParam) {
            parameter.paramName = "Path"+parameter.paramName;
        }

        if (StringUtils.isBlank(parameter.dataFormat)) {
            if (parameter.dataType != null && parameter.dataType.equals("Object")) {
                parameter.dataType = "object";
                parameter.isMap = false;
            }
        }
        if(StringHelper.existInSetIgnoreCase(parameter.dataType, enumsDict) || StringHelper.existInSetIgnoreCase(parameter.dataFormat, enumsDict)){
            parameter.vendorExtensions.put("x-has-enum-params", true);
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

    private void sanitizeDataFormat(CodegenParameter parameter) {
        if (parameter.dataFormat == null) return;
        if (parameter.isMap) {
            List<String> splitDataFormat = new ArrayList<>(List.of(parameter.dataFormat.split("-")));
            parameter.vendorExtensions.put("x-map-value", splitDataFormat.get(splitDataFormat.size()-1));
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

    private CodegenParameter resolveContainerDataType(CodegenParameter parameter, String existsInDataType)  {
        parameter.dataType = parameter.dataType.replace(existsInDataType, "");
        parameter.dataType = parameter.dataType.substring(0, parameter.dataType.length()-1);
        /// Can be re-used
        resolveDataType(parameter);
        ////
        if(StringHelper.existInSetIgnoreCase(parameter.dataType, enumsDict)){//List of enums present
            parameter.vendorExtensions.put("x-has-enum-params", true);
        }
        parameter.dataType = existsInDataType + parameter.dataType + ApplicationConstants.LIST_END;
        return parameter;
    }

    @Override
    public List<CodegenParameter> resolveParameter(List<CodegenParameter> parameters) {
        for (CodegenParameter parameter: parameters) {
            // Adding reserved keyword for backward compatibility
            if (ApplicationConstants._CONFIGURATION.equals(parameter.paramName)) {
                parameter.paramName = "Configuration";
            }
            resolveParameter(parameter);
        }
        return parameters;
    }
}
