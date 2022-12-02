package com.twilio.oai.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.CodegenOperation;

@UtilityClass
public class Utility {

    public void setComplexDataMapping(final List<CodegenModel> allModels, Map<String, String> modelFormatMap) {
        allModels.forEach(item -> {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(item.modelJson);
                if (jsonNode.has("type") && jsonNode.has("format") &&
                    jsonNode.get("type").textValue().equals("object")) {
                    modelFormatMap.put(item.classname, jsonNode.get("format").textValue());
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    public static Map<String, Map<String, Object>> getConventionalMap() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(ApplicationConstants.CONFIG_CSHARP_JSON_PATH), new TypeReference<>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    public void addModelsToLocalModelList(final Map<String, ModelsMap> modelMap, List<CodegenModel> localModels){
        for (final ModelsMap mods : modelMap.values()) {
            final List<ModelMap> modList = mods.getModels();
            modList
                    .stream()
                    .map(ModelMap::getModel)
                    .map(CodegenModel.class::cast)
                    .collect(Collectors.toCollection(() -> localModels));
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<CodegenOperation> getOperations(final Map<String, Object> resource) {
        return (ArrayList<CodegenOperation>) resource.computeIfAbsent(
                "operations",
                k -> new ArrayList<>());
    }
}
