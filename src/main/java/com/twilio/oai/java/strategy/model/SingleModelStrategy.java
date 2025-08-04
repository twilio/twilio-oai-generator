package com.twilio.oai.java.strategy.model;


import com.twilio.oai.common.StringUtils;
import com.twilio.oai.java.nestedmodels.MustacheModel;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import static com.twilio.oai.common.ApplicationConstants.DOT;
import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.X_MODEL_TYPE;
import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;
import static com.twilio.oai.common.EnumConstants.ModelType;
public class SingleModelStrategy implements ModelIdentifierStrategy {
    ModelType type = ModelType.SINGLE;
    @Override
    public boolean identify(CodegenProperty codegenProperty, CodegenModel codegenModel) {
        if (codegenProperty.isContainer) return false;
        type(codegenProperty);
        variableName(codegenProperty);
        dataType(codegenProperty);
        cacheModelClass(codegenProperty, codegenModel);
        return true;
    }

    @Override
    public ModelType getType() {
        return type;
    }

    private void type(final CodegenProperty codegenProperty) {
        codegenProperty.vendorExtensions.put(X_MODEL_TYPE, type);
    }

    private void variableName(CodegenProperty codegenProperty) {
        // This will be used in CRUDF(creator, reader etc) classes.
        codegenProperty.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenProperty.baseName));
    }
    // basetype
    private void dataType(CodegenProperty codegenProperty) {
        //TODO: Need to identify whether the property is defined using ref or directly defined property
        codegenProperty.vendorExtensions.put(X_DATATYPE, ResourceCache.getResourceName() + DOT + codegenProperty.dataType);
    }

    private void cacheModelClass(CodegenProperty codegenProperty, CodegenModel codegenModel) {
        MustacheModel mustacheModel = new MustacheModel(codegenProperty, codegenModel);
        ResourceCache.addToModelClasses(mustacheModel);
    }
}
