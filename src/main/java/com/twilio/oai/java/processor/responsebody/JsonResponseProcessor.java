package com.twilio.oai.java.processor.responsebody;

import com.twilio.oai.java.processor.Processor;
import org.openapitools.codegen.CodegenOperation;

public class JsonResponseProcessor implements ResponseBodyProcessor, Processor {
    @Override
    public void process(CodegenOperation codegenOperation) {
        
    }

    @Override
    public int getPriority() {
        return Processor.DEFAULT_PRIORITY;
    }

    @Override
    public String getContentType() {
        return "application/json";
    }
}
