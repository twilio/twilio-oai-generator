package com.twilio.oai.java.processor.parameter.enumidentifcation;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenParameter;

import static com.twilio.oai.common.ApplicationConstants.DOT;
import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;
import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;

/*
  - name: singleParam
    in: query
    description: A single enum value as a query parameter
    required: false
    schema:
      type: string
      enum:
        - asc
        - desc
      example: asc
 */
public class ParameterSingleEnumStrategy implements EnumIdentificationStrategy {

    private final OpenApiEnumType type = OpenApiEnumType.PARAMETER_SINGLE;

    @Override
    public boolean identify(final CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema() == null) return false;
        if (codegenParameter.getSchema()._enum != null && codegenParameter.isEnum) {
            System.out.println("Identified as single parameter enum: " + codegenParameter.baseName);
            codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.PARAMETER_SINGLE);

            // Resolve
            codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
            codegenParameter.vendorExtensions.put(X_DATATYPE,
                    ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(codegenParameter.baseName));
            return true;
        }
        return false;
    }

    @Override
    public OpenApiEnumType getType() {
        return type;
    }
}
