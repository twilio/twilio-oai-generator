package com.twilio.oai.java.processor.requestbody;

import com.twilio.oai.common.Utility;
import com.twilio.oai.java.cache.ResourceCacheContext;
import com.twilio.oai.java.format.Deserializer;
import com.twilio.oai.java.processor.enums.EnumProcessorFactory;
import com.twilio.oai.java.processor.model.ModelProcessorFactory;
import com.twilio.oai.java.processor.model.parameter.ParamModelProcessorManager;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public class RecursiveModelProcessor {
    EnumProcessorFactory enumProcessorFactory = EnumProcessorFactory.getInstance();
    ModelProcessorFactory modelProcessorFactory = ModelProcessorFactory.getInstance();
    ParamModelProcessorManager paramModelProcessorManager = ParamModelProcessorManager.getInstance();

    public void processBody(CodegenOperation codegenOperation) {
        // codegenOperation.bodyParam.vars.get(3).ref: #/components/schemas/types
        
        codegenOperation.bodyParam.vars.forEach(property -> processModelRecursively(property));
        
    }

    public void process(CodegenProperty codegenProperty) {
        processModelRecursively(codegenProperty);
    }
    
    public void process(CodegenParameter codegenParameter) {
        CodegenModel codegenModel = ResourceCacheContext.get().getAllModelsByDefaultGenerator().stream()
                .filter(model -> model.classname.equalsIgnoreCase(codegenParameter.dataType))
                .findFirst()
                .orElse(null);
        if (codegenModel == null) return;

        paramModelProcessorManager.applyProcessor(codegenParameter, codegenModel);

        if (codegenModel.vars != null && !codegenModel.vars.isEmpty()) {
            for (CodegenProperty modelProperty : codegenModel.vars) {
                processModelRecursively(modelProperty);
            }
        }
    }

    // Only in case of oneOf or allOf
    public void processModelRecursively(CodegenParameter codegenParameter, CodegenModel codegenModel) {
        
        
        paramModelProcessorManager.applyProcessor(codegenParameter, codegenModel);

        if (codegenModel.vars != null && !codegenModel.vars.isEmpty()) {
            for (CodegenProperty modelProperty : codegenModel.vars) {
                processModelRecursively(modelProperty);
            }
        }
    }

    public void processResponse(final CodegenOperation codegenOperation) {
        // Make sure to filter pagination models.
        return ;
    }

    
    // DFS based recursive logic
    public void processModelRecursively(final CodegenProperty codegenProperty) {
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
