package com.twilio.oai.resolver.csharp;

import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.Resolver;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

@Setter
public class CodegenParameterResolver extends Resolver<CodegenParameter> {
    private final Map<String, Map<String, Object>> conventionMap;
    private String className;
    private boolean hasEnumsInResource = false;
    private boolean hasEnumsInOptions = false;

    private Set<String> enumsDict;
    private final CodegenParameterDataTypeResolver codegenParameterDataTypeResolver = new CodegenParameterDataTypeResolver();

    private final CodegenParameterContainerDataTypeResolver codegenParameterContainerDataTypeResolver = new CodegenParameterContainerDataTypeResolver(codegenParameterDataTypeResolver);
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

    public void resolveParameters(List<CodegenParameter> parameters) {
        parameters.forEach(this::resolve);
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

    public boolean isHasEnumsInResource() {
        return hasEnumsInResource;
    }

    public boolean isHasEnumsInOptions() {
        return hasEnumsInOptions;
    }
}
