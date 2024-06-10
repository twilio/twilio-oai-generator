package com.twilio.oai.resolver.java;

import com.twilio.oai.CodegenUtils;
import com.twilio.oai.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenSecurity;

import static com.twilio.oai.common.ApplicationConstants.ENUM_VARS;
import static com.twilio.oai.common.ApplicationConstants.LIST_END;
import static com.twilio.oai.common.ApplicationConstants.LIST_START;
import static com.twilio.oai.common.ApplicationConstants.REF_ENUM_EXTENSION_NAME;

public class JavaConventionResolver {
    private static final String VALUES = "values";
    public static final String AUTH_IMPORT_CLASS = "x-auth-import-class";
    public static final String HTTP_CLASS_PREFIX = "x-http-class-prefix";
    public static final String NOAUTH_IMPORT_CLASS = ".noauth";
    public static final String NOAUTH_HTTP_CLASS_PREFIX = "NoAuth";
    public static final String BEARER_AUTH_IMPORT_CLASS = ".bearertoken";
    public static final String BEARER_AUTH_HTTP_CLASS_PREFIX = "BearerToken";

    public static final String EMPTY_STRING = "";

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
            if (parameter.enumName.contains(ApplicationConstants.ENUM)) {
                parameter.enumName = Utility.removeEnumName(parameter.enumName);
            }
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
            // Check if parameter enum is already resolved or not.
            if (parameter.enumName == null || !parameter.dataType.startsWith(resourceName + ".")) {
                // What if datatype is null ?
                // remove resource name from datatype
                parameter.enumName = parameter.dataType;
            }
            if (parameter.dataType == null || !parameter.dataType.startsWith(resourceName + "." )) {
                parameter.dataType = resourceName + "." + parameter.dataType;
            }
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
                            ? property.dataType : resourceName + ApplicationConstants.DOT + property.enumName;
                }

            }
            return property;
        }
        return property;
    }

    public Map<String, Object> populateSecurityAttributes(CodegenOperation co) {
        ArrayList<CodegenSecurity> authMethods = (ArrayList) co.authMethods;
        if(authMethods == null){
            co.vendorExtensions.put(AUTH_IMPORT_CLASS, NOAUTH_IMPORT_CLASS);
            co.vendorExtensions.put(HTTP_CLASS_PREFIX, NOAUTH_HTTP_CLASS_PREFIX);
        }else{
            for(CodegenSecurity c : authMethods){
                if(c.isOAuth == true){
                    co.vendorExtensions.put(AUTH_IMPORT_CLASS, BEARER_AUTH_IMPORT_CLASS);
                    co.vendorExtensions.put(HTTP_CLASS_PREFIX, BEARER_AUTH_HTTP_CLASS_PREFIX );
                }
                else{
                    co.vendorExtensions.put(AUTH_IMPORT_CLASS, EMPTY_STRING);
                    co.vendorExtensions.put(HTTP_CLASS_PREFIX, EMPTY_STRING);
                }
            }
        }
        return co.vendorExtensions;
    }
}
