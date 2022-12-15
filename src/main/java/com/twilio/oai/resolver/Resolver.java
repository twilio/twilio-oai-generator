package com.twilio.oai.resolver;

import java.util.Map;
import java.util.Optional;

import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

public abstract class Resolver<T extends IJsonSchemaValidationProperties> {
    public abstract T resolve(T arg);

    protected Optional<String> getMapperValue(final String key, Map<String, Object> properties) {
        if (properties.containsKey(key)) {
            return Optional.of((String) properties.get(key));
        }

        return Optional.empty();
    }

    protected String getDataType(final CodegenParameter codegenParameter) {
        if (codegenParameter.dataFormat != null) {
            if (codegenParameter.dataFormat.startsWith(LanguageConventionResolver.PREFIXED_COLLAPSIBLE_MAP)) {
                return LanguageConventionResolver.PREFIXED_COLLAPSIBLE_MAP;
            }

            return codegenParameter.dataFormat;
        }

        if (codegenParameter.isAnyType || codegenParameter.isFreeFormObject) {
            return "object";
        }

        if ("decimal".equalsIgnoreCase(codegenParameter.dataType)) {
            // TODO Fix after refactor.
            return null;
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
            return "object";
        }

        if ("decimal".equalsIgnoreCase(codegenProperty.dataType)) {
            // TODO Fix after refactor.
            return null;
        }

        return codegenProperty.openApiType;
    }
}
