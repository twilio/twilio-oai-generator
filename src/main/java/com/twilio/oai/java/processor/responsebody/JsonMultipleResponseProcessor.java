package com.twilio.oai.java.processor.responsebody;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.cache.ResourceCacheContext;
import com.twilio.oai.java.constants.MustacheConstants;
import com.twilio.oai.java.format.Deserializer;
import com.twilio.oai.java.nestedmodels.MustacheModel;
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
        CodegenModel responseModel = getModel(codegenOperation);

        if (responseModel == null) {
            codegenOperation.vendorExtensions.put(MustacheConstants.X_RESPONSE_DATATYPE, "void");
            return;
        }

        // Process enum and deserializers for each property
        responseModel.vars.forEach(codegenProperty -> {
            enumProcessorFactory.applyProcessor(codegenProperty);
            Deserializer.addDeserializer(codegenProperty);
            recursiveModelProcessor.process(codegenProperty);
        });

        // Create a MustacheModel for this response and add to cache
        String responseClassName = generateResponseClassName(codegenOperation, responseModel);
        MustacheModel mustacheModel = new MustacheModel(responseClassName,
            responseModel.vars.stream().filter(v -> v.required).collect(java.util.stream.Collectors.toList()),
            responseModel.vars);
        ResourceCacheContext.get().addToResponse(mustacheModel);

        // Set the response datatype for the operation
        codegenOperation.vendorExtensions.put(MustacheConstants.X_RESPONSE_DATATYPE, responseClassName);
    }

    private void processResponseWithPagination(CodegenOperation codegenOperation) {
        // check if pagination exists, if no, go to processResponseWithoutPagination
        Map<String, CodegenModel> codegenModelMap = ResourceCacheContext.get().getAllModelsMapByDefaultGenerator();
        CodegenProperty responseProperty = null;
        CodegenModel paginationModel = null;

        for (CodegenResponse response: codegenOperation.responses) {
            CodegenModel codegenModel = codegenModelMap.get(StringUtils.toPascalCase(response.baseType));
            if (codegenModel == null) continue;

            for (CodegenProperty codegenProperty: codegenModel.vars) {
                if (codegenProperty.name.equals(ResourceCacheContext.get().getRecordKey())) {
                    responseProperty = codegenProperty;
                    paginationModel = codegenModel;
                    break;
                }
            }
            if (responseProperty != null) break;
        }

        if (responseProperty == null) {
            codegenOperation.vendorExtensions.put(MustacheConstants.X_RESPONSE_DATATYPE, "void");
            return;
        }

        // Process the response property and create MustacheModel for pagination response
        recursiveModelProcessor.process(responseProperty);

        if (paginationModel != null) {
            // Process all properties in the pagination model
            paginationModel.vars.forEach(codegenProperty -> {
                enumProcessorFactory.applyProcessor(codegenProperty);
                Deserializer.addDeserializer(codegenProperty);
                recursiveModelProcessor.process(codegenProperty);
            });

            // Create MustacheModel for the pagination wrapper response
            String responseClassName = generateResponseClassName(codegenOperation, paginationModel);
            MustacheModel mustacheModel = new MustacheModel(responseClassName,
                paginationModel.vars.stream().filter(v -> v.required).collect(java.util.stream.Collectors.toList()),
                paginationModel.vars);
            ResourceCacheContext.get().addToResponse(mustacheModel);
        }

        String listResponseDatatype = (String)responseProperty.vendorExtensions.get(ApplicationConstants.X_DATATYPE);
        listResponseDatatype = listResponseDatatype.replaceFirst("^List(?=<)", "ResourceSet");
        codegenOperation.vendorExtensions.put(MustacheConstants.X_RESPONSE_DATATYPE, listResponseDatatype);
    }

    /**
     * Generates a unique response class name based on the operation and model.
     * For operations, we derive from the operation ID (e.g., "CreateMessages" -> "CreateMessagesResponse")
     * If the model already has a unique name, we can use it.
     */
    private String generateResponseClassName(CodegenOperation codegenOperation, CodegenModel responseModel) {
        // Use the response model's class name if it's specific enough
        String modelName = responseModel.classname;

        // Check if model name already contains "Response" or is operation-specific
        if (modelName.endsWith("Response") || modelName.contains("Operation")) {
            return modelName;
        }

        // Otherwise, generate from operation ID
        // e.g., "CreateMessages" -> "CreateMessagesResponse"
        String operationId = codegenOperation.operationId;
        if (operationId != null && !operationId.isEmpty()) {
            // Remove "List" prefix for list operations to avoid "ListMessagesResponse"
            if (operationId.startsWith("List")) {
                return operationId + "Response";
            }
            return operationId + "Response";
        }

        // Fallback to model name with Response suffix
        return modelName + "Response";
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
