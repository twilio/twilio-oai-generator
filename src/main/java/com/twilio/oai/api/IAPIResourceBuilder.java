package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.resolver.IResolver;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public interface IAPIResourceBuilder {
    IAPIResourceBuilder setTemplate();
    IAPIResourceBuilder setOperations(IResolver<CodegenParameter> codegenParameterIResolver);
    IAPIResourceBuilder setResponseModel(IResolver<CodegenProperty> codegenPropertyIResolver);
    IAPIResourceBuilder setAdditionalProps(DirectoryStructureService directoryStructureService);
    APIResources build();
}
