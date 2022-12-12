package com.twilio.oai.resolver.php;

import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.LanguagePropertyResolver;
import org.openapitools.codegen.CodegenProperty;


import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_VEND_EXT;
import static com.twilio.oai.resolver.php.PhpParameterResolver.SERALIZE_ARRAY_MAP;

public class PhpPropertyResolver extends LanguagePropertyResolver {
    public final static String MAP_STRING = "map";

    public PhpPropertyResolver(IConventionMapper mapper) {
        super(mapper);
    }

    @Override
    public  void resolveProperties(CodegenProperty codegenProperty) {
        super.resolveProperties(codegenProperty);
        if(codegenProperty.dataType.equals(LanguageConventionResolver.MIXED)) {
            codegenProperty.dataType = "array";
        }
        if (codegenProperty.dataType.equalsIgnoreCase(LanguageConventionResolver.MIXED_ARRAY)) {
            codegenProperty.dataType = "array[]";
        }
        if (codegenProperty.dataType.equals("float")) {
            codegenProperty.dataType = "string";
        }
        if(codegenProperty.dataType.equals(LanguageConventionResolver.LIST_OBJECT)) {
            codegenProperty.dataType = "array";
        }
        if (codegenProperty.dataType.contains("Enum") || codegenProperty.complexType != null) {
            if(codegenProperty.openApiType.equals("array")) {
                codegenProperty.dataType = "string[]";
            } else {
                codegenProperty.dataType = "string";
            }
        }
    }

}
