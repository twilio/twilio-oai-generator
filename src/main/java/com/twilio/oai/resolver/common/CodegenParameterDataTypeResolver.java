package com.twilio.oai.resolver.common;

import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.Resolver;

import org.openapitools.codegen.CodegenParameter;

public class CodegenParameterDataTypeResolver extends Resolver<CodegenParameter> {

    private IConventionMapper mapper;

    public CodegenParameter resolve(CodegenParameter parameter) {
        assignDataType(parameter);
        return parameter;
    }

    protected void assignDataType(CodegenParameter parameter) {
        mapper
            .properties()
            .getString(parameter.dataFormat)
            .or(() -> mapper.properties().getString(parameter.dataType))
            .ifPresent(dataType -> parameter.dataType = dataType);
    }

    public void setMapper(IConventionMapper mapper) {
        this.mapper = mapper;
    }
}
