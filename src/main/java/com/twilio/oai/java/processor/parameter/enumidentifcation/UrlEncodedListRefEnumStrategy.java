package com.twilio.oai.java.processor.parameter.enumidentifcation;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.nestedmodels.MustacheEnum;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;

import java.util.Map;

import static com.twilio.oai.common.ApplicationConstants.DOT;
import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;
import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;

/*
          application/x-www-form-urlencoded:
            schema:
              type: object
              properties:
                # REUSABLE_ARRAY
                singleBodyRefArray:
                  type: array
                  items:
                    $ref: '#/components/schemas/singleReusable'
                  description: An array of reusable enum in the request body
                  example: [ electronics, clothing ]
 */
public class UrlEncodedListRefEnumStrategy implements EnumIdentificationStrategy {

    private final OpenApiEnumType type = OpenApiEnumType.URL_ENCODED_BODY_REF_LIST;

    @Override
    public boolean identify(final CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema() != null) return false;
        if (codegenParameter.items != null && codegenParameter.items.isEnumRef && !codegenParameter.items.isEnum) {
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

    private void type(CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.URL_ENCODED_BODY_REF_LIST);
    }
    private void variableName(CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
    }
    private void datatype(CodegenParameter codegenParameter) {
        // codegenParameter.dataType = List<AccountEnumStatus>
        // enumExistingDatatype = AccountEnumStatus
        String enumExistingDatatype = Utility.extractDatatypeFromContainer(codegenParameter.dataType);
        // enumClassName = Status
        String enumClassName = Utility.getEnumNameFromDatatype(enumExistingDatatype);
        // enumNonContainerDatatype = Account.Status
        String enumNonContainerDatatype = ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(enumClassName);
        // resolvedDataType = List<Account.Status>
        String resolvedDataType = Utility.replaceDatatypeInContainer(codegenParameter.dataType, enumNonContainerDatatype);
        codegenParameter.vendorExtensions.put(X_DATATYPE, resolvedDataType);
    }

    private void cacheEnumClass(CodegenParameter codegenParameter) {
        String enumExistingDatatype = Utility.extractDatatypeFromContainer(codegenParameter.dataType);
        String enumClassName = Utility.getEnumNameFromDatatype(enumExistingDatatype);
        Map<String, Object> values = null;
        for (CodegenModel codegenModel: ResourceCache.getAllModelsByDefaultGenerator()) {
            if (enumClassName.equals(codegenModel.classname)) {
                values = codegenModel.allowableValues;
            }
        }
        MustacheEnum mustacheEnum = new MustacheEnum(enumClassName, values);
        ResourceCache.addToEnumClasses(mustacheEnum);
    }
}
