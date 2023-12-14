package com.twilio.oai.resolver.python;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageParamResolver;

import com.twilio.oai.resolver.common.CodegenModelResolver;
import org.openapitools.codegen.CodegenParameter;


import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_VEND_EXT;

public class PythonParameterResolver extends LanguageParamResolver {
  public PythonParameterResolver(final IConventionMapper mapper, final CodegenModelResolver codegenModelResolver) {
    super(mapper, codegenModelResolver);
  }

  @Override
  public CodegenParameter resolve(final CodegenParameter codegenParameter, ApiResourceBuilder apiResourceBuilder) {
    super.resolve(codegenParameter, apiResourceBuilder);

    if (codegenParameter.isArray) {
      codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT, "serialize.map");
    }
    return codegenParameter;
  }
}
