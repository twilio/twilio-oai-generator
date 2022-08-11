package com.twilio.oai.common;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;
import java.util.Map;

public abstract class Resolver {

    public abstract void setModelFormatMap(Map<String, String> modelFormatMap);
    public abstract CodegenParameter resolveParameter(CodegenParameter parameter);
    public abstract List<CodegenParameter> resolveParameter(List<CodegenParameter> parameters);
    public abstract CodegenModel resolveParameter(CodegenModel codegenModel);
}
