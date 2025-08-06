package com.twilio.oai.java.processor.enums.property;

import com.twilio.oai.StringHelper;
import com.twilio.oai.common.EnumConstants.OpenApiEnumType;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.nestedmodels.MustacheEnum;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.twilio.oai.common.ApplicationConstants.DOT;
import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;
import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;
public class ReusablePropEnumProcessor implements PropertyEnumProcessor {
    private final OpenApiEnumType type = OpenApiEnumType.PROPERTY_REF;
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

    private void cacheEnumClass(CodegenProperty codegenProperty) {
        System.out.println(codegenProperty.baseName);
        String enumClassName = Utility.getEnumNameFromRef(codegenProperty.getRef());
        List<Map<String, Object>> enumValues = new ArrayList<>();
        for (String s : (List<String>) codegenProperty.allowableValues.get("values")) {
            HashMap<String, Object> valueMap = new HashMap<>();
            valueMap.put("name", StringHelper.toSnakeCase(s).toUpperCase());
            valueMap.put("value",  "\"" + s + "\""); // adding extra quote as this is how enumVars are stored
            enumValues.add(valueMap);
        }
        MustacheEnum mustacheEnum = new MustacheEnum(StringUtils.toPascalCase(enumClassName), enumValues);
        ResourceCache.addToEnumClasses(mustacheEnum);
    }
}
