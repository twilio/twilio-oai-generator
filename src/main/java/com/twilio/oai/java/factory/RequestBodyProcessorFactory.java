package com.twilio.oai.java.factory;

import com.twilio.oai.LoggerUtil;
import com.twilio.oai.java.processor.requestbody.FormParamProcessor;
import com.twilio.oai.java.processor.requestbody.JsonRequestProcessor;
import com.twilio.oai.java.processor.requestbody.RequestBodyProcessor;
import org.openapitools.codegen.CodegenOperation;

import java.util.HashMap;
import java.util.Map;
/*
    
 */
public class RequestBodyProcessorFactory implements ProcessorFactory<RequestBodyProcessor, CodegenOperation> {
    private final Map<String, RequestBodyProcessor> processorMap = new HashMap<>();
    private static final RequestBodyProcessorFactory INSTANCE = new RequestBodyProcessorFactory();
    private RequestBodyProcessorFactory() {
        processorMap.put("application/json", new JsonRequestProcessor());
        processorMap.put("application/x-www-form-urlencoded", new FormParamProcessor());
    }
    public static RequestBodyProcessorFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public RequestBodyProcessor getProcessor(CodegenOperation codegenOperation) {
        String contentType = "";
        if (codegenOperation.consumes == null) return null;
        for (Map<String, String> mediaType : codegenOperation.consumes) {
            contentType = mediaType.get("mediaType");
            if (!processorMap.containsKey(contentType)) {
                LoggerUtil.logWarning(this.getClass().getName(), "No processor found for content type: " + contentType);
            }
            return processorMap.get(contentType);
        }
        return null;
    }
}
