package com.twilio.oai.java.factory;

import com.twilio.oai.LoggerUtil;
import com.twilio.oai.java.feature.constructor.FormConstructorGenerator;
import com.twilio.oai.java.feature.constructor.JsonConstructorGenerator;
import com.twilio.oai.java.processor.Processor;
import org.openapitools.codegen.CodegenOperation;

import java.util.HashMap;
import java.util.Map;

public class ConstructorFactory {
    private final Map<String, Processor> constructorGeneratorMap = new HashMap<>();
    
    public ConstructorFactory() {
        constructorGeneratorMap.put("application/json", new JsonConstructorGenerator());
        constructorGeneratorMap.put("application/x-www-form-urlencoded", new FormConstructorGenerator());
    }
    
    public Processor getConstructorGenerator(CodegenOperation codegenOperation) {
        String contentType = "";
        if (codegenOperation.consumes == null) return null;
        for (Map<String, String> mediaType : codegenOperation.consumes) {
            contentType = mediaType.get("mediaType");
            return constructorGeneratorMap.getOrDefault(contentType, constructorGeneratorMap.get("application/x-www-form-urlencoded"));
        }
        return constructorGeneratorMap.get("application/x-www-form-urlencoded");
    }
}
