package com.twilio.oai.java.processor.enums.parameter.param;

import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
import com.twilio.oai.common.StringUtils;
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
public class InlineParamEnumProcessor implements ParameterEnumProcessor {

    private final OpenApiEnumType type = OpenApiEnumType.PARAMETER_INLINE;

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

    @Override
    public boolean shouldProcess(CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema() == null) return false;
        if (codegenParameter.getSchema()._enum != null && codegenParameter.isEnum) {
            return true;
        }
        return false;
    }

    private void type(CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_ENUM_TYPE, type);
    }
    private void variableName(CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
    }
    private void datatype(CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_DATATYPE,
                ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(codegenParameter.baseName));
    }

    private void cacheEnumClass(CodegenParameter codegenParameter) {
        List<Map<String, Object>> enumValues = (List<Map<String, Object>>) codegenParameter.allowableValues.get("enumVars");
        MustacheEnum mustacheEnum = new MustacheEnum(StringUtils.toPascalCase(codegenParameter.baseName), enumValues);
        ResourceCache.addToEnumClasses(mustacheEnum);
    }
}
