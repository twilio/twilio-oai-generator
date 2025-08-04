package com.twilio.oai.java.strategy.model;

import com.twilio.oai.common.Utility;
import com.twilio.oai.java.format.Deserializer;
import com.twilio.oai.java.format.Promoter;
import com.twilio.oai.java.processor.EnumProcessor;
import com.twilio.oai.java.strategy.enums.parameter.EnumIdentifierHandler;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;

public class RecursiveModelProcessor {
    EnumProcessor enumProcessor = EnumProcessor.getInstance();
    EnumIdentifierHandler enumIdentifierHandler = new EnumIdentifierHandler();
    ModelProcessor modelProcessor = new ModelProcessor();

    public void processBody(CodegenOperation codegenOperation) {
        // codegenOperation.bodyParam.vars.get(3).ref: #/components/schemas/types
        codegenOperation.bodyParam.vars.forEach(property -> processModelRecursively(property));
    }

    public void processResponse(CodegenOperation codegenOperation) {
        // Make sure to filter pagination models.
        return ;
    }

    
    // DFS based recursive logic
    private void processModelRecursively(CodegenProperty codegenProperty) {
        CodegenModel codegenModel = Utility.getModelFromOpenApiType(codegenProperty);
        /*
        This code block has access to all the codegenProperty for a nested model.
        Add your logic to process the property.
        Start
        */
        
        
        if (isEnum(codegenProperty)) {
            // Logic 1: Enum Logic
            processEnum(codegenProperty);
            return;
        }
        if (codegenModel == null) {
            // For non ref models, CodegenModel will be present. Non model nor enum.
            // Logic 3: Normal variable logic
            Deserializer.addDeserializer(codegenProperty);

            return;
        }
        // Logic 2: nested model logic
        modelProcessor.process(codegenProperty, codegenModel);

        /* 
        End
         */
        
        
        
        // A Model has been identified, look for child models
        if (codegenModel.vars != null && !codegenModel.vars.isEmpty()) {
            for (CodegenProperty modelProperty : codegenModel.vars) {
                processModelRecursively(modelProperty);
            }
        }
    }



    private boolean isEnum(CodegenProperty codegenProperty) {
        return enumIdentifierHandler.identify(codegenProperty);
    }

    private void processEnum(CodegenProperty codegenProperty) {
        enumProcessor.process(codegenProperty);
    }


}
