package com.twilio.oai.java.processor.enums.property;

import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.nestedmodels.MustacheEnum;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;
import java.util.Map;

import static com.twilio.oai.common.ApplicationConstants.DOT;
import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;
import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;

public class ReusableListPropEnumProcessor implements PropertyEnumProcessor {
    private final OpenApiEnumType type = OpenApiEnumType.PROPERTY_INLINE;
    @Override
    public void process(final CodegenProperty codegenProperty) {
        if (!shouldProcess(codegenProperty)) return;
        type(codegenProperty);
        variableName(codegenProperty);
        datatype(codegenProperty);
        cacheEnumClass(codegenProperty);
    }

    @Override
    public OpenApiEnumType getType() {
        return type;
    }

    @Override
    public boolean shouldProcess(CodegenProperty codegenProperty) {
        if (codegenProperty.items != null && codegenProperty.items.isEnumRef && !codegenProperty.items.isEnum) {
            return true;
        }
        return false;
    }

    private void type(CodegenProperty codegenProperty) {
        codegenProperty.vendorExtensions.put(X_ENUM_TYPE, type);
    }
    private void variableName(CodegenProperty codegenProperty) {
        codegenProperty.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenProperty.baseName));
    }
    private void datatype(CodegenProperty codegenProperty) {
        String enumExistingDatatype = Utility.extractDatatypeFromContainer(codegenProperty.dataType);
        // enumClassName = Status
        String enumClassName = Utility.getEnumNameFromDatatype(enumExistingDatatype);
        // enumNonContainerDatatype = Account.Status
        String enumNonContainerDatatype = ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(enumClassName);
        // resolvedDataType = List<Account.Status>
        String resolvedDataType = Utility.replaceDatatypeInContainer(codegenProperty.dataType, enumNonContainerDatatype);
        codegenProperty.vendorExtensions.put(X_DATATYPE, resolvedDataType);
    }

    private void cacheEnumClass(CodegenProperty codegenProperty) {
        String enumExistingDatatype = Utility.extractDatatypeFromContainer(codegenProperty.dataType);
        String enumClassName = Utility.getEnumNameFromDatatype(enumExistingDatatype);
        List<Map<String, Object>> enumValues = (List<Map<String, Object>>) codegenProperty.items.allowableValues.get("enumVars");
        
        MustacheEnum mustacheEnum = new MustacheEnum(enumClassName, enumValues);
        ResourceCache.addToEnumClasses(mustacheEnum);
    }
}
