package com.twilio.oai.java.processor.enums.parameter.body;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.cache.ResourceCacheContext;
import com.twilio.oai.java.nestedmodels.MustacheEnum;
import com.twilio.oai.java.processor.enums.parameter.ParameterEnumProcessor;
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
          # REUSABLE_SINGLE
          singleBodyRef:
            $ref: '#/components/schemas/singleReusable'
            description: A reusable single-value enum in the request body
 */
public class ReusableBodyEnumProcessor implements ParameterEnumProcessor {
    private final EnumConstants.OpenApiEnumType type = EnumConstants.OpenApiEnumType.FORM_PARAM_REF;
    @Override
    public boolean shouldProcess(CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema() != null) return false;
        if (!codegenParameter.isEnum && codegenParameter.isEnumRef) {
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
        codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.FORM_PARAM_REF);
    }
    private void variableName(CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
    }
    private void datatype(CodegenParameter codegenParameter) {
        String enumDatatypeResolved = StringUtils.toPascalCase(Utility.getEnumNameFromDefaultDatatype(codegenParameter.dataType));
        codegenParameter.vendorExtensions.put(X_DATATYPE, ResourceCacheContext.get().getResourceName() + DOT + enumDatatypeResolved);
    }

    private void cacheEnumClass(CodegenParameter codegenParameter) {
        // TODO: Best way to store = codegenParameter.allowableValues.get("enumVars")
        String enumClassName = StringUtils.toPascalCase(Utility.getEnumNameFromDefaultDatatype(codegenParameter.dataType));
        List<Map<String, Object>> enumValues = (List<Map<String, Object>>) codegenParameter.allowableValues.get("enumVars");

        MustacheEnum mustacheEnum = new MustacheEnum(enumClassName, enumValues);
        ResourceCacheContext.get().addToEnumClasses(mustacheEnum);
    }
}
