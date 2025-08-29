package com.twilio.oai.java.feature.datamodels;

import org.openapitools.codegen.CodegenModel;

public class OneOf implements DataModel {

    @Override
    public void apply(final CodegenModel codegenModel) {
        System.out.println(codegenModel);
        
    }

    @Override
    public boolean shouldApply(final CodegenModel codegenModel) {
        if (codegenModel == null || codegenModel.oneOf == null || codegenModel.oneOf.isEmpty()) {
            return false;
        }
        return true;
    }
}
