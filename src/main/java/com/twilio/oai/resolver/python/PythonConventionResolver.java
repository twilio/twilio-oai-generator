package com.twilio.oai.resolver.python;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.Segments;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.twilio.oai.common.ApplicationConstants.CONFIG_NODE_JSON_PATH;

public class PythonConventionResolver{
    private final Map<String, Map<String, Object>> conventionMap = getConventionalMap();

    public void resolveParameter(final CodegenParameter codegenParameter) {
        if (codegenParameter.dataFormat != null) {
            getDataType(StringUtils.underscore(codegenParameter.dataFormat)).ifPresent(dataType -> codegenParameter.dataType = dataType);
        }

        codegenParameter.dataType = removeEnumName(codegenParameter.dataType);
        codegenParameter.baseType = removeEnumName(codegenParameter.baseType);
    }

    public CodegenModel resolveModel(CodegenModel model) {
        for (CodegenProperty property : model.vars) {
            if (property.dataFormat != null) {
                getDataType(StringUtils.underscore(property.dataFormat)).ifPresent(dataType -> property.dataType = dataType);
            }

            property.dataType = removeEnumName(property.dataType);
            property.complexType = removeEnumName(property.complexType);
        }

        return model;
    }

    @SuppressWarnings("unchecked")
    public CodegenModel resolveComplexType(CodegenModel model, Map<String, String> modelFormatMap) {
        for (final CodegenProperty prop : model.vars) {
            if (modelFormatMap.containsKey(prop.complexType)) {
                final String propertyName = StringUtils.underscore(modelFormatMap.get(prop.complexType));

                getDataType(propertyName).ifPresent(dataType -> {
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

    private Map<String, Map<String, Object>> getConventionalMap() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_NODE_JSON_PATH), new TypeReference<>() {});
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<String> getDataType(final String dataFormat) {
        final Map<String, Object> properties = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment());

        if (properties.containsKey(dataFormat)) {
            return Optional.of((String) properties.get(dataFormat));
        }

        return Optional.empty();
    }

    private String removeEnumName(final String dataType) {
        return dataType == null ? null : dataType.replace("Enum", "");
    }
}
