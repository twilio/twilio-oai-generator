package com.twilio.oai;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.node.NodeCaseResolver;
import com.twilio.oai.common.Utility;
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
import org.openapitools.codegen.IJsonSchemaValidationProperties;
import org.openapitools.codegen.SupportingFile;
import org.openapitools.codegen.languages.TypeScriptNodeClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;
import static com.twilio.oai.resource.Resource.IGNORE_EXTENSION_NAME;

public class TwilioNodeGenerator extends TypeScriptNodeClientCodegen {

    public static final String VERSION_TEMPLATE = "version.mustache";
    public static final String FILENAME_EXTENSION = ".ts";

    private final TwilioCodegenAdapter twilioCodegen;
    private final IResourceTree resourceTree = new ResourceMap(new Inflector());
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
        additionalProperties,
        resourceTree,
        new NodeCaseResolver());

    private final Map<String, String> modelFormatMap = new HashMap<>();
    private final NodeConventionResolver conventionResolver = new NodeConventionResolver();

    private final List<CodegenModel> allModels = new ArrayList<>();

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
        resourceTree.getResources().forEach(resource -> resource.updateFamily(openAPI, resourceTree));

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

        for (final ModelsMap mods : results.values()) {
            final List<ModelMap> modList = mods.getModels();

            // Add all the models to the local models list.
            modList
                .stream()
                .map(ModelMap::getModel)
                .map(CodegenModel.class::cast)
                .collect(Collectors.toCollection(() -> this.allModels));
        }

        Utility.setComplexDataMapping(this.allModels, this.modelFormatMap);
        this.allModels.forEach(model -> model.setClassname(removeEnumName(model.getClassname())));

        // Return an empty collection so no model files get generated.
        return new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        final List<CodegenOperation> opList = directoryStructureService.processOperations(results);
        if (directoryStructureService.isVersionLess()) {
            apiTemplateFiles.put(VERSION_TEMPLATE, FILENAME_EXTENSION);
        }

        final Map<String, Object> resources = new HashMap<>();
        final Map<String, CodegenModel> models = new HashMap<>();

        final boolean hasInstanceOperations = opList.stream().anyMatch(PathUtils::isInstanceOperation);

        // iterate over the operation and perhaps modify something
        boolean hasPaginationOperation = false;
        for (final CodegenOperation co : opList) {
            // Group operations by resource.
            final String[] filePathArray = co.baseName.split(PATH_SEPARATOR_PLACEHOLDER);

            final String itemName = filePathArray[filePathArray.length - 1];
            final String instanceName = itemName + "Instance";
            co.returnType = instanceName;
            final boolean isInstanceOperation = PathUtils.isInstanceOperation(co);

            String resourceName;
            String parentResourceName = null;

            co.returnType = instanceName;

            updateCodeOperationParams(co);
            if (isInstanceOperation) {
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

            co.allParams.forEach(param -> param.dataType = resolveModelDataType(param, param.dataType, models));
            co.allParams.removeAll(co.pathParams);
            co.requiredParams.removeAll(co.pathParams);
            co.hasParams = !co.allParams.isEmpty();
            co.hasRequiredParams = !co.requiredParams.isEmpty();
            co.queryParams.forEach(this::addSerializeVendorExtension);
            co.formParams.forEach(this::addSerializeVendorExtension);
            co.httpMethod = co.httpMethod.toLowerCase();

            final Map<String, Object> dependentMap = PathUtils.getStringMap(resource, "dependents");
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

            if (isInstanceOperation || (!hasInstanceOperations )) {
                co.responses
                    .stream()
                    .map(response -> response.dataType)
                    .filter(Objects::nonNull)
                    .map(this::getModel)
                    .flatMap(Optional::stream)
                    .map(conventionResolver::resolveModel)
                    .map(item -> conventionResolver.resolveComplexType(item, modelFormatMap))
                    .forEach(model -> {
                        model.vars.forEach(prop -> prop.dataType = resolveModelDataType(prop, prop.dataType, models));

                        model.setName(itemName);
                        resource.put("responseModel", model);

                        model
                            .getVars()
                            .forEach(variable -> {
                                variable.vendorExtensions.put("x-name",
                                        itemName +
                                                variable.getNameInCamelCase());
                                addDeserializeVendorExtension(variable);
                            });
                    });
            }
        }

        resources.values().stream().map(resource -> (Map<String, Object>) resource).forEach(resource -> {
            final String parentResourceName = (String) resource.get("parentResourceName");
            if (parentResourceName != null) {
                final Map<String, Object> parentResource = PathUtils.getStringMap(resources, parentResourceName);
                parentResource.put("instanceResource", resource);
            }

            PathUtils.flattenStringMap(resource, "dependents");
        });

        results.put("resources", resources.values());
        results.put("hasPaginationOperation", hasPaginationOperation);
        results.put("models", models.values());
        return results;
    }

    private String resolveModelDataType(final IJsonSchemaValidationProperties prop,
                                        final String dataType,
                                        final Map<String, CodegenModel> models) {
        final String modelDataType = removeEnumName(dataType);

        if (prop.getComplexType() != null) {
            addModel(models, removeEnumName(prop.getComplexType()));
        } else {
            addModel(models, modelDataType);
        }

        return modelDataType;
    }

    private String removeEnumName(final String dataType) {
        return dataType.replace("Enum", "");
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

    private void addModel(final Map<String, CodegenModel> models, final String dataType) {
        getModel(dataType).ifPresent(model -> {
            if (models.putIfAbsent(model.getClassname(), model) == null) {
                model.getVars().forEach(property -> addModel(models, property.dataType));
            }
        });
    }

    private void addSerializeVendorExtension(CodegenParameter param) {
        if (param.isDate) {
            param.vendorExtensions.put("x-serialize", "serialize.iso8601Date");
        }
        if (param.isDateTime) {
            param.vendorExtensions.put("x-serialize", "serialize.iso8601DateTime");
        }
        if (param.isFreeFormObject) {
            if (param.dataFormat != null && param.dataFormat.startsWith("prefixed-collapsible-map")) {
                param.vendorExtensions.put("x-serialize", "serialize.prefixedCollapsibleMap");
                String[] formatArray = param.dataFormat.split("-");
                param.vendorExtensions.put("x-prefixed-collapsible-map", formatArray[formatArray.length-1]);
            }
            else {
                param.vendorExtensions.put("x-serialize", "serialize.object");
            }
        }
        if (param.isBoolean) {
            param.vendorExtensions.put("x-serialize", "serialize.bool");
        }

        if (param.isArray) {
            param.vendorExtensions.put("x-serialize", "serialize.map");
            param.vendorExtensions.put("x-is-array", true);
        }
    }

    private void addDeserializeVendorExtension(CodegenProperty variable) {
        if (variable.dataFormat != null && variable.dataFormat.equals("date")) {
            variable.vendorExtensions.put("x-deserialize", "deserialize.iso8601Date");
        }
        if (variable.dataFormat != null && variable.dataFormat.equals("date-time")) {
            variable.vendorExtensions.put("x-deserialize", "deserialize.iso8601DateTime");
        }
        if (variable.dataFormat != null && variable.dataFormat.equals("date-time-rfc-2822")) {
            variable.vendorExtensions.put("x-deserialize", "deserialize.rfc2822DateTime");
        }
        if (variable.isInteger) {
            variable.vendorExtensions.put("x-deserialize", "deserialize.integer");
        }
        if (variable.isDecimal) {
            variable.vendorExtensions.put("x-deserialize", "deserialize.decimal");
        }
    }

    private Optional<CodegenModel> getModel(final String modelName) {
        return allModels.stream().filter(model -> model.getClassname().equals(modelName)).findFirst();
    }

    private void addOperationName(final CodegenOperation operation, final String name) {
        operation.vendorExtensions.put("x-name", name);
        operation.vendorExtensions.put("x-name-lower", name.toLowerCase());
    }

    private void updateCodeOperationParams(final CodegenOperation co) {
        co.allParams = co.allParams
                .stream()
                .map(conventionResolver::resolveParameter)
                .map(Optional::get)
                .collect(Collectors.toList());
        co.pathParams = co.pathParams
                .stream()
                .map(conventionResolver::resolveParameter)
                .map(Optional::get)
                .collect(Collectors.toList());
        co.optionalParams = co.optionalParams
                .stream()
                .map(conventionResolver::resolveParameter)
                .map(Optional::get)
                .collect(Collectors.toList());
        co.requiredParams = co.requiredParams
                .stream()
                .map(conventionResolver::resolveParameter)
                .map(Optional::get)
                .collect(Collectors.toList());
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
        return super.toVarName(twilioCodegen.toParamName(name));
    }
}
