package com.twilio.oai.resolver.java;

import java.util.*;

import com.twilio.oai.Segments;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.common.CodegenModelResolver;
import com.twilio.oai.resolver.common.CodegenParameterResolver;
import com.twilio.oai.resolver.IConventionMapper;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import static com.twilio.oai.common.ApplicationConstants.*;
import static com.twilio.oai.common.ApplicationConstants.ENUM_VARS;

public class JavaConventionResolver {
    private final String VENDOR_PREFIX = "x-";
    private final String PREFIXED_COLLAPSIBLE_MAP = "prefixed-collapsible-map";
    private final String X_PREFIXED_COLLAPSIBLE_MAP = "x-prefixed-collapsible-map";
    private static final String HTTP_METHOD = "http-method";
    private final String HYPHEN = "-";
    private final String OBJECT = "object";

    private final String LIST_OBJECT = "List<Object>";

    private final String PHONE_NUMBER_FORMAT = "phone-number";

    private final String X_IS_PHONE_NUMBER_FORMAT = "x-is-phone-number-format";

    private IConventionMapper conventionMapper;
    private CodegenModelResolver codegenModelResolver;
    private CodegenParameterResolver codegenParameterResolver;

    private static final String VALUES = "values";

    public JavaConventionResolver(IConventionMapper conventionMapper) {
        this.conventionMapper = conventionMapper;
        codegenModelResolver = new CodegenModelResolver(conventionMapper,
                Arrays.asList(EnumConstants.JavaDataTypes.values()));
        codegenParameterResolver = new CodegenParameterResolver(conventionMapper,
                Arrays.asList(EnumConstants.JavaDataTypes.values()));
    }

    public CodegenModel resolve(CodegenModel model) {
        codegenModelResolver.resolve(model);
        for (CodegenProperty property : model.vars) {
            Map<String, Map<String, Object>> vendorExtensions = new HashMap<>();

            for (Segments segment: Segments.values()) {
                Map<String, Object> segmentMap = getMapperByType(segment);
                if (segmentMap.containsKey(property.dataFormat)) {
                    Map<String, Object> segmentElements = new HashMap<>();
                    segmentElements.put(VENDOR_PREFIX + property.dataFormat, String.format(segmentMap.get(property.dataFormat).toString() , property.name));
                    vendorExtensions.put(segment.getSegment(), segmentElements);
                }
            }

            property.nameInSnakeCase = StringHelper.toSnakeCase(property.name);

            // Merges the vendorExtensions map with the existing property's vendor extensions
            vendorExtensions.forEach(
                    (key, value) -> property.getVendorExtensions().merge(key, value, (oldValue, newValue) -> newValue)
            );
        }
        return model;
    }

    Map<String, Object> getMapperByType(Segments segments) {
        switch (segments){
            case SEGMENT_PROPERTIES:
                return conventionMapper.properties();
            case SEGMENT_LIBRARY:
                return conventionMapper.libraries();
            case SEGMENT_DESERIALIZE:
                return conventionMapper.deserialize();
            case SEGMENT_SERIALIZE:
                return conventionMapper.serialize();
            case SEGMENT_PROMOTIONS:
                return conventionMapper.promotions();
            case SEGMENT_HYDRATE:
                return conventionMapper.hydrate();
        }
        return null;

    }

    public CodegenParameter resolveParameter(CodegenParameter parameter) {
        if(parameter.dataType.equalsIgnoreCase(OBJECT) || parameter.dataType.equals(LIST_OBJECT)) {
            if (parameter.dataType.equals(LIST_OBJECT)) {
                parameter.dataType = "List<" + conventionMapper.properties().get(OBJECT)+ ">";
                parameter.baseType = "" + conventionMapper.properties().get(OBJECT);
            } else {
                parameter.dataType = (String) conventionMapper.properties().get(OBJECT);
            }
            parameter.isFreeFormObject = true;
        }

        boolean hasPromotion = conventionMapper.promotions().containsKey(parameter.dataFormat);
        if (hasPromotion) {
            // cloning to prevent update in source map
            HashMap<String, String> promotionsMap = new HashMap<>((Map) conventionMapper.promotions().get(parameter.dataFormat));
            promotionsMap.replaceAll((dataType, value) -> String.format(value, parameter.paramName) );
            parameter.vendorExtensions.put("x-promotions", promotionsMap);
        }

        codegenParameterResolver.resolve(parameter);

        if( PHONE_NUMBER_FORMAT.equals(parameter.dataFormat)) {
            parameter.vendorExtensions.put(X_IS_PHONE_NUMBER_FORMAT, true);
        }
        // prevent special format properties to be considered as enum
        if (conventionMapper.properties().containsKey(parameter.dataFormat)) {
            parameter.isEnum = false;
            parameter.allowableValues = null;
        }
        parameter.paramName = StringHelper.toFirstLetterLower(parameter.paramName);
        return parameter;
    }

    public CodegenParameter resolveParamTypes(CodegenParameter codegenParameter) {
        boolean hasProperty = conventionMapper.properties().containsKey(codegenParameter.dataFormat);
        if (hasProperty) {
            codegenParameter.dataType = (String) conventionMapper.properties().get(codegenParameter.dataFormat);
        }
        return codegenParameter;
    }

    public CodegenParameter prefixedCollapsibleMap(CodegenParameter parameter) {
        if (parameter.dataFormat != null && parameter.dataFormat.startsWith(PREFIXED_COLLAPSIBLE_MAP)) {
            String[] split_format_array = parameter.dataFormat.split(HYPHEN);
            parameter.vendorExtensions.put(X_PREFIXED_COLLAPSIBLE_MAP, split_format_array[split_format_array.length - 1]);
            parameter.dataType = (String)conventionMapper.properties().get(PREFIXED_COLLAPSIBLE_MAP);
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

     @SuppressWarnings("unchecked")
    public CodegenParameter resolveEnumParameter(CodegenParameter parameter, String resourceName) {
        if( parameter.isEnum && !parameter.vendorExtensions.containsKey(REF_ENUM_EXTENSION_NAME)) {
            parameter.enumName = StringHelper.camelize(parameter.enumName);
            if (parameter.items != null && parameter.items.allowableValues != null && parameter.items.allowableValues.containsKey(VALUES)) {
                parameter.dataType = LIST_START + resourceName+"."+ parameter.enumName + LIST_END;
                parameter.baseType = resourceName + "." + parameter.enumName;
            } else {
                parameter.dataType = resourceName + "." + parameter.enumName;
            }

            return parameter;
        }
        if (parameter.items != null && parameter.items.allowableValues != null && parameter.items.allowableValues.containsKey(VALUES) ) {
            parameter.isEnum = true;
            parameter.enumName = parameter.baseType;
            parameter._enum = (List<String>) parameter.items.allowableValues.get(VALUES);
            parameter.dataType = LIST_START + resourceName + "." + parameter.baseType + LIST_END;
            parameter.baseType = resourceName + "." + parameter.baseType;
            parameter.allowableValues = parameter.items.allowableValues;
        }
        if (parameter.allowableValues != null && parameter.allowableValues.containsKey(ENUM_VARS)) {
            parameter.isEnum = true;
            parameter._enum = (List<String>) parameter.allowableValues.get(VALUES);
            parameter.enumName = parameter.dataType;
            parameter.dataType = resourceName + "." + parameter.dataType;
        }
        return parameter;
    }
}
