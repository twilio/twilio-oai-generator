package com.twilio.oai.java.processor;

import com.twilio.oai.java.factory.RequestBodyProcessorFactory;
import com.twilio.oai.java.feature.ConstructorGeneratorProcessor;
import com.twilio.oai.java.feature.SetterMethodGenerator;
import com.twilio.oai.java.processor.requestbody.RequestBodyProcessor;
import com.twilio.oai.java.processor.responsebody.ResponseProcessorFactory;
import org.openapitools.codegen.CodegenOperation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JavaOperationProcessor implements Processor {
    private final List<Processor> processors = new ArrayList<>();

    private JavaOperationProcessor() {
        // Register processors
        processors.add(ParameterProcessor.getInstance());
        processors.add(SetterMethodGenerator.getInstance());
        processors.add(ConstructorGeneratorProcessor.getInstance());
    }
    public static synchronized JavaOperationProcessor getInstance() {
        return new JavaOperationProcessor();
    }

    @Override
    public void process(CodegenOperation codegenOperation) {
        
        updateHttpMethod(codegenOperation);
        
        // Factories are dynamic and can be added as needed
        RequestBodyProcessorFactory requestBodyProcessorFactory = RequestBodyProcessorFactory.getInstance();
        RequestBodyProcessor requestBodyProcessor = requestBodyProcessorFactory.getProcessor(codegenOperation);
        if (requestBodyProcessor != null)
            requestBodyProcessor.process(codegenOperation);

        processors.sort(Comparator.comparingInt(Processor::getPriority));
        for (Processor processor : processors) {
            processor.process(codegenOperation);
        }

        ResponseProcessorFactory responseProcessorFactory = ResponseProcessorFactory.getInstance();
        responseProcessorFactory.applyProcessor(codegenOperation);
        
        // This has to be last as it update operation for mustache files.
        
    }

    private void updateHttpMethod(CodegenOperation codegenOperation) {
        
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
