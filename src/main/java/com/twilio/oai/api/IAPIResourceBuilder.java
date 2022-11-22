package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.resolver.ISchemaResolver;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

public interface IAPIResourceBuilder {
    IAPIResourceBuilder template();
    IAPIResourceBuilder operations(ISchemaResolver<CodegenParameter> codegenParameterIResolver);
    IAPIResourceBuilder responseModel(ISchemaResolver<CodegenProperty> codegenPropertyIResolver);
    IAPIResourceBuilder additionalProps(DirectoryStructureService directoryStructureService);
    APIResources build();
}
