package com.twilio.oai.resolver.csharp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.Resolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.IJsonSchemaValidationProperties;
import java.util.HashMap;
import java.util.Map;


public class CodegenModelResolver implements Resolver<CodegenModel> {

    private final Map<String, Map<String, Object>> conventionMap;
    private String className;
    private Map<String, IJsonSchemaValidationProperties> enums;
    private  Map<String, String> modelFormatMap = new HashMap<>();
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

            codegenModelDataTypeResolver.setModelFormatMap(modelFormatMap);
            codegenModelDataTypeResolver.setConventionMap(conventionMap);
            codegenModelDataTypeResolver.setClassName(className);
            codegenModelDataTypeResolver.setEnums(enums);

            if (isContainerType(codegenProperty)) {
                codegenModelContainerDataTypeResolver.resolve(codegenProperty);
            } else {
                codegenModelDataTypeResolver.resolve(codegenProperty);
            }
        }

        return codegenModel;
    }

    private boolean isContainerType(CodegenProperty codegenProperty){
        for (EnumConstants.CsharpDataTypes dataType: EnumConstants.CsharpDataTypes.values()) {
            if (codegenProperty.dataType != null && codegenProperty.dataType.startsWith(dataType.getValue())) {
                return true;
            }
        }
        return false;
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
