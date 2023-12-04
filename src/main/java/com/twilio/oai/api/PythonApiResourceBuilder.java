package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.template.IApiActionTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;

public class PythonApiResourceBuilder extends FluentApiResourceBuilder {
    public PythonApiResourceBuilder(final IApiActionTemplate template,
                                    final List<CodegenOperation> codegenOperations,
                                    final List<CodegenModel> allModels,
                                    final DirectoryStructureService directoryStructureService) {
        super(template, codegenOperations, allModels, directoryStructureService);
    }

    public PythonApiResourceBuilder(final IApiActionTemplate template,
                                    final List<CodegenOperation> codegenOperations,
                                    final List<CodegenModel> allModels,
                                    final DirectoryStructureService directoryStructureService,
                                    final Map<String, Boolean> toggleMap) {
        super(template, codegenOperations, allModels, directoryStructureService);
        this.toggleMap = toggleMap;
    }

    @Override
    public ApiResourceBuilder updateOperations(final Resolver<CodegenParameter> codegenParameterIResolver) {
        super.updateOperations(codegenParameterIResolver);
        updatePaths();
        for (final CodegenOperation co : codegenOperationList) {
            co.httpMethod = co.httpMethod.toLowerCase();
            updateNamespaceSubPart(co);
            if (co.operationId.startsWith("list")) {
                addOperationName(co, "Page");
            }

            for (CodegenParameter cp : co.allParams) {
                if (cp.paramName.equals("from")) {
                    cp.paramName = "from_";
                }
            }
        }
        nestedModels.forEach(nestedModel -> nestedModel.getVars().forEach(variable -> {
            if (variable.complexType != null) {
                getModelByClassname(variable.complexType).ifPresent(model -> {
                    variable.datatypeWithEnum = getApiName() + ApplicationConstants.LIST + ApplicationConstants.DOT + Utility.removeEnumName(variable.complexType);
                    variable.dataType = variable.dataType.replaceFirst(variable.complexType, variable.datatypeWithEnum);
                    variable.baseType = variable.dataType;
                });
            }
        }));
        return this;
    }

    @Override
    public ApiResourceBuilder updateResponseModel(Resolver<CodegenProperty> codegenPropertyResolver, Resolver<CodegenModel> codegenModelResolver) {

        super.updateResponseModel(codegenPropertyResolver, codegenModelResolver);
        if (responseModel != null) {
            responseModel.getVars().forEach(variable -> {
                if (variable.complexType != null && !variable.complexType.contains(ApplicationConstants.ENUM) && !variable.getHasVars()) {
                    getModelByClassname(variable.complexType).ifPresent(model -> {
                        variable.baseType = variable.baseType.replace(variable.datatypeWithEnum, "str");
                        variable.datatypeWithEnum = "str";
                    });
                }
            });
        }

        return this;
    }

    private void updateNamespaceSubPart(CodegenOperation co) {
        namespaceSubPart = Arrays
            .stream(co.baseName.split(PATH_SEPARATOR_PLACEHOLDER))
            .map(StringHelper::toSnakeCase)
            .collect(Collectors.joining("."));
    }

    private void updatePaths() {
        if (listPath != null) {
            listPath = listPath.replace("$", "");
        }
        if (instancePath != null) {
            instancePath = instancePath.replace("$", "");
        }
    }

    @Override
    protected String getDataTypeName(final String dataType) {
        if (dataType != null && dataType.contains(ApplicationConstants.ENUM)) {
            return '"' + getApiName() + "Instance." + Utility.removeEnumName(dataType) + '"';
        }

        return dataType;
    }

    @Override
    public ApiResourceBuilder updateModel(Resolver<CodegenModel> codegenModelResolver) {
        super.updateModel(codegenModelResolver);
        return this;
    }
}
