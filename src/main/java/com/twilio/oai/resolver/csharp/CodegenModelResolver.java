package com.twilio.oai.resolver.csharp;

import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.Resolver;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

@Setter
public class CodegenModelResolver extends Resolver<CodegenModel> {
    private final Map<String, Map<String, Object>> conventionMap;
    private String className;
    private boolean hasEnumsInResource = false;
    private boolean hasEnumsInOptions = false;

    private Set<String> enumsDict;
    private Map<String, IJsonSchemaValidationProperties> enums;
    private Map<String, String> modelFormatMap;
    private final CodegenModelDataTypeResolver codegenModelDataTypeResolver = new CodegenModelDataTypeResolver();
    private final CodegenModelContainerDataTypeResolver codegenModelContainerDataTypeResolver = new CodegenModelContainerDataTypeResolver(codegenModelDataTypeResolver);
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

            if(codegenProperty.isEnum && StringHelper.existInSetIgnoreCase(codegenProperty.dataType, enumsDict)){
                hasEnumsInResource = true;
            }

            codegenModelDataTypeResolver.setModelFormatMap(modelFormatMap);
            codegenModelDataTypeResolver.setConventionMap(conventionMap);
            codegenModelDataTypeResolver.setClassName(className);
            codegenModelDataTypeResolver.setEnums(enums);
            codegenModelDataTypeResolver.setEnumsDict(enumsDict);

            if (isContainerType(codegenProperty)) {
                codegenModelContainerDataTypeResolver.resolve(codegenProperty);
            } else {
                codegenModelDataTypeResolver.resolve(codegenProperty);
            }

            if(codegenProperty.vendorExtensions.containsKey("x-has-enum-params")){
                hasEnumsInResource = true;
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

    public Map<String, IJsonSchemaValidationProperties> getEnums() {
        return enums;
    }

    public boolean isHasEnumsInOptions() {
        return hasEnumsInOptions;
    }
    public boolean isHasEnumsInResource() {
        return hasEnumsInResource;
    }
}
