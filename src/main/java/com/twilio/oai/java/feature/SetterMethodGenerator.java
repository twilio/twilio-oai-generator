package com.twilio.oai.java.feature;

import com.twilio.oai.java.processor.Processor;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

import java.util.ArrayList;
import java.util.List;

// Add all setter variables for an operation which is non path parameters
public class SetterMethodGenerator implements Processor {
    public static SetterMethodGenerator instance;

    private SetterMethodGenerator() {
    }

    public static synchronized SetterMethodGenerator getInstance() {
        if (instance == null) {
            instance = new SetterMethodGenerator();
        }
        return instance;
    }

    @Override
    public void process(final CodegenOperation codegenOperation) {
        List<CodegenParameter> codegenParameters = new ArrayList<>();
        codegenOperation.allParams.forEach(param -> {
            if (!param.isPathParam) {
                codegenParameters.add(param);
            }
        });
        codegenOperation.vendorExtensions.put("x-non-path-params", codegenParameters);
    }

    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY + 99;
    }
}
