package com.twilio.oai.java.processor.responsebody.paginationremover;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;

public abstract class ResponsePaginationRemover {
    abstract boolean shouldProcess(final CodegenOperation codegenOperation);

    abstract CodegenProperty getResponse(final CodegenOperation codegenOperation);
}
