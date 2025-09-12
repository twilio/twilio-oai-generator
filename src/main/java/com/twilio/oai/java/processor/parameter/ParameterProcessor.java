package com.twilio.oai.java.processor.parameter;

import com.twilio.oai.java.processor.enums.EnumProcessorFactory;
import org.openapitools.codegen.CodegenOperation;

/*
 * This class includes processing of path, query, header and required parameters.
 */
public class ParameterProcessor {

    public static ParameterProcessor instance;
    EnumProcessorFactory enumProcessorFactory;
    private ParameterProcessor () {
        this.enumProcessorFactory = EnumProcessorFactory.getInstance();
    }

    public static synchronized ParameterProcessor getInstance() {
        if (instance == null) {
            instance = new ParameterProcessor();
        }
        return instance;
    }

    public void process(final CodegenOperation codegenOperation) {
        codegenOperation.pathParams.forEach(param -> {
            param.paramName = "path" + param.paramName;
        });

        codegenOperation.allParams.stream().filter(param -> param.isPathParam).forEach(param -> {
            param.paramName = "path" + param.paramName;
        });

        codegenOperation.requiredParams.stream().filter(param -> param.isPathParam).forEach(param -> {
            param.paramName = "path" + param.paramName;
        });
        
        codegenOperation.queryParams.forEach(param -> enumProcessorFactory.applyProcessor(param));
        codegenOperation.pathParams.forEach(param -> enumProcessorFactory.applyProcessor(param));
        codegenOperation.headerParams.forEach(param -> enumProcessorFactory.applyProcessor(param));
        codegenOperation.requiredParams.forEach(param -> enumProcessorFactory.applyProcessor(param));

        // Required for setter methods and promotion method generation
        codegenOperation.allParams.forEach(param -> enumProcessorFactory.applyProcessor(param));

        //codegenOperation.allParams.forEach(param -> enumProcessorFactory.applyProcessor(param));
    }
}