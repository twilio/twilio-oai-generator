package com.twilio.oai.resolver.csharp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.EnumsResolver;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.Resolver;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CodegenParameterResolver implements Resolver<CodegenParameter> {
    private final Map<String, Map<String, Object>> conventionMap;
    private String className;
    private boolean hasEnumsInResource = false;
    private boolean hasEnumsInOptions = false;
    private EnumsResolver enumsResolver;

    private HashSet<String> enumsDict;
    private CodegenParameterDataTypeResolver codegenParameterDataTypeResolver = new CodegenParameterDataTypeResolver();

    private CodegenParameterContainerDataTypeResolver codegenParameterContainerDataTypeResolver = new CodegenParameterContainerDataTypeResolver(codegenParameterDataTypeResolver);
    private Map<String, IJsonSchemaValidationProperties> enums;
    public CodegenParameterResolver() {
        conventionMap = getConventionalMap();
    }

    public CodegenParameter resolve(CodegenParameter parameter){
        if (parameter == null) {
            return null;
        }

        if(parameter.isEnum  && StringHelper.existInSetIgnoreCase(parameter.dataType, enumsDict)){
            hasEnumsInOptions = true;
        }

        codegenParameterDataTypeResolver.setEnums(enums);
        codegenParameterDataTypeResolver.setClassName(className);
        codegenParameterDataTypeResolver.setConventionMap(conventionMap);
        codegenParameterDataTypeResolver.setEnumsDict(enumsDict);

        if (isContainerType(parameter)) {
            codegenParameterContainerDataTypeResolver.resolve(parameter);
        } else {
            codegenParameterDataTypeResolver.resolve(parameter);
        }
        if(parameter.vendorExtensions.containsKey("x-has-enum-params")){
            hasEnumsInOptions = true;
        }
        return parameter;
    }

    private boolean isContainerType(CodegenParameter parameter){
        for (EnumConstants.CsharpDataTypes dataType: EnumConstants.CsharpDataTypes.values()) {
            if (parameter.dataType != null && parameter.dataType.startsWith(dataType.getValue())) {
                return true;
            }
        }
        return false;
    }

    public List<CodegenParameter> resolveParameters(List<CodegenParameter> parameters) {
        for (CodegenParameter parameter: parameters) {
            // Adding reserved keyword for backward compatibility
            if (ApplicationConstants._CONFIGURATION.equals(parameter.paramName)) {
                parameter.paramName = "Configuration";
            }
            resolve(parameter);
        }
        return parameters;
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

    public boolean isHasEnumsInResource() {
        return hasEnumsInResource;
    }

    public void setHasEnumsInResource(boolean hasEnumsInResource) {
        this.hasEnumsInResource = hasEnumsInResource;
    }

    public boolean isHasEnumsInOptions() {
        return hasEnumsInOptions;
    }

    public void setHasEnumsInOptions(boolean hasEnumsInOptions) {
        this.hasEnumsInOptions = hasEnumsInOptions;
    }

    public void setEnumsDict(HashSet<String> enumsDict) {
        this.enumsDict = enumsDict;
    }

}
