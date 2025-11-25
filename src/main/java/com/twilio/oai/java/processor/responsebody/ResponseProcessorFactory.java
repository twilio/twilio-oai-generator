package com.twilio.oai.java.processor.responsebody;

import org.openapitools.codegen.CodegenOperation;

import java.util.ArrayList;
import java.util.List;

public class ResponseProcessorFactory {
    private final List<ResponseProcessor> processors = new ArrayList<>();
    private static final ResponseProcessorFactory INSTANCE = new ResponseProcessorFactory();

    private ResponseProcessorFactory() {
        processors.add(new JsonResponseProcessor());
        processors.add(new JsonMultipleResponseProcessor());
    }

    public static ResponseProcessorFactory getInstance() {
        return INSTANCE;
    }
    public void process(final CodegenOperation codegenOperation) {
        for (ResponseProcessor responseProcessor : processors) {
            if (responseProcessor.shouldProcess(codegenOperation)) {
                responseProcessor.process(codegenOperation);
            }
        }
    }
}
