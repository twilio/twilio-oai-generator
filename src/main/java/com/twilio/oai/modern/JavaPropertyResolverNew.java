package com.twilio.oai.modern;

import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public class JavaPropertyResolverNew {
    
    public void resolve(final CodegenProperty codegenProperty) {
        System.out.println(codegenProperty);
    }
    
    private void resolveContainer() {
        
    }
    
    private void resolveDirect() {
        // CodegenParameter does not have nested objects.
    }
}
