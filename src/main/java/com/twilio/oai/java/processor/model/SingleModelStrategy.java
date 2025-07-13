package com.twilio.oai.java.processor.model;


import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import static com.twilio.oai.common.ApplicationConstants.DOT;
import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.X_IS_MODEL;
import static com.twilio.oai.common.ApplicationConstants.X_MODEL_TYPE;
import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;
import static com.twilio.oai.common.EnumConstants.ModelType;
public class SingleModelStrategy implements ModelIdentifierStrategy {
    ModelType type = ModelType.SINGLE;
    @Override
    public boolean identify(CodegenProperty codegenProperty) {
        if (codegenProperty.isContainer) return false;
        type(codegenProperty);
        variableName(codegenProperty);
        dataType(codegenProperty);
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
        codegenProperty.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenProperty.baseName));
    }
    // basetype
    private void dataType(CodegenProperty codegenProperty) {
        //TODO: Need to identify whether the property is defined using ref or directly defined property
        codegenProperty.vendorExtensions.put(X_DATATYPE, ResourceCache.getResourceName() + DOT + codegenProperty.dataType);
    }
}
