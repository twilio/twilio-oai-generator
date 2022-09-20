package com.twilio.oai.resolver.csharp;

import com.twilio.oai.Segments;
import com.twilio.oai.resolver.Resolver;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.HashMap;
import java.util.Map;

public class CodegenModelDataTypeResolver implements Resolver<CodegenProperty> {
    private Map<String, Map<String, Object>> conventionMap;
    private Map<String, IJsonSchemaValidationProperties> enums;
    private CodegenModelComplexResolver codegenModelComplexResolver = new CodegenModelComplexResolver();
    private String className;
    private  Map<String, String> modelFormatMap = new HashMap<>();

    public CodegenProperty resolve(CodegenProperty codegenProperty){
        String property = Segments.SEGMENT_PROPERTIES.getSegment();
        String deserialize = Segments.SEGMENT_DESERIALIZE.getSegment();
        if (conventionMap.get(property).containsKey(codegenProperty.dataFormat)) {
            codegenProperty.dataType = (String)conventionMap.get(property).get(codegenProperty.dataFormat);
        } else if (conventionMap.get(property).containsKey(codegenProperty.dataType)) {
            codegenProperty.dataType = (String)conventionMap.get(property).get(codegenProperty.dataType);
        }

        if (conventionMap.get(deserialize).containsKey(codegenProperty.dataFormat)) {
            codegenProperty.vendorExtensions.put("x-jsonConverter", conventionMap.get(deserialize).get(codegenProperty.dataFormat));
        }

        // Exceptional case, data format does not exist for Object type.
        if (codegenProperty.dataFormat == null) {
            if (codegenProperty.dataType.equals("Object")) {
                codegenProperty.dataType = "object";
            } else if (codegenProperty.dataType.equals("List<Object" )) {
                codegenProperty.dataType = "List<object";
            }
        }

        if (codegenProperty.complexType != null && codegenProperty.complexType.contains("Enum")) { // codegenProperty.dataType.contains(className) &&
            String[] value = codegenProperty.complexType.split("Enum");
            codegenProperty.enumName = value[value.length-1] + "Enum";
            if (codegenProperty.items != null) {
                codegenProperty.items.enumName = value[value.length-1] + "Enum";
            }
            if (enums == null) {
                enums = new HashMap<>();
            }
            codegenProperty.dataType = className + "Resource." + codegenProperty.enumName;
            codegenProperty.vendorExtensions.put("x-jsonConverter", "StringEnumConverter");
            enums.putIfAbsent(codegenProperty.enumName, codegenProperty);
        } else if (codegenProperty.complexType != null && modelFormatMap.containsKey(codegenProperty.complexType)) {
            //resolveComplex(codegenProperty);
            codegenModelComplexResolver.setModelFormatMap(modelFormatMap);
            codegenModelComplexResolver.setConventionalMap(conventionMap);
            codegenModelComplexResolver.resolve(codegenProperty);
        }

        return codegenProperty;
    }
    public void setClassName(String className){
        this.className = className;
    }

    public void setEnums(Map<String, IJsonSchemaValidationProperties> enums) {
        this.enums = enums;
    }
    public void setModelFormatMap(final Map<String, String> modelFormatMap) {
        this.modelFormatMap = new HashMap<>(modelFormatMap);
    }
    public void setConventionMap(Map<String, Map<String, Object>> conventionMap) {
        this.conventionMap = conventionMap;
    }
}
