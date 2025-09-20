package com.twilio.oai.java.processor.requestbody;

import com.twilio.oai.LoggerUtil;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;

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
        if (!codegenOperation.getHasBodyParam()) return;
        List<CodegenParameter> bodyParams = codegenOperation.bodyParams;
        if (bodyParams.size() > 1) {
            LoggerUtil.logSevere(this.getClass().getName(), "Multiple request bodies found " + codegenOperation.operationId);
        }
        
//        if (codegenOperation.bodyParams != null && !codegenOperation.bodyParams.isEmpty() && codegenOperation.bodyParams.get(0).isOneOf) {
//            processOneOf(codegenOperation);
//            return;
//        }
        
        if (bodyParams.get(0).vars != null && !bodyParams.get(0).vars.isEmpty()) {
            processProperty(codegenOperation);
        } else {
            processOneOf(codegenOperation);
        }
    }
    
    public void processOneOf(CodegenOperation codegenOperation) {
        System.out.println(codegenOperation.bodyParams);
        CodegenParameter codegenParameter = codegenOperation.bodyParams.get(0);
        if (codegenParameter.getContent() != null && codegenParameter.getContent().get("application/json") != null) {
            CodegenProperty codegenProperty = codegenParameter.getContent().get("application/json").getSchema();
            if (codegenProperty != null) {
                recursiveModelProcessor.process(codegenProperty);
            }
        }
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
