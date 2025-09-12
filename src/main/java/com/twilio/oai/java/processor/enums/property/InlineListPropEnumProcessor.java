package com.twilio.oai.java.processor.enums.property;

import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.cache.ResourceCacheContext;
import com.twilio.oai.java.nestedmodels.MustacheEnum;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;
import java.util.Map;

import static com.twilio.oai.common.ApplicationConstants.DOT;
import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;
import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;

public class InlineListPropEnumProcessor implements PropertyEnumProcessor {
    private final OpenApiEnumType type = OpenApiEnumType.PROPERTY_LIST;

    @Override
    public void process(final CodegenProperty codegenProperty) {
        if (!shouldProcess(codegenProperty)) return;
        type(codegenProperty);
        variableName(codegenProperty);
        datatype(codegenProperty);
        cacheEnumClass(codegenProperty);
    }

    public boolean shouldProcess(CodegenProperty codegenProperty) {
        if (codegenProperty.isEnum && !codegenProperty.isEnumRef && codegenProperty.items != null && codegenProperty.items.get_enum() != null) {
            return true;
        }
        return false;
    }

    @Override
    public OpenApiEnumType getType() {
        // Return the type of enum this strategy identifies
        return type;
    }

    private void type(CodegenProperty codegenProperty) {
        codegenProperty.vendorExtensions.put(X_ENUM_TYPE, type);
    }
    private void variableName(CodegenProperty codegenProperty) {
        codegenProperty.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenProperty.baseName));
    }
    private void datatype(CodegenProperty codegenProperty) {
        String enumClassName = StringUtils.toPascalCase(codegenProperty.baseName);
        String enumNonContainerDatatype = ResourceCacheContext.get().getResourceName() + DOT + StringUtils.toPascalCase(enumClassName);
        String resolvedDataType = Utility.replaceDatatypeInContainer(codegenProperty.dataType, enumNonContainerDatatype);
        codegenProperty.vendorExtensions.put(X_DATATYPE, resolvedDataType);
        
        // Resolve BaseType for List as it is used in promoter as setter method.
        String baseType = Utility.getEnumNameFromDatatype(codegenProperty.baseType);
        if (baseType != null) {
            String resolvedBaseType = ResourceCacheContext.get().getResourceName() + DOT + StringUtils.toPascalCase(baseType);
            codegenProperty.baseType = resolvedBaseType;
        }
    }

    private void cacheEnumClass(CodegenProperty codegenProperty) {
        List<Map<String, Object>> enumValues = (List<Map<String, Object>>)  codegenProperty.allowableValues.get("enumVars");
        MustacheEnum mustacheEnum = new MustacheEnum(StringUtils.toPascalCase(codegenProperty.baseName), enumValues);
        ResourceCacheContext.get().addToEnumClasses(mustacheEnum);
    }
}
