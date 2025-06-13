package com.twilio.oai.resolver.python;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguagePropertyResolver;
import org.openapitools.codegen.CodegenProperty;

public class PythonPropertyResolver extends LanguagePropertyResolver {
    private static final String LIST_OBJECT = "List<Object>";

    public PythonPropertyResolver(IConventionMapper mapper) {
        super(mapper);
    }

//    @Override
//    protected void handleAnyType(CodegenProperty codegenProperty, ApiResourceBuilder apiResourceBuilder) {
//        if((codegenProperty.dataType.equalsIgnoreCase(OBJECT) || codegenProperty.dataType.equals(LIST_OBJECT)) && codegenProperty.vendorExtensions.get("x-is-anytype") == null) {
//            String objectType = mapper.properties().getString(OBJECT).orElseThrow();
//
//            if (codegenProperty.isAnyType || (codegenProperty.isArray && codegenProperty.items.isAnyType)) {
//                objectType = "object";
//                codegenProperty.vendorExtensions.put("x-is-anytype", true);
//            }
//
//            else
//                codegenProperty.isFreeFormObject = true;
//
//            if (codegenProperty.dataType.equals(LIST_OBJECT)) {
//                codegenProperty.dataType = ApplicationConstants.LIST_START + objectType + ApplicationConstants.LIST_END;
//                codegenProperty.baseType = objectType;
//            } else {
//                codegenProperty.dataType = objectType;
//            }
//        }
//    }

    @Override
    protected void resolveProperties(CodegenProperty codegenProperty, ApiResourceBuilder apiResourceBuilder) {
        // update _form paramName to from_ for backward compatibility
        if (codegenProperty.name.equals("_from") && codegenProperty.dataFormat != null &&
                codegenProperty.dataFormat.equals(ApplicationConstants.PHONE_NUMBER)) {
            codegenProperty.name = "from_";
        }
        super.resolveProperties(codegenProperty, apiResourceBuilder);
    }
}
