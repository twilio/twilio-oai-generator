package com.twilio.oai.resolver;

import com.twilio.oai.common.ApplicationConstants;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import static com.twilio.oai.common.ApplicationConstants.OBJECT;

public abstract class Resolver<T extends IJsonSchemaValidationProperties> {
    public abstract T resolve(T arg);

    protected String getDataType(final CodegenParameter codegenParameter) {
        if (codegenParameter.dataFormat != null) {
            if (codegenParameter.dataFormat.startsWith(ApplicationConstants.PREFIXED_COLLAPSIBLE_MAP)) {
                return ApplicationConstants.PREFIXED_COLLAPSIBLE_MAP;
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
            if (codegenProperty.dataFormat.startsWith(ApplicationConstants.PREFIXED_COLLAPSIBLE_MAP)) {
                return ApplicationConstants.PREFIXED_COLLAPSIBLE_MAP;
            }

            return codegenProperty.dataFormat;
        }

        if (codegenProperty.isAnyType || codegenProperty.isFreeFormObject) {
            return OBJECT;
        }

        return codegenProperty.openApiType;
    }
}
