package com.twilio.oai;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.node.NodeCaseResolver;
import com.twilio.oai.resolver.node.NodeConventionResolver;
import com.twilio.oai.resource.IResourceTree;
import com.twilio.oai.resource.ResourceMap;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.SupportingFile;
import org.openapitools.codegen.languages.TypeScriptNodeClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import static com.twilio.oai.common.ApplicationConstants.DEPENDENTS;
import static com.twilio.oai.common.ApplicationConstants.IGNORE_EXTENSION_NAME;
import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;
import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_EXTENSION_NAME;
import static com.twilio.oai.common.ApplicationConstants.DESERIALIZE_EXTENSION_NAME;

public class TwilioNodeGenerator extends TypeScriptNodeClientCodegen {

    private static final String VERSION_TEMPLATE = "version.mustache";
    private static final String FILENAME_EXTENSION = ".ts";
    private final TwilioCodegenAdapter twilioCodegen;
    private final IResourceTree resourceTree = new ResourceMap(new Inflector());
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
        additionalProperties,
        resourceTree,
        new NodeCaseResolver());

    private final Map<String, String> modelFormatMap = new HashMap<>();
    private final NodeConventionResolver conventionResolver = new NodeConventionResolver();

    public TwilioNodeGenerator() {
        super();

        twilioCodegen = new TwilioCodegenAdapter(this, getName());
    }

    @Override
    public void processOpts() {
        super.processOpts();

        twilioCodegen.processOpts();
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        final String domain = twilioCodegen.getDomainFromOpenAPI(openAPI);
        twilioCodegen.setDomain(StringHelper.camelize(domain, true));

        openAPI.getPaths().forEach(resourceTree::addResource);
        resourceTree.getResources().forEach(resource -> resource.updateFamily(resourceTree));

        directoryStructureService.configure(openAPI);

        directoryStructureService
            .getApiVersionClass()
            .ifPresent(apiVersionClass -> supportingFiles.add(new SupportingFile(VERSION_TEMPLATE,
                                                                                 ".." + File.separator +
                                                                                     apiVersionClass +
                                                                                     FILENAME_EXTENSION)));
    }

    @Override
    public String apiFilename(final String templateName, final String tag) {
        if (directoryStructureService.isVersionLess() && templateName.equals(VERSION_TEMPLATE)) {
            return apiFileFolder() + File.separator + directoryStructureService.getApiVersionClass().orElseThrow() +
                FILENAME_EXTENSION;
        }

        return super.apiFilename(templateName, tag);
    }

    @Override
    public String toApiFilename(final String name) {
        return directoryStructureService.toApiFilename(name);
    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(final Map<String, ModelsMap> allModels) {
        final Map<String, ModelsMap> results = super.postProcessAllModels(allModels);

        directoryStructureService.postProcessAllModels(results, modelFormatMap);

        // Return an empty collection so no model files get generated.
        return new HashMap<>();
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        final List<CodegenOperation> opList = directoryStructureService.processOperations(results);
        final String recordKey = directoryStructureService.getRecordKey(opList);
        if (directoryStructureService.isVersionLess()) {
            apiTemplateFiles.put(VERSION_TEMPLATE, FILENAME_EXTENSION);
        }

        final Map<String, Object> resources = new TreeMap<>();
        final Map<String, CodegenModel> models = new TreeMap<>();

        final boolean hasInstanceOperations = opList.stream().anyMatch(PathUtils::isInstanceOperation);

        // iterate over the operation and perhaps modify something
        boolean hasPaginationOperation = false;
        for (final CodegenOperation co : opList) {
            // Group operations by resource.
            final String[] filePathArray = co.baseName.split(PATH_SEPARATOR_PLACEHOLDER);

            final String itemName = filePathArray[filePathArray.length - 1];
            final String instanceName = itemName + "Instance";

            String resourceName;
            String parentResourceName = null;

            co.returnType = instanceName;

            updateCodeOperationParams(co);
            if (PathUtils.isInstanceOperation(co)) {
                resourceName = itemName + "Context";
                parentResourceName = itemName + "ListInstance";
            } else {
                resourceName = itemName + "ListInstance";
            }

            if (co.nickname.startsWith("update")) {
                addOperationName(co, "Update");
            } else if (co.nickname.startsWith("delete")) {
                addOperationName(co, "Remove");
                co.returnType = "boolean";
            } else if (co.nickname.startsWith("create")) {
                addOperationName(co, "Create");
            } else if (co.nickname.startsWith("fetch")) {
                addOperationName(co, "Fetch");
            } else if (co.nickname.startsWith("list")){
                hasPaginationOperation = true;
                co.returnType = itemName + "Page";
                addOperationName(co, "Page");
            }

            final Map<String, Object> resource = PathUtils.getStringMap(resources, resourceName);
            final ArrayList<CodegenOperation> resourceOperationList = getOperations(resource);
            final boolean ignoreOperation = Optional
                .ofNullable(co.vendorExtensions.get(IGNORE_EXTENSION_NAME))
                .map(Boolean.class::cast)
                .orElse(false);

            if (!ignoreOperation) {
                resourceOperationList.add(co);
            }
            resource.put("name", itemName);
            resource.put("resourceName", resourceName);
            resource.put("parentResourceName", parentResourceName);
            resource.put("instanceName", instanceName);

            updateResourcePath(resource, co);
            twilioCodegen.populateCrudOperations(resource, co);

            co.allParams.forEach(param -> addModel(param.baseType, param.dataType, models));
            co.allParams.removeAll(co.pathParams);
            co.requiredParams.removeAll(co.pathParams);
            co.hasParams = !co.allParams.isEmpty();
            co.hasRequiredParams = !co.requiredParams.isEmpty();
            co.queryParams.forEach(this::addSerializeVendorExtension);
            co.formParams.forEach(this::addSerializeVendorExtension);
            co.httpMethod = co.httpMethod.toLowerCase();

            final Map<String, Object> dependentMap = PathUtils.getStringMap(resource, DEPENDENTS);
            resourceTree
                .dependents(co.path)
                .forEach(dependent -> dependent
                    .getPathItem()
                    .readOperations()
                    .forEach(operation -> directoryStructureService.addDependent(dependentMap,
                                                                                 dependent.getName(),
                                                                                 operation)));
            dependentMap
                .values()
                .stream()
                .map(DirectoryStructureService.DependentResource.class::cast)
                .forEach(dependent -> {
                    if (dependent.getType().equals(instanceName)) {
                        dependent.setType(instanceName + "Import");
                        dependent.setClassName(instanceName + "Import");
                        dependent.setImportName(instanceName + " as " + dependent.getType());
                    }
                });
        }

        for (final String resourceName : resources.keySet()) {
            final Map<String, Object> resource = PathUtils.getStringMap(resources, resourceName);

            final String name = (String) resource.get("name");
            final String parentResourceName = (String) resource.get("parentResourceName");
            final List<CodegenOperation> operations = getOperations(resource);

            // Build the response model for this resource.
            operations
                .stream()
                .filter(operation -> !hasInstanceOperations || PathUtils.isInstanceOperation(operation))
                .flatMap(operation -> getResponseModel(operation, recordKey).stream())
                .forEach(responseModel -> {
                    resource.put("responseModel", responseModel);
                    responseModel.getVars().forEach(prop -> addModel(prop.complexType, prop.dataType, models));

                    final List<CodegenModel> allResponseModels = opList
                        .stream()
                        .flatMap(co -> getResponseModel(co, recordKey).stream())
                        .collect(Collectors.toList());
                    updateResponseModels(responseModel, allResponseModels, name);
                });

            if (parentResourceName != null) {
                final boolean parentExists = resources.containsKey(parentResourceName);
                final Map<String, Object> parentResource = PathUtils.getStringMap(resources, parentResourceName);
                parentResource.put("instanceResource", resource);

                if (!parentExists) {
                    parentResource.put("resourceName", parentResourceName);
                    parentResource.put("name", name);

                    final List<CodegenParameter> resourcePathParams = operations.get(0).pathParams;

                    // If the resource has only "parent params", move its dependents onto the parent.
                    if (resourcePathParams.stream().allMatch(PathUtils::isParentParam)) {
                        final Map<String, Object> dependents = PathUtils.getStringMap(resource, DEPENDENTS);
                        resource.remove(DEPENDENTS);
                        parentResource.put(DEPENDENTS, dependents.values());
                    }

                    // Fill out the parent's path params with any "parent params".
                    parentResource.put("resourcePathParams",
                                       resourcePathParams
                                           .stream()
                                           .filter(PathUtils::isParentParam)
                                           .collect(Collectors.toList()));
                    getOperations(parentResource);
                }
            }

            PathUtils.flattenStringMap(resource, DEPENDENTS);
        }

        results.put("resources", resources.values());
        results.put("hasPaginationOperation", hasPaginationOperation);
        results.put("models", models.values());
        return results;
    }

    private Optional<CodegenModel> getResponseModel(final CodegenOperation operation, final String recordKey) {
        return operation.responses
            .stream()
            .map(response -> response.dataType)
            .filter(Objects::nonNull)
            .flatMap(modelName -> directoryStructureService.getModelCoPath(modelName, operation, recordKey).stream())
            .map(conventionResolver::resolveModel)
            .map(item -> conventionResolver.resolveComplexType(item, modelFormatMap))
            .findFirst();
    }

    private void updateResponseModels(final CodegenModel responseModel,
                                      final List<CodegenModel> allModels,
                                      final String itemName) {
        allModels.forEach(model -> {
            model.setName(itemName);
            model.getVars().forEach(variable -> {
                variable.vendorExtensions.put("x-name", itemName + variable.getNameInCamelCase());
                addDeserializeVendorExtension(variable);
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
    }

    private void addModel(final String complexType, final String dataType, final Map<String, CodegenModel> models) {
        directoryStructureService.addModel(models, complexType != null ? complexType : dataType);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<CodegenOperation> getOperations(final Map<String, Object> resource) {
        return (ArrayList<CodegenOperation>) resource.computeIfAbsent(
            "operations",
            k -> new ArrayList<>());
    }

    private void updateResourcePath(final Map<String, Object> resource, final CodegenOperation operation) {
        final List<CodegenParameter> resourcePathParams = new ArrayList<>();

        String path = PathUtils.removeFirstPart(operation.path);
        for (final CodegenParameter pathParam : operation.pathParams) {
            final String target = "{" + pathParam.baseName + "}";

            if (path.contains(target)) {
                path = path.replace(target, "${" + pathParam.paramName + "}");
                resourcePathParams.add(pathParam);
            }
        }

        resource.put("path", path);
        resource.put("resourcePathParams", resourcePathParams);
    }

    private void addSerializeVendorExtension(CodegenParameter param) {
        if (param.isDate) {
            param.vendorExtensions.put(SERIALIZE_EXTENSION_NAME, "serialize.iso8601Date");
        }
        if (param.isDateTime) {
            param.vendorExtensions.put(SERIALIZE_EXTENSION_NAME, "serialize.iso8601DateTime");
        }
        if (param.isFreeFormObject) {
            if (param.dataFormat != null && param.dataFormat.startsWith("prefixed-collapsible-map")) {
                param.vendorExtensions.put(SERIALIZE_EXTENSION_NAME, "serialize.prefixedCollapsibleMap");
                String[] formatArray = param.dataFormat.split("-");
                param.vendorExtensions.put("x-multi-name", formatArray[formatArray.length-1]);
            }
            else {
                param.vendorExtensions.put(SERIALIZE_EXTENSION_NAME, "serialize.object");
            }
        }
        if (param.isAnyType) {
            param.vendorExtensions.put(SERIALIZE_EXTENSION_NAME, "serialize.object");
        }
        if (param.isBoolean) {
            param.vendorExtensions.put(SERIALIZE_EXTENSION_NAME, "serialize.bool");
        }

        if (param.isArray) {
            param.vendorExtensions.put(SERIALIZE_EXTENSION_NAME, "serialize.map");
            final String transform = param.baseType.equals("any") ? "serialize.object" : "";
            param.vendorExtensions.put("x-transform", transform);
        }
    }

    private void addDeserializeVendorExtension(CodegenProperty variable) {
        if (variable.dataFormat != null && variable.dataFormat.equals("date")) {
            variable.vendorExtensions.put(DESERIALIZE_EXTENSION_NAME, "deserialize.iso8601Date");
        }
        if (variable.dataFormat != null && variable.dataFormat.equals("date-time")) {
            variable.vendorExtensions.put(DESERIALIZE_EXTENSION_NAME, "deserialize.iso8601DateTime");
        }
        if (variable.dataFormat != null && variable.dataFormat.equals("date-time-rfc-2822")) {
            variable.vendorExtensions.put(DESERIALIZE_EXTENSION_NAME, "deserialize.rfc2822DateTime");
        }
        if (variable.isInteger) {
            variable.vendorExtensions.put(DESERIALIZE_EXTENSION_NAME, "deserialize.integer");
        }
        if (variable.isDecimal) {
            variable.vendorExtensions.put(DESERIALIZE_EXTENSION_NAME, "deserialize.decimal");
        }
    }

    private void addOperationName(final CodegenOperation operation, final String name) {
        operation.vendorExtensions.put("x-name", name);
        operation.vendorExtensions.put("x-name-lower", name.toLowerCase());
    }

    private void updateCodeOperationParams(final CodegenOperation operation) {
        operation.allParams.forEach(conventionResolver::resolveParameter);
        operation.pathParams.forEach(conventionResolver::resolveParameter);
        operation.optionalParams.forEach(conventionResolver::resolveParameter);
        operation.requiredParams.forEach(conventionResolver::resolveParameter);
    }

    @Override
    public String getName() {
        return EnumConstants.Generator.TWILIO_NODE.getValue();
    }

    @Override
    public String getHelp() {
        return "Generates the twilio-node helper library.";
    }

    @Override
    public String toParamName(final String name) {
        return Arrays
            .stream(twilioCodegen.toParamName(name).split("\\."))
            .map(input -> StringHelper.camelize(input, true))
            .collect(Collectors.joining("."));
    }
}
