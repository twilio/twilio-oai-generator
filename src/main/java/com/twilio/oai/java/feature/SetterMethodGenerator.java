package com.twilio.oai.java.feature;

import com.twilio.oai.java.processor.enums.EnumProcessorFactory;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

import java.util.ArrayList;
import java.util.List;

// Add all setter variables for an operation which is non path parameters
public class SetterMethodGenerator {
    public static SetterMethodGenerator instance;

    private SetterMethodGenerator() {
    }
    public static synchronized SetterMethodGenerator getInstance() {
        if (instance == null) {
            synchronized (SetterMethodGenerator.class) {
                if (instance == null) {
                    instance = new SetterMethodGenerator();
                }
            }
        }
        return instance;
    }

    public void apply(final CodegenOperation codegenOperation) {
        List<CodegenParameter> codegenParameters = new ArrayList<>();
        codegenOperation.allParams.forEach(param -> {
            if (!param.isPathParam) {
                codegenParameters.add(param);
            }
        });
        codegenOperation.vendorExtensions.put("x-non-path-params", codegenParameters);
    }
}
