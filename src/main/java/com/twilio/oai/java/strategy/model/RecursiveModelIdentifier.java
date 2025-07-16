package com.twilio.oai.java.strategy.model;

import com.twilio.oai.common.Utility;
import com.twilio.oai.java.processor.requestbody.JsonRequestProcessor;
import com.twilio.oai.java.strategy.enums.parameter.EnumIdentifierHandler;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

public class RecursiveModelIdentifier {
    JsonRequestProcessor jsonRequestProcessor;
    EnumIdentifierHandler enumIdentifierHandler = new EnumIdentifierHandler();
    ModelIdentifierHandler modelIdentifierHandler = new ModelIdentifierHandler();
    public RecursiveModelIdentifier(JsonRequestProcessor jsonRequestProcessor) {
        this.jsonRequestProcessor = jsonRequestProcessor;
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
