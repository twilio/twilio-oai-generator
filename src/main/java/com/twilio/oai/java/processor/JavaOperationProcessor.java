package com.twilio.oai.java.processor;

import com.twilio.oai.java.factory.RequestBodyProcessorFactory;
import com.twilio.oai.java.processor.requestbody.RequestBodyProcessor;
import org.openapitools.codegen.CodegenOperation;

import java.util.ArrayList;
import java.util.List;

public class JavaOperationProcessor implements Processor {
    private final List<Processor> processors = new ArrayList<>();

    private JavaOperationProcessor() {
        // Register processors
        processors.add(ParameterProcessor.getInstance());
    }
    public static synchronized JavaOperationProcessor getInstance() {
        return new JavaOperationProcessor();
    }

    @Override
    public void process(CodegenOperation codegenOperation) {
        for (Processor processor : processors) {
            processor.process(codegenOperation);
        }
        // Factories are dynamic and can be added as needed
        RequestBodyProcessorFactory requestBodyProcessorFactory = RequestBodyProcessorFactory.getInstance();
        RequestBodyProcessor requestBodyProcessor = requestBodyProcessorFactory.getProcessor(codegenOperation);
        requestBodyProcessor.process(codegenOperation);
    }
}
