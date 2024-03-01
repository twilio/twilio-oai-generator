package com.twilio.oai.resolver.common;

import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public abstract class CodegenConventionResolver {
    public abstract CodegenParameter resolveEnumParameter(CodegenParameter parameter, String resourceName);

    public abstract CodegenProperty resolveEnumProperty(CodegenProperty property, String resourceName);
}

