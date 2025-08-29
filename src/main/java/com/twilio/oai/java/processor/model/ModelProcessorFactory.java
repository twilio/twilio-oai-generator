package com.twilio.oai.java.processor.model;

import com.twilio.oai.java.feature.datamodels.DataModelManager;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;

public class ModelProcessorFactory {

    private static ModelProcessorFactory instance;
    private final List<ModelProcessor> modelProcessors;
    private DataModelManager dataModelManager = DataModelManager.getInstance();
    public static synchronized ModelProcessorFactory getInstance() {
        if (instance == null) {
            synchronized (ModelProcessorFactory.class) {
                if (instance == null) {
                    instance = new ModelProcessorFactory();
                }
            }
        }
        return instance;
    }

    private ModelProcessorFactory() {
        modelProcessors = List.of(
            new SingleModelStrategy(),
            new ListModelStrategy()
        );
    }
    
    public void applyProcessor(CodegenProperty codegenProperty, CodegenModel codegenModel) {
        
        // Process 
        for (ModelProcessor modelProcessor : modelProcessors) {
            if (modelProcessor.shouldProcess(codegenProperty, codegenModel)) {
                modelProcessor.process(codegenProperty, codegenModel);
                return;
            }
        }
    }
}
