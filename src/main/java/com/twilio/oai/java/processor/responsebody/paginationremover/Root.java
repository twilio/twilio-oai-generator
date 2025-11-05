package com.twilio.oai.java.processor.responsebody.paginationremover;

import org.openapitools.codegen.CodegenOperation;

public class Root extends ResponsePaginationRemover {
    @Override
    public boolean shouldProcess(CodegenOperation codegenOperation) {
        // check if the response body is the pagination model itself
        System.out.println(codegenOperation);
        return true;
    }

    @Override
    public void removePagination(final CodegenOperation codegenOperation) {
        // No-op
    }
}
