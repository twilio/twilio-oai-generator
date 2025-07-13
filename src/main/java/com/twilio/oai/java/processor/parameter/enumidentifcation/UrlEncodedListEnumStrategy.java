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
                # PROPERTY_ARRAY
                singleBodyArray:
                  type: array
                  items:
                    type: string
                    enum: [ new, sale, featured ]
                  description: An array of enum values in the request body
                  example: [ new, featured ]
 */
public class UrlEncodedListEnumStrategy implements EnumIdentificationStrategy {

    private final OpenApiEnumType type = OpenApiEnumType.URL_ENCODED_BODY_LIST;

    @Override
    public boolean identify(final CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema() != null) return false;
        if (codegenParameter.items != null && codegenParameter.items._enum != null && codegenParameter.isEnum) {
            System.out.println("Identified as single url encoded body enum: " + codegenParameter.baseName);
            codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.URL_ENCODED_BODY_SINGLE);

            // Resolve
            codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));

            // codegenParameter.dataType = List<String>
            // enumExistingDatatype = String
            String enumExistingDatatype = Utility.extractDatatypeFromContainer(codegenParameter.dataType);
            String enumClassName = StringUtils.toPascalCase(codegenParameter.baseName);
            String enumNonContainerDatatype = ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(enumClassName);
            String resolvedDataType = Utility.replaceDatatypeInContainer(codegenParameter.dataType, enumNonContainerDatatype);
            codegenParameter.vendorExtensions.put(X_DATATYPE, resolvedDataType);
            return true;
        }
        return false;
    }

    @Override
    public OpenApiEnumType getType() {
        return type;
    }
}
