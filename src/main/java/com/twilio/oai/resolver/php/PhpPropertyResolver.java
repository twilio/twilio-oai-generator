package com.twilio.oai.resolver.php;

import com.twilio.oai.StringHelper;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.LanguagePropertyResolver;

import org.openapitools.codegen.CodegenProperty;

import static com.twilio.oai.common.ApplicationConstants.ARRAY;
import static com.twilio.oai.common.ApplicationConstants.STRING;

public class PhpPropertyResolver extends LanguagePropertyResolver {
    public static final String MAP_STRING = "map";

    public PhpPropertyResolver(IConventionMapper mapper) {
        super(mapper);
    }

    @Override
    public CodegenProperty resolve(final CodegenProperty codegenProperty) {
        super.resolve(codegenProperty);

        codegenProperty.baseName = StringHelper.camelize(codegenProperty.baseName, true);

        return codegenProperty;
    }

    @Override
    public void resolveProperties(CodegenProperty codegenProperty) {
        super.resolveProperties(codegenProperty);
        if (codegenProperty.dataType.equals(LanguageConventionResolver.MIXED)) {
            codegenProperty.dataType = ARRAY;
        }
        if (codegenProperty.dataType.equalsIgnoreCase(LanguageConventionResolver.MIXED_ARRAY)) {
            codegenProperty.dataType = ARRAY + "[]";
        }
        if (codegenProperty.dataType.equals("float")) {
            codegenProperty.dataType = STRING;
        }
        if (codegenProperty.dataType.equals(LanguageConventionResolver.LIST_OBJECT)) {
            codegenProperty.dataType = ARRAY;
        }
        if (codegenProperty.dataType.contains("Enum") || codegenProperty.complexType != null) {
            if (codegenProperty.openApiType.equals(ARRAY)) {
                codegenProperty.dataType = STRING + "[]";
            } else {
                codegenProperty.dataType = STRING;
            }
        }
    }
}
