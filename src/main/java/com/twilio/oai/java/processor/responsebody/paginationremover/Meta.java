package com.twilio.oai.java.processor.responsebody.paginationremover;

import com.twilio.oai.common.Utility;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenResponse;

public class Meta extends ResponsePaginationRemover {
    @Override
    public boolean shouldProcess(CodegenOperation codegenOperation) {
        // Check if in the response body there is a "meta" property
        System.out.println(codegenOperation);
        for (CodegenResponse response: codegenOperation.responses) {
            // choose only that response which is application/json
            response.getContent().forEach((contentType, mediaType) -> {
                System.out.println("Content Type: " + contentType);
                System.out.println("Media Type: " + mediaType);
                CodegenProperty codegenProperty = Utility.getPropertyFromMediaType(mediaType);
//                if (mediaType.getSchema() != null && mediaType.getSchema().getProperties() != null) {
//                    mediaType.getSchema().getProperties().forEach((propertyName, propertySchema) -> {
//                        if ("meta".equals(propertyName)) {
//                            // found meta property
//                            return;
//                        }
//                    });
//                }
            });
        }
        return false;
    }

    @Override
    public void removePagination(CodegenOperation codegenOperation) {

    }
}
