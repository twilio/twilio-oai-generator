package com.twilio.oai.api;

import org.openapitools.codegen.CodegenModel;

public class JavaApiResources extends ApiResources{
    public long serialVersionUID;
    public CodegenModel responseModel;
    public JavaApiResources(JavaApiResourceBuilder apiResourceBuilder) {
        super(apiResourceBuilder);
        this.responseModel = apiResourceBuilder.responseModel;
        this.serialVersionUID = apiResourceBuilder.serialVersionUID;
    }
}
