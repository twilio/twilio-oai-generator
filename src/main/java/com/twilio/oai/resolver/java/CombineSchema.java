package com.twilio.oai.resolver.java;


import com.twilio.oai.api.ApiResourceBuilder;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;

import java.util.ArrayList;
import java.util.List;

public class CombineSchema {
    final ApiResourceBuilder apiResourceBuilder;
    public CombineSchema(final ApiResourceBuilder apiResourceBuilder) {
        this.apiResourceBuilder = apiResourceBuilder;
    }
    
    public void combine(CodegenOperation co) {
        conditional(co);
        oneOf();
        allOf();
    }
    
    private void conditional(CodegenOperation co) {
        // With conditional parameter constructor is only applicable for request body schema
    }
    
    private void oneOf() {
        for (CodegenModel codegenModel: apiResourceBuilder.getAllModels()) {
            if (codegenModel.oneOf != null && !codegenModel.oneOf.isEmpty()) {
                codegenModel.vendorExtensions.put("x-constructor-required", true);
                List<List<CodegenProperty>> constructors = new ArrayList<>();
                codegenModel.interfaceModels.forEach(model -> {
                    constructors.add(model.vars);
                });
                codegenModel.vendorExtensions.put("constructors", constructors);
            }
            
        }
    }

    private void allOf() {

    }
}
