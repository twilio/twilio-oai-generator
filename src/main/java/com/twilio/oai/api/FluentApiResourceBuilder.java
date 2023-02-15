package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.PathUtils;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.template.IApiActionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import static com.twilio.oai.common.ApplicationConstants.STRING;
import static com.twilio.oai.template.AbstractApiActionTemplate.API_TEMPLATE;

public abstract class FluentApiResourceBuilder extends ApiResourceBuilder {
    protected final DirectoryStructureService directoryStructureService;

    String instancePath;
    String listPath;
    List<CodegenParameter> instancePathParams;
    List<CodegenParameter> listPathParams;
    CodegenModel responseModel;

    protected final Map<String, DirectoryStructureService.DependentResource> dependents = new TreeMap<>();
    protected final Map<String, CodegenModel> models = new TreeMap<>();

    protected FluentApiResourceBuilder(final IApiActionTemplate template,
                                       final List<CodegenOperation> codegenOperations,
                                       final List<CodegenModel> allModels,
                                       final DirectoryStructureService directoryStructureService) {
        super(template, codegenOperations, allModels);
        this.directoryStructureService = directoryStructureService;
    }

    @Override
    public IApiResourceBuilder updateTemplate() {
        template.clean();
        template.add(API_TEMPLATE);
        template.addSupportVersion();

        return this;
    }

    @Override
    public ApiResourceBuilder updateOperations(final Resolver<CodegenParameter> codegenParameterIResolver) {
        super.updateOperations(codegenParameterIResolver);

        for (final CodegenOperation co : codegenOperationList) {
            co.allParams.forEach(param -> addModel(models, param.baseType, param.dataType));
            co.allParams.removeAll(co.pathParams);
            co.requiredParams.removeAll(co.pathParams);
            co.hasParams = !co.allParams.isEmpty();
            co.hasRequiredParams = !co.requiredParams.isEmpty();

            updateResourcePath(co);

            directoryStructureService
                .getResourceTree()
                .dependents(co.path)
                .forEach(dependent -> dependent.getPathItem().readOperations().forEach(operation -> {
                    final DirectoryStructureService.DependentResource dependentResource =
                        directoryStructureService.generateDependent(
                        dependent.getName(),
                        operation);
                    dependentResource.setInstanceDependent(PathUtils.isInstanceOperation(co));
                    dependents.put(dependentResource.getFilename(), dependentResource);
                }));
        }

        if (instancePath == null) {
            instancePathParams = listPathParams;
        }

        if (listPath == null) {
            final List<CodegenParameter> resourcePathParams = codegenOperationList
                .stream()
                .findFirst()
                .orElseThrow().pathParams;

            // If the resource has only "parent params", mark its dependents as list.
            if (resourcePathParams.stream().allMatch(PathUtils::isParentParam)) {
                dependents.values().forEach(dependent -> dependent.setInstanceDependent(false));
            }

            // Fill out the list path params with any "parent params".
            listPathParams = resourcePathParams.stream().filter(PathUtils::isParentParam).collect(Collectors.toList());
        }

        return this;
    }

    private void updateResourcePath(final CodegenOperation operation) {
        final List<CodegenParameter> resourcePathParams = new ArrayList<>();

        String path = PathUtils.removeFirstPart(operation.path);
        for (final CodegenParameter pathParam : operation.pathParams) {
            final String target = "{" + pathParam.baseName + "}";

            if (path.contains(target)) {
                path = path.replace(target, "${" + pathParam.paramName + "}");
                resourcePathParams.add(pathParam);
            }
        }

        if (PathUtils.isInstanceOperation(operation)) {
            instancePath = path;
            instancePathParams = resourcePathParams;
        } else {
            listPath = path;
            listPathParams = resourcePathParams;
        }
    }

    @Override
    public ApiResourceBuilder updateResponseModel(final Resolver<CodegenProperty> codegenPropertyResolver,
                                                  final Resolver<CodegenModel> codegenModelResolver) {
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
                codegenModelResolver.resolve(model);

                model.setName(resourceName);
                model.getVars().forEach(variable -> {
                    codegenPropertyResolver.resolve(variable);
                    variable.vendorExtensions.put("x-name", resourceName + variable.getNameInCamelCase());

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

            responseModel.getVars().forEach(variable -> addModel(models, variable.complexType, variable.dataType));
        });

        return this;
    }

    @Override
    public FluentApiResources build() {
        return new FluentApiResources(this);
    }

    @Override
    public IApiResourceBuilder setImports(final DirectoryStructureService directoryStructureService) {
        return this;
    }
}
