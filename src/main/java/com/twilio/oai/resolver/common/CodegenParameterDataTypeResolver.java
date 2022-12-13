package com.twilio.oai.resolver.common;

import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.IConventionMapper;
import org.openapitools.codegen.CodegenParameter;

public class CodegenParameterDataTypeResolver extends Resolver<CodegenParameter> {

    private IConventionMapper mapper;

    public CodegenParameter resolve(CodegenParameter parameter) {
        assignDataType(parameter);
        return parameter;
    }

    private void assignDataType(CodegenParameter parameter){
        if (mapper.properties().containsKey(parameter.dataFormat)) {
            parameter.dataType = (String) mapper.properties().get(parameter.dataFormat);
        } else if (mapper.properties().containsKey(parameter.dataType)) {
            parameter.dataType = (String) mapper.properties().get(parameter.dataType);
        }
    }

    public void setMapper(IConventionMapper mapper) {
        this.mapper = mapper;
    }
}
