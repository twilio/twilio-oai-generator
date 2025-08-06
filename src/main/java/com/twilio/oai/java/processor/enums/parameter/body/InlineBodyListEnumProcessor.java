package com.twilio.oai.java.processor.enums.parameter.body;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.nestedmodels.MustacheEnum;
import com.twilio.oai.java.processor.enums.parameter.ParameterEnumProcessor;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenParameter;

import java.util.List;
import java.util.Map;

import static com.twilio.oai.common.ApplicationConstants.DOT;
import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;
import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;

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
public class InlineBodyListEnumProcessor implements ParameterEnumProcessor {
    private final EnumConstants.OpenApiEnumType type = EnumConstants.OpenApiEnumType.FORM_PARAM_LIST_INLINE;
    @Override
    public boolean shouldProcess(CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema() != null) return false;
        if (codegenParameter.items != null && codegenParameter.items._enum != null && codegenParameter.isEnum) {
            return true;
        }
        return false;
    }

    @Override
    public void process(CodegenParameter codegenParameter) {
        if (!shouldProcess(codegenParameter)) return;
        type(codegenParameter);
        variableName(codegenParameter);
        datatype(codegenParameter);
        cacheEnumClass(codegenParameter);
    }

    @Override
    public EnumConstants.OpenApiEnumType getType() {
        return type;
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
        List<Map<String, Object>> enumValues = (List<Map<String, Object>>) codegenParameter.allowableValues.get("enumVars");

        MustacheEnum mustacheEnum = new MustacheEnum(enumClassName, enumValues);
        ResourceCache.addToEnumClasses(mustacheEnum);
    }
}
