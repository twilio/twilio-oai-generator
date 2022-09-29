package com.twilio.oai;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.SupportingFile;
import org.openapitools.codegen.languages.TypeScriptNodeClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;
import org.openapitools.codegen.utils.StringUtils;
import com.twilio.oai.resource.IResourceTree;
import com.twilio.oai.resource.ResourceMap;


public class TwilioNodeGenerator extends TypeScriptNodeClientCodegen {

    // Unique string devoid of symbols.
    public static final String PATH_SEPARATOR_PLACEHOLDER = "1234567890";
    public static final String IGNORE_EXTENSION_NAME = "x-ignore";
    public static final String PREVIEW_STRING = "Preview";

    private final List<CodegenModel> allModels = new ArrayList<>();
    private final Inflector inflector = new Inflector();
    private final Map<String, String> subDomainMap = new HashMap<>();
    private final Map<String, String> resourceNameMap = new HashMap<>();

    public TwilioNodeGenerator() {
        super();

        // Remove the "API" suffix from the API filenames.
        apiSuffix = "";

        // Find the templates in the local resources dir.
        embeddedTemplateDir = templateDir = getName();
    }

    @Override
    public void processOpts() {
        super.processOpts();

        final String inputSpecPattern = ".+_(?<domain>.+)_(?<version>.+)\\..+";
        final String version = inputSpec.replaceAll(inputSpecPattern, "${version}");
        final String domain = inputSpec.replaceAll(inputSpecPattern, "${domain}");
        apiPackage = version; // Place the API files in the version folder.
        additionalProperties.put("apiVersion", version);
        additionalProperties.put("apiVersionClass", version.toUpperCase());
        additionalProperties.put("domain", StringUtils.camelize(domain));

        supportingFiles.clear();
        supportingFiles.add(new SupportingFile("version.mustache", apiPackage.toUpperCase() + ".ts"));
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        final IResourceTree resourceTree = new ResourceMap(inflector);
        final Map<String, Object> versionResources = getStringMap(additionalProperties, "versionResources");

        openAPI.getPaths().forEach((name, path) -> {
            resourceTree.addResource(name, path);

            // Certain paths exists which have no operations and are only parent placeholders. We need these paths to
            // exist as resources during post-processing.
            if (path.readOperations().isEmpty()) {
                path.setGet(new Operation());
                path.getGet().addExtension(IGNORE_EXTENSION_NAME, true);
            }
        });

        openAPI.getPaths().forEach((name, path) -> path.readOperations().forEach(operation -> {
            // Group operations together by tag. This gives us one file/post-process per resource.
            final List<String> ancestors = resourceTree.ancestors(name, operation);
            final String tag = String.join(PATH_SEPARATOR_PLACEHOLDER, ancestors);

            if (!resourceNameMap.containsKey(name)) {
                final String resourceName = ancestors.get(ancestors.size() - 1);
                resourceNameMap.put(name, resourceName);
                resourceNameMap.put(PathUtils.removeExtension(PathUtils.removePathParamIds(name)), resourceName);
            }

            if (isPreviewDomain()) {
                subDomainMap.put(tag, extractSubDomainName(name));
            }

            operation.addTagsItem(tag);

            if (!tag.contains(PATH_SEPARATOR_PLACEHOLDER)) {
                addVersionResource(versionResources, path, name, tag);
            }

            // Gather a list of dependents for the operation as those with a path that directly under the current path.
            operation.addExtension("x-dependents",
                                   openAPI
                                       .getPaths()
                                       .keySet()
                                       .stream()
                                       .map(PathUtils::removePathParamIds)
                                       .map(PathUtils::removeExtension)
                                       .filter(p -> p.matches(PathUtils.escapeRegex(
                                           PathUtils.removeExtension(PathUtils.removePathParamIds(name)) + "/[^{/]+")))
                                       .collect(Collectors.toList()));
        }));

        flattenStringMap(additionalProperties, "versionResources");
    }

    @Override
    public String toApiFilename(final String name) {
        // Replace the path separator placeholder with the actual separator and lowercase the first character of each
        // path part.
        String[] split = super.toApiFilename(name).split(PATH_SEPARATOR_PLACEHOLDER);
        if(isPreviewDomain()){
            return getSubDomainName(subDomainMap, name) + "/" +Arrays.stream(Arrays.copyOfRange(split, 0, split.length - 1)).map(String::toLowerCase).collect(Collectors.joining("/")) + "/"+split[split.length-1];
        }

        return Arrays
            .stream(super.toApiFilename(name).split(PATH_SEPARATOR_PLACEHOLDER))
            .map(part -> StringUtils.camelize(part, true))
            .collect(Collectors.joining(File.separator));
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

        // Return an empty collection so no model files get generated.
        return new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);

        final Map<String, Object> resources = new HashMap<>();

        final OperationMap ops = results.getOperations();
        final String classname = (String) ops.get("classname");
        final List<CodegenOperation> opList = ops.getOperation();

        final boolean hasInstanceOperations = opList.stream().anyMatch(this::isInstanceOperation);

        results.put("apiVersionPath", getRelativeRoot(classname));

        // iterate over the operation and perhaps modify something
        for (final CodegenOperation co : opList) {
            // Group operations by resource.
            final String[] filePathArray = co.baseName.split(PATH_SEPARATOR_PLACEHOLDER);

            final String itemName = filePathArray[filePathArray.length - 1];
            final String instanceName = itemName + "Instance";
            final boolean isInstanceOperation = isInstanceOperation(co);
            final HttpMethod httpMethod = HttpMethod.fromString(co.httpMethod);
            String resourceName;
            String parentResourceName = null;

            co.returnType = instanceName;

            if (isInstanceOperation) {
                resourceName = itemName + "Context";
                parentResourceName = itemName + "ListInstance";
                if (httpMethod == HttpMethod.GET) {
                    addOperationName(co, "Fetch");
                } else if (httpMethod == HttpMethod.POST) {
                    addOperationName(co, "Update");
                } else if (httpMethod == HttpMethod.DELETE) {
                    addOperationName(co, "Remove");
                    co.returnType = "boolean";
                    co.vendorExtensions.put("x-is-delete-operation", true);
                }
            } else {
                resourceName = itemName + "ListInstance";
                if (httpMethod == HttpMethod.POST) {
                    addOperationName(co, "Create");
                } else if (httpMethod == HttpMethod.GET) {
                    addOperationName(co, "Page");
                    co.returnType = itemName + "Page";
                    co.vendorExtensions.put("x-is-list-operation", true);
                }
            }

            final Map<String, Object> resource = getStringMap(resources, resourceName);
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

            co.allParams.removeAll(co.pathParams);
            co.requiredParams.removeAll(co.pathParams);
            co.hasParams = !co.allParams.isEmpty();
            co.hasRequiredParams = !co.requiredParams.isEmpty();
            co.queryParams.forEach(this::addSerializeVendorExtension);
            co.formParams.forEach(this::addSerializeVendorExtension);
            co.httpMethod = co.httpMethod.toLowerCase();

            if (co.bodyParam != null) {
                addModel(resource, co.bodyParam.dataType);
            }

            final Map<String, Object> dependents = getStringMap(resource, "dependents");
            for (final String dependentPath : (List<String>) co.vendorExtensions.get("x-dependents")) {
                addDependent(dependents, dependentPath);
            }

            if (isInstanceOperation || (!hasInstanceOperations && httpMethod == HttpMethod.POST)) {
                co.responses
                    .stream()
                    .map(response -> response.dataType)
                    .filter(Objects::nonNull)
                    .map(this::getModel)
                    .flatMap(Optional::stream)
                    .map(this::resolveComplexType)
                    .forEach(model -> {
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

            results.put("apiFilename", StringUtils.camelize(getResourceName(co.path), true));
        }

        resources.values().stream().map(resource -> (Map<String, Object>) resource).forEach(resource -> {
            final String parentResourceName = (String) resource.get("parentResourceName");
            if (parentResourceName != null) {
                final Map<String, Object> parentResource = getStringMap(resources, parentResourceName);
                parentResource.put("instanceResource", resource);
            }

            flattenStringMap(resource, "models");
            flattenStringMap(resource, "dependents");
        });

        results.put("resources", resources.values());

        return results;
    }

    private boolean isInstanceOperation(final CodegenOperation operation) {
        return isInstancePath(operation.path);
    }

    private boolean isInstancePath(final String path) {
        return PathUtils.removeExtension(path).endsWith("}");
    }

    @SuppressWarnings("unchecked")
    private ArrayList<CodegenOperation> getOperations(final Map<String, Object> resource) {
        return (ArrayList<CodegenOperation>) resource.computeIfAbsent(
            "operations",
            k -> new ArrayList<>());
    }

    private void updateResourcePath(final Map<String, Object> resource, final CodegenOperation operation) {
        final List<CodegenParameter> resourcePathParams = new ArrayList<>();

        String path = operation.path;
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

    /**
     * Adds a version resource to the versionResources map
     */
    private void addVersionResource(final Map<String, Object> versionResources, final PathItem pathItem, String path, String tag){
        final Map<String, Object> versionResource = getStringMap(versionResources, tag);
        String cleanedPath = StringUtils.underscore(PathUtils.getLastPathPart(PathUtils.cleanPathAndRemoveFirstElement(path)));

        // If there is a classname extension (custom name), use that. Else: break down the path and use that
        final String mountName = getCustomClassName(pathItem).orElse(cleanedPath);
        versionResource.put("name", tag);
        versionResource.put("mountName", StringUtils.underscore(mountName));
        versionResource.put("filename", StringUtils.camelize(tag, true));
    }

    /**
     * Given a PathItem, returns the custom "className" from the x-twilio extension if it exists, else return null
     */
    private Optional<String> getCustomClassName(final PathItem pathItem) {
        return PathUtils.getTwilioExtension(pathItem, "className");
    }

    protected String getRelativeRoot(final String classname) {
        return Arrays
            .stream(classname.split(PATH_SEPARATOR_PLACEHOLDER))
            .map(part -> "..")
            .collect(Collectors.joining(File.separator));
    }

    private void addModel(final Map<String, Object> resource, final String dataType) {
        getModel(dataType).ifPresent(model -> {
            if (getStringMap(resource, "models").putIfAbsent(model.getClassname(), model) == null) {
                model.getVars().forEach(property -> addModel(resource, property.dataType));
            }
        });
    }

    private void addDependent(final Map<String, Object> dependents, final String dependentPath) {
        final Map<String, Object> dependent = getStringMap(dependents, dependentPath);
        final String dependentName = getResourceName(dependentPath);
        // Use custom class name if available. Do I have access to that?
        final String mountName = generateMountName(dependentPath);
        dependent.put("name", inflector.singular(dependentName));
        dependent.put("mountName", StringUtils.underscore(mountName));
        dependent.put("filename", StringUtils.camelize(dependentName, true));
    }

    private CodegenModel resolveComplexType(CodegenModel item) {
        for (CodegenProperty prop : item.vars) {
            if (prop.complexType != null) {
                prop.dataType = prop.isArray ? "Array<object>" : "object";
            }
        }
        return item;
    }

    private String extractSubDomainName(String name) {
        String[] split = name.split("/");
        if(split.length > 1 && split[1] != null) {
            String result = split[1];
            result = result.substring(0, 1).toLowerCase() + result.substring(1);
            return result;
        }
        return null;
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

    @SuppressWarnings("unchecked")
    private Map<String, Object> getStringMap(final Map<String, Object> resource, final String key) {
        return (Map<String, Object>) resource.computeIfAbsent(key, k -> new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    private void flattenStringMap(final Map<String, Object> resource, final String key) {
        resource.computeIfPresent(key, (k, dependents) -> ((Map<String, Object>) dependents).values());
    }

    private String getResourceName(final String path) {
        if (resourceNameMap.containsKey(path)) {
            return resourceNameMap.get(path);
        }
        return PathUtils.getLastPathPart(PathUtils.cleanPathAndRemoveFirstElement(path));
    }

    /**
     * If there is a custom class name, return that. Else return a cleaned version of the original path
     */
    private String generateMountName(final String path){
        // Determine if the path has a custom class name
        if (resourceNameMap.containsKey(path)){
            String resource = inflector.singular(PathUtils.getLastPathPart(PathUtils.cleanPathAndRemoveFirstElement(path)));
            String className = resourceNameMap.get(path);
            if (!className.equals(resource)){
                // Use plural version of resourceName
                return className;
            }
        }
        return PathUtils.getLastPathPart(PathUtils.cleanPathAndRemoveFirstElement(path));
    }

    private void addOperationName(final CodegenOperation operation, final String name) {
        operation.vendorExtensions.put("x-name", name);
        operation.vendorExtensions.put("x-name-lower", name.toLowerCase());
    }

    private String getSubDomainName(Map<String, String> subDomainMap, String name) {
        return subDomainMap.entrySet().stream().filter(x -> x.getKey().equalsIgnoreCase(name)).findFirst().get().getValue();
    }

    private boolean isPreviewDomain(){
        return this.additionalProperties.get("domain").equals(PREVIEW_STRING);
    }

    @Override
    public String getName() {
        return "twilio-node";
    }

    @Override
    public String getHelp() {
        return "Generates the twilio-node helper library.";
    }

    @Override
    public String toParamName(String name) {
        name = name.replace("<", "Before");
        name = name.replace(">", "After");
        name = super.toVarName(name);
        return name;
    }
}
