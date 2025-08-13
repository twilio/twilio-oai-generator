package com.twilio.oai.java.processor.model;

import com.twilio.oai.common.EnumConstants.ModelType;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.nestedmodels.MustacheModel;
import com.twilio.oai.java.ResourceCache;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import static com.twilio.oai.common.ApplicationConstants.DOT;
import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.X_MODEL_TYPE;
import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;

public class ListModelStrategy implements ModelProcessor {
    ModelType type = ModelType.LIST;
    @Override
    public void process(CodegenProperty codegenProperty, CodegenModel codegenModel) {
        if (!codegenProperty.isContainer) return;
        type(codegenProperty);
        variableName(codegenProperty);
        dataType(codegenProperty);
        cacheModelClass(codegenProperty, codegenModel);
        
    }
    
    @Override
    public boolean shouldProcess(CodegenProperty codegenProperty, CodegenModel codegenModel) {
        if (codegenProperty.isContainer) return true;
        return false;
    }
    
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
        String datatypeInsideContainer = Utility.extractDatatypeFromContainer(codegenProperty.dataType);
        String updatedDataType = Utility.replaceDatatypeInContainer(codegenProperty.dataType, ResourceCache.getResourceName() + DOT + datatypeInsideContainer);
        codegenProperty.vendorExtensions.put(X_DATATYPE, updatedDataType);
    }

    private void cacheModelClass(CodegenProperty codegenProperty, CodegenModel codegenModel) {
        MustacheModel mustacheModel = new MustacheModel(codegenProperty, codegenModel);
        ResourceCache.addToModelClasses(mustacheModel);
    }
}
