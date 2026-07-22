package com.twilio.oai.java.processor.parameter;

import com.twilio.oai.java.processor.enums.EnumProcessorFactory;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenOperation;

import java.util.stream.Collectors;

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
//        if (codegenOperation.operationId.toLowerCase().startsWith("list")) {
//            codegenOperation.queryParams = codegenOperation.queryParams.stream()
//                .filter(param -> !param.baseName.equalsIgnoreCase("PageSize"))
//                .collect(Collectors.toList());
//            codegenOperation.optionalParams = codegenOperation.optionalParams.stream()
//                .filter(param -> !param.baseName.equalsIgnoreCase("PageSize"))
//                .collect(Collectors.toList());
//            codegenOperation.allParams = codegenOperation.allParams.stream()
//                .filter(param -> !(param.isQueryParam && param.baseName.equalsIgnoreCase("PageSize")))
//                .collect(Collectors.toList());
//        }

        codegenOperation.pathParams.forEach(param -> {
            String capitalized = StringUtils.capitalize(param.paramName);
            param.paramName = "path" + capitalized;
        });

        codegenOperation.allParams.stream().filter(param -> param.isPathParam).forEach(param -> {
            String capitalized = StringUtils.capitalize(param.paramName);
            param.paramName = "path" + capitalized;
        });

        codegenOperation.requiredParams.stream().filter(param -> param.isPathParam).forEach(param -> {
            String capitalized = StringUtils.capitalize(param.paramName);
            param.paramName = "path" + capitalized;
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