package com.twilio.oai.java.processor.responsebody;

import com.twilio.oai.common.Utility;
import com.twilio.oai.java.cache.ResourceCacheContext;
import com.twilio.oai.java.processor.requestbody.RecursiveModelProcessor;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class V1JsonResponseProcessor implements  ResponseProcessor {
    RecursiveModelProcessor recursiveModelProcessor = new RecursiveModelProcessor();
    
    @Override
    public void process(CodegenOperation codegenOperation) {
        // delete operation does not have response body
        if (codegenOperation.operationId.toLowerCase().startsWith("delete")) return;
        List<CodegenModel> allModels = ResourceCacheContext.get().getAllModelsByDefaultGenerator();
        CodegenResponse response = codegenOperation.responses.stream()
                .filter(codegenResponse -> codegenResponse.is2xx || codegenResponse.is3xx)
                .findFirst().get();
        if (response == null || response.getContent() == null) return;

        String modelName = response.dataType;
        String recordKey = ResourceCacheContext.get().getRecordKey();

        Optional<CodegenModel> responseModel = Utility.getModel(allModels, modelName, recordKey, codegenOperation);

        if ((responseModel == null) || responseModel.isEmpty() || (Integer.parseInt(response.code) >= 400)) {
            return;
        }
        CodegenModel codegenModel = responseModel.get();
        for (CodegenProperty property : codegenModel.vars) {
            recursiveModelProcessor.process(property);
        }
        String operationId = codegenOperation.operationId;
        // Adding responseModel vars to cache
        if (operationId.toLowerCase().startsWith("create")) {
            codegenModel.vars.forEach(ResourceCacheContext.get().getResponseCreate()::add);
        } else if (operationId.toLowerCase().startsWith("update")) {
            codegenModel.vars.forEach(ResourceCacheContext.get().getResponseUpdate()::add);
        } else if (operationId.toLowerCase().startsWith("patch")) {
            codegenModel.vars.forEach(ResourceCacheContext.get().getResponsePatch()::add);
        } else if (operationId.toLowerCase().startsWith("list")) {
            codegenModel.vars.forEach(ResourceCacheContext.get().getResponseList()::add);
        } else if (operationId.toLowerCase().startsWith("fetch")) {
            codegenModel.vars.forEach(ResourceCacheContext.get().getResponseFetch()::add);
        }
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public boolean shouldProcess(CodegenOperation codegenOperation) {
        return ResourceCacheContext.get().isV1();
    }
}
