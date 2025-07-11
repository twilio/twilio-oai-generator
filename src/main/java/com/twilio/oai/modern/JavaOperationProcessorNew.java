package com.twilio.oai.modern;

import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenResponse;

public class JavaOperationProcessorNew {
    JavaEnumResolverNew javaEnumResolverNew = new JavaEnumResolverNew();
    
    public void process(CodegenOperation codegenOperation) {
        processParameter(codegenOperation);
    }
    public void processParameter(CodegenOperation codegenOperation) {
//        for (CodegenResponse response : codegenOperation.responses) {
//            System.out.println(response);
//        }
//        ResourceCache.allModels.get(0).vars.forEach(var -> javaEnumResolverNew.resolve(var));
        codegenOperation.queryParams.forEach(param -> javaEnumResolverNew.resolve(param));
        codegenOperation.pathParams.forEach(param -> javaEnumResolverNew.resolve(param));
        codegenOperation.headerParams.forEach(param -> javaEnumResolverNew.resolve(param));
        codegenOperation.formParams.forEach(param -> javaEnumResolverNew.resolve(param));
        

    }
}
