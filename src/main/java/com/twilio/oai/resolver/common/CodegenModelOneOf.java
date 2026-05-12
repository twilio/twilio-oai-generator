package com.twilio.oai.resolver.common;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class CodegenModelOneOf {
    private static volatile CodegenModelOneOf INSTANCE;

    private CodegenModelOneOf() {
    }

    public static CodegenModelOneOf getInstance() {
        if (INSTANCE == null) {
            synchronized (CodegenModelOneOf.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CodegenModelOneOf();
                }
            }
        }
        return INSTANCE;
    }

    public void resolve(CodegenModel model) {
        Map<String, CodegenProperty> flattenProps = new LinkedHashMap<>();
        // Flatten oneOf, note: nested oneOfs are not handled here
        for (CodegenModel subModel: model.interfaceModels) {
            for (CodegenProperty property: subModel.vars) {
                property.required = false;
                CodegenProperty existing = flattenProps.get(property.getName());
                if (existing != null) {
                    mergeEnumValues(existing, property);
                } else {
                    flattenProps.put(property.getName(), property);
                }
            }
        }
        // Add existing properties in parallel to oneOf
        for (CodegenProperty property: model.vars) {
            CodegenProperty existing = flattenProps.get(property.getName());
            if (existing != null) {
                mergeEnumValues(existing, property);
            } else {
                flattenProps.put(property.getName(), property);
            }
        }
        List<CodegenProperty> finalProps = new ArrayList<>(flattenProps.values());
        model.vars = finalProps;
    }

    @SuppressWarnings("unchecked")
    private void mergeEnumValues(CodegenProperty target, CodegenProperty source) {
        if (source.allowableValues == null || source.allowableValues.get("values") == null) return;
        if (target.allowableValues == null) {
            target.allowableValues = source.allowableValues;
            target.isEnum = source.isEnum;
            target._enum = source._enum;
            return;
        }
        List<String> targetValues = (List<String>) target.allowableValues.get("values");
        List<String> sourceValues = (List<String>) source.allowableValues.get("values");
        if (targetValues == null || sourceValues == null) return;
        for (String val : sourceValues) {
            if (!targetValues.contains(val)) {
                targetValues.add(val);
            }
        }
        if (target._enum != null && source._enum != null) {
            for (String val : source._enum) {
                if (!target._enum.contains(val)) {
                    target._enum.add(val);
                }
            }
        } else if (target._enum == null) {
            target._enum = source._enum;
        }
        target.isEnum = true;
    }
}
