package com.twilio.oai.java.processor.responsebody;

import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.cache.ResourceCacheContext;
import org.openapitools.codegen.CodegenMediaType;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenResponse;

import java.util.Map;

public abstract class JsonResponseAbstractProcessor {
    protected CodegenModel getModel(final CodegenOperation codegenOperation) {
        if (codegenOperation.responses != null && !codegenOperation.responses.isEmpty()) {
            for (CodegenResponse codegenResponse: codegenOperation.responses) {
                if (codegenResponse.is2xx || codegenResponse.is3xx) {
                    if (codegenResponse == null || codegenResponse.getContent() == null) return null;
                    CodegenMediaType codegenMediaType = codegenResponse.getContent().get(getContentType()); // covers application/json
                    if (codegenMediaType == null) {
                        codegenMediaType = codegenResponse.getContent().get("application/scim+json"); // special case for Orgs API
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

    protected CodegenProperty getCodegenProperty(final CodegenOperation codegenOperation) {
        Map<String, CodegenModel> codegenModelMap = ResourceCacheContext.get().getAllModelsMapByDefaultGenerator();
        if (codegenOperation.responses != null && !codegenOperation.responses.isEmpty()) {
            for (CodegenResponse codegenResponse: codegenOperation.responses) {
                if (codegenResponse.is2xx || codegenResponse.is3xx) {
                    if (codegenResponse == null || codegenResponse.getContent() == null) return null;
                    CodegenMediaType codegenMediaType = codegenResponse.getContent().get(getContentType());
                    if (codegenMediaType == null) {
                        codegenMediaType = codegenResponse.getContent().get("application/scim+json"); // special case for Orgs API
                        if (codegenMediaType == null) return null;
                    }

                    if (codegenMediaType.getSchema().isContainer) {
                        // It covers special case in which response is list
                        // TODO: Handle in future.
                    }
                    return codegenMediaType.getSchema();
                }
            }
        }
        return null;
    }

    public String getContentType() {
        return "application/json";
    }
}
