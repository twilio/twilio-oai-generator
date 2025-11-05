package com.twilio.oai.java.processor.responsebody.paginationremover;

import org.openapitools.codegen.CodegenOperation;

public abstract class ResponsePaginationRemover {
    abstract boolean shouldProcess(final CodegenOperation codegenOperation);

    void removePagination(final CodegenOperation codegenOperation) {
        
    }
}
