package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.PathUtils;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resource.Resource;
import com.twilio.oai.template.IApiActionTemplate;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;
import static com.twilio.oai.common.EnumConstants.Operation;

public abstract class ApiResourceBuilder implements IApiResourceBuilder {
    public static final String META_LIST_PARAMETER_KEY = "x-list-parameters";
    public static final String META_CONTEXT_PARAMETER_KEY = "x-context-parameters";
    public static final String NESTED_CONTENT_TYPE = "application/json";

    protected final IApiActionTemplate template;
    @Getter
    protected final List<CodegenModel> allModels;
    protected final List<CodegenOperation> codegenOperationList;
    protected final Map<String, CodegenModel> modelTree = new TreeMap<>();
    protected final List<CodegenParameter> requiredPathParams = new ArrayList<>();
    protected Set<CodegenProperty> apiResponseModels = new LinkedHashSet<>();
    protected final Map<String, Object> metaAPIProperties = new HashMap<>();
    protected final List<CodegenOperation> listOperations = new ArrayList<>();
    protected final List<CodegenOperation> instanceOperations = new ArrayList<>();
    protected Set<CodegenModel> nestedModels = new LinkedHashSet<>();
    protected String version = "";
    protected final String recordKey;
    protected String apiPath = "";
    protected String namespaceSubPart = "";
    boolean isNestedRequestBody;
    @Getter
    protected Map<String, Boolean> toggleMap = new HashMap<>();

    protected ApiResourceBuilder(IApiActionTemplate template, List<CodegenOperation> codegenOperations, List<CodegenModel> allModels) {
        this.template = template;
        this.codegenOperationList = codegenOperations;
        this.allModels = allModels;
        this.recordKey = Utility.getRecordKey(allModels, codegenOperations);
    }

    protected ApiResourceBuilder(IApiActionTemplate template, List<CodegenOperation> codegenOperations,
                                 List<CodegenModel> allModels, Set<CodegenModel> nestedModels) {
        this(template, codegenOperations, allModels);
        this.nestedModels = nestedModels;
    }

    @Override
    public ApiResourceBuilder updateOperations(Resolver<CodegenParameter> codegenParameterIResolver) {
        this.codegenOperationList.forEach(codegenOperation -> {
            updateNestedContent(codegenOperation);
            codegenOperation.allParams.forEach(param -> addModel(modelTree, param.baseType, param.dataType));

            codegenOperation.allParams.forEach(param -> resolveParam(codegenParameterIResolver, param));
            codegenOperation.pathParams.forEach(param -> resolveParam(codegenParameterIResolver, param));
            codegenOperation.queryParams.forEach(param -> resolveParam(codegenParameterIResolver, param));
            codegenOperation.formParams.forEach(param -> resolveParam(codegenParameterIResolver, param));
            codegenOperation.bodyParams.forEach(param -> resolveParam(codegenParameterIResolver, param));
            codegenOperation.requiredParams.forEach(param -> resolveParam(codegenParameterIResolver, param));
            codegenOperation.optionalParams.forEach(param -> resolveParam(codegenParameterIResolver, param));

            if (codegenOperation.vendorExtensions.containsKey("x-ignore")) {
                requiredPathParams.addAll(codegenOperation.pathParams
                        .stream()
                        .filter(PathUtils::isParentParam)
                        .filter(codegenParameter -> requiredPathParams
                                .stream()
                                .noneMatch(param -> param.baseName.equals(codegenParameter.baseName)))
                        .collect(Collectors.toList()));
            } else {
                requiredPathParams.addAll(codegenOperation.pathParams
                        .stream()
                        .filter(codegenParameter -> requiredPathParams
                                .stream()
                                .noneMatch(param -> param.baseName.equals(codegenParameter.baseName)))
                        .collect(Collectors.toList()));
            }

            codegenOperation.vendorExtensions.putAll(mapOperation(codegenOperation));
        });
        return this;
    }

    protected void resolveParam(final Resolver<CodegenParameter> codegenParameterIResolver,
                                final CodegenParameter param) {
        codegenParameterIResolver.resolve(param, this);
    }

    @Override
    public ApiResourceBuilder updateResponseModel(Resolver<CodegenProperty> codegenPropertyResolver) {
        codegenOperationList.forEach(codegenOperation -> {
            List<CodegenModel> responseModels = new ArrayList<>();
            codegenOperation.responses
                    .stream()
                    .map(response -> response.baseType)
                    .filter(Objects::nonNull)
                    .map(modelName -> this.getModel(modelName, codegenOperation))
                    .flatMap(Optional::stream)
                    .forEach(item -> {
                        item.vars.forEach(e -> codegenPropertyResolver.resolve(e, this));
                        item.allVars.forEach(e -> codegenPropertyResolver.resolve(e, this));
                        responseModels.add(item);
                    });
            this.apiResponseModels.addAll(getDistinctResponseModel(responseModels));
        });
        return this;
    }

    @Override
    public ApiResourceBuilder updateAdditionalProps(DirectoryStructureService directoryStructureService) {
        version = directoryStructureService.getApiVersionClass().get();
        return this;
    }

    @Override
    public IApiResourceBuilder updateApiPath() {
        apiPath = codegenOperationList.get(0).path;
        return this;
    }

    @Override
    public ApiResourceBuilder updateModel(Resolver<CodegenModel> codegenModelResolver) {
        if (!isNestedRequestBody) return this;
        List<CodegenParameter> parameters = new ArrayList<>();
        List<CodegenResponse> responses = new ArrayList<>();
        for (CodegenOperation co: this.codegenOperationList) {
            for (CodegenResponse cr : co.responses.stream().filter(response -> response.is2xx).collect(Collectors.toList())) {
                responses.add(cr);
            }
            parameters.addAll(co.allParams);
            parameters.addAll(co.requiredParams);
            parameters.addAll(co.queryParams);
            parameters.addAll(co.bodyParams);
            parameters.addAll(co.formParams);
        }
        // Nested parameters
        for(CodegenParameter cp : parameters) {
            final CodegenModel model = getModel(cp.dataType);
            if(model != null) {
                nestedModels.add(model);
            }
        }

        // Nested properties
        List<CodegenModel> extraProps = new ArrayList<>();
        for(CodegenModel cm : nestedModels) {
            for(CodegenProperty cp : cm.vars) {
                final CodegenModel model = getModel(cp.complexType);
                if(model != null) {
                    extraProps.add(model);
                }
            }
        }
        nestedModels.addAll(extraProps);

        // Polymorphism
        List<CodegenModel> interfaces = new ArrayList<>();
        for(CodegenModel cm : nestedModels) {
            if (cm.interfaces == null) continue;
            for (String interName: cm.interfaces) {
                final CodegenModel model = getModel(interName);
                if(model != null) {
                     interfaces.add(model);
                }
            }
        }
        nestedModels.addAll(interfaces);
        return this;
    }

    private CodegenModel getModel(String modelName ) {
        for (CodegenModel cm : this.allModels) {
            if (cm.classname.equals(modelName)) {
                return cm;
            }
        }
        return null;
    }

    @Override
    public ApiResources build() {
        return new ApiResources(this);
    }

    protected Map<String, Object> mapOperation(CodegenOperation operation) {
        final String operationType = Utility.populateCrudOperations(operation);
        metaAPIProperties.put(operationType, operation);

        Map<String, Object> operationMap = operation.vendorExtensions;
        if (PathUtils.isInstanceOperation(operation)) {
            applyInstanceOperation(operation, operationMap);
            operationMap.put("x-is-context-operation", "true");
        } else {
            applyListOperation(operation, operationMap);
            operationMap.put("x-is-list-operation", "true");
        }

        if (StringUtils.startsWithIgnoreCase(operation.operationId, "update")) {
            addOperationName(operation, Operation.UPDATE.getValue());
        } else if (StringUtils.startsWithIgnoreCase(operation.operationId, "delete")) {
            addOperationName(operation, Operation.DELETE.getValue());
        } else if (StringUtils.startsWithIgnoreCase(operation.operationId, "create")) {
            addOperationName(operation, Operation.CREATE.getValue());
        } else if (StringUtils.startsWithIgnoreCase(operation.operationId, "fetch")) {
            addOperationName(operation, Operation.FETCH.getValue());
        } else if (StringUtils.startsWithIgnoreCase(operation.operationId, "list")) {
            addOperationName(operation, Operation.READ.getValue());
        }

        operationMap.put("hasRequiredNonPathParams",
                (operation.requiredParams.stream().anyMatch(param -> !param.isPathParam)));
        operationMap.put("hasOptionalQueryParams",
                (operation.optionalParams.stream().anyMatch(param -> param.isQueryParam)));
        operationMap.put("hasOptionalHeaderParams",
                (operation.optionalParams.stream().anyMatch(param -> param.isHeaderParam)));
        operationMap.put("hasOptionalFormParams",
                (operation.optionalParams.stream().anyMatch(param -> param.isFormParam)));
        return operationMap;
    }

    private void applyListOperation(CodegenOperation operation, Map<String, Object> operationMap) {
        if (!operation.vendorExtensions.containsKey("x-ignore")) {
            operationMap.put("x-resource-name", getApiName() + "ListInstance");
            metaAPIProperties.put("x-is-list-operation", "true");
            metaAPIProperties.put(META_LIST_PARAMETER_KEY, operation.allParams);
        }
    }

    private void applyInstanceOperation(CodegenOperation operation, Map<String, Object> operationMap) {
        if (!operation.vendorExtensions.containsKey("x-ignore")) {
            operationMap.put("x-resource-name", getApiName() + "Context");
            metaAPIProperties.put("x-is-context-operation", "true");
            metaAPIProperties.put(META_CONTEXT_PARAMETER_KEY, operation.allParams);
        }
    }

    protected void addOperationName(final CodegenOperation operation, final String name) {
        operation.vendorExtensions.put("x-name", name);
        operation.vendorExtensions.put("x-name-lower", name.toLowerCase());
    }

    protected Optional<CodegenModel> getModel(final String className, final CodegenOperation codegenOperation) {
        return Utility.getModel(allModels, className, recordKey, codegenOperation);
    }

    protected Optional<CodegenModel> getModelByClassname(final String classname) {
        return Utility.getModelByClassname(allModels, classname);
    }

    protected Optional<CodegenModel> getModelbyName(final String name) {
        return Utility.getModelByName(allModels, name);
    }

    protected void addModel(final Map<String, CodegenModel> models, final String complexType, final String dataType) {
        getModelByClassname(complexType != null ? complexType : dataType).ifPresent(model -> {
            if (models.putIfAbsent(model.getClassname(), model) == null) {
                model.getVars().forEach(property -> addModel(models, property.complexType, property.dataType));
            }
        });
    }

    protected Set<CodegenProperty> getDistinctResponseModel(List<CodegenModel> responseModels) {
        Set<CodegenProperty> distinctResponseModels = new LinkedHashSet<>();
        for (CodegenModel codegenModel : responseModels) {
            for (CodegenProperty property : codegenModel.vars) {
                property.nameInCamelCase = StringHelper.camelize(property.nameInSnakeCase);
                if (Arrays
                        .stream(Operation.values())
                        .anyMatch(value -> value.getValue().equals(property.nameInCamelCase))) {
                    property.nameInCamelCase = "_" + property.nameInCamelCase;
                }
                distinctResponseModels.add(property);
            }
        }
        return distinctResponseModels;
    }

    public boolean hasPaginationOperation() {
        return codegenOperationList.stream().anyMatch(co -> co.operationId.toLowerCase().startsWith("list"));
    }

    public String getApiName() {
        final List<String> filePathArray = new ArrayList<>(Arrays.asList(codegenOperationList.get(0).baseName.split(
                PATH_SEPARATOR_PLACEHOLDER)));
        return filePathArray.remove(filePathArray.size() - 1);
    }

    static void updateDependents(DirectoryStructureService directoryStructureService, List<Resource> resourceList, List<Object> dependentList) {
        resourceList.forEach(dependent -> dependent.getPathItem().readOperations()
                .forEach(opr -> directoryStructureService.addContextdependents(dependentList,
                        dependent.getName(),
                        opr)));
        resourceList.stream().filter(dependent -> dependent.getPathItem().readOperations().isEmpty()).
                forEach(dep -> directoryStructureService.addContextdependents(dependentList, dep.getName(), null));

        dependentList.stream().map(DirectoryStructureService.ContextResource.class::cast)
            .filter(contextResource -> contextResource.getParent().matches("(.*)Function\\\\(.*)"))
                .forEach(contextResource -> contextResource.setParent(contextResource.getParent().replace("\\Function\\", "\\TwilioFunction\\")));
    }

    protected void categorizeOperations() {
        codegenOperationList.stream().filter(operation -> !operation.vendorExtensions.containsKey("x-ignore")).forEach(codegenOperation -> {
            Optional<String> pathType = Optional.ofNullable(codegenOperation.vendorExtensions.get("x-path-type").toString());
            if (pathType.isPresent()) {
                if (pathType.get().equals("list")) {
                    listOperations.add(codegenOperation);
                    codegenOperation.vendorExtensions.put("listOperation", true);
                    metaAPIProperties.put("hasListOperation", true);
                } else {
                    instanceOperations.add(codegenOperation);
                    codegenOperation.vendorExtensions.put("instanceOperation", true);
                    metaAPIProperties.put("hasInstanceOperation", true);
                }
            }
        });
    }

    protected boolean updateNestedContent(CodegenOperation co) {
        if(!isNestedRequestBody) {
            if (co.bodyParam != null && co.bodyParam.getContent() != null) {
                isNestedRequestBody = co.bodyParam.getContent().containsKey(NESTED_CONTENT_TYPE);
            }
        }
        return isNestedRequestBody;
    }
}
