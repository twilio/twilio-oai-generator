package com.twilio.oai.resolver.csharp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.Segments;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.Resolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CodegenModelResolver implements Resolver<CodegenModel> {

    private final Map<String, Map<String, Object>> conventionMap;
    private String className;
    private Map<String, IJsonSchemaValidationProperties> enums;
    private  Map<String, String> modelFormatMap = new HashMap<>();
    private CodegenModelComplexResolver codegenModelComplexResolver = new CodegenModelComplexResolver();
    private CodegenModelDataTypeResolver codegenModelDataTypeResolver = new CodegenModelDataTypeResolver();
    private CodegenModelContainerDataTypeResolver codegenModelContainerDataTypeResolver = new CodegenModelContainerDataTypeResolver(codegenModelDataTypeResolver);
    public CodegenModelResolver(){
        conventionMap = getConventionalMap();
    }

    @Override
    public CodegenModel resolve(CodegenModel codegenModel){
        if (codegenModel == null) {
            return null;
        }
        for (CodegenProperty codegenProperty : codegenModel.vars) {
            new PropertyFormat().sanitize(codegenProperty);
            String existsInDataType = null;
            for (EnumConstants.CsharpDataTypes dataType: EnumConstants.CsharpDataTypes.values()) {
                if (codegenProperty.dataType != null && codegenProperty.dataType.startsWith(dataType.getValue())) {
                    existsInDataType = dataType.getValue();
                    break;
                }
            }
//            System.out.println(codegenProperty);
            codegenModelDataTypeResolver.setModelFormatMap(modelFormatMap);
            codegenModelDataTypeResolver.setConventionMap(conventionMap);
            codegenModelDataTypeResolver.setClassName(className);
            codegenModelDataTypeResolver.setEnums(enums);
            if (existsInDataType != null) {
                //resolveContainerDataType(codegenProperty, existsInDataType);
                //resolveContainerDataType(codegenProperty);
                codegenModelContainerDataTypeResolver.resolve(codegenProperty);
            } else {

                codegenModelDataTypeResolver.resolve(codegenProperty);
                //resolveDataType(codegenProperty);
            }
//            System.out.println(codegenProperty);
        }

        return codegenModel;
    }

    public static Map<String, Map<String, Object>> getConventionalMap() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(ApplicationConstants.CONFIG_CSHARP_JSON_PATH), new TypeReference<Map<String, Map<String, Object>>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, IJsonSchemaValidationProperties> getEnums() {
        return enums;
    }

    public void setEnums(Map<String, IJsonSchemaValidationProperties> enums) {
        this.enums = enums;
    }

    public void setModelFormatMap(final Map<String, String> modelFormatMap) {
        this.modelFormatMap = new HashMap<>(modelFormatMap);
    }

}
