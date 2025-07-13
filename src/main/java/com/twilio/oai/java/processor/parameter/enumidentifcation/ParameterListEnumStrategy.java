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
  Example:
        - name: arrayParam
          in: query
          description: Order items using an array of enums
          required: false
          schema:
            type: array
            items:
              type: string
              enum:
                - asc
                - desc
            example: [asc]
 */
public class ParameterListEnumStrategy implements EnumIdentificationStrategy {

    private final OpenApiEnumType type = OpenApiEnumType.PARAMETER_LIST;

    @Override
    public boolean identify(final CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema() == null) return false;
        if (codegenParameter.getSchema()._enum == null &&
                codegenParameter.getSchema().items != null && codegenParameter.getSchema().items._enum != null) {
            // Identify
            System.out.println("Identified as array parameter enum: " + codegenParameter.baseName);
            codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.PARAMETER_LIST);


            // Resolve
            codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));

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
