package com.twilio.oai.resolver.java;

import com.twilio.oai.CodegenUtils;
import com.twilio.oai.StringHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import static com.twilio.oai.common.ApplicationConstants.ENUM_VARS;
import static com.twilio.oai.common.ApplicationConstants.LIST_END;
import static com.twilio.oai.common.ApplicationConstants.LIST_START;
import static com.twilio.oai.common.ApplicationConstants.REF_ENUM_EXTENSION_NAME;

public class JavaConventionResolver {
    private static final String VALUES = "values";

    private ContainerResolver containerResolver = new ContainerResolver(Arrays.asList(EnumConstants.JavaDataTypes.values()));

    /*
     * Mustache           Object (property or parameter)
     * enum class name  = enumName
     * data type        = datatype (apiName.enumName)
     * variable name    = name
     */
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

    public CodegenProperty resolveEnumProperty(CodegenProperty property, String resourceName) {
        if(CodegenUtils.isPropertySchemaEnumJava(property)) {
            // complexType contains the class name, dataType contains the data type for enum which prefixes class name.
            // name contains variable name
            // This is new gen enums
            Stack<String> containerTypes = new Stack<>();
            if (property.isContainer) {
                containerResolver.unwrapContainerType(property, containerTypes);
                property.enumName = property.dataType;
                property.dataType = resourceName + ApplicationConstants.DOT + property.dataType;
                if (property.complexType.contains(ApplicationConstants.ENUM)) {
                    property.complexType = Utility.removeEnumName(property.complexType);
                    property.dataType = Utility.removeEnumName(property.dataType);
                }
                containerResolver.rewrapContainerType(property, containerTypes);
                property.isEnum = true;
                property.allowableValues = property.items.allowableValues;
                property._enum = (List<String>) property.items.allowableValues.get(VALUES);
            } else {
                if (property.complexType.contains(ApplicationConstants.ENUM)) {
                    property.complexType = Utility.removeEnumName(property.complexType);
                    property.dataType = Utility.removeEnumName(property.dataType);
                }
                property.enumName = property.dataType;
                property.dataType = resourceName + ApplicationConstants.DOT + property.dataType;
            }
            return property;
        }
        return property;
    }
}
