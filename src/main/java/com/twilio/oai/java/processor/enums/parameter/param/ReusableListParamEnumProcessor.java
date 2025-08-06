package com.twilio.oai.java.processor.enums.parameter.param;

import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
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
        - name: arrayParamRef
          in: query
          required: false
          description: An array parameter referencing a reusable schema
          schema:
            type: array
            items:
              $ref: '#/components/schemas/singleReusable'
 */
public class ReusableListParamEnumProcessor implements ParameterEnumProcessor {

    private final OpenApiEnumType type = OpenApiEnumType.PARAMETER_LIST_REF;

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
        if (codegenParameter.getSchema().items != null && codegenParameter.getSchema().items.getRef() != null
                && codegenParameter.getSchema().items.isEnumRef) {
            return true;
        }
        return false;
    }

    private void type(CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_ENUM_TYPE, OpenApiEnumType.PARAMETER_LIST_REF);
    }
    private void variableName(CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
    }
    private void datatype(CodegenParameter codegenParameter) {
        // #/components/schemas/singleReusable or #/components/schemas/content_enum_single_reusable
        // enumRefResolved = singleReusable
        String enumRefResolved = Utility.getEnumNameFromRef(codegenParameter.getSchema().items.getRef());
        // enumNonContainerDatatype = Content.SingleReusable
        String enumNonContainerDatatype = ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(enumRefResolved);
        // resolvedDataType = List<Content.SingleReusable>
        String resolvedDataType = Utility.replaceDatatypeInContainer(codegenParameter.dataType, enumNonContainerDatatype);
        codegenParameter.vendorExtensions.put(X_DATATYPE, resolvedDataType);
    }

    private void cacheEnumClass(CodegenParameter codegenParameter) {
        List<Map<String, Object>> enumValues = (List<Map<String, Object>>)  codegenParameter.getSchema().items.allowableValues.get("enumVars");
        
        MustacheEnum mustacheEnum = new MustacheEnum(StringUtils.toPascalCase(
                Utility.getEnumNameFromRef(codegenParameter.getSchema().items.getRef())), enumValues);
        ResourceCache.addToEnumClasses(mustacheEnum);
    }
}
