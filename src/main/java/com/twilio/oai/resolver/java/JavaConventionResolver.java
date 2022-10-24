package com.twilio.oai.resolver.java;

import java.util.*;

import com.twilio.oai.Segments;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.common.CodegenModelResolver;
import com.twilio.oai.resolver.common.CodegenParameterResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public class JavaConventionResolver {
    private final String VENDOR_PREFIX = "x-";
    private final String PREFIXED_COLLAPSIBLE_MAP = "prefixed-collapsible-map";
    private final String X_PREFIXED_COLLAPSIBLE_MAP = "x-prefixed-collapsible-map";
    private final String HYPHEN = "-";
    private final String OBJECT = "object";

    private final String LIST_OBJECT = "List<Object>";

    private final String PHONE_NUMBER_FORMAT = "phone-number";

    private final String X_IS_PHONE_NUMBER_FORMAT = "x-is-phone-number-format";

    private Map<String, Map<String, Object>> conventionMap;
    private CodegenModelResolver codegenModelResolver;
    private CodegenParameterResolver codegenParameterResolver;

    public JavaConventionResolver(Map<String, Map<String, Object>> conventionMap) {
        this.conventionMap = conventionMap;
        codegenModelResolver = new CodegenModelResolver(conventionMap,
                Arrays.asList(EnumConstants.JavaDataTypes.values()),
                EnumConstants.JavaDataTypes.LIST.getValue(),
                ApplicationConstants.LIST_END);
        codegenParameterResolver = new CodegenParameterResolver(conventionMap,
                Arrays.asList(EnumConstants.JavaDataTypes.values()));
    }

    public CodegenModel resolve(CodegenModel model) {
        codegenModelResolver.resolve(model);
        for (CodegenProperty property : model.vars) {
            Map<String, Map<String, Object>> vendorExtensions = new HashMap<>();

            for (Segments segment: Segments.values()) {
                Map<String, Object> segmentMap = conventionMap.get(segment.getSegment());
                if (segmentMap.containsKey(property.dataFormat)) {
                    Map<String, Object> segmentElements = new HashMap<>();
                    segmentElements.put(VENDOR_PREFIX + property.dataFormat, String.format(segmentMap.get(property.dataFormat).toString() , property.name));
                    vendorExtensions.put(segment.getSegment(), segmentElements);
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

    public CodegenParameter resolveParameter(CodegenParameter parameter) {
        if(parameter.dataType.equalsIgnoreCase(OBJECT) || parameter.dataType.equals(LIST_OBJECT)) {
            if (parameter.dataType.equals(LIST_OBJECT)) {
                parameter.dataType = "List<" + conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(OBJECT)+ ">";
                parameter.baseType = "" + conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(OBJECT);
            } else {
                parameter.dataType = (String) conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(OBJECT);
            }
            parameter.isFreeFormObject = true;
        }

        boolean hasPromotion = conventionMap.get(Segments.SEGMENT_PROMOTIONS.getSegment()).containsKey(parameter.dataFormat);
        if (hasPromotion) {
            // cloning to prevent update in source map
            HashMap<String, String> promotionsMap = new HashMap<>((Map) conventionMap
                    .get(Segments.SEGMENT_PROMOTIONS.getSegment()).get(parameter.dataFormat));
            promotionsMap.replaceAll((dataType, value) -> String.format(value, parameter.paramName) );
            parameter.vendorExtensions.put("x-promotions", promotionsMap);
        }

        codegenParameterResolver.resolve(parameter);

        if( PHONE_NUMBER_FORMAT.equals(parameter.dataFormat)) {
            parameter.vendorExtensions.put(X_IS_PHONE_NUMBER_FORMAT, true);
        }
        parameter.paramName = StringHelper.toFirstLetterLower(parameter.paramName);
        return parameter;
    }

    public CodegenParameter resolveParamTypes(CodegenParameter codegenParameter) {
        boolean hasProperty = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).containsKey(codegenParameter.dataFormat);
        if (hasProperty) {
            codegenParameter.dataType = (String) conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(codegenParameter.dataFormat);
        }
        return codegenParameter;
    }

    public CodegenParameter prefixedCollapsibleMap(CodegenParameter parameter) {
        if (parameter.dataFormat != null && parameter.dataFormat.startsWith(PREFIXED_COLLAPSIBLE_MAP)) {
            String[] split_format_array = parameter.dataFormat.split(HYPHEN);
            parameter.vendorExtensions.put(X_PREFIXED_COLLAPSIBLE_MAP, split_format_array[split_format_array.length - 1]);
            parameter.dataType = (String)conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(PREFIXED_COLLAPSIBLE_MAP);
        }
        parameter.paramName = StringHelper.toFirstLetterLower(parameter.paramName);
        return parameter;
    }

    // Resolves the dataType for a property if the property.complexType is in the given modelFormatMap
    public CodegenModel resolveComplexType(CodegenModel item, Map<String, String> modelFormatMap) {
        codegenModelResolver.setModelFormatMap(modelFormatMap);
        codegenModelResolver.resolve(item);
        return item;
    }
}
