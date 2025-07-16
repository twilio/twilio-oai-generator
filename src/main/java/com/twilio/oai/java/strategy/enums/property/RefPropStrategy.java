package com.twilio.oai.java.strategy.enums.property;

import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import static com.twilio.oai.common.ApplicationConstants.DOT;
import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;
import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;
public class RefPropStrategy implements PropertyEnumProcessingStrategy {
    private final OpenApiEnumType type = OpenApiEnumType.PROPERTY_REF;
    @Override
    public boolean process(final CodegenProperty codegenProperty) {
        if (!codegenProperty.isEnum && codegenProperty.isEnumRef && codegenProperty.getRef() != null) {
            type(codegenProperty);
            variableName(codegenProperty);
            datatype(codegenProperty);
            return true;
        }
        return false;
    }

    @Override
    public OpenApiEnumType getType() {
        return type;
    }

    @Override
    public boolean isStrategyApplicable(CodegenProperty codegenProperty) {
        if (!codegenProperty.isEnum && codegenProperty.isEnumRef && codegenProperty.getRef() != null) {
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
        String enumDatatypeResolved = StringUtils.toPascalCase(Utility.getEnumNameFromDefaultDatatype(codegenProperty.getRef()));
        codegenProperty.vendorExtensions.put(X_DATATYPE, ResourceCache.getResourceName() + DOT + enumDatatypeResolved);
    }

    private void cacheEnumClass(CodegenParameter codegenParameter) {

    }
}
