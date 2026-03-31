package com.twilio.oai.java.processor;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.java.cache.ResourceCacheContext;
import com.twilio.oai.java.feature.Inequality;
import com.twilio.oai.java.feature.SetterMethodGenerator;
import com.twilio.oai.java.feature.constructor.ConstructorFactory;
import com.twilio.oai.java.format.Promoter;
import com.twilio.oai.java.processor.auth.SecuritySchemeManager;
import com.twilio.oai.java.processor.parameter.ParameterProcessor;
import com.twilio.oai.java.processor.requestbody.RequestBodyProcessorFactory;
import com.twilio.oai.java.processor.responsebody.ResponseProcessorFactory;
import org.openapitools.codegen.CodegenOperation;

public class JavaOperationProcessor {

    public static JavaOperationProcessor instance;
    public static synchronized JavaOperationProcessor getInstance() {
        if (instance == null) {
            synchronized (JavaOperationProcessor.class) {
                if (instance == null) {
                    instance = new JavaOperationProcessor();
                }
            }
        }
        return instance;
    }

    private JavaOperationProcessor() { }

    public void process(final CodegenOperation codegenOperation) {
        SecuritySchemeManager.getInstance().applyProcessor(codegenOperation);

        ParameterProcessor.getInstance().process(codegenOperation);
        RequestBodyProcessorFactory.getInstance().process(codegenOperation);
        ResponseProcessorFactory.getInstance().process(codegenOperation);


        // All Features should be applied after processors are completed
        ConstructorFactory.getInstance().applyFeature(codegenOperation);
        SetterMethodGenerator.getInstance().apply(codegenOperation);
        Inequality.getInstance().process(codegenOperation);
        Promoter.addPromoter(codegenOperation);
        
        // It is required to set response class for v1 APIs as it is used in the common action response.
        // v0.x APIs response is equal to ResourceName
        // Created class for response because as per v0.x APIs response class must inherit from Resource class.
        if (ResourceCacheContext.get().isV1()) {
            if (codegenOperation.vendorExtensions.get("x-common-action-method") == null) {
                throw new RuntimeException("x-common-action-method is required for all APIs");
            }

            String opType = (String)codegenOperation.vendorExtensions.get("x-common-action-method");
            opType = opType.substring(0, 1).toUpperCase() + opType.substring(1);
            if ("Read".equals(opType)) opType = "List";
        
            String resourceName = ResourceCacheContext.get().getResourceName();
            codegenOperation.vendorExtensions.put(ApplicationConstants.X_RESPONSE_CLASS,  
                    resourceName + "." + opType + resourceName+ "Response");
        } else {
            codegenOperation.vendorExtensions.put(ApplicationConstants.X_RESPONSE_CLASS, 
                    ResourceCacheContext.get().getResourceName());
        }
    }
}
