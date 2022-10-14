package com.twilio.oai.resolver.node;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.Segments;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.utils.StringUtils;

import java.util.*;
import java.util.function.Function;

public class NodeConventionResolver {
    final Map<String, Map<String, Object>> conventionMap;
    static final String CONFIG_NODE_JSON_PATH = "config/node.json";

    public NodeConventionResolver(){
        conventionMap = getConventionalMap();
    }

    public Optional<CodegenParameter> resolveParameter(CodegenParameter codegenParameter) {
        if (codegenParameter.dataFormat == null) {
            return Optional.of(codegenParameter);
        }
        boolean hasProperty = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).containsKey(StringUtils.underscore(codegenParameter.dataFormat));
        if (hasProperty) {
            codegenParameter.dataType = (String) conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(StringUtils.underscore(codegenParameter.dataFormat));
        }
        return Optional.of(codegenParameter);
    }

    public CodegenModel resolveModel(CodegenModel model){
        for (CodegenProperty property : model.vars) {
            if (property.dataFormat != null) {
                boolean hasProperty = conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).containsKey(StringUtils.underscore(property.dataFormat));
                if (hasProperty) {
                    property.dataType = (String)conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).get(StringUtils.underscore(property.dataFormat));
                }
            }
        }
        return model;
    }

    public CodegenModel resolveComplexType(CodegenModel model, Map<String, String> modelFormatMap){
        for (CodegenProperty prop: model.vars) {
            if(modelFormatMap.containsKey(prop.complexType)) {
                boolean hasProperty =  conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).
                        containsKey(StringUtils.underscore(modelFormatMap.get(prop.complexType)));
                if (hasProperty) {
                    prop.dataType = (String)conventionMap.get(Segments.SEGMENT_PROPERTIES.getSegment()).
                            get(StringUtils.underscore(modelFormatMap.get(prop.complexType)));

                    if (prop.containerType != null && prop.containerType.equals("array")) {
                        prop.dataType = "Array<" + prop.dataType + ">";
                    }

                    model.vendorExtensions.computeIfAbsent("x-imports", k -> new ArrayList<>());

                    HashMap<String, String> obj = (HashMap<String, String>) conventionMap.get(Segments.SEGMENT_LIBRARY.getSegment()).
                            get(StringUtils.underscore(modelFormatMap.get(prop.complexType)));
                    ArrayList array = (ArrayList) model.vendorExtensions.get("x-imports");

                    HashMap<String, String> value = new HashMap<>();

                    // If the object isn't already there, add it
                    value.put("name", prop.dataType);
                    value.put("path", obj.get(prop.dataType));

                    if (!importAlreadyExists(prop.dataType, array)) {
                        array.add(value);
                    }
                }
            }
        }
        return model;
    }

    private Boolean importAlreadyExists(String importName, ArrayList<HashMap<String, String>> array){
        for (HashMap<String, String> element : array) {
            if (importName.equals(element.get("name"))) {
                return true;
            }
        }
        return false;
    }

    private Boolean ximportVendorExtensionExists(CodegenModel model) {
        return model.vendorExtensions.containsKey("x-imports");
    }
    public Map<String, Map<String, Object>> getConventionalMap() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_NODE_JSON_PATH), new TypeReference<Map<String, Map<String, Object>>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
