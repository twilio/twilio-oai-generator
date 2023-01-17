package com.twilio.oai.resolver.csharp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.EnumsResolver;
import com.twilio.oai.common.ApplicationConstants;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CSharpResolver  {
    private final CodegenModelResolver codegenModelResolver;
    private final CodegenParameterResolver codegenParameterResolver;
    private final EnumsResolver enumsResolver;
    private final Set<String> enumsDict;
    private final Map<String, Map<String, Object>> conventionMap;

    Map<String, IJsonSchemaValidationProperties> enums;

    public CSharpResolver() {
        conventionMap = getConventionalMap();
        enumsResolver = new EnumsResolver(conventionMap);
        enumsDict = enumsResolver.getEnumsDict();
        codegenModelResolver = new CodegenModelResolver();
        codegenParameterResolver = new CodegenParameterResolver();
        codegenModelResolver.setEnumsDict(enumsDict);
        codegenParameterResolver.setEnumsDict(enumsDict);
    }

    public void resolve(CodegenParameter parameter){
        codegenParameterResolver.resolve(parameter);
    }

    public void resolve(CodegenModel codegenModel){
        codegenModelResolver.resolve(codegenModel);
    }

    public void resolve(List<CodegenParameter> parameters){
        codegenParameterResolver.resolveParameters(parameters);
    }


    public void setClassName(String className) {
        codegenParameterResolver.setClassName(className);
        codegenModelResolver.setClassName(className);
    }

    public void setEnums(Map<String, IJsonSchemaValidationProperties> enums) {
        this.enums = enums;
        codegenModelResolver.setEnums(enums);
        codegenParameterResolver.setEnums(enums);
    }

    public Map<String, IJsonSchemaValidationProperties> getEnums() {
        return enums;
    }

    public void setHasEnumsInResource(boolean hasEnumsInResource) {
        codegenModelResolver.setHasEnumsInResource(hasEnumsInResource);
        codegenParameterResolver.setHasEnumsInResource(hasEnumsInResource);
    }

    public boolean isHasEnumsInOptions() {
        return codegenParameterResolver.isHasEnumsInOptions() || codegenModelResolver.isHasEnumsInOptions();
    }

    public void setHasEnumsInOptions(boolean hasEnumsInOptions) {
        codegenModelResolver.setHasEnumsInOptions(hasEnumsInOptions);
        codegenParameterResolver.setHasEnumsInOptions(hasEnumsInOptions);
    }
    public static Map<String, Map<String, Object>> getConventionalMap() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(ApplicationConstants.CONFIG_CSHARP_JSON_PATH), new TypeReference<Map<String, Map<String, Object>>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isHasEnumsInResource() {
        return codegenParameterResolver.isHasEnumsInResource() || codegenModelResolver.isHasEnumsInResource();
    }

    public void setModelFormatMap(Map<String, String> modelFormatMap) {
        codegenModelResolver.setModelFormatMap(modelFormatMap);
    }
}
