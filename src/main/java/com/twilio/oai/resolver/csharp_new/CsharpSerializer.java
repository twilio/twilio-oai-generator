package com.twilio.oai.resolver.csharp_new;

import com.twilio.oai.Segments;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.csharp.ParameterFormat;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenParameter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.twilio.oai.common.EnumConstants.*;

public class CsharpSerializer extends Serializer {

    private List<CsharpDataTypes> languageDataTypes;

    private LinkedHashMap<String, CodegenParameter> getParams = new LinkedHashMap<>();

    private ParameterFormat parameterFormat;

    public CsharpSerializer(List<CsharpDataTypes> languageDataTypes) {
        this.languageDataTypes = languageDataTypes;
        parameterFormat = new ParameterFormat();
    }

    @Override
    public CodegenParameter serialize(CodegenParameter parameter) {
        getParams.clear();
        serializeContainer(parameter);
        return parameter;
    }

    public CodegenParameter serializeContainer(CodegenParameter parameter) {
        String existsInDataType = null;
        for (EnumConstants.CsharpDataTypes dataType: EnumConstants.CsharpDataTypes.values()) {
            if (parameter.dataType != null && parameter.dataType.startsWith(dataType.getValue())) {
                existsInDataType = dataType.getValue();
                break;
            }
        }

        if (existsInDataType != null) {
            parameter.dataType = parameter.dataType.replace(existsInDataType, "");
            parameter.dataType = parameter.dataType.substring(0, parameter.dataType.length()-1);
        }

        serializeNonContainer(parameter);

        if (existsInDataType != null) {
            parameter.dataType = existsInDataType + parameter.dataType + ApplicationConstants.LIST_END;
        }
        return parameter;
    }

    @Override
    public CodegenParameter serializeNonContainer(CodegenParameter parameter) {
        Map<String, Object> serializeMap = Utility.getConventionalMap().get(Segments.SEGMENT_SERIALIZE.getSegment());
        parameterFormat.sanitize(parameter);

        // If dataformat or datatype found in csharp.json (serialize).
        String serializedFormat = parameter.dataFormat != null ? (String) serializeMap.get(parameter.dataFormat)
                : (String) serializeMap.get(parameter.dataType);

        if (serializedFormat == null) {
            serializedFormat = parameter.paramName + "." +"ToString()";
        } else {
            serializedFormat = parameter.isMap ? String.format(serializedFormat, parameter.paramName, parameter.vendorExtensions.get("x-map-value"))
                    : String.format(serializedFormat, parameter.paramName);
        }
        parameter.vendorExtensions.put("x-param-to-string", serializedFormat);
        return parameter;
    }

}
