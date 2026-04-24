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
        // delete operation does not have response body unless explicitly defined in the spec
        if (codegenOperation.operationId.toLowerCase().startsWith("delete") && !hasResponseBody(codegenOperation)) return;
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
        } else if (operationId.toLowerCase().startsWith("delete")) {
            codegenModel.vars.forEach(ResourceCacheContext.get().getResponseDelete()::add);
        } else if (operationId.toLowerCase().startsWith("patch")) {
            codegenModel.vars.forEach(ResourceCacheContext.get().getResponsePatch()::add);
        } else if (operationId.toLowerCase().startsWith("list")) {
            resolveListResponseProperties(codegenModel, allModels);
        } else if (operationId.toLowerCase().startsWith("fetch")) {
            codegenModel.vars.forEach(ResourceCacheContext.get().getResponseFetch()::add);
        }
    }

    private void resolveListResponseProperties(CodegenModel codegenModel, List<CodegenModel> allModels) {
        String recordKey = ResourceCacheContext.get().getRecordKey();
        for (CodegenProperty property : codegenModel.vars) {
            if (property.isContainer && property.baseName.equals(recordKey)) {
                CodegenModel itemModel = resolveItemModel(property, allModels);
                if (itemModel != null) {
                    for (CodegenProperty itemProp : itemModel.vars) {
                        recursiveModelProcessor.process(itemProp);
                    }
                    itemModel.vars.forEach(ResourceCacheContext.get().getResponseList()::add);
                    return;
                }
            }
        }
        codegenModel.vars.stream()
                .filter(property -> !"meta".equalsIgnoreCase(property.baseName))
                .forEach(ResourceCacheContext.get().getResponseList()::add);
    }

    private CodegenModel resolveItemModel(CodegenProperty arrayProperty, List<CodegenModel> allModels) {
        if (arrayProperty.items == null) return null;

        String complexType = arrayProperty.items.getComplexType();
        if (complexType != null) {
            for (CodegenModel model : allModels) {
                if (complexType.equals(model.classname)) {
                    return model;
                }
            }
        }

        String ref = arrayProperty.items.getRef();
        if (ref != null) {
            return Utility.getModelFromRef(ref);
        }

        String dataType = arrayProperty.items.dataType;
        if (dataType != null) {
            for (CodegenModel model : allModels) {
                if (dataType.equals(model.classname)) {
                    return model;
                }
            }
        }

        return null;
    }

    private boolean hasResponseBody(final CodegenOperation codegenOperation) {
        if (codegenOperation.responses == null) return false;
        return codegenOperation.responses.stream()
                .filter(r -> r.is2xx || r.is3xx)
                .anyMatch(r -> r.getContent() != null && !r.getContent().isEmpty());
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
