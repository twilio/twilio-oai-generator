package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.resolver.ISchemaResolver;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public interface IApiResourceBuilder {
    IApiResourceBuilder updateOperations(ISchemaResolver<CodegenParameter> codegenParameterIResolver);
    IApiResourceBuilder updateResponseModel(ISchemaResolver<CodegenProperty> codegenPropertyIResolver);
    IApiResourceBuilder updateAdditionalProps(DirectoryStructureService directoryStructureService);
    IApiResourceBuilder updateApiPath();
    ApiResources build();
}
