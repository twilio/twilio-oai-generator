package com.twilio.oai.java.processor.body;

import com.twilio.oai.LoggerUtil;
import com.twilio.oai.java.processor.model.RecursiveModelIdentifier;
import com.twilio.oai.java.processor.parameter.enumidentifcation.EnumIdentifierHandler;
import com.twilio.oai.java.processor.property.ModelResolver;
import org.openapitools.codegen.CodegenOperation;

public class JsonProcessor implements RequestBodyProcessor {
    final EnumIdentifierHandler enumIdentifierHandler = new EnumIdentifierHandler();
    
    final RecursiveModelIdentifier recursiveModelIdentifier = new RecursiveModelIdentifier(this);
    public JsonProcessor() {
        
        ModelResolver modelResolver = new ModelResolver();
        // Constructor can be used for initialization if needed
        
    }
    @Override
    public void process(CodegenOperation codegenOperation) {
        System.out.println(codegenOperation.operationId);
        if (!codegenOperation.getHasBodyParam()) return;
        if (codegenOperation.bodyParams.size() > 1) {
            LoggerUtil.logSevere(this.getClass().getName(), "Multiple request bodies found " + codegenOperation.operationId);
        }
        // codegenOperation.bodyParam.vars.get(3).ref: #/components/schemas/types
        codegenOperation.bodyParam.vars.forEach(property -> enumIdentifierHandler.identify(property));
        codegenOperation.bodyParam.vars.forEach(property -> {
            recursiveModelIdentifier.identify(property);
        });
    }
    
    public void resolve() {
        
    }

    @Override
    public String getContentType() {
        return "application/json";
    }
}
