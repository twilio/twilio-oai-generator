package com.twilio.oai.java.processor.model.parameter;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.java.cache.ResourceCacheContext;
import com.twilio.oai.java.nestedmodels.MustacheModel;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;

import static com.twilio.oai.common.ApplicationConstants.DOT;
import static com.twilio.oai.common.ApplicationConstants.X_MODEL_TYPE;
import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;

public class SingleModelStrategy implements ModelProcessor {
    EnumConstants.ModelType type = EnumConstants.ModelType.SINGLE;

    @Override
    public boolean shouldProcess(CodegenParameter codegenParameter, CodegenModel codegenModel) {
        if (!codegenParameter.isContainer) return true;
        return false;
    }

    @Override
    public void process(CodegenParameter codegenParameter, CodegenModel codegenModel) {
        if (codegenParameter.isContainer) return;
        type(codegenParameter);
        variableName(codegenParameter);
        dataType(codegenParameter);
        cacheModelClass(codegenParameter, codegenModel);
    }

    public EnumConstants.ModelType getType() {
        return type;
    }

    private void type(final CodegenParameter codegenParameter) {
        codegenParameter.vendorExtensions.put(X_MODEL_TYPE, type);
    }

    private void variableName(CodegenParameter codegenParameter) {
        // This will be used in CRUDF(creator, reader etc) classes.
        codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
    }
    // basetype
    private void dataType(CodegenParameter codegenParameter) {
        //TODO: Need to identify whether the property is defined using ref or directly defined property
        //codegenParameter.vendorExtensions.put(X_DATATYPE, ResourceCacheContext.get().getResourceName() + DOT + codegenParameter.dataType);
        codegenParameter.dataType = ResourceCacheContext.get().getResourceName() + DOT + codegenParameter.dataType;
    }

    private void cacheModelClass(CodegenParameter codegenParameter, CodegenModel codegenModel) {
        MustacheModel mustacheModel = new MustacheModel(codegenParameter, codegenModel);
        ResourceCacheContext.get().addToModelClasses(mustacheModel);
    }

}
