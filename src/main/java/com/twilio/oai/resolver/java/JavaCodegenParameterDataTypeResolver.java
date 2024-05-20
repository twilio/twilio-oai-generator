package com.twilio.oai.resolver.java;

import com.twilio.oai.CodegenUtils;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.common.CodegenParameterDataTypeResolver;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import static com.twilio.oai.common.ApplicationConstants.ENUM;
import static com.twilio.oai.common.ApplicationConstants.ENUM_VARS;
import static com.twilio.oai.common.ApplicationConstants.LIST_END;
import static com.twilio.oai.common.ApplicationConstants.LIST_START;
import static com.twilio.oai.common.ApplicationConstants.REF_ENUM_EXTENSION_NAME;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.StringHelper;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

public class JavaCodegenParameterDataTypeResolver extends CodegenParameterDataTypeResolver {
    protected final IConventionMapper mapper;
    private static final String VALUES = "values";

    public Set<IJsonSchemaValidationProperties> enums = new HashSet<>();

    public JavaCodegenParameterDataTypeResolver(IConventionMapper mapper) {
        super(mapper);
        this.mapper = mapper;
    }

    /* Moved from TwilioJavaGenerator postProcessParameter function*/
    public CodegenParameter processEnumParams(CodegenParameter parameter){
        if (parameter.dataType.startsWith(LIST_START) && CodegenUtils.isParameterSchemaEnumJava(parameter)) {
            if (parameter.dataType.contains(ENUM)) {
                String lastValue = Utility.removeEnumName(parameter.dataType);
                parameter.dataType = LIST_START + lastValue;
                parameter.vendorExtensions.put(REF_ENUM_EXTENSION_NAME, true);
                parameter.baseType = lastValue.substring(0, lastValue.length() - 1);
            }
        } else if (CodegenUtils.isParameterSchemaEnumJava(parameter)) {
            parameter.vendorExtensions.put(REF_ENUM_EXTENSION_NAME, true);
            parameter.enumName = parameter.dataType;
            parameter.dataType = Utility.removeEnumName(parameter.dataType);
            parameter.baseType = Utility.removeEnumName(parameter.dataType);
            parameter.isEnum = true;
        } else if (parameter.isEnum) {
            parameter.enumName = parameter.paramName;
        } else {
            if (parameter.isPathParam) {
                parameter.paramName = "Path" + parameter.paramName.substring(0, 1).toUpperCase() + parameter.paramName.substring(1);
            }
        }
        return parameter;
    }

    /* Moved from TwilioJavaGenerator postProcessModelProperty*/
    public CodegenProperty postProcessModelEnumProperty(CodegenProperty property){
        if (property.dataType.startsWith(LIST_START) && CodegenUtils.isPropertySchemaEnumJava(property)) {
            String lastValue = Utility.removeEnumName(property.dataType);
            property.dataType = LIST_START + lastValue;
            property.vendorExtensions.put(REF_ENUM_EXTENSION_NAME, true);
            property.complexType = lastValue.substring(0, lastValue.length() - 1);
            property.baseType = lastValue.substring(0, lastValue.length() - 1);
            property.isEnum = true;
            property.allowableValues = property.items.allowableValues;
            property._enum = (List<String>) property.items.allowableValues.get(VALUES);
        } else if (CodegenUtils.isPropertySchemaEnumJava(property)) {
            property.vendorExtensions.put(REF_ENUM_EXTENSION_NAME, true);
            property.dataType = Utility.removeEnumName(property.dataType);
            property.complexType = property.dataType;
            property.baseType = property.dataType;
            property.isEnum = true;
            property._enum = (List<String>) property.allowableValues.get(VALUES);
        } else if (property.isEnum) {
            property.enumName = property.baseName;
        }
        property.isEnum = property.isEnum && property.dataFormat == null;
        return property;
    }

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
        ContainerResolver containerResolver = new ContainerResolver(Arrays.asList(EnumConstants.JavaDataTypes.values()));
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
