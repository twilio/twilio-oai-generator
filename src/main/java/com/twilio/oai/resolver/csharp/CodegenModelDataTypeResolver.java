package com.twilio.oai.resolver.csharp;

import com.twilio.oai.Segments;
import com.twilio.oai.StringHelper;
import com.twilio.oai.resolver.Resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import static com.twilio.oai.common.ApplicationConstants.LIST_START;
import static com.twilio.oai.common.ApplicationConstants.OBJECT;

@Setter
public class CodegenModelDataTypeResolver extends Resolver<CodegenProperty> {
    private final CodegenModelComplexResolver codegenModelComplexResolver = new CodegenModelComplexResolver();
    private Map<String, Map<String, Object>> conventionMap;
    private Map<String, IJsonSchemaValidationProperties> enums;
    private String className;
    private Map<String, String> modelFormatMap = new HashMap<>();
    @Getter
    private Set<String> enumsDict;

    public CodegenProperty resolve(CodegenProperty codegenProperty){

        assignDataType(codegenProperty);
        // Exceptional case, data format does not exist for Object type.
        assignDataTypeObjectForNullDataFormat(codegenProperty);

        if (codegenProperty.complexType != null && codegenProperty.complexType.contains("Enum")) { // codegenProperty.dataType.contains(className) &&
            handleEnums(codegenProperty);
        } else if (codegenProperty.complexType != null && modelFormatMap.containsKey(codegenProperty.complexType)) {

            codegenModelComplexResolver.setModelFormatMap(modelFormatMap);
            codegenModelComplexResolver.setConventionMap(conventionMap);
            codegenModelComplexResolver.setEnumsDict(enumsDict);
            codegenModelComplexResolver.resolve(codegenProperty);
        }

        return codegenProperty;
    }

    private void assignDataType(CodegenProperty codegenProperty){
        String property = Segments.SEGMENT_PROPERTIES.getSegment();
        String deserialize = Segments.SEGMENT_DESERIALIZE.getSegment();

        if(StringHelper.existInSetIgnoreCase(codegenProperty.dataType, enumsDict) || StringHelper.existInSetIgnoreCase(codegenProperty.dataFormat, enumsDict)){
            codegenProperty.vendorExtensions.put("x-has-enum-params", true);
        }

        if (conventionMap.get(property).containsKey(codegenProperty.dataFormat)) {
            codegenProperty.dataType = (String)conventionMap.get(property).get(codegenProperty.dataFormat);
        } else if (conventionMap.get(property).containsKey(codegenProperty.dataType)) {
            codegenProperty.dataType = (String)conventionMap.get(property).get(codegenProperty.dataType);
        }

        if (conventionMap.get(deserialize).containsKey(codegenProperty.dataFormat)) {
            codegenProperty.vendorExtensions.put("x-jsonConverter", conventionMap.get(deserialize).get(codegenProperty.dataFormat));
        }
    }

    private void assignDataTypeObjectForNullDataFormat(CodegenProperty codegenProperty){
        if (codegenProperty.dataFormat == null) {
            if (codegenProperty.dataType.equals(OBJECT)) {
                codegenProperty.dataType = "object";
            } else if (codegenProperty.dataType.equals(LIST_START + OBJECT )) {
                codegenProperty.dataType = "List<object";
            }
        }
    }

    private void handleEnums(CodegenProperty codegenProperty){
        codegenProperty.isEnum = true;
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
        enums.put(codegenProperty.enumName, codegenProperty);
    }
}
