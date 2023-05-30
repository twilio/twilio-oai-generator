package com.twilio.oai.api;

import com.twilio.oai.resolver.csharp.OperationStore;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.ArrayList;
import java.util.List;

public class JavaApiResources extends ApiResources{
    public long serialVersionUID;
    public CodegenModel responseModel;

    List<CodegenProperty> enums;
    public JavaApiResources(JavaApiResourceBuilder apiResourceBuilder) {
        super(apiResourceBuilder);
        this.responseModel = apiResourceBuilder.responseModel;
        this.serialVersionUID = apiResourceBuilder.serialVersionUID;
        this.enums = apiResourceBuilder.enums;
    }
}
