package com.twilio.oai.java.processor.request;

import com.twilio.oai.LoggerUtil;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.java.processor.parameter.enumidentifcation.EnumIdentifierHandler;
import com.twilio.oai.java.processor.property.ModelResolver;
import com.twilio.oai.java.processor.property.PropertyEnumIdentifier;
import org.openapitools.codegen.CodegenOperation;

public class JsonProcessor implements RequestBodyProcessor {
    final PropertyEnumIdentifier propertyEnumIdentifier = new PropertyEnumIdentifier();
    
    final RecursiveTraversor recursiveTraversor = new RecursiveTraversor();
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
        //codegenOperation.bodyParam.vars.forEach(property -> propertyEnumIdentifier.identify(property));
        codegenOperation.bodyParam.vars.forEach(property -> {
            recursiveTraversor.traverse(property);
        });
    }

    @Override
    public String getContentType() {
        return "application/json";
    }
}
