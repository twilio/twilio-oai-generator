package com.twilio.oai.java.feature;

import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

import java.util.ArrayList;
import java.util.List;

/*
 This class builds the constructor parameters from mandatory fields from parameter(path, query, header), request body and conditional fields
 from request body.
 */
public class FormConstructorBuilder implements OperationFeature {

    @Override
    public void apply(CodegenOperation codegenOperation) {
        codegenOperation.vendorExtensions.put("x-java-constructor", true);
    }
}
