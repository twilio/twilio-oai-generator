package com.twilio.oai.modern;

import com.twilio.oai.java.processor.ParameterProcessor;
import com.twilio.oai.java.processor.Processor;
import com.twilio.oai.java.processor.RequestBodyProcessorHandler;
import com.twilio.oai.java.processor.request.RequestBodyProcessorFactory;
import org.openapitools.codegen.CodegenOperation;

import java.util.ArrayList;
import java.util.List;

public class JavaOperationProcessorNew {
    private final List<Processor> processors = new ArrayList<>();

    public JavaOperationProcessorNew() {
        // Register processors
        //processors.add(new ParameterProcessor());
        processors.add(new RequestBodyProcessorHandler());

    }
    public void process(CodegenOperation codegenOperation) {
        for (Processor processor : processors) {
            processor.process(codegenOperation);
        }
    }
    public void processParameter(final CodegenOperation codegenOperation) {
//        for (CodegenResponse response : codegenOperation.responses) {
//            System.out.println(response);
//        }
//      ResourceCache.allModels.get(0).vars.forEach(var -> javaEnumResolverNew.resolve(var));
        
        //codegenOperation.formParams.forEach(param -> javaEnumResolverNew.resolve(param));
    }
    
    public void processRequestBody(final CodegenOperation codegenOperation) {
        RequestBodyProcessorFactory requestBodyProcessorFactory = RequestBodyProcessorFactory.getInstance();
        
        
    }
}
