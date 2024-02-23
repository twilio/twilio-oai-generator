package com.twilio.oai;

import java.util.LinkedHashMap;
import java.util.Map;

import com.twilio.oai.common.EnumConstants.PredefinedJavaEnumsFormat;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public  class CodegenUtils {

    private CodegenUtils() {

    }

    public static boolean isPropertySchemaEnum(CodegenProperty codegenProperty) {
        if (isPredefinedEnum(codegenProperty)) {
            return false;
        }
        if(codegenProperty.isEnum) {
            return true;
        }
        boolean enumValues = codegenProperty.allowableValues != null &&
                codegenProperty.allowableValues.containsKey(TwilioJavaGenerator.VALUES);
        boolean listEnumValues = codegenProperty.items != null && (codegenProperty.items.allowableValues != null
                && codegenProperty.items.allowableValues.containsKey(TwilioJavaGenerator.VALUES));
        return enumValues || listEnumValues;
    }

    public static boolean isParameterSchemaEnum(CodegenParameter codegenParameter) {
        if (isPredefinedEnum(codegenParameter)) {
            return false;
        }
        if(codegenParameter.isEnum) {
            return true;
        }
        boolean enumValues = codegenParameter.allowableValues != null &&
                codegenParameter.allowableValues.containsKey(TwilioJavaGenerator.VALUES);
        boolean listEnumValues = codegenParameter.items != null && (codegenParameter.items.allowableValues != null
                && codegenParameter.items.allowableValues.containsKey(TwilioJavaGenerator.VALUES));
        return enumValues || listEnumValues;
    }

    // TODO: Refactor java code.
    public static boolean isPropertySchemaEnumJava(CodegenProperty codegenProperty) {
        // If an enum is predefined, we don't need to create enum model. for example: HttpMethod
        if (isPredefinedEnum(codegenProperty)) {
            return false;
        }
        if(codegenProperty.isEnum) {
            return true;
        }
        boolean enumValues = codegenProperty.allowableValues != null &&
                codegenProperty.allowableValues.containsKey(TwilioJavaGenerator.VALUES);
        boolean listEnumValues = codegenProperty.items != null && (codegenProperty.items.allowableValues != null
                && codegenProperty.items.allowableValues.containsKey(TwilioJavaGenerator.VALUES));
        return enumValues || listEnumValues;
    }

    public static boolean isParameterSchemaEnumJava(CodegenParameter codegenParameter) {
        if (isPredefinedEnum(codegenParameter)) {
            return false;
        }
        if(codegenParameter.isEnum) {
            return true;
        }
        boolean enumValues = codegenParameter.allowableValues != null &&
                codegenParameter.allowableValues.containsKey(TwilioJavaGenerator.VALUES);
        boolean listEnumValues = codegenParameter.items != null && (codegenParameter.items.allowableValues != null
                && codegenParameter.items.allowableValues.containsKey(TwilioJavaGenerator.VALUES));
        return enumValues || listEnumValues;
    }

    public static void mergeVendorExtensionProperty(Map<String, Object> vendorExtensions, Map<String, Object> value, String field ) {
        if (value != null && !value.isEmpty()) {
            if( vendorExtensions.containsKey(field))
                ((LinkedHashMap<String, Object>)vendorExtensions.get(field)).putAll(value);
            else
                vendorExtensions.put(field, value);
        }
    }

    public static boolean isPredefinedEnum(CodegenProperty codegenProperty) {
        if (codegenProperty.dataFormat != null) {
            String format = codegenProperty.dataFormat;
            for (PredefinedJavaEnumsFormat value: PredefinedJavaEnumsFormat.values()) {
                if (value.getValue().equals(format)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isPredefinedEnum(CodegenParameter codegenParameter) {
        if (codegenParameter.dataFormat != null) {
            String format = codegenParameter.dataFormat;
            for (PredefinedJavaEnumsFormat value: PredefinedJavaEnumsFormat.values()) {
                if (value.getValue().equals(format)) {
                    return true;
                }
            }
        }
        return false;
    }
}