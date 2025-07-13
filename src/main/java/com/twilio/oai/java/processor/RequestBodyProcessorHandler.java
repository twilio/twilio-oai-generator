package com.twilio.oai.java.processor;

import com.twilio.oai.java.processor.body.RequestBodyProcessor;
import com.twilio.oai.java.processor.body.RequestBodyProcessorFactory;
import org.openapitools.codegen.CodegenOperation;

import java.util.Map;

public class RequestBodyProcessorHandler implements Processor {
    @Override
    public void process(CodegenOperation codegenOperation) {
        RequestBodyProcessorFactory requestBodyProcessorFactory = RequestBodyProcessorFactory.getInstance();
        // No request body (example GET operation)
        if (codegenOperation.consumes == null) return;
        for (Map<String, String> mediaType : codegenOperation.consumes) {
            String contentType = mediaType.get("mediaType");
            RequestBodyProcessor requestBodyProcessor = requestBodyProcessorFactory.getProcessor(contentType);
            if (requestBodyProcessor == null) return;
            requestBodyProcessor.process(codegenOperation);
        }
        
        
    }
}
