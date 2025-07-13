package com.twilio.oai.java.processor.model;

import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.X_IS_MODEL;

import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.processor.body.JsonProcessor;
import com.twilio.oai.java.processor.parameter.enumidentifcation.EnumIdentifierHandler;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

public class RecursiveModelIdentifier {
    JsonProcessor jsonProcessor;
    EnumIdentifierHandler enumIdentifierHandler = new EnumIdentifierHandler();
    ModelIdentifierHandler modelIdentifierHandler = new ModelIdentifierHandler();
    public RecursiveModelIdentifier(JsonProcessor jsonProcessor) {
        this.jsonProcessor = jsonProcessor;
    }

    public boolean identify(CodegenModel codegenModel) {
        return false;
    }
    
    public boolean identify(CodegenProperty codegenProperty) {
        if (identifyEnum(codegenProperty)) return true;
        traverseModel(codegenProperty);
        return true;
    }
    
    
    public void traverse(CodegenProperty property) {
        
    }

    private boolean identifyEnum(CodegenProperty codegenProperty) {
        return enumIdentifierHandler.identify(codegenProperty);
    }
    
    private void traverseModel(CodegenProperty codegenProperty) {
        CodegenModel codegenModel = Utility.getModelFromOpenApiType(codegenProperty);
        if (codegenModel == null) return;
        // Process only those codegen properties that are models
        modelIdentifierHandler.identify(codegenProperty);

        // A Model has been identified, look for child models
        if (codegenModel.vars != null && !codegenModel.vars.isEmpty()) {
            for (CodegenProperty modelProperty : codegenModel.vars) {
                traverseModel(modelProperty);
            }
        }
    }


}
