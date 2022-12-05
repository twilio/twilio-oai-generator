package com.twilio.oai.resolver.php;

import com.twilio.oai.StringHelper;
import com.twilio.oai.resolver.LanguageParamResolver;
import com.twilio.oai.resolver.Resolver;
import org.openapitools.codegen.CodegenParameter;

import static com.twilio.oai.resolver.php.PhpPropertyResolver.MAP_STRING;

public class PhpParameterResolver extends LanguageParamResolver {
    public final static String SERALIZE_ARRAY_MAP = "Serialize::map";

    public PhpParameterResolver(IConventionMapper mapper) {
        super(mapper);
    }

    @Override
    public void resolveProperties(CodegenParameter codegenParameter) {
        super.resolveProperties(codegenParameter);
        if (codegenParameter.dataType.equalsIgnoreCase(LanguageConventionResolver.MIXED)) {
            codegenParameter.dataType = "array";
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
        }
    }
}
