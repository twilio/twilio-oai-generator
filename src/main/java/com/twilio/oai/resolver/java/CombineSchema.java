package com.twilio.oai.resolver.java;

import com.twilio.oai.api.ApiResourceBuilder;
import org.openapitools.codegen.CodegenModel;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CombineSchema {
    final List<CodegenModel> allModels;
    final ApiResourceBuilder apiResourceBuilder;
    public CombineSchema(final ApiResourceBuilder apiResourceBuilder, final List<CodegenModel> allModels) {
        this.apiResourceBuilder = apiResourceBuilder;
        this.allModels = allModels;
    }
    
    public void combine() {
        oneOf();
        allOf();
    }
    
    private void oneOf() {
        for (CodegenModel codegenModel: allModels) {
            if (codegenModel.oneOf != null && !codegenModel.oneOf.isEmpty()) {
                codegenModel.vendorExtensions.put("x-constructor-required", true);
                codegenModel.vendorExtensions.put("constructors", codegenModel.interfaceModels);
                // models are already added to nested model
            }
        }
    }

    private void allOf() {

    }
}
