package com.twilio.oai.java.processor.requestbody;

import com.twilio.oai.LoggerUtil;
import com.twilio.oai.java.processor.Processor;
import com.twilio.oai.java.strategy.model.RecursiveModelProcessor;
import org.openapitools.codegen.CodegenOperation;

import static com.twilio.oai.common.ApplicationConstants.X_REQUEST_CONTENT_TYPE;
import static com.twilio.oai.java.MustacheConstants.X_REQUEST_LANGUAGE_CONTENT_TYPE_CONSTANT;

public class JsonRequestProcessor implements RequestBodyProcessor, Processor {
    
    
    final RecursiveModelProcessor recursiveModelProcessor = new RecursiveModelProcessor();
    public JsonRequestProcessor() {
        // Constructor can be used for initialization if needed
    }
    @Override
    public void process(CodegenOperation codegenOperation) {
        codegenOperation.vendorExtensions.put(X_REQUEST_CONTENT_TYPE, getContentType());
        codegenOperation.vendorExtensions.put(X_REQUEST_CONTENT_TYPE, getContentType());
        codegenOperation.vendorExtensions.put(X_REQUEST_LANGUAGE_CONTENT_TYPE_CONSTANT, "JSON");
        System.out.println(codegenOperation.operationId);
        if (!codegenOperation.getHasBodyParam()) return;
        if (codegenOperation.bodyParams.size() > 1) {
            LoggerUtil.logSevere(this.getClass().getName(), "Multiple request bodies found " + codegenOperation.operationId);
        }
        
        
        recursiveModelProcessor.processBody(codegenOperation);
        
    }

    @Override
    public int getPriority() {
        return Processor.DEFAULT_PRIORITY;
    }

    public void resolve() {
        
    }

    @Override
    public String getContentType() {
        return "application/json";
    }
}
