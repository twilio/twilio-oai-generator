package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.template.IApiActionTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

public class PythonApiResourceBuilder extends FluentApiResourceBuilder {
    public PythonApiResourceBuilder(final IApiActionTemplate template,
                                    final List<CodegenOperation> codegenOperations,
                                    final List<CodegenModel> allModels,
                                    final DirectoryStructureService directoryStructureService) {
        super(template, codegenOperations, allModels, directoryStructureService);
    }

    @Override
    public ApiResourceBuilder updateOperations(final Resolver<CodegenParameter> codegenParameterIResolver) {
        super.updateOperations(codegenParameterIResolver);

        for (final CodegenOperation co : codegenOperationList) {
            co.httpMethod = co.httpMethod.toLowerCase();

            if (co.operationId.startsWith("list")) {
                addOperationName(co, "Page");
            }

            dependents.values().forEach(dependent -> {
                dependent.setImportName(dependent.getImportName().replace("Instance", ""));
            });

        }
        return this;
    }

    @Override
    public ApiResourceBuilder updateVersionImports(){
        ArrayList<DirectoryStructureService.DependentResource> versionResources = (ArrayList<DirectoryStructureService.DependentResource>) this.directoryStructureService.getAdditionalProperties().get("versionResources");
        versionResources.forEach(versionResource -> versionResource.setImportName(versionResource.getImportName().replace("Instance", "")));
        return this;
    }
}
