package com.twilio.oai.java.processor.responsebody;

import com.twilio.oai.common.Utility;
import com.twilio.oai.java.cache.ResourceCacheContext;
import com.twilio.oai.java.format.Deserializer;
import com.twilio.oai.java.processor.enums.EnumProcessorFactory;
import com.twilio.oai.java.processor.requestbody.RecursiveModelProcessor;
import org.openapitools.codegen.CodegenMediaType;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenResponse;

import java.util.Map;

public class JsonResponseProcessor implements ResponseProcessor {
    EnumProcessorFactory enumProcessorFactory = EnumProcessorFactory.getInstance();
    RecursiveModelProcessor recursiveModelProcessor = new RecursiveModelProcessor();
    @Override
    public void process(final CodegenOperation codegenOperation) {
        System.out.println(codegenOperation.operationId);
        
        // delete operation does not have response body
        if (codegenOperation.operationId.toLowerCase().startsWith("delete")) return;
        
        CodegenModel codegenModel = getModel(codegenOperation);
        if (codegenModel == null) return;
        CodegenModel responseModel = codegenModel;

        if (codegenOperation.operationId.toLowerCase().startsWith("list")) {
            responseModel = getModelFromListOperation(codegenModel);
            if (responseModel == null) return;
        }
        
        responseModel.vars.forEach(codegenProperty -> {
            enumProcessorFactory.applyProcessor(codegenProperty);
            Deserializer.addDeserializer(codegenProperty);
        });
        responseModel.vars.stream().forEach(property -> recursiveModelProcessor.process(property));
        
        // Adding responseModel vars to cache
        responseModel.vars.forEach(ResourceCacheContext.get().getResponse()::add);
    }

    //model.vars.get(0).items.ref to get actual output
    private CodegenModel getModelFromListOperation(CodegenModel codegenModel) {
        System.out.println(codegenModel);
        for (CodegenProperty codegenProperty: codegenModel.vars) {
            // We expect list operation to have pagination.
            if (codegenProperty.isContainer) {
                String ref = codegenProperty.items.getRef();
                CodegenModel responseModel = Utility.getModelFromRef(ref);
                return responseModel;
            }
        }
        return null;
    }

    public boolean shouldProcess(final CodegenOperation codegenOperation) {
        if (ResourceCacheContext.get().isV1()) return false;

        if (codegenOperation.produces != null && !codegenOperation.produces.isEmpty()) {
            for (Map<String, String> contentType : codegenOperation.produces) {
                if (getContentType().equals(contentType.get("mediaType")) || "application/scim+json".equals(contentType.get("mediaType"))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getContentType() {
        return "application/json";
    }
    
    // if $ref, model name: codegenOperation.responses.get(0).content.get("application/json").schema.getRef()
    // output: #/components/schemas/api.v2010.account.message
    private CodegenModel getModel(final CodegenOperation codegenOperation) {
        if (codegenOperation.responses != null && !codegenOperation.responses.isEmpty()) {
            for (CodegenResponse codegenResponse: codegenOperation.responses) {
                if (codegenResponse.is2xx || codegenResponse.is3xx) {
                    if (codegenResponse == null || codegenResponse.getContent() == null) return null;
                    CodegenMediaType codegenMediaType = codegenResponse.getContent().get(getContentType());
                    if (codegenMediaType == null) {
                        codegenMediaType = codegenResponse.getContent().get("application/scim+json");
                        if (codegenMediaType == null) return null;
                    }

                    if (codegenMediaType.getSchema().isContainer) {
                        // It covers special case in which response is list
                        // TODO: Handle in future.
                    }
                    String ref = codegenMediaType.getSchema().getRef();
                    if (ref == null) return null;
                    CodegenModel model = Utility.getModelFromRef(ref);
                    return model;
                    
                }
            }
        }
        return null;
    }
}
