package com.twilio.oai.java.processor.responsebody;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.java.cache.ResourceCacheContext;
import com.twilio.oai.java.format.Deserializer;
import com.twilio.oai.java.processor.enums.EnumProcessorFactory;
import com.twilio.oai.java.processor.requestbody.RecursiveModelProcessor;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;

import java.util.Map;

public class JsonMultipleResponseProcessor implements ResponseProcessor {
    EnumProcessorFactory enumProcessorFactory = EnumProcessorFactory.getInstance();
    RecursiveModelProcessor recursiveModelProcessor = new RecursiveModelProcessor();
    @Override
    public void process(final CodegenOperation codegenOperation) {
        /* There are 5 types of operation we are supporting.
         * delete -- does not have response body 
         * fetch, create, update --> have body 
         * list  --> has pagination and body
         */
        if (codegenOperation.operationId.toLowerCase().startsWith("delete")) return;
        
    }
    
    public boolean shouldProcess(final CodegenOperation codegenOperation) {
        if (!ResourceCacheContext.get().isV1()) return false;
        
        if (codegenOperation.produces != null && !codegenOperation.produces.isEmpty()) {
            for (Map<String, String> contentType : codegenOperation.produces) {
                if (getContentType().equals(contentType.get("mediaType")) || "application/scim+json".equals(contentType.get("mediaType"))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void processResponseWithoutPagination() {
        
    }

    private void processResponseWithPagination() {
        
    }

    @Override
    public String getContentType() {
        return "application/json";
    }
}
