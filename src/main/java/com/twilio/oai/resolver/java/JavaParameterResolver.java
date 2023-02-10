package com.twilio.oai.resolver.java;

import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.LanguageParamResolver;
import com.twilio.oai.resolver.common.CodegenParameterResolver;
import com.twilio.oai.resolver.IConventionMapper;
import org.openapitools.codegen.CodegenParameter;

import java.util.Arrays;
import java.util.HashMap;

public class JavaParameterResolver extends LanguageParamResolver {
    public static final String OBJECT = "object";
    private static final String LIST_OBJECT = "List<Object>";
     public static final String X_IS_PHONE_NUMBER_FORMAT = "x-is-phone-number-format";
     public static final String PHONE_NUMBER_FORMAT = "phone-number";

     private final CodegenParameterResolver codegenParameterResolver;
    public JavaParameterResolver(IConventionMapper mapper) {
        super(mapper);
        codegenParameterResolver = new CodegenParameterResolver(mapper, Arrays.asList(EnumConstants.JavaDataTypes.values()));
    }

    @Override
    public void resolveProperties(CodegenParameter parameter) {
        if(parameter.dataType.equalsIgnoreCase(OBJECT) || parameter.dataType.equals(LIST_OBJECT)) {
            final String objectType = mapper.properties().getString(OBJECT).orElseThrow();

            if (parameter.dataType.equals(LIST_OBJECT)) {
                parameter.dataType = ApplicationConstants.LIST_START + objectType + ApplicationConstants.LIST_END;
                parameter.baseType = objectType;
            } else {
                parameter.dataType = objectType;
            }

            parameter.isFreeFormObject = true;
            parameter.isString = false;
        }

        mapper.promotions().getMap(parameter.dataFormat).ifPresent(promotions -> {
            // cloning to prevent update in source map
            HashMap<String, String> promotionsMap = new HashMap<>(promotions);
            promotionsMap.replaceAll((dataType, value) -> value.replaceAll("\\{.*}", parameter.paramName) );
            parameter.vendorExtensions.put(ApplicationConstants.PROMOTION_EXTENSION_NAME, promotionsMap);
        });

        codegenParameterResolver.resolve(parameter);

        if( PHONE_NUMBER_FORMAT.equals(parameter.dataFormat)) {
            parameter.vendorExtensions.put(X_IS_PHONE_NUMBER_FORMAT, true);
        }
        // prevent special format properties to be considered as enum
        mapper.properties().get(parameter.dataFormat).ifPresent(prop -> {
            parameter.isEnum = false;
            parameter.allowableValues = null;
        });
        parameter.paramName = StringHelper.toFirstLetterLower(parameter.paramName);
    }
}
