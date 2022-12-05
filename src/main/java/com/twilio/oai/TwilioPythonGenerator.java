package com.twilio.oai;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.python.PythonConventionResolver;
import com.twilio.oai.resolver.python.PythonCaseResolver;
import com.twilio.oai.resource.IResourceTree;
import com.twilio.oai.resource.ResourceMap;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.PythonClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static com.twilio.oai.common.ApplicationConstants.*;
import static com.twilio.oai.common.ApplicationConstants.DEPENDENTS;

public class TwilioPythonGenerator extends PythonClientCodegen {
    private final TwilioCodegenAdapter twilioCodegen;
    private final IResourceTree resourceTree = new ResourceMap(new Inflector());
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
            additionalProperties,
            resourceTree,
            new PythonCaseResolver());
    private static final String INIT_TEMPLATE = "version.mustache";
    private static final String FILENAME_EXTENSION = ".py";
    private final List<CodegenModel> allModels = new ArrayList<>();
    private final Map<String, String> modelFormatMap = new HashMap<>();
    //PYTHON CHANGE
    private final PythonConventionResolver conventionResolver = new PythonConventionResolver();



    public TwilioPythonGenerator() {
        super();

        twilioCodegen = new TwilioCodegenAdapter(this, getName());
    }

    //need to figure out how python OAI generator is using engine
    @Override
    public void processOpts() {
        twilioCodegen.processOpts();
        apiTemplateFiles.clear();
        modelTemplateFiles.clear();
        apiTemplateFiles.clear();
        modelTestTemplateFiles.clear();
        modelDocTemplateFiles.clear();
        apiDocTemplateFiles.clear();
    }


    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        final String domain = twilioCodegen.getDomainFromOpenAPI(openAPI);
        twilioCodegen.setDomain(StringHelper.camelize(domain, true));

        openAPI.getPaths().forEach(resourceTree::addResource);
        resourceTree.getResources().forEach(resource -> resource.updateFamily(resourceTree));

        directoryStructureService.configure(openAPI);
        apiTemplateFiles.put("api-single.mustache", FILENAME_EXTENSION);
        supportingFiles.add(new SupportingFile(INIT_TEMPLATE,
        "__init__" + FILENAME_EXTENSION));
    }

    @Override
    public String apiFilename(final String templateName, final String tag) {
        if (directoryStructureService.isVersionLess() && templateName.equals(INIT_TEMPLATE)) {
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
        final OperationsMap results = objs;
        final List<CodegenOperation> opList = directoryStructureService.processOperations(results);

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
            co.returnType = instanceName;
            final boolean isInstanceOperation = PathUtils.isInstanceOperation(co);

            String resourceName;
            String parentResourceName = null;

            co.returnType = instanceName;

            if (isInstanceOperation) {
                resourceName = itemName + "Context";
                parentResourceName = itemName + "ListInstance";
            } else {
                resourceName = itemName + "ListInstance";
            }

            if (co.operationIdCamelCase.startsWith("Update")) {
                co.vendorExtensions.put("x-is-update", true);
                addOperationName(co, "Update");
            } else if (co.operationIdCamelCase.startsWith("Delete")) {
                co.vendorExtensions.put("x-is-delete", true);
                addOperationName(co, "Delete");
                co.returnType = "boolean";
            } else if (co.operationIdCamelCase.startsWith("Create")) {
                co.vendorExtensions.put("x-is-create", true);
                addOperationName(co, "Create");
            } else if (co.operationIdCamelCase.startsWith("Fetch")) {
                co.vendorExtensions.put("x-is-fetch", true);
                addOperationName(co, "Fetch");
            } else if (co.operationIdCamelCase.startsWith("List")){
                hasPaginationOperation = true;
                co.returnType = itemName + "Page";
                co.vendorExtensions.put("x-is-page", true);
                addOperationName(co, "Page");
            }

            final Map<String, Object> resource = PathUtils.getStringMap(resources, resourceName);
            final ArrayList<CodegenOperation> resourceOperationList = Utility.getOperations(resource);
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

            if (isInstanceOperation || (!hasInstanceOperations )) {
                co.responses
                        .stream()
                        .map(response -> response.dataType)
                        .filter(Objects::nonNull)
                        .map(modelName -> directoryStructureService.getModelByClassname(modelName))
                        .flatMap(Optional::stream)
                        .map(conventionResolver::resolveModel)
                        .map(item -> conventionResolver.resolveComplexType(item, modelFormatMap))
                        .forEach(model -> {
                            model.vars.forEach(prop -> addModel(prop.complexType, prop.dataType, models));

                            model.setName(itemName);
                            resource.put("responseModel", model);

                            model
                                    .getVars()
                                    .forEach(variable -> {
                                        variable.vendorExtensions.put("x-name",
                                                itemName +
                                                        variable.getNameInCamelCase());
                                    });
                        });
            }
        }

        for (final String resourceName : resources.keySet()) {
            final Map<String, Object> resource = PathUtils.getStringMap(resources, resourceName);
            final String parentResourceName = (String) resource.get("parentResourceName");
            if (resourceName.endsWith("ListInstance")) {
                resource.put("isListResource", true);
            }
            else if (resourceName.endsWith("Context")) {
                resource.put("isContextResource", true);
            }
            else if (resourceName.endsWith("Page")) {
                resource.put("isPageResource", true);
            }
            else {
                resource.put("isInstanceResource", true);
            }
            if (parentResourceName != null) {
                final boolean parentExists = resources.containsKey(parentResourceName);
                final Map<String, Object> parentResource = PathUtils.getStringMap(resources, parentResourceName);
                parentResource.put("instanceResource", resource);

                if (!parentExists) {
                    parentResource.put("resourceName", parentResourceName);
                    parentResource.put("name", resource.get("name"));

                    final List<CodegenParameter> resourcePathParams = Utility.getOperations(resource).get(0).pathParams;

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
                    Utility.getOperations(parentResource);
                }
            }

            PathUtils.flattenStringMap(resource, DEPENDENTS);
        }

        results.put("resources", resources.values());
        results.put("hasPaginationOperation", hasPaginationOperation);
        results.put("models", models.values());
        return results;
    }

    @Override
    public String getName() {
        return EnumConstants.Generator.TWILIO_PYTHON.getValue();
    }

    //HELPER FUNCTIONS
    private String removeEnumName(final String dataType) {
        return dataType.replace("Enum", "");
    }
    private void addOperationName(final CodegenOperation operation, final String name) {
        operation.vendorExtensions.put("x-name", name);
        operation.vendorExtensions.put("x-name-lower", name.toLowerCase());
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

    private void addModel(final String complexType, final String dataType, final Map<String, CodegenModel> models) {
        directoryStructureService.addModel(models, complexType != null ? complexType : dataType);
    }
}
