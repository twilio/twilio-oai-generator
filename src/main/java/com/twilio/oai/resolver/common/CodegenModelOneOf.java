package com.twilio.oai.resolver.common;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class CodegenModelOneOf {
    public void resolve(CodegenModel model) {
        if (model.discriminator != null) {
            // For Future feature, currently models are generated same way with discriminator or without discriminator.
        }
        TreeSet<CodegenProperty> flattenProps = new TreeSet<>(Comparator.comparing(CodegenProperty::getName));
        // Flatten oneOf, note: nested oneOfs are not handled here
        for (CodegenModel subModel: model.interfaceModels) {
            for (CodegenProperty property: subModel.vars) {
                flattenProps.add(property);
                property.required = false;
            }
        }
        // Add existing properties in parallel to oneOf
        for (CodegenProperty property: model.vars) {
            flattenProps.add(property);
        }
        List<CodegenProperty> finalProps = new ArrayList<>(flattenProps);
        model.vars = finalProps;
    }
}
