package com.twilio.oai.java.processor.property;

import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenProperty;

import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;
import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;
import static com.twilio.oai.common.ApplicationConstants.DOT;
import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;

public class JsonListRefEnumStrategy implements PropertyIdentificationStrategy {
    private final OpenApiEnumType type = OpenApiEnumType.PROPERTY_SINGLE;
    @Override
    public boolean identify(final CodegenProperty codegenProperty) {
        if (codegenProperty.items != null && codegenProperty.items.isEnumRef && !codegenProperty.items.isEnum) {
            codegenProperty.vendorExtensions.put(X_ENUM_TYPE, type);

            codegenProperty.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenProperty.baseName));
            
            String enumExistingDatatype = Utility.extractDatatypeFromContainer(codegenProperty.dataType);
            // enumClassName = Status
            String enumClassName = Utility.getEnumNameFromDatatype(enumExistingDatatype);
            // enumNonContainerDatatype = Account.Status
            String enumNonContainerDatatype = ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(enumClassName);
            // resolvedDataType = List<Account.Status>
            String resolvedDataType = Utility.replaceDatatypeInContainer(codegenProperty.dataType, enumNonContainerDatatype);
            codegenProperty.vendorExtensions.put(X_DATATYPE, resolvedDataType);
            return true;
        }
        // Check if the parameter is a single enum type
        return false;
    }

    @Override
    public OpenApiEnumType getType() {
        return type;
    }
}
