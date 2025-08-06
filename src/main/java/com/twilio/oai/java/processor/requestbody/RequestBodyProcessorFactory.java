package com.twilio.oai.java.processor.requestbody;

import org.openapitools.codegen.CodegenOperation;

import java.util.List;

public class RequestBodyProcessorFactory {
    private static RequestBodyProcessorFactory instance;
    private final List<RequestBodyProcessor> requestBodyProcessors;

    private RequestBodyProcessorFactory() {
        this.requestBodyProcessors = List.of(
            new UrlEncodedProcessor()
        );
    }

    public static synchronized RequestBodyProcessorFactory getInstance() {
        if (instance == null) {
            synchronized (RequestBodyProcessorFactory.class) {
                if (instance == null) {
                    instance = new RequestBodyProcessorFactory();
                }
            }
        }
        return instance;
    }

    public void process(final CodegenOperation codegenOperation) {
        for (RequestBodyProcessor requestBodyProcessor: requestBodyProcessors) {
            if (requestBodyProcessor.shouldProcess(codegenOperation)) {
                requestBodyProcessor.process(codegenOperation);
                return; // Exit after the first processor that applies
            }
        }
    }
}
