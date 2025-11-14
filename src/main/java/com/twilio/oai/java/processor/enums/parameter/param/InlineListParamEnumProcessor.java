package com.twilio.oai.java.processor.enums.parameter.param;

import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
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
public class InlineListParamEnumProcessor implements ParameterEnumProcessor {

    private final OpenApiEnumType type = OpenApiEnumType.PARAMETER_LIST_INLINE;

    @Override
    public boolean shouldProcess(CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema() == null) return false;
        if (codegenParameter.getSchema()._enum == null &&
                codegenParameter.getSchema().items != null && codegenParameter.getSchema().items._enum != null) {
            return true;
        }
        return false;
    }

    @Override
    public void process(final CodegenParameter codegenParameter) {
        if (!shouldProcess(codegenParameter)) return;
        type(codegenParameter);
        variableName(codegenParameter);
        datatype(codegenParameter);
        cacheEnumClass(codegenParameter);
    }

    @Override
    public OpenApiEnumType getType() {
        return type;
    }

    private void type(CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_ENUM_TYPE, OpenApiEnumType.PARAMETER_LIST_INLINE);
    }
    private void variableName(CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
    }
    private void datatype(CodegenParameter codegenParameter) {
        String enumClassName = StringUtils.toPascalCase(codegenParameter.baseName);
        String enumNonContainerDatatype = ResourceCacheContext.get().getResourceName() + DOT + StringUtils.toPascalCase(enumClassName);
        String resolvedDataType = Utility.replaceDatatypeInContainer(codegenParameter.dataType, enumNonContainerDatatype);
        codegenParameter.vendorExtensions.put(X_DATATYPE, resolvedDataType);

        // Resolve BaseType for List as it is used in promoter as setter method.
        codegenParameter.baseType = enumNonContainerDatatype;
        // enumNonContainerDatatype = Account.Status
        
    }

    private void cacheEnumClass(CodegenParameter codegenParameter) {
        List<Map<String, Object>> enumValues = (List<Map<String, Object>>) codegenParameter.allowableValues.get("enumVars");

        MustacheEnum mustacheEnum = new MustacheEnum( StringUtils.toPascalCase(codegenParameter.baseName), enumValues);
        ResourceCacheContext.get().addToEnumClasses(mustacheEnum);
    }
}
