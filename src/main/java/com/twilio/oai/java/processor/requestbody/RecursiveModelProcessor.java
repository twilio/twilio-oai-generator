package com.twilio.oai.java.processor.requestbody;

import com.twilio.oai.common.Utility;
import com.twilio.oai.java.feature.datamodels.DataModelManager;
import com.twilio.oai.java.format.Deserializer;
import com.twilio.oai.java.processor.enums.EnumProcessorFactory;
import com.twilio.oai.java.processor.model.ModelProcessorFactory;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;

public class RecursiveModelProcessor {
    EnumProcessorFactory enumProcessorFactory = EnumProcessorFactory.getInstance();
    ModelProcessorFactory modelProcessorFactory = ModelProcessorFactory.getInstance();

    public void processBody(CodegenOperation codegenOperation) {
        // codegenOperation.bodyParam.vars.get(3).ref: #/components/schemas/types
        codegenOperation.bodyParam.vars.forEach(property -> processModelRecursively(property));
        
    }

    public void processResponse(final CodegenOperation codegenOperation) {
        // Make sure to filter pagination models.
        return ;
    }

    
    // DFS based recursive logic
    private void processModelRecursively(final CodegenProperty codegenProperty) {
        CodegenModel codegenModel = Utility.getModelFromOpenApiType(codegenProperty);
        /*
        This code block has access to all the codegenProperty for a nested model.
        Add your logic to process the property.
        ------------------  Start ------------------ 
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
        modelProcessorFactory.applyProcessor(codegenProperty, codegenModel);

        /* 
        ------------------ End ------------------ 
         */
        
        
        
        // A Model has been identified, look for child models
        if (codegenModel.vars != null && !codegenModel.vars.isEmpty()) {
            for (CodegenProperty modelProperty : codegenModel.vars) {
                processModelRecursively(modelProperty);
            }
        }
    }

    private boolean isEnum(CodegenProperty codegenProperty) {
        return enumProcessorFactory.isEnum(codegenProperty);
    }

    private void processEnum(CodegenProperty codegenProperty) {
        enumProcessorFactory.applyProcessor(codegenProperty);
    }
}
