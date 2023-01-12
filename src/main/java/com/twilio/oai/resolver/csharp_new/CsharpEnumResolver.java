package com.twilio.oai.resolver.csharp_new;

import com.twilio.oai.Segments;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.common.ContainerResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CsharpEnumResolver {

    private Map<String, Map<String, Object>> conventionMap = Utility.getConventionalMap();

    ContainerResolver containerResolver = new ContainerResolver(Arrays.asList(EnumConstants.CsharpDataTypes.values()));

    public CsharpEnumResolver() {}

    public CodegenParameter resolve(CodegenParameter parameter) {
        String unrappedContainer = containerResolver.unWrapContainerType(parameter);
        String className = OperationCache.className;
        String property = Segments.SEGMENT_PROPERTIES.getSegment();
        if (conventionMap.get(property).containsKey(parameter.dataFormat) || conventionMap.get(property).containsKey(parameter.dataType)) {
            containerResolver.reWrapContainerType(parameter, unrappedContainer);
            return parameter;
        }
        if (parameter.dataType.contains("Enum")) { // parameter.dataType.contains(className) &&
            parameter.isEnum = true;
            String[] value = parameter.dataType.split("Enum");
            parameter.enumName = value[value.length-1] + "Enum";
            if (parameter.items != null) {
                parameter.items.enumName = value[value.length-1] + "Enum";
            }

            if (OperationCache.enums == null) {
                OperationCache.enums = new HashMap<>();
            }
            parameter.dataType = className + "Resource." + parameter.enumName;
             // Using enums to avoid duplicate enum creation
            OperationCache.enums.putIfAbsent(parameter.enumName, parameter);
            OperationCache.isEnumPresentInResource = true;
        } else if (parameter.isEnum) {
            parameter.dataType = className + "Resource." + parameter.enumName;
            if (parameter.items != null) {
                parameter.items.enumName = parameter.enumName;
            }
            OperationCache.enums.putIfAbsent(parameter.enumName, parameter);
            OperationCache.isEnumPresentInResource = true;
        }
        containerResolver.reWrapContainerType(parameter, unrappedContainer);
        return parameter;
    }

    public CodegenProperty resolve(CodegenProperty codegenProperty) {
        if (codegenProperty == null) {
            return null;
        }
        String unrappedContainer = containerResolver.unWrapContainerType(codegenProperty);
        if (codegenProperty.complexType == null || !codegenProperty.complexType.contains("Enum")) {
            if (codegenProperty.dataType != null) {
                Object importStm = conventionMap.get("library").get(StringHelper.toSnakeCase(codegenProperty.dataType));
                if (importStm != null && importStm instanceof String && importStm.equals("Twilio.Types")) {
                    OperationCache.isEnumPresentInResource = true;
                }
            }
            containerResolver.reWrapContainerType(codegenProperty, unrappedContainer);
            return codegenProperty;
        }
        String className = OperationCache.className;
        codegenProperty.isEnum = true;
        String[] value = codegenProperty.complexType.split("Enum");
        codegenProperty.enumName = value[value.length-1] + "Enum";
        if (codegenProperty.items != null) {
            codegenProperty.items.enumName = value[value.length-1] + "Enum";
        }
        if (OperationCache.enums == null) {
            OperationCache.enums = new HashMap<>();
        }
        codegenProperty.dataType = className + "Resource." + codegenProperty.enumName;
        codegenProperty.vendorExtensions.put("x-jsonConverter", "StringEnumConverter");
        OperationCache.enums.put(codegenProperty.enumName, codegenProperty);
        OperationCache.isEnumPresentInResource = true;
        containerResolver.reWrapContainerType(codegenProperty, unrappedContainer);
        return codegenProperty;
    }

    public void resolve(CodegenModel codegenModel) {
        for (CodegenProperty codegenProperty : codegenModel.vars) {
            resolve(codegenProperty);
        }
    }
}
