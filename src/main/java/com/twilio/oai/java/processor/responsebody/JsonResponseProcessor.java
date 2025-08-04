package com.twilio.oai.java.processor.responsebody;

import com.twilio.oai.common.Utility;
import com.twilio.oai.java.format.Deserializer;
import com.twilio.oai.java.processor.EnumProcessor;
import com.twilio.oai.java.processor.Processor;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenMediaType;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenResponse;

import java.util.HashMap;
import java.util.Map;

public class JsonResponseProcessor implements ResponseProcessor, Processor {
    EnumProcessor enumProcessor = EnumProcessor.getInstance();
    @Override
    public void process(CodegenOperation codegenOperation) {
        System.out.println(codegenOperation.operationId);
        
        CodegenModel codegenModel = getModel(codegenOperation);
        if (codegenModel == null) return;
        CodegenModel responseModel = codegenModel;

        if (codegenOperation.operationId.toLowerCase().startsWith("list")) {
            responseModel = getModelFromListOperation(codegenModel);
            if (responseModel == null) return;
        }
        
        responseModel.vars.forEach(codegenProperty -> {
            enumProcessor.process(codegenProperty);
            Deserializer.addDeserializer(codegenProperty);
        });
        responseModel.vars.forEach(ResourceCache.getResponse()::add);
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
        System.out.println(codegenOperation.operationId);
        if (codegenOperation.produces != null && !codegenOperation.produces.isEmpty()) {
            for (Map<String, String> contentType : codegenOperation.produces) {
                if (getContentType().equals(contentType.get("mediaType"))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getPriority() {
        return Processor.DEFAULT_PRIORITY;
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
                    if (codegenResponse == null) return null;
                    CodegenMediaType codegenMediaType = codegenResponse.getContent().get(getContentType());
                    if (codegenMediaType == null) return null;
                    String ref = codegenMediaType.getSchema().getRef();
                    CodegenModel model = Utility.getModelFromRef(ref);
                    return model;
                    
                }
            }
        }
        return null;
    }
}
