package com.twilio.oai.java.strategy.enums.parameter;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.nestedmodels.MustacheEnum;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenParameter;

import java.util.List;
import java.util.Map;

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
public class RefParamStrategy implements ParameterEnumProcessingStrategy {

    private final OpenApiEnumType type = OpenApiEnumType.PARAMETER_REF;

    @Override
    public boolean process(final CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema() == null) return false;
        if (codegenParameter.getSchema().getRef() != null && codegenParameter.isEnumRef) {
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
        if (codegenParameter.getSchema() == null) return false;
        if (codegenParameter.getSchema().getRef() != null && codegenParameter.isEnumRef) {
            return true;
        }
        return false;
    }

    private void type(CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.PARAMETER_REF);
    }
    private void variableName(CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
    }
    private void datatype(CodegenParameter codegenParameter) {
        String enumRefResolved = Utility.getEnumNameFromRef(codegenParameter.getSchema().getRef());
        codegenParameter.vendorExtensions.put(X_DATATYPE,
                ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(enumRefResolved));
    }

    private void cacheEnumClass(CodegenParameter codegenParameter) {
        List<Map<String, Object>> enumValues = (List<Map<String, Object>>)  codegenParameter.allowableValues.get("enumVars");
        MustacheEnum mustacheEnum = new MustacheEnum(StringUtils.toPascalCase(
                Utility.getEnumNameFromRef(codegenParameter.getSchema().getRef())), enumValues);
        ResourceCache.addToEnumClasses(mustacheEnum);
    }
}
