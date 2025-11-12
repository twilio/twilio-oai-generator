package com.twilio.oai.java.processor.model.parameter;


import com.twilio.oai.java.processor.model.ModelProcessorFactory;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;

import java.util.List;

public class ParamModelProcessorManager {
    private static ParamModelProcessorManager instance;
    private final List<ModelProcessor> modelProcessors;
    public static synchronized ParamModelProcessorManager getInstance() {
        if (instance == null) {
            synchronized (ModelProcessorFactory.class) {
                if (instance == null) {
                    instance = new ParamModelProcessorManager();
                }
            }
        }
        return instance;
    }

    private ParamModelProcessorManager() {
        modelProcessors = List.of(
                new SingleModelStrategy()
        );
    }

    public void applyProcessor(CodegenParameter codegenParameter, CodegenModel codegenModel) {

        // Process 
        for (ModelProcessor modelProcessor : modelProcessors) {
            if (modelProcessor.shouldProcess(codegenParameter, codegenModel)) {
                modelProcessor.process(codegenParameter, codegenModel);
                return;
            }
        }
    }
}
