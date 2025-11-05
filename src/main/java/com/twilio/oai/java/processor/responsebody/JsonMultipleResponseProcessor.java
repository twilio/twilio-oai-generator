package com.twilio.oai.java.processor.responsebody;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.cache.ResourceCacheContext;
import com.twilio.oai.java.format.Deserializer;
import com.twilio.oai.java.processor.enums.EnumProcessorFactory;
import com.twilio.oai.java.processor.requestbody.RecursiveModelProcessor;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;

import java.util.Map;

public class JsonMultipleResponseProcessor extends JsonResponseAbstractProcessor implements ResponseProcessor {
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
        
        if (codegenOperation.operationId.toLowerCase().startsWith("list")) {
            processResponseWithPagination(codegenOperation);
        } else {
            processResponseWithoutPagination(codegenOperation);
        }
        
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
    
    private void processResponseWithoutPagination(CodegenOperation codegenOperation) {
        System.out.println(codegenOperation.operationId);
        CodegenModel codegenModel = getModel(codegenOperation);
        System.out.println(codegenModel);
    }

    private void processResponseWithPagination(CodegenOperation codegenOperation) {
        System.out.println(codegenOperation.operationId);
        CodegenModel codegenModel = getModel(codegenOperation);
        System.out.println(codegenModel);
        int size = codegenModel.vars.size();
        for (int i=0; i<size; i++) {
            CodegenProperty codegenProperty = codegenModel.vars.get(i);
            if (codegenProperty.getRef() == null) continue;
            CodegenModel model = Utility.getModelFromRef(codegenProperty.getRef());
            
            
        }
    }

    @Override
    public String getContentType() {
        return "application/json";
    }
    
    boolean isPaginatedModel(CodegenProperty codegenProperty) {
        System.out.println(codegenProperty);
        return true;
    }
}
