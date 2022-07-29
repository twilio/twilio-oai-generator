package com.twilio.oai.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenModel;

import java.util.List;
import java.util.Map;

@UtilityClass
public class Utility {

    public String toFirstLetterCaps(String input) {
        return StringUtils.isBlank(input) ? input : input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public void setComplexDataMapping(final List<CodegenModel> allModels, Map<String, String> modelFormatMap) {
        allModels.forEach(item -> {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(item.modelJson);
                if (jsonNode.get("type").textValue().equals("object") && jsonNode.has("format")) {
                    modelFormatMap.put(item.classFilename, jsonNode.get("format").textValue());
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
