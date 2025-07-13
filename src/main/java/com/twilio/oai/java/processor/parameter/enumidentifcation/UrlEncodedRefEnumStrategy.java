package com.twilio.oai.java.processor.parameter.enumidentifcation;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenParameter;

import static com.twilio.oai.common.ApplicationConstants.DOT;
import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;
import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;

/*
          application/x-www-form-urlencoded:
            schema:
              type: object
              properties:
                # REUSABLE_SINGLE
                singleBodyRef:
                  $ref: '#/components/schemas/singleReusable'
                  description: A reusable single-value enum in the request body
 */
public class UrlEncodedRefEnumStrategy implements EnumIdentificationStrategy {

    private final OpenApiEnumType type = OpenApiEnumType.URL_ENCODED_BODY_REF;

    @Override
    public boolean identify(final CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema() != null) return false;
        if (!codegenParameter.isEnum && codegenParameter.isEnumRef) {
            codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.URL_ENCODED_BODY_REF);
            codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
            String enumDatatypeResolved = StringUtils.toPascalCase(Utility.getEnumNameFromDefaultDatatype(codegenParameter.dataType));
            codegenParameter.vendorExtensions.put(X_DATATYPE, ResourceCache.getResourceName() + DOT + enumDatatypeResolved);
            return true;
        }
        return false;
    }

    @Override
    public OpenApiEnumType getType() {
        return type;
    }
}
