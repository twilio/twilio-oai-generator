package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.template.IApiActionTemplate;

import java.util.List;
import java.util.stream.Collectors;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

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
            populateRequestBodyParams(co, codegenParameterIResolver);

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

    private void populateRequestBodyParams(final CodegenOperation co, Resolver<CodegenParameter> codegenParameterIResolver) {
        if (co.httpMethod.equals("post") && co.getHasFormParams()) {
            List<CodegenProperty> bodyParams = co.formParams.get(0).getContent().get("application/x-www-form-urlencoded").getSchema().vars;
            co.allParams = bodyParams.stream().map(p -> createCodeGenParameterFromProperty(p, codegenParameterIResolver)).collect(Collectors.toList());
        }
    }

    private CodegenParameter createCodeGenParameterFromProperty(CodegenProperty co, Resolver<CodegenParameter> codegenParameterIResolver) {
        CodegenParameter parameter = new CodegenParameter();
        parameter.isEnum = co.isEnum;
        parameter.required = co.required;
        parameter.isArray = co.isArray;
        parameter.baseName = co.baseName;
        parameter.enumName = co.enumName;
        parameter.allowableValues = co.allowableValues;
        parameter.dataType = co.dataType;
        parameter.description = co.description;
        parameter.vendorExtensions = co.vendorExtensions;
        parameter.datatypeWithEnum = co.datatypeWithEnum;
        parameter.paramName = co.name;
        return codegenParameterIResolver.resolve(parameter);
    }
}
