package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.StringHelper;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.template.IApiActionTemplate;

import java.util.List;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;

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
            updateNamespaceSubPart(co);
            if (co.operationId.startsWith("list")) {
                addOperationName(co, "Page");
            }

            for (CodegenParameter cp : co.allParams) {
                if (cp.paramName.equals("_from")) {
                    cp.paramName = "from_";
                }
            }
        }
        return this;
    }

    private void updateNamespaceSubPart(CodegenOperation co) {
        String[] resourcePath = co.baseName.split(PATH_SEPARATOR_PLACEHOLDER);
        String resourceBasePath = "";
        for (String resource: resourcePath) {
            resourceBasePath += StringHelper.toSnakeCase(resource) + ".";
        }
        namespaceSubPart = resourceBasePath.substring(0,resourceBasePath.length()-1);
    }

}
