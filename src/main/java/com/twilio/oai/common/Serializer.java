package com.twilio.oai.common;

import com.twilio.oai.Segments;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenParameter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class Serializer {
    private final Map<String, Map<String, Object>> conventionMap = Utility.getConventionalMap();

    public List<CodegenParameter> serialize(final List<CodegenParameter> codegenParameters) {
        LinkedHashMap<String, CodegenParameter> getParams = new LinkedHashMap<>();
        for (CodegenParameter codegenParameter: codegenParameters) {
            serializeInDirect(codegenParameter, getParams);
        }
        return getParams.values().stream().collect(Collectors.toList());
    }

    public void serializeInDirect(final CodegenParameter codegenParameter, final LinkedHashMap<String, CodegenParameter> getParams) {
        String existsInDataType = null;
        for (EnumConstants.CsharpDataTypes dataType: EnumConstants.CsharpDataTypes.values()) {
            if (codegenParameter.dataType != null && codegenParameter.dataType.startsWith(dataType.getValue())) {
                existsInDataType = dataType.getValue();
                break;
            }
        }

        if (existsInDataType != null) {
            codegenParameter.dataType = codegenParameter.dataType.replace(existsInDataType, "");
            codegenParameter.dataType = codegenParameter.dataType.substring(0, codegenParameter.dataType.length()-1);
        }

        serializeDirect(codegenParameter, getParams);

        if (existsInDataType != null) {
            codegenParameter.dataType = existsInDataType + codegenParameter.dataType + ApplicationConstants.LIST_END;
        }
    }

    public void serializeDirect(final CodegenParameter codegenParameter, final LinkedHashMap<String, CodegenParameter> getParams) {
        Map<String, Object> serializeMap = conventionMap.get(Segments.SEGMENT_SERIALIZE.getSegment());
        Map<String, Object> serializeDataTypeMap = conventionMap.get("serialize_dataType");

        String serializedFormat;
        if (codegenParameter.dataFormat != null) {
            serializedFormat = (String) serializeMap.get(codegenParameter.dataFormat);
        } else {
            serializedFormat = (String) serializeDataTypeMap.get(codegenParameter.dataType);
        }

        if (serializedFormat != null) {
            if (codegenParameter.isMap) {
                serializedFormat = String.format(serializedFormat, codegenParameter.paramName, codegenParameter.vendorExtensions.get("x-map-value"));
            } else {
                serializedFormat = String.format(serializedFormat, codegenParameter.paramName);
            }
        } else {
            serializedFormat = codegenParameter.paramName + "." +"ToString()";
        }

        if (codegenParameter.paramName.endsWith("Before")) {
            CodegenParameter parent = getParams.get(StringUtils.chomp(codegenParameter.paramName, "Before"));
            if (parent != null) {
                parent.vendorExtensions.put("x-before", codegenParameter);
                parent.vendorExtensions.put("x-before-or-after", true);
                codegenParameter.vendorExtensions.put("x-ignore-in-header", true);
            }
        } else if (codegenParameter.paramName.endsWith("After")) {
            CodegenParameter parent = getParams.get(StringUtils.chomp(codegenParameter.paramName, "After"));
            if (parent != null) {
                parent.vendorExtensions.put("x-after", codegenParameter);
                parent.vendorExtensions.put("x-before-or-after", true);
                codegenParameter.vendorExtensions.put("x-ignore-in-header", true);
            }
        } else {
            getParams.put(codegenParameter.paramName, codegenParameter);
        }

        codegenParameter.vendorExtensions.put("x-param-to-string", serializedFormat);
    }
}
