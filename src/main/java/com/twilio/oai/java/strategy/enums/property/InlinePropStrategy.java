package com.twilio.oai.java.strategy.enums.property;

import static com.twilio.oai.common.ApplicationConstants.DOT;
import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;
import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;

import com.twilio.oai.StringHelper;
import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.java.nestedmodels.MustacheEnum;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
codegenProperty._enum = true
codegenProperty.isEnum = true
codegenProperty.isEnumRef = true
 */
public class InlinePropStrategy implements PropertyEnumProcessingStrategy {
    private final OpenApiEnumType type = OpenApiEnumType.PROPERTY_INLINE;
    @Override
    public boolean process(final CodegenProperty codegenProperty) {
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

    @Override
    public boolean isStrategyApplicable(CodegenProperty codegenProperty) {
        if (codegenProperty.isEnum && codegenProperty.isEnumRef && codegenProperty.get_enum() != null) {
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
        codegenProperty.vendorExtensions.put(X_DATATYPE,
                ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(codegenProperty.baseName));
    }

    private void cacheEnumClass(CodegenProperty codegenProperty) {
        List<Map<String, Object>> enumValues = new ArrayList<>();
        
        for (String s : (List<String>) codegenProperty.allowableValues.get("values")) {
            HashMap<String, Object> valueMap = new HashMap<>();
            valueMap.put("name", StringHelper.toSnakeCase(s).toUpperCase());
            valueMap.put("value",  "\"" + s + "\""); // adding extra quote as this is how enumVars are stored
            enumValues.add(valueMap);
        }
        MustacheEnum mustacheEnum = new MustacheEnum(StringUtils.toPascalCase(codegenProperty.baseName), enumValues);
        ResourceCache.addToEnumClasses(mustacheEnum);
    }
}
