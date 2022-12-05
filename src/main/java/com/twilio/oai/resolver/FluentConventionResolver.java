package com.twilio.oai.resolver;

import com.twilio.oai.Segments;
import com.twilio.oai.common.Utility;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class FluentConventionResolver {
    public void resolveParameter(final CodegenParameter codegenParameter, Map<String, Map<String, Object>> conventionMap) {
        if (codegenParameter.dataFormat != null) {
            getDataType(StringUtils.underscore(codegenParameter.dataFormat), conventionMap).ifPresent(dataType -> codegenParameter.dataType = dataType);
        }

        codegenParameter.dataType = Utility.removeEnumName(codegenParameter.dataType);
        codegenParameter.baseType = Utility.removeEnumName(codegenParameter.baseType);
    }

    public CodegenModel resolveModel(CodegenModel model, Map<String, Map<String, Object>> conventionMap) {
        for (CodegenProperty property : model.vars) {
            if (property.dataFormat != null) {
                getDataType(StringUtils.underscore(property.dataFormat), conventionMap).ifPresent(dataType -> property.dataType = dataType);
            }

            property.dataType = Utility.removeEnumName(property.dataType);
            property.complexType = Utility.removeEnumName(property.complexType);
        }

        return model;
    }

    @SuppressWarnings("unchecked")
    public CodegenModel resolveComplexType(CodegenModel model, Map<String, String> modelFormatMap, Map<String, Map<String, Object>> conventionMap) {
        for (final CodegenProperty prop : model.vars) {
            if (modelFormatMap.containsKey(prop.complexType)) {
                final String propertyName = StringUtils.underscore(modelFormatMap.get(prop.complexType));

                getDataType(propertyName, conventionMap).ifPresent(dataType -> {
                    prop.dataType = dataType;
                    prop.complexType = null;

                    if (prop.containerType != null && prop.containerType.equals("array")) {
                        prop.dataType = "Array<" + prop.dataType + ">";
                    }

                    // Add custom object import path
                    Map<String, String> propMap = (Map<String, String>) conventionMap.get(Segments.SEGMENT_LIBRARY.getSegment()).
                            get(propertyName);
                    ArrayList<Map<String, String>> imports =
                            (ArrayList<Map<String, String>>) model.vendorExtensions.computeIfAbsent(
                                    "x-imports",
                                    k -> new ArrayList<>());
                    if (imports.stream().noneMatch(item -> prop.dataType.equals(item.get("name")))) {
                        Map<String, String> customObjectImport = new HashMap<>();
                        customObjectImport.put("name", prop.dataType);
                        customObjectImport.put("path", propMap.get(prop.dataType));
                        imports.add(customObjectImport);
                    }
                });
            }
        }
        return model;
    }

    private Optional<String> getDataType(final String dataFormat, Map<String, Map<String, Object>> conventionMap) {
        final Map<String, Object> properties = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment());

        if (properties.containsKey(dataFormat)) {
            return Optional.of((String) properties.get(dataFormat));
        }

        return Optional.empty();
    }

}