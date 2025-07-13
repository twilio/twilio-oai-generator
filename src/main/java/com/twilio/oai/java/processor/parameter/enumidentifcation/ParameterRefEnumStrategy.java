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
        - name: singleParamRef
          in: header
          schema:
            $ref: '#/components/schemas/singleReusable'
 */
public class ParameterRefEnumStrategy implements EnumIdentificationStrategy {

    private final OpenApiEnumType type = OpenApiEnumType.PARAMETER_REF;

    @Override
    public boolean identify(final CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema() == null) return false;
        if (codegenParameter.getSchema().getRef() != null && codegenParameter.isEnumRef) {
            // Identify
            System.out.println("Identified as single parameter enum ref: " + codegenParameter.baseName);
            codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.PARAMETER_REF);

            /* Resolve
                X_NAME_NAME: variable name
                
            */
            codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
            String enumRefResolved = Utility.getEnumNameFromRef(codegenParameter.getSchema().getRef());
            codegenParameter.vendorExtensions.put(X_DATATYPE,
                    ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(enumRefResolved));
            return true;
        }
        return false;
    }

    @Override
    public OpenApiEnumType getType() {
        return type;
    }
}
