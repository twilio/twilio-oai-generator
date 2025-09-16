package com.twilio.oai.java.processor.enums.parameter.body;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.cache.ResourceCacheContext;
import com.twilio.oai.java.nestedmodels.MustacheEnum;
import com.twilio.oai.java.processor.enums.parameter.ParameterEnumProcessor;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;

import java.util.ArrayList;
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
          # REUSABLE_ARRAY
          singleBodyRefArray:
            type: array
            items:
              $ref: '#/components/schemas/singleReusable'
            description: An array of reusable enum in the request body
            example: [ electronics, clothing ]
 */
// renamed from FormParamListRefStrategy
public class ReusableBodyListEnumProcessor implements ParameterEnumProcessor {
    private final EnumConstants.OpenApiEnumType type = EnumConstants.OpenApiEnumType.FORM_PARAM_LIST_REF;
    @Override
    public boolean shouldProcess(CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema() != null) return false;
        if (codegenParameter.items != null && codegenParameter.items.isEnumRef && !codegenParameter.items.isEnum) {
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
        codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.FORM_PARAM_LIST_REF);
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
        String enumNonContainerDatatype = ResourceCacheContext.get().getResourceName() + DOT + StringUtils.toPascalCase(enumClassName);
        // resolvedDataType = List<Account.Status>
        String resolvedDataType = Utility.replaceDatatypeInContainer(codegenParameter.dataType, enumNonContainerDatatype);
        codegenParameter.vendorExtensions.put(X_DATATYPE, resolvedDataType);

        // Resolve BaseType for List as it is used in promoter as setter method.
        codegenParameter.baseType = enumNonContainerDatatype;
    }

    private void cacheEnumClass(CodegenParameter codegenParameter) {
        String baseDataType = Utility.extractDatatypeFromContainer(codegenParameter.dataType);
        if (baseDataType == null) {
            throw new RuntimeException("Not able to fetch enum baseType for List Enum with ref" + " DataType: " +codegenParameter.dataType);
        }
        String enumClassName = Utility.getEnumNameFromDatatype(baseDataType);
        if (enumClassName == null) {
            throw new RuntimeException("Not able to fetch enum class name from baseDataType for List Enum with ref"
                    + "baseType:"+ baseDataType + " DataType: " + codegenParameter.dataType);
        }

        Map<String, Object> values = null;
        List<Map<String, Object>> enumValues = new ArrayList<>();

        for (CodegenModel codegenModel: ResourceCacheContext.get().getAllModelsByDefaultGenerator()) {
            if (baseDataType.equals(codegenModel.classname)) {
                values = codegenModel.allowableValues;
                enumValues = (List<Map<String, Object>>) codegenModel.allowableValues.get("enumVars");
                break;
            }
        }
        if (enumValues == null || enumValues.isEmpty()) {
            System.out.println("Exception occurred:" + codegenParameter.baseName);
            throw new RuntimeException("No enum values found for Enum" + " DataType: " +codegenParameter.dataType);
        }
        MustacheEnum mustacheEnum = new MustacheEnum(enumClassName, enumValues);
        ResourceCacheContext.get().addToEnumClasses(mustacheEnum);
    }
}
