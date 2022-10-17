package com.twilio.oai.resolver.java;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.Segments;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public class JavaConventionResolver {
    final static Map<String, Map<String, Object>> conventionMap = getConventionalMap() ;
    final static String VENDOR_PREFIX = "x-";
    final static String PREFIXED_COLLAPSIBLE_MAP = "prefixed-collapsible-map";
    final static String X_PREFIXED_COLLAPSIBLE_MAP = "x-prefixed-collapsible-map";
    final static String HYPHEN = "-";
    public static final String OBJECT = "object";

    public static final String LIST_OBJECT = "List<Object>";

    public static final String PHONE_NUMBER_FORMAT = "phone-number";

    final static String X_IS_PHONE_NUMBER_FORMAT = "x-is-phone-number-format";

    // Resolves the vendor extensions, property datatype, and datatype for each property in the OAS schema object
    public static Optional<CodegenModel> resolve(Optional<CodegenModel> model) {
        // Loop through all properties (without parent's properties) of an OAS schema object
        for (CodegenProperty property : model.get().vars) {
            Map<String, Map<String, Object>> vendorExtensions = new HashMap<>();

            // Builds the vendorExtensions map
            for (Segments segment: Segments.values()) {     // ["library", "properties", "serialize", "deserialize", ...] in java.json
                Map<String, Object> segmentMap = conventionMap.get(segment.getSegment());
                if (segmentMap.containsKey(property.dataFormat)) {
                    Map<String, Object> segmentElements = new HashMap<>();
                    segmentElements.put(VENDOR_PREFIX + property.dataFormat, String.format(segmentMap.get(property.dataFormat).toString() , property.name));
                    vendorExtensions.put(segment.getSegment(), segmentElements);    // i.e. ["properties"]->[["x-date"]->["ZonedDateTime"]]
                }
            }

            // Checks if the conventionMap.properties has the current property data format
            //      If so, sets the property.dataType value to the conventionMap.properties[property.dataFormat] string value
            boolean hasProperty = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).containsKey(property.dataFormat);
            if (hasProperty) {
                property.dataType = (String)conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(property.dataFormat);
            }

            // If the property.dataType is an "object" or a "List<Object>",
            //      convert property.dataType to conventionMap.properties["object"], or "List<" + conventionMap.properties["object"] + ">"
            if(property.dataType.equalsIgnoreCase(OBJECT) || property.dataType.equals(LIST_OBJECT)) {
                if (property.dataType.equals(LIST_OBJECT)) {
                    property.dataType = "List<" + conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(OBJECT) + ">";
                } else {
                    property.dataType = (String) conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(OBJECT);
                }
            }

            // Converts property.nameInSnakeCase to all lowercase
            property.nameInSnakeCase = property.nameInSnakeCase.toLowerCase(Locale.ROOT);

            // Merges the vendorExtensions map with the existing property's vendor extensions
            vendorExtensions.forEach(
                    (key, value) -> property.getVendorExtensions().merge(key, value, (oldValue, newValue) -> newValue)
            );
        }
        return model;
    }

    // Resolves the Java datatypes (basetype and datatype), "promotions", and "properties" for the OAS operation parameter.
    // Relies on the vendorExtensions map being assigned to the parameter
    public static Optional<CodegenParameter> resolveParameter(CodegenParameter parameter) {

        // Sets the parameter's dataType property
        if(parameter.dataType.equalsIgnoreCase(OBJECT) || parameter.dataType.equals(LIST_OBJECT)) {
            if (parameter.dataType.equals(LIST_OBJECT)) {
                parameter.dataType = "List<" + conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(OBJECT)+ ">";
                parameter.baseType = "" + conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(OBJECT);
            } else {
                parameter.dataType = (String) conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(OBJECT);
            }
            parameter.isFreeFormObject = true;
        }

        // TODO: This can be replaced with the common.CodegenParameterDataTypeResolver
        // Check if the parameter dataFormat has a "promotion" in the conventionMap.promotions
        //      If so, set the parameter vendorExtensions "x-promotions" to the (formatted) promotion map
        boolean hasPromotion = conventionMap.get(Segments.SEGMENT_PROMOTIONS.getSegment()).containsKey(parameter.dataFormat);
        if (hasPromotion) {
            // cloning to prevent update in source map
            HashMap<String, String> promotionsMap = new HashMap<>((Map) conventionMap
                    .get(Segments.SEGMENT_PROMOTIONS.getSegment()).get(parameter.dataFormat));
            promotionsMap.replaceAll((dataType, value) -> String.format(value, parameter.paramName) );
            parameter.vendorExtensions.put("x-promotions", promotionsMap);
        }

        // TODO: This can be replaced with the common.CodegenParameterDataTypeResolver
        // Check if the parameter dataFormat has a "properties" in the conventionMap.properties
        //      If so, set the parameter dataType value to the conventionMap value
        boolean hasProperty = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).containsKey(parameter.dataFormat);
        if (hasProperty) {
            parameter.dataType = (String)conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(parameter.dataFormat);
        }

        // If it's a phone number format, put an vendor extension to flag its a phone number
        if( PHONE_NUMBER_FORMAT.equals(parameter.dataFormat)) {
            parameter.vendorExtensions.put(X_IS_PHONE_NUMBER_FORMAT, true);
        }
        parameter.paramName = StringHelper.toFirstLetterLower(parameter.paramName);
        return Optional.of(parameter);
    }

    // Resolves the data type for the given OAS operation parameter
    public static CodegenParameter resolveParamTypes(CodegenParameter codegenParameter) {
        boolean hasProperty = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).containsKey(codegenParameter.dataFormat);
        if (hasProperty) {
            codegenParameter.dataType = (String) conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(codegenParameter.dataFormat);
        }
        return codegenParameter;
    }

    // If the OAS operation parameter dataFormat starts with "prefixed-collapsible-map"
    //      Put a "x-prefixed-collapsible-map" vendor extension and set to "prefixed"
    //      Sets the parameter.dataType to prefixed-collapsible-map conventionMap property value
    public static CodegenParameter prefixedCollapsibleMap(CodegenParameter parameter) {
        if (parameter.dataFormat != null && parameter.dataFormat.startsWith(PREFIXED_COLLAPSIBLE_MAP)) {
            String[] split_format_array = parameter.dataFormat.split(HYPHEN);
            parameter.vendorExtensions.put(X_PREFIXED_COLLAPSIBLE_MAP, split_format_array[split_format_array.length - 1]);
            parameter.dataType = (String)conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(PREFIXED_COLLAPSIBLE_MAP);
        }
        parameter.paramName = StringHelper.toFirstLetterLower(parameter.paramName);
        return parameter;
    }

    // Reads the conventionMap configuration JSON file, deserialize it, and return it 
    public static Map<String, Map<String, Object>> getConventionalMap() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(ApplicationConstants.CONFIG_JAVA_JSON_PATH), new TypeReference<Map<String, Map<String, Object>>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Resolves the dataType for a property if the property.complexType is in the given modelFormatMap
    public static Optional<CodegenModel> resolveComplexType(Optional<CodegenModel> item, Map<String, String> modelFormatMap) {
        for (CodegenProperty prop: item.get().vars) {
            if(modelFormatMap.containsKey(prop.complexType)) {
                boolean hasProperty =  conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).containsKey(modelFormatMap.get(prop.complexType));
                if (hasProperty) {
                    if ( prop.containerType != null && prop.containerType.equals("array")) {
                        prop.dataType = "List<" + conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(modelFormatMap.get(prop.complexType)) + ">";
                    } else {
                        prop.dataType = (String)conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(modelFormatMap.get(prop.complexType));
                    }
                }
            }
        }
        return item;
    }
}
