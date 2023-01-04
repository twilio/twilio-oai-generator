package com.twilio.oai.resolver.php;

import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.LanguageParamResolver;

import org.openapitools.codegen.CodegenParameter;

import static com.twilio.oai.common.ApplicationConstants.ARRAY;
import static com.twilio.oai.common.ApplicationConstants.OBJECT;
import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_VEND_EXT;
import static com.twilio.oai.common.ApplicationConstants.STRING;
import static com.twilio.oai.resolver.php.PhpPropertyResolver.MAP_STRING;

public class PhpParameterResolver extends LanguageParamResolver {
    public static final String SERIALIZE_ARRAY_MAP = "Serialize::map";
    public static final String SERIALIZE_ARRAY_JSON_OBJECT = "Serialize::jsonObject";
    public static final String ARRAY_OF_ARRAY_STRING = "array-of-array";
    public static final String FLOAT = "float";

    public PhpParameterResolver(IConventionMapper mapper) {
        super(mapper);
    }

    @Override
    public void resolveProperties(CodegenParameter codegenParameter) {
        super.resolveProperties(codegenParameter);
        if (codegenParameter.dataType.equalsIgnoreCase(LanguageConventionResolver.MIXED)) {
            codegenParameter.dataType = ARRAY;
            codegenParameter.isArray = true;
        }
        if (codegenParameter.dataType.equalsIgnoreCase(LanguageConventionResolver.MIXED_ARRAY)) {
            codegenParameter.dataType = ARRAY + "[]";
            codegenParameter.isArray = true;
        }
        if (codegenParameter.dataType.equalsIgnoreCase(OBJECT) ||
            codegenParameter.dataType.equals(LanguageConventionResolver.LIST_OBJECT)) {
            codegenParameter.dataType = ARRAY;
        }
        if (codegenParameter.dataType.contains("Enum") || codegenParameter.dataType == FLOAT) {
            codegenParameter.dataType = STRING;
        }
    }

    @Override
    public void resolveSerialize(CodegenParameter codegenParameter) {
        super.resolveSerialize(codegenParameter);
        if (codegenParameter.dataType != null && codegenParameter.dataType.contains("[]")) {
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT, SERIALIZE_ARRAY_MAP);
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT + LanguageConventionResolver.HYPHEN + MAP_STRING, true);
            if (codegenParameter.dataType.contains(ARRAY)) {
                codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT + LanguageConventionResolver.HYPHEN + ARRAY_OF_ARRAY_STRING, SERIALIZE_ARRAY_JSON_OBJECT);
            }
        }
        if (codegenParameter.dataType != null && codegenParameter.dataType.equals(ARRAY)) {
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT, SERIALIZE_ARRAY_JSON_OBJECT);
        }
    }
}
