package com.twilio.oai.resolver.csharp_new;

import com.twilio.oai.Segments;
import com.twilio.oai.api.CsharpApiResourceBuilder;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.IConventionMapper;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.HashMap;
import java.util.Map;

public class CsharpEnumResolver {

    private Map<String, Map<String, Object>> conventionMap = Utility.getConventionalMap();

    public CsharpEnumResolver() {}

    public void resolve(CodegenParameter parameter, String className) {

        String property = Segments.SEGMENT_PROPERTIES.getSegment();
        if (conventionMap.get(property).containsKey(parameter.dataFormat) || conventionMap.get(property).containsKey(parameter.dataType)) {
            return;
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
        } else if (parameter.isEnum) {
            parameter.dataType = className + "Resource." + parameter.enumName;
            if (parameter.items != null) {
                parameter.items.enumName = parameter.enumName;
            }
            OperationCache.enums.putIfAbsent(parameter.enumName, parameter);
        }
    }

    public void resolve(CodegenProperty codegenProperty, String className) {
        if (codegenProperty == null) {
            return;
        }
        if (codegenProperty.complexType == null || !codegenProperty.complexType.contains("Enum")) {
            return;
        }
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
    }

    public void resolve(CodegenModel codegenModel, String className) {
        for (CodegenProperty codegenProperty : codegenModel.vars) {
            resolve(codegenProperty, className);
        }
    }
}
