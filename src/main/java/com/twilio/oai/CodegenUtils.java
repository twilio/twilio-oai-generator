package com.twilio.oai;

import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.LinkedHashMap;
import java.util.Map;

public  class CodegenUtils {

    public static boolean isPropertySchemaEnum(CodegenProperty codegenProperty) {
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
        if (codegenParameter.isEnum) {
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
        if(codegenProperty.isEnum) {
            return false;
        }
        boolean enumValues = codegenProperty.allowableValues != null &&
                codegenProperty.allowableValues.containsKey(TwilioJavaGenerator.VALUES);
        boolean listEnumValues = codegenProperty.items != null && (codegenProperty.items.allowableValues != null
                && codegenProperty.items.allowableValues.containsKey(TwilioJavaGenerator.VALUES));
        return enumValues || listEnumValues;
    }

    public static boolean isParameterSchemaEnumJava(CodegenParameter codegenParameter) {
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
        if(codegenProperty.isEnum) {
            return false;
        }
        boolean enumValues = codegenProperty.allowableValues != null &&
                codegenProperty.allowableValues.containsKey(TwilioJavaGenerator.VALUES);
        boolean listEnumValues = codegenProperty.items != null && (codegenProperty.items.allowableValues != null
                && codegenProperty.items.allowableValues.containsKey(TwilioJavaGenerator.VALUES));
        return enumValues || listEnumValues;
    }

    public static boolean isParameterSchemaEnumJava(CodegenParameter codegenParameter) {
        if(codegenParameter.isEnum) {
            return false;
        }
        boolean enumValues = codegenParameter.allowableValues != null &&
                codegenParameter.allowableValues.containsKey(TwilioJavaGenerator.VALUES);
        boolean listEnumValues = codegenParameter.items != null && (codegenParameter.items.allowableValues != null
                && codegenParameter.items.allowableValues.containsKey(TwilioJavaGenerator.VALUES));
        return enumValues || listEnumValues;
    }

    public static void mergeVendorExtensionProperty(Map<String, Object> vendorExtensions, LinkedHashMap value, String field ) {
        if (value != null && !value.isEmpty()) {
            if( vendorExtensions.containsKey(field))
                ((LinkedHashMap)vendorExtensions.get(field)).putAll(value);
            else
                vendorExtensions.put(field, value);
        }
    }
}
