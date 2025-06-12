package com.twilio.oai.resolver.python;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageParamResolver;

import com.twilio.oai.resolver.common.CodegenModelResolver;
import org.openapitools.codegen.CodegenParameter;


import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_VEND_EXT;

public class PythonParameterResolver extends LanguageParamResolver {
  public static final String OBJECT = "object";
  private static final String LIST_OBJECT = "List<Object>";

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

//  @Override
//  protected void handleAnyType(CodegenParameter codegenParameter, ApiResourceBuilder apiResourceBuilder) {
//    if((codegenParameter.dataType.equalsIgnoreCase(OBJECT) || codegenParameter.dataType.equals(LIST_OBJECT)) && codegenParameter.vendorExtensions.get("x-is-anytype") == null) {
//      String objectType = mapper.properties().getString(OBJECT).orElseThrow();
//
//      if (codegenParameter.isAnyType || (codegenParameter.isArray && codegenParameter.items.isAnyType)) {
//        objectType = "object";
//        codegenParameter.vendorExtensions.put("x-is-anytype", true);
//      }
//
//      else
//        codegenParameter.isFreeFormObject = true;
//
//      if (codegenParameter.dataType.equals(LIST_OBJECT)) {
//        codegenParameter.dataType = ApplicationConstants.LIST_START + objectType + ApplicationConstants.LIST_END;
//        codegenParameter.baseType = objectType;
//      } else {
//        codegenParameter.dataType = objectType;
//      }
//    }
//  }
}
