package com.twilio.oai.java.processor.responsebody.paginationremover;

import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;

public class Root extends ResponsePaginationRemover {
    @Override
    public boolean shouldProcess(CodegenOperation codegenOperation) {
        // check if the response body is the pagination model itself
        System.out.println(codegenOperation);
        return false;
    }

    @Override
    CodegenProperty getResponse(CodegenOperation codegenOperation) {
        return null;
    }
}
