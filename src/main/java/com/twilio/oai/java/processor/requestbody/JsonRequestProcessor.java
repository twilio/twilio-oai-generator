package com.twilio.oai.java.processor.requestbody;

import com.twilio.oai.LoggerUtil;
import org.openapitools.codegen.CodegenOperation;

import static com.twilio.oai.common.ApplicationConstants.X_REQUEST_CONTENT_TYPE;
import static com.twilio.oai.java.constants.MustacheConstants.X_REQUEST_LANGUAGE_CONTENT_TYPE_CONSTANT;

public class JsonRequestProcessor implements RequestBodyProcessor {
    final RecursiveModelProcessor recursiveModelProcessor = new RecursiveModelProcessor();
    public JsonRequestProcessor() {
        // Constructor can be used for initialization if needed
    }

    @Override
    public boolean shouldProcess(final CodegenOperation codegenOperation) {
        // TODO
        if (codegenOperation.bodyParams != null && !codegenOperation.bodyParams.isEmpty()) return true;
        return false;
    }

    /*
    Note:
    In json request body parameter could appear as list of codegenProperty or codegenParameter
 */
    @Override
    public void process(CodegenOperation codegenOperation) {
        codegenOperation.vendorExtensions.put(X_REQUEST_CONTENT_TYPE, getContentType());
        codegenOperation.vendorExtensions.put(X_REQUEST_CONTENT_TYPE, getContentType());
        codegenOperation.vendorExtensions.put(X_REQUEST_LANGUAGE_CONTENT_TYPE_CONSTANT, "JSON");
        System.out.println(codegenOperation.operationId);
        if (!codegenOperation.getHasBodyParam()) return;
        if (codegenOperation.bodyParams.size() > 1) {
            LoggerUtil.logSevere(this.getClass().getName(), "Multiple request bodies found " + codegenOperation.operationId);
        }

        processParameter(codegenOperation);
        
//        if (codegenOperation.bodyParam.vars != null && codegenOperation.bodyParam.vars.size() > 0) {
//            processProperty(codegenOperation);
//        } else {
//            processParameter(codegenOperation);
//        }
    }

    @Override
    public String getContentType() {
        return "application/json";
    }
    
    
    /*
    If you see in below example, there is no property defined.
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/Cat'
    
    Cat:
      allOf:
        - type: object
          properties:
            account_sid:
              type: string
        - oneOf:
          - $ref: '#/components/schemas/One'
          - $ref: '#/components/schemas/Two'
     */
    // Process list: codegenOperation.bodyParams
    private void processParameter(CodegenOperation codegenOperation) {
        if (codegenOperation.bodyParams.isEmpty()) return;
        // Request Body has oneOf and its instance variables will be present as CodegenParameter
        codegenOperation.bodyParams.stream().forEach(codegenParameter -> recursiveModelProcessor.process(codegenParameter));
        // Used in constructor generation.
        codegenOperation.requiredParams.stream().forEach(codegenParameter -> recursiveModelProcessor.process(codegenParameter));
        
        
        
    }

    // List of instance variable can be processed by list: codegenOperation.bodyParam.vars
    private void processProperty(CodegenOperation codegenOperation) {
        if (codegenOperation.bodyParam != null && codegenOperation.bodyParam.vars.isEmpty()) return;
        codegenOperation.bodyParams.stream().forEach(codegenParameter -> recursiveModelProcessor.process(codegenParameter));
        //codegenOperation.bodyParam.vars.stream().forEach(property -> recursiveModelProcessor.processModelRecursively(property));
    }
}
