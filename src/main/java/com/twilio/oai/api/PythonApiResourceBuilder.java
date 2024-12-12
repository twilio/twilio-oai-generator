package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.python.PythonCodegenModelResolver;
import com.twilio.oai.template.IApiActionTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import static com.twilio.oai.common.ApplicationConstants.HTTP_METHOD;
import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;
import static com.twilio.oai.common.ApplicationConstants.STRING;

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

    /**
     * Processes the operations list and resolves the parameters and properties of request body.
     * Returns the resource builder with resolved request body.
     * @param codegenParameterIResolver the Python Parameter Resolver
     * @return updated PythonApiResourceBuilder
     */
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
        return this;
    }

    @Override
    public void updateHttpMethod(CodegenOperation co) {
        co.vendorExtensions.put(HTTP_METHOD, co.httpMethod);
    }

    /**
     * Processes the response body and its models to resolve nested response models.
     * Returns the resource builder with resolved response body.
     * @param codegenPropertyResolver the Python Property Resolver
     * @param codegenModelResolver the CodegenModelResolver for python
     * @return updated PythonApiResourceBuilder
     */
    public ApiResourceBuilder updateResponseModel(Resolver<CodegenProperty> codegenPropertyResolver, PythonCodegenModelResolver codegenModelResolver) {
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
                    variable.vendorExtensions.put("json-name", variable.baseName);

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

                updateDataType(variable.complexType, variable.dataType, (dataTypeWithEnum, dataType) -> {
                    variable.datatypeWithEnum = dataTypeWithEnum;
                    variable.baseType = dataType;
                });
            });
        });

        modelTree.values().forEach(model -> model.setName(getModelName(model.getClassname())));
        if (responseModel != null) {
            responseModel.getVars().forEach(variable -> {
                if (variable.complexType != null && !variable.complexType.contains(ApplicationConstants.ENUM)) {
                    getModelByClassname(variable.complexType).ifPresent(model -> {
                        variable.baseType = variable.baseType.replace(variable.datatypeWithEnum, "str");
                        variable.datatypeWithEnum = "str";
                        if(!model.vendorExtensions.containsKey("part-of-request-model"))
                            model.vendorExtensions.put("part-of-response-model", true);
                    });
                }
            });
        }

        return this;
    }

    /**
     * Processes the response body and its datatypes using parent function
     * Returns the resource builder with resolved response body.
     * @param codegenPropertyResolver the Python Property Resolver
     * @param codegenModelResolver the CodegenModelResolver for python
     * @return updated PythonApiResourceBuilder
     */
    @Override
    public PythonApiResourceBuilder updateResponseModel(final Resolver<CodegenProperty> codegenPropertyResolver,
                                                        final Resolver<CodegenModel> codegenModelResolver) {
        super.updateResponseModel(codegenPropertyResolver, codegenModelResolver);
        if (responseModel != null) {
            responseModel.getVars().forEach(variable -> {
                if (variable.complexType != null && !variable.complexType.contains(ApplicationConstants.ENUM)) {
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
