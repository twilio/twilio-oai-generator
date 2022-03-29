package com.twilio.oai;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public class ConventionResolver {
    final static Map<String, Map<String, Object>> conventionMap = getConventionalMap() ;
    final static String VENDOR_PREFIX = "x-";
    final static String PREFIXED_COLLAPSIBLE_MAP = "prefixed-collapsible-map";
    final static String X_PREFIXED_COLLAPSIBLE_MAP = "x-prefixed-collapsible-map";
    final static String HYPHEN = "-";

    public static Optional<CodegenModel> resolve(Optional<CodegenModel> model) {
        for (CodegenProperty property : model.get().vars) {
            Map<String, Map<String, Object>> vendorExtensions = new HashMap<>();
            for (Segments segment: Segments.values()) {
                Map<String, Object> segmentMap = conventionMap.get(segment.getSegment());
                if (segmentMap.containsKey(property.dataFormat)) {
                    Map<String, Object> segmentElements = new HashMap<>();
                    segmentElements.put(VENDOR_PREFIX + property.dataFormat, String.format(segmentMap.get(property.dataFormat).toString() , property.name));
                    vendorExtensions.put(segment.getSegment(), segmentElements);
                }
            }
            boolean hasProperty = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).containsKey(property.dataFormat);
            if (hasProperty) {
                property.dataType = (String)conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(property.dataFormat);
            }
            property.nameInSnakeCase = property.nameInSnakeCase.toLowerCase(Locale.ROOT);
            vendorExtensions.forEach(
                    (key, value) -> property.getVendorExtensions().merge(key, value, (oldValue, newValue) -> newValue));
        }
        return model;
    }

    public static CodegenParameter resolveParamTypes(CodegenParameter codegenParameter) {
        boolean hasProperty = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).containsKey(codegenParameter.dataFormat);
        if (hasProperty) {
            codegenParameter.dataType = (String) conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(codegenParameter.dataFormat);
        }
        return codegenParameter;
    }

    public static CodegenParameter prefixedCollapsibleMap(CodegenParameter parameter) {
        if (parameter.dataFormat != null && parameter.dataFormat.startsWith(PREFIXED_COLLAPSIBLE_MAP)) {
            String[] split_format_array = parameter.dataFormat.split(HYPHEN);
            parameter.vendorExtensions.put(X_PREFIXED_COLLAPSIBLE_MAP, split_format_array[split_format_array.length - 1]);
            parameter.dataType = (String)conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(PREFIXED_COLLAPSIBLE_MAP);
        }
        return parameter;
    }

    public static Map<String, Map<String, Object>> getConventionalMap() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream("config/java.json"), new TypeReference<Map<String, Map<String, Object>>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


