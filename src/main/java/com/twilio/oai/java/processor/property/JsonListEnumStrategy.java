package com.twilio.oai.java.processor.property;

import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.nestedmodels.MustacheEnum;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import static com.twilio.oai.common.ApplicationConstants.DOT;
import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;
import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;

public class JsonListEnumStrategy implements PropertyIdentificationStrategy {
    private final OpenApiEnumType type = OpenApiEnumType.PROPERTY_LIST;

    @Override
    public boolean identify(final CodegenProperty codegenProperty) {
        if (codegenProperty.isEnum && !codegenProperty.isEnumRef && codegenProperty.items != null && codegenProperty.items.get_enum() != null) {
            type(codegenProperty);
            variableName(codegenProperty);
            datatype(codegenProperty);
            return true;
        }
        // Check if the parameter is a single enum type
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
        String enumNonContainerDatatype = ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(enumClassName);
        String resolvedDataType = Utility.replaceDatatypeInContainer(codegenProperty.dataType, enumNonContainerDatatype);
        codegenProperty.vendorExtensions.put(X_DATATYPE, resolvedDataType);
    }

    private void cacheEnumClass(CodegenParameter codegenParameter) {
        
    }
}
