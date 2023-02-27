package com.twilio.oai.resolver.python;

import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageParamResolver;

import org.openapitools.codegen.CodegenParameter;

import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_VEND_EXT;

public class PythonParameterResolver extends LanguageParamResolver {
  public PythonParameterResolver(final IConventionMapper mapper) {
    super(mapper);
  }

  @Override
  public CodegenParameter resolve(final CodegenParameter codegenParameter) {
    super.resolve(codegenParameter);

    if (codegenParameter.isArray) {
      codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT, "serialize.map");
    }
    return codegenParameter;
  }
}
