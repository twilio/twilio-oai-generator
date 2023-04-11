package com.twilio.oai.resolver.php;

import com.twilio.oai.StringHelper;
import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.LanguagePropertyResolver;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;

import static com.twilio.oai.common.ApplicationConstants.ARRAY;
import static com.twilio.oai.common.ApplicationConstants.STRING;
import static com.twilio.oai.resolver.php.PhpParameterResolver.FLOAT;

public class PhpPropertyResolver extends LanguagePropertyResolver {
    public static final String MAP_STRING = "map";
    public static final String OPEN_API_STRING = "OpenAPI";

    public PhpPropertyResolver(IConventionMapper mapper) {
        super(mapper);
    }

    @Override
    public CodegenProperty resolve(final CodegenProperty codegenProperty, ApiResourceBuilder apiResourceBuilder) {
        super.resolve(codegenProperty, apiResourceBuilder);

        codegenProperty.baseName = StringHelper.camelize(codegenProperty.baseName, true);

        return codegenProperty;
    }

    @Override
    public void resolveProperties(CodegenProperty codegenProperty, ApiResourceBuilder apiResourceBuilder) {
        super.resolveProperties(codegenProperty, apiResourceBuilder);
        if (codegenProperty.dataType.equals(LanguageConventionResolver.MIXED)) {
            codegenProperty.dataType = ARRAY;
            codegenProperty.isArray = true;
        }
        if (codegenProperty.dataType.equalsIgnoreCase(LanguageConventionResolver.MIXED_ARRAY)) {
            codegenProperty.dataType = ARRAY + "[]";
            codegenProperty.isArray = true;
        }
        if (codegenProperty.dataType.equals(FLOAT)) {
            codegenProperty.dataType = STRING;
        }
        if (codegenProperty.dataType.equals(LanguageConventionResolver.LIST_OBJECT)) {
            codegenProperty.dataType = ARRAY;
        }
        if (codegenProperty.dataType.contains("Enum") || codegenProperty.complexType != null) {
            if (codegenProperty.openApiType.equals(ARRAY)) {
                codegenProperty.dataType = STRING + "[]";
            } else if (codegenProperty.openApiType.equals(STRING) || codegenProperty.dataType.contains(OPEN_API_STRING)) {
                codegenProperty.dataType = STRING;
            }
        }
        if(codegenProperty.isNullable && !codegenProperty.required && !codegenProperty.dataType.contains("|null")){
            codegenProperty.dataType = codegenProperty.dataType + "|null";
        }
    }
}
