package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.common.CodegenModelResolver;
import com.twilio.oai.resolver.java.JavaPropertyResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;

public interface IApiResourceBuilder {
    IApiResourceBuilder updateTemplate();
    IApiResourceBuilder updateOperations(Resolver<CodegenParameter> codegenParameterIResolver);
    IApiResourceBuilder updateResponseModel(Resolver<CodegenProperty> codegenPropertyIResolver);

    default IApiResourceBuilder updateResponseModel(Resolver<CodegenProperty> codegenPropertyIResolver, Resolver<CodegenModel> codegenModelResolver){
        return updateResponseModel( codegenPropertyIResolver);
    }
    IApiResourceBuilder updateAdditionalProps(DirectoryStructureService directoryStructureService);
    IApiResourceBuilder updateApiPath();
    IApiResourceBuilder setImports(DirectoryStructureService directoryStructureService);
    <T extends ApiResources> T build();

    IApiResourceBuilder updateModel(Resolver<CodegenModel> codegenModelResolver);
}
