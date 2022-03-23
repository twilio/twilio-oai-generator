package com.twilio.oai;

import java.util.*;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public class ConventionResolver {
    final static ConventionMap conventionMap = new ConventionMap();
    final static String VENDOR_PREFIX = "x-";
    final static String PREFIXED_COLLAPSIBLE_MAP = "prefixed_collapsible_map";
    final static String X_PREFIXED_COLLAPSIBLE_MAP = "x-prefixed_collapsible_map";
    final static String UNDER_SCORE = "_";
    final static List<String> SEGMENTS = Arrays.asList(ConventionMap.SEGMENT_HYDRATE, ConventionMap.SEGMENT_SERIALIZE, ConventionMap.SEGMENT_DESERIALIZE, ConventionMap.SEGMENT_LIBRARY);

    public static Optional<CodegenModel> resolve(Optional<CodegenModel> model) {
        for (CodegenProperty property : model.get().vars) {
            Map<String, Map<String, Object>> vendorExtensions = new HashMap<>();
            for (String segmentString: SEGMENTS) {
                Map<String, Object> segment = conventionMap.get(segmentString);
                if (segment.containsKey(property.dataFormat)) {
                    Map<String, Object> segmentElements = new HashMap<>();
                    segmentElements.put(VENDOR_PREFIX + property.dataFormat, String.format(segment.get(property.dataFormat).toString() , property.name));
                    vendorExtensions.put(segmentString, segmentElements);
                }
            }
            if (conventionMap.get(ConventionMap.SEGMENT_TYPES).containsKey(property.dataFormat)) {
                property.dataType = (String)conventionMap.get(ConventionMap.SEGMENT_TYPES).get(property.dataFormat);
            }
            vendorExtensions.forEach(
                    (key, value) -> property.getVendorExtensions().merge(key, value, (oldValue, newValue) -> newValue));
        }
        return model;
    }

    public static CodegenParameter prefixedCollapsibleMap(CodegenParameter parameter) {
        if (parameter.dataFormat != null && parameter.dataFormat.startsWith(PREFIXED_COLLAPSIBLE_MAP)) {
            String[] split_format_array = parameter.dataFormat.split(UNDER_SCORE);
            parameter.vendorExtensions.put(X_PREFIXED_COLLAPSIBLE_MAP, split_format_array[split_format_array.length - 1]);
        }
        return parameter;
    }

    public static CodegenParameter resolveTypes(CodegenParameter codegenParameter) {
        if (codegenParameter.dataType == "Object") {
            codegenParameter.dataType = "Map<String,Object>";
        }
        return codegenParameter;
    }
}


