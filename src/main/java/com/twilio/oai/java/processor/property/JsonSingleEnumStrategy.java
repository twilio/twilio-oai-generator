package com.twilio.oai.java.processor.property;

import static com.twilio.oai.common.ApplicationConstants.DOT;
import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;
import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;

import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.java.nestedmodels.MustacheEnum;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.Map;

/*
codegenProperty._enum = true
codegenProperty.isEnum = true
codegenProperty.isEnumRef = true
 */
public class JsonSingleEnumStrategy implements PropertyIdentificationStrategy {
    private final OpenApiEnumType type = OpenApiEnumType.PROPERTY_SINGLE;
    @Override
    public boolean identify(final CodegenProperty codegenProperty) {
        if (codegenProperty.isEnum && codegenProperty.isEnumRef && codegenProperty.get_enum() != null) {
            type(codegenProperty);
            variableName(codegenProperty);
            datatype(codegenProperty);
            cacheEnumClass(codegenProperty);
            return true;
        }
        return false;
    }

    @Override
    public OpenApiEnumType getType() {
        return type;
    }

    private void type(CodegenProperty codegenProperty) {
        codegenProperty.vendorExtensions.put(X_ENUM_TYPE, type);
    }
    private void variableName(CodegenProperty codegenProperty) {
        codegenProperty.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenProperty.baseName));
    }
    private void datatype(CodegenProperty codegenProperty) {
        codegenProperty.vendorExtensions.put(X_DATATYPE,
                ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(codegenProperty.baseName));
    }

    private void cacheEnumClass(CodegenProperty codegenProperty) {
        Map<String, Object> values = null;
        
        //MustacheEnum mustacheEnum = new MustacheEnum(StringUtils.toPascalCase(codegenProperty.baseName), codegenProperty.items.allowableValues);
        //ResourceCache.addToEnumClasses(mustacheEnum);
    }
}
