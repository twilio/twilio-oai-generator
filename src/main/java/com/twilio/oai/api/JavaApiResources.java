package com.twilio.oai.api;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JavaApiResources extends ApiResources{
    public long serialVersionUID;
    public CodegenModel responseModel;

    List<IJsonSchemaValidationProperties> enums;

    ArrayList<List<CodegenProperty>> modelParameters;

    public JavaApiResources(JavaApiResourceBuilder apiResourceBuilder) {
        super(apiResourceBuilder);
        this.responseModel = apiResourceBuilder.responseModel;
        this.serialVersionUID = apiResourceBuilder.serialVersionUID;
        this.enums = new ArrayList<>(apiResourceBuilder.enums);
        // TODO: Need to improve, if there are requestBody in create and update operation.
        this.modelParameters = apiResourceBuilder.modelParameters;
        
    }
}
