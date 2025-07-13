package com.twilio.oai.java.processor.body;

import com.twilio.oai.LoggerUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RequestBodyProcessorFactory {
    private static final Logger LOGGER = Logger.getLogger(RequestBodyProcessorFactory.class.getName());
    private final Map<String, RequestBodyProcessor> processorMap = new HashMap<>();
    private static final RequestBodyProcessorFactory INSTANCE = new RequestBodyProcessorFactory();
    private RequestBodyProcessorFactory() {
        processorMap.put("application/json", new JsonProcessor());
        processorMap.put("application/x-www-form-urlencoded", new FormParamProcessor());
    }
    public static RequestBodyProcessorFactory getInstance() {
        return INSTANCE;
    }
    public RequestBodyProcessor getProcessor(String contentType) {
        if (!processorMap.containsKey(contentType)) {
            LoggerUtil.logWarning(this.getClass().getName(), "No processor found for content type: " + contentType);
            return null;
        }
        return processorMap.get(contentType);
    }
}
