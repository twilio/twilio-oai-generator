package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.template.IApiActionTemplate;

import java.util.List;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

public class NodeApiResourceBuilder extends FluentApiResourceBuilder {
    public NodeApiResourceBuilder(final IApiActionTemplate template,
                                  final List<CodegenOperation> codegenOperations,
                                  final List<CodegenModel> allModels,
                                  final DirectoryStructureService directoryStructureService) {
        super(template, codegenOperations, allModels, directoryStructureService);
    }

    @Override
    public ApiResourceBuilder updateOperations(final Resolver<CodegenParameter> codegenParameterIResolver) {
        super.updateOperations(codegenParameterIResolver);

        final String apiName = getApiName();
        final String resourceName = apiName + "Instance";

        for (final CodegenOperation co : codegenOperationList) {
            co.httpMethod = co.httpMethod.toLowerCase();

            if (co.nickname.startsWith("delete")) {
                addOperationName(co, "Remove");
                co.returnType = "boolean";
            } else if (co.nickname.startsWith("list")) {
                addOperationName(co, "Page");
                co.returnType = apiName + "Page";
            } else {
                co.returnType = resourceName;
            }

            dependents.values().forEach(dependent -> {
                if (dependent.getType().equals(resourceName)) {
                    dependent.setType(resourceName + "Import");
                    dependent.setClassName(resourceName + "Import");
                    dependent.setImportName(resourceName + " as " + dependent.getType());
                }
            });
        }

        return this;
    }
}
