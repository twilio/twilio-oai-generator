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
                # PROPERTY_ARRAY
                singleBodyArray:
                  type: array
                  items:
                    type: string
                    enum: [ new, sale, featured ]
                  description: An array of enum values in the request body
                  example: [ new, featured ]
 */
public class FormParamListStrategy implements ParameterEnumProcessingStrategy {

    private final OpenApiEnumType type = OpenApiEnumType.FORM_PARAM_LIST_INLINE;

    @Override
    public boolean process(final CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema() != null) return false;
        if (codegenParameter.items != null && codegenParameter.items._enum != null && codegenParameter.isEnum) {
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
        if (codegenParameter.items != null && codegenParameter.items._enum != null && codegenParameter.isEnum) {
            return true;
        }
        return false;
    }

    private void type(CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.FORM_PARAM_INLINE);
    }
    private void variableName(CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
    }
    private void datatype(CodegenParameter codegenParameter) {
        // codegenParameter.dataType = List<String>
        // enumExistingDatatype = String
        String enumExistingDatatype = Utility.extractDatatypeFromContainer(codegenParameter.dataType);
        String enumClassName = StringUtils.toPascalCase(codegenParameter.baseName);
        String enumNonContainerDatatype = ResourceCache.getResourceName() + DOT + enumClassName;
        String resolvedDataType = Utility.replaceDatatypeInContainer(codegenParameter.dataType, enumNonContainerDatatype);
        codegenParameter.vendorExtensions.put(X_DATATYPE, resolvedDataType);
    }

    private void cacheEnumClass(CodegenParameter codegenParameter) {
        String enumClassName = StringUtils.toPascalCase(codegenParameter.baseName);
        MustacheEnum mustacheEnum = new MustacheEnum(enumClassName, codegenParameter.allowableValues);
        ResourceCache.addToEnumClasses(mustacheEnum);
    }
}
