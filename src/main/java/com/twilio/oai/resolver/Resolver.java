package com.twilio.oai.resolver;

import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import static com.twilio.oai.common.ApplicationConstants.OBJECT;

public abstract class Resolver<T extends IJsonSchemaValidationProperties> {
    public abstract T resolve(T arg);

    protected String getDataType(final CodegenParameter codegenParameter) {
        if (codegenParameter.dataFormat != null) {
            if (codegenParameter.dataFormat.startsWith(LanguageConventionResolver.PREFIXED_COLLAPSIBLE_MAP)) {
                return LanguageConventionResolver.PREFIXED_COLLAPSIBLE_MAP;
            }

            return codegenParameter.dataFormat;
        }

        if (codegenParameter.isAnyType || codegenParameter.isFreeFormObject) {
            return OBJECT;
        }

        return codegenParameter.dataType;
    }

    protected String getDataType(final CodegenProperty codegenProperty) {
        if (codegenProperty.dataFormat != null) {
            if (codegenProperty.dataFormat.startsWith(LanguageConventionResolver.PREFIXED_COLLAPSIBLE_MAP)) {
                return LanguageConventionResolver.PREFIXED_COLLAPSIBLE_MAP;
            }

            return codegenProperty.dataFormat;
        }

        if (codegenProperty.isAnyType || codegenProperty.isFreeFormObject) {
            return OBJECT;
        }

        return codegenProperty.openApiType;
    }
}
