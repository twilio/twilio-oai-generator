package com.twilio.oai.resolver.php;

import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.LanguageParamResolver;
import org.openapitools.codegen.CodegenParameter;

import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_VEND_EXT;
import static com.twilio.oai.resolver.php.PhpPropertyResolver.MAP_STRING;

public class PhpParameterResolver extends LanguageParamResolver {
    public final static String SERALIZE_ARRAY_MAP = "Serialize::map";
    public final static String SERALIZE_ARRAY_JSON_OBJECT = "Serialize::jsonObject";
    public final static String ARRAY_OF_ARRAY_STRING = "array-of-array";
    public PhpParameterResolver(IConventionMapper mapper) {
        super(mapper);
    }

    @Override
    public void resolveProperties(CodegenParameter codegenParameter) {
        super.resolveProperties(codegenParameter);
        if (codegenParameter.dataType.equalsIgnoreCase(LanguageConventionResolver.MIXED)) {
            codegenParameter.dataType = "array";
        }
        if (codegenParameter.dataType.equalsIgnoreCase(LanguageConventionResolver.MIXED_ARRAY)) {
            codegenParameter.dataType = "array[]";
        }
        if (codegenParameter.dataType.equalsIgnoreCase(LanguageConventionResolver.OBJECT) ||
                codegenParameter.dataType.equals(LanguageConventionResolver.LIST_OBJECT)) {
            codegenParameter.dataType = "array";
        }
        if (codegenParameter.dataType.contains("Enum")) {
            codegenParameter.dataType = "string";
        }
    }

    @Override
    public void resolveSerialize(CodegenParameter codegenParameter) {
        super.resolveSerialize(codegenParameter);
        if (codegenParameter.dataType != null && codegenParameter.dataType.contains("[]")) {
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT, SERALIZE_ARRAY_MAP);
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT + LanguageConventionResolver.HYPHEN + MAP_STRING, true);
            if(codegenParameter.dataType.contains("array")) {
                codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT + LanguageConventionResolver.HYPHEN + ARRAY_OF_ARRAY_STRING, SERALIZE_ARRAY_JSON_OBJECT);
            }
        }
        if (codegenParameter.dataType != null && codegenParameter.dataType.equals("array")) {
            codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT, SERALIZE_ARRAY_JSON_OBJECT);
        }
    }
}
