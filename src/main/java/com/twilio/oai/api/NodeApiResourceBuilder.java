package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.node.NodeCodegenModelResolver;
import com.twilio.oai.template.IApiActionTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import static com.twilio.oai.common.ApplicationConstants.STRING;

public class NodeApiResourceBuilder extends FluentApiResourceBuilder {
    public NodeApiResourceBuilder(final IApiActionTemplate template,
                                  final List<CodegenOperation> codegenOperations,
                                  final List<CodegenModel> allModels,
                                  final DirectoryStructureService directoryStructureService) {
        super(template, codegenOperations, allModels, directoryStructureService);
    }

    public NodeApiResourceBuilder(final IApiActionTemplate template,
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

    @Override
    public void updateHttpMethod(CodegenOperation co) {
        // TODO: Update http method
    }

    public ApiResourceBuilder updateResponseModel(Resolver<CodegenProperty> codegenPropertyResolver, NodeCodegenModelResolver codegenModelResolver) {
        final String resourceName = getApiName();

        final List<CodegenModel> allResponseModels = codegenOperationList
                .stream()
                .flatMap(co -> co.responses
                        .stream()
                        .map(response -> response.dataType)
                        .filter(Objects::nonNull)
                        .flatMap(modelName -> getModel(modelName, co).stream())
                        .findFirst()
                        .stream())
                .collect(Collectors.toList());

        allResponseModels.stream().findFirst().ifPresent(firstModel -> {
            responseModel = firstModel;

            allResponseModels.forEach(model -> {
                codegenModelResolver.resolveResponseModel(model, this);

                model.setName(resourceName);
                model.getVars().forEach(variable -> {
                    codegenPropertyResolver.resolve(variable, this);

                    instancePathParams
                            .stream()
                            .filter(param -> param.paramName.equals(variable.name))
                            .filter(param -> param.dataType.equals(STRING))
                            .filter(param -> !param.dataType.equals(variable.dataType))
                            .forEach(param -> param.vendorExtensions.put("x-stringify", true));
                });

                if (model != responseModel) {
                    // Merge any vars from the model that aren't part of the response model.
                    model.getVars().forEach(variable -> {
                        if (responseModel.getVars().stream().noneMatch(v -> v.getName().equals(variable.getName()))) {
                            responseModel.getVars().add(variable);
                        }
                    });
                }
            });

            responseModel.getVars().forEach(variable -> {
                addModel(modelTree, variable.complexType, variable.dataType);

                super.updateDataType(variable.complexType, variable.dataType, (dataTypeWithEnum, dataType) -> {
                    variable.datatypeWithEnum = dataTypeWithEnum;
                    variable.baseType = dataType;
                });
            });
        });

        modelTree.values().forEach(model -> model.setName(getModelName(model.getClassname())));

        return this;
    }

    @Override
    public ApiResourceBuilder updateModel(Resolver<CodegenModel> codegenModelResolver) {
        super.updateModel(codegenModelResolver);
        return this;
    }

    @Override
    protected String getModelName(final String classname) {
        return removeEnumName(classname);
    }

    @Override
    protected String getDataTypeName(final String dataType) {
        return removeEnumName(dataType);
    }

    public String removeEnumName(final String dataType) {
        if (dataType != null && dataType.contains(ApplicationConstants.ENUM)) {
            return getApiName() + Utility.removeEnumName(dataType);
        }

        return dataType;
    }
}
