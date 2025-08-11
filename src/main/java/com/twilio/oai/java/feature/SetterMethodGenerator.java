package com.twilio.oai.java.feature;

import com.twilio.oai.java.constants.MustacheConstants;
import com.twilio.oai.java.processor.enums.EnumProcessorFactory;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
        List<CodegenParameter> setterParameters = codegenOperation.allParams.stream().filter(param -> !param.isPathParam).collect(Collectors.toList());
        codegenOperation.vendorExtensions.put("x-setter-methods", setterParameters);
    }
}
