package com.twilio.oai;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.utils.StringUtils;

public class ConventionResolver {
    final static Map<String, Map<String, Object>> conventionMap = getConventionalMap() ;
    final static String VENDOR_PREFIX = "x-";
    final static String PREFIXED_COLLAPSIBLE_MAP = "prefixed-collapsible-map";
    final static String X_PREFIXED_COLLAPSIBLE_MAP = "x-prefixed-collapsible-map";
    final static String HYPHEN = "-";
    public static final String OBJECT = "object";

    public static final String PHONE_NUMBER_FORMAT = "phone-number";

    final static String X_IS_PHONE_NUMBER_FORMAT = "x-is-phone-number-format";

    final static String CONFIG_JAVA_JSON_PATH = "config/java.json";

    final static String LIST_PREFIX = "list-";

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

            if(property.dataType.equalsIgnoreCase(OBJECT)) {
                property.dataType = (String)conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(OBJECT);
            }
            property.nameInSnakeCase = property.nameInSnakeCase.toLowerCase(Locale.ROOT);
            vendorExtensions.forEach(
                    (key, value) -> property.getVendorExtensions().merge(key, value, (oldValue, newValue) -> newValue));
        }
        return model;
    }

    public static Optional<CodegenParameter> resolveParameter(CodegenParameter parameter) {

        if(parameter.dataType.equalsIgnoreCase(OBJECT)) {
            parameter.dataType = (String)conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(OBJECT);
        }
        boolean hasPromotion = conventionMap.get(Segments.SEGMENT_PROMOTIONS.getSegment()).containsKey(parameter.dataFormat);
        if (hasPromotion) {
            // cloning to prevent update in source map
            HashMap<String, String> promotionsMap = new HashMap<>((Map) conventionMap
                    .get(Segments.SEGMENT_PROMOTIONS.getSegment()).get(parameter.dataFormat));
            if(parameter.isArray && conventionMap.get(Segments.SEGMENT_PROMOTIONS.getSegment()).containsKey(LIST_PREFIX+parameter.dataFormat)) {
                promotionsMap.put(parameter.baseType, (String) ((Map)conventionMap.get(Segments.SEGMENT_PROMOTIONS.getSegment()).get(LIST_PREFIX+parameter.dataFormat)).get(parameter.dataFormat));
            }
            promotionsMap.replaceAll((dataType, value) -> String.format(value, parameter.paramName) );
            parameter.vendorExtensions.put("x-promotions", promotionsMap);
        }

        boolean hasProperty = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).containsKey(parameter.dataFormat);
        if (hasProperty) {
            parameter.dataType = (String)conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(parameter.dataFormat);
        }
        if( PHONE_NUMBER_FORMAT.equals(parameter.dataFormat)) {
            parameter.vendorExtensions.put(X_IS_PHONE_NUMBER_FORMAT, true);
        }
        parameter.paramName = StringUtils.camelize(parameter.paramName, true);
        return Optional.of(parameter);
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
        parameter.paramName = StringUtils.camelize(parameter.paramName, true);
        return parameter;
    }

    public static Map<String, Map<String, Object>> getConventionalMap() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_JAVA_JSON_PATH), new TypeReference<Map<String, Map<String, Object>>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Optional<CodegenModel> resolveComplexType(Optional<CodegenModel> item, Map<String, String> modelFormatMap) {
        for (CodegenProperty prop: item.get().vars) {
            if(modelFormatMap.containsKey(prop.complexType)) {
                boolean hasProperty =  conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).containsKey(modelFormatMap.get(prop.complexType));
                if (hasProperty) {
                    if ( prop.containerType != null && prop.containerType.equals("array")) {
                        prop.dataType = "List<" + (String)conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(modelFormatMap.get(prop.complexType)) + ">";
                    } else {
                        prop.dataType = (String)conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(modelFormatMap.get(prop.complexType));
                    }
                }
            }
        }
        return item;
    }
}


