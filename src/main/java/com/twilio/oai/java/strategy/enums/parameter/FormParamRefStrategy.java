package com.twilio.oai.java.strategy.enums.parameter;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.nestedmodels.MustacheEnum;
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
public class FormParamRefStrategy implements ParameterEnumProcessingStrategy {

    private final OpenApiEnumType type = OpenApiEnumType.FORM_PARAM_REF;

    @Override
    public boolean process(final CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema() != null) return false;
        if (!codegenParameter.isEnum && codegenParameter.isEnumRef) {
            type(codegenParameter);
            variableName(codegenParameter);
            datatype(codegenParameter);
            cacheEnumClass(codegenParameter);
            return true;
        }
        return false;
    }

    @Override
    public OpenApiEnumType getType() {
        return type;
    }

    @Override
    public boolean isStrategyApplicable(CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema() != null) return false;
        if (!codegenParameter.isEnum && codegenParameter.isEnumRef) {
            return true;
        }
        return false;
    }

    private void type(CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.FORM_PARAM_REF);
    }
    private void variableName(CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
    }
    private void datatype(CodegenParameter codegenParameter) {
        String enumDatatypeResolved = StringUtils.toPascalCase(Utility.getEnumNameFromDefaultDatatype(codegenParameter.dataType));
        codegenParameter.vendorExtensions.put(X_DATATYPE, ResourceCache.getResourceName() + DOT + enumDatatypeResolved);
    }

    private void cacheEnumClass(CodegenParameter codegenParameter) {
        // TODO: Best way to store = codegenParameter.allowableValues.get("enumVars")
        String enumClassName = StringUtils.toPascalCase(Utility.getEnumNameFromDefaultDatatype(codegenParameter.dataType));
        MustacheEnum mustacheEnum = new MustacheEnum(enumClassName, codegenParameter.allowableValues);
        ResourceCache.addToEnumClasses(mustacheEnum);
    }
}
