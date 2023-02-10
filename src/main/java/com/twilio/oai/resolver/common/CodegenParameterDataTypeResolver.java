package com.twilio.oai.resolver.common;

import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.Resolver;

import lombok.Setter;
import org.openapitools.codegen.CodegenParameter;

public class CodegenParameterDataTypeResolver extends Resolver<CodegenParameter> {
    @Setter
    private IConventionMapper mapper;

    public CodegenParameter resolve(CodegenParameter parameter) {
        assignDataType(parameter);
        return parameter;
    }

    private void assignDataType(CodegenParameter parameter) {
        mapper
            .properties()
            .getString(parameter.dataFormat)
            .or(() -> mapper.properties().getString(parameter.dataType))
            .ifPresent(dataType -> {parameter.dataType = dataType; parameter.isString = false;});
    }
}
