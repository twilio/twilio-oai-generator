package com.twilio.oai.java.processor.responsebody;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.java.cache.ResourceCacheContext;
import com.twilio.oai.java.constants.MustacheConstants;
import com.twilio.oai.java.processor.enums.EnumProcessorFactory;
import com.twilio.oai.java.processor.requestbody.RecursiveModelProcessor;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenResponse;

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
        CodegenProperty codegenProperty = getCodegenProperty(codegenOperation);
        if (codegenProperty == null) {
            codegenOperation.vendorExtensions.put(MustacheConstants.X_RESPONSE_DATATYPE, "void");
            return;
        }
        recursiveModelProcessor.process(codegenProperty);
        codegenOperation.vendorExtensions.put(MustacheConstants.X_RESPONSE_DATATYPE, codegenProperty.vendorExtensions.get(ApplicationConstants.X_DATATYPE));
    }

    private void processResponseWithPagination(CodegenOperation codegenOperation) {
        // check if pagination exists, if no, go to processResponseWithoutPagination
        Map<String, CodegenModel> codegenModelMap = ResourceCacheContext.get().getAllModelsMapByDefaultGenerator();
        CodegenProperty responseProperty = null;
        for (CodegenResponse response: codegenOperation.responses) {
            CodegenModel codegenModel = codegenModelMap.get(StringUtils.toPascalCase(response.baseType));
            for (CodegenProperty codegenProperty: codegenModel.vars) {
                if (codegenProperty.name.equals(ResourceCacheContext.get().getRecordKey())) {
                    responseProperty = codegenProperty;
                }
            }
        }
        
        if (responseProperty == null) {
            codegenOperation.vendorExtensions.put(MustacheConstants.X_RESPONSE_DATATYPE, "void");
            return;
        }
        recursiveModelProcessor.process(responseProperty);
        String listResponseDatatype = (String)responseProperty.vendorExtensions.get(ApplicationConstants.X_DATATYPE);
        listResponseDatatype = listResponseDatatype.replaceFirst("^List(?=<)", "ResourceSet");
        codegenOperation.vendorExtensions.put(MustacheConstants.X_RESPONSE_DATATYPE, listResponseDatatype);
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
