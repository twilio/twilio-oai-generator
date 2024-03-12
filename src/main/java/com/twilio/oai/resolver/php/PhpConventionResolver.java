package com.twilio.oai.resolver.php;

import com.twilio.oai.CodegenUtils;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.common.CodegenConventionResolver;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static com.twilio.oai.common.ApplicationConstants.*;

public class PhpConventionResolver extends CodegenConventionResolver {
    private static final String VALUES = "values";

    private PhpContainerResolver containerResolver = new PhpContainerResolver(Arrays.asList(EnumConstants.PhpDataTypes.values()));

    /*
     * Mustache           Object (property or parameter)
     * enum class name  = enumName
     * data type        = datatype (apiName.enumName)
     * variable name    = name
     */
     @SuppressWarnings("unchecked")
    public CodegenParameter resolveEnumParameter(CodegenParameter parameter, String resourceName) {
        if( parameter.isEnum && !parameter.vendorExtensions.containsKey(REF_ENUM_EXTENSION_NAME)) {
            if (parameter.enumName.contains(ApplicationConstants.ENUM)) {
                parameter.enumName = Utility.removeEnumName(parameter.enumName);
            }
            parameter.enumName = StringHelper.camelize(parameter.enumName);
            if (parameter.items != null && parameter.items.allowableValues != null && parameter.items.allowableValues.containsKey(VALUES)) {
                parameter.dataType = resourceName+"."+ parameter.enumName;
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
            parameter.dataType = resourceName + "." + parameter.baseType;
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
                property.dataType = containerResolver.unwrapContainerType(property, containerTypes);
                if (property.dataType != null && property.dataType.contains(ApplicationConstants.DOT)) {
                    // If models is resolved more than twice then we need to remove previous resource name
                    property.dataType = property.dataType.substring(property.dataType.indexOf(ApplicationConstants.DOT) + 1);
                }
                property.enumName = property.enumName == null ? property.dataType : property.enumName;
                property.dataType = property.dataType != null && property.dataType.contains(resourceName + ApplicationConstants.DOT)
                        ? property.dataType: resourceName + ApplicationConstants.DOT + property.dataType;
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
                if (property.dataType != null && property.dataType.contains(ApplicationConstants.DOT)) {
                    // If models is resolved more than twice then we need to remove previous resource name
                    property.dataType = property.dataType.substring(property.dataType.indexOf(ApplicationConstants.DOT) + 1);
                }
                property.enumName = property.enumName == null ? property.dataType : property.enumName;
                if (resourceName != null) {
                    // It will restrict the data type to be ResourceName.ResourceName.EnumName.
                    property.dataType = property.dataType != null && property.dataType.contains(resourceName + ApplicationConstants.DOT)
                            ? property.dataType: resourceName + ApplicationConstants.DOT + property.dataType;
                }

            }
            return property;
        }
        return property;
    }
}
