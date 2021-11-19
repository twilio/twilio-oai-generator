package com.twilio.oai;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.AllArgsConstructor;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.SupportingFile;
import org.openapitools.codegen.languages.JavaClientCodegen;
import org.openapitools.codegen.utils.StringUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class TwilioJavaGenerator extends JavaClientCodegen {

    // Unique string devoid of symbols.
    private static final String PATH_SEPARATOR_PLACEHOLDER = "1234567890";

    private final List<CodegenModel> allModels = new ArrayList<>();

    public TwilioJavaGenerator() {
        super();

        // Remove the "API" suffix from the API filenames.
        apiNameSuffix = "";

        // Find the templates in the local resources dir.
        embeddedTemplateDir = templateDir = getName();
    }

    @AllArgsConstructor
    private enum ResourceOperation {
        CREATE("Create"),
        FETCH("Fetch"),
        UPDATE("Update"),
        DELETE("Delete");

        private final String prefix;
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
        apiTemplateFiles.put("api.mustache", ".java");
        apiTemplateFiles.put("creator.mustache", "Creator.java");
        apiTemplateFiles.put("deleter.mustache", "Deleter.java");
        apiTemplateFiles.put("fetcher.mustache", "Fetcher.java");
        apiTemplateFiles.put("reader.mustache", "Reader.java");
        apiTemplateFiles.put("updater.mustache", "Updater.java");

        supportingFiles.add(new SupportingFile("Domains.mustache", "Domains.java"));
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        final Map<String, Object> versionResources = getStringMap(additionalProperties, "versionResources");

        // check if there is a read operation and add reader template

        openAPI.getPaths().forEach((name, path) ->
                {
                    if (!path.readOperations().isEmpty()) {
                        apiTemplateFiles.put("reader.mustache", "Reader.java");
                        apiTemplateFiles.put("fetcher.mustache", "Fetcher.java");
                    }
                }
        );


        openAPI.getPaths().forEach((name, path) -> path.readOperations().forEach(operation -> {
            // Group operations together by tag. This gives us one file/post-process per resource.
            final String tag = PathUtils.cleanPath(name).replace("/", PATH_SEPARATOR_PLACEHOLDER);
            operation.addTagsItem(tag);

            if (!tag.contains(PATH_SEPARATOR_PLACEHOLDER)) {
                addDependent(versionResources, tag);
            }

            // Gather a list of dependents for the operation as those with a path that directly under the current path.
            operation.addExtension("x-dependents",
                    openAPI
                            .getPaths()
                            .keySet()
                            .stream()
                            .filter(p -> PathUtils
                                    .removePathParamIds(p)
                                    .matches(
                                            PathUtils.escapeRegex(PathUtils.removePathParamIds(name)) + "/[^{]+"))
                            .collect(Collectors.toList()));
        }));

        flattenStringMap(additionalProperties, "versionResources");
    }

    @Override
    public String toApiFilename(final String name) {
        // Replace the path separator placeholder with the actual separator and lowercase the first character of each
        // path part.
        return Arrays
                .stream(super.toApiFilename(name).split(PATH_SEPARATOR_PLACEHOLDER))
                .map(part -> StringUtils.camelize(part, false))
                .collect(Collectors.joining(File.separator));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> postProcessAllModels(final Map<String, Object> allModels) {
        final Map<String, Object> results = super.postProcessAllModels(allModels);

        for (final Object obj : results.values()) {
            final Map<String, Object> mods = (Map<String, Object>) obj;
            final ArrayList<Map<String, Object>> modList = (ArrayList<Map<String, Object>>) mods.get("models");

            // Add all the models to the local models list.
            modList
                    .stream()
                    .map(model -> model.get("model"))
                    .map(CodegenModel.class::cast)
                    .collect(Collectors.toCollection(() -> this.allModels));
        }

        // Return an empty collection so no model files get generated.
        return new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> postProcessOperationsWithModels(final Map<String, Object> objs,
                                                               final List<Object> allModels) {
        final Map<String, Object> results = super.postProcessOperationsWithModels(objs, allModels);

        final Map<String, Map<String, Object>> resources = new LinkedHashMap<>();

        final Map<String, Object> ops = getStringMap(results, "operations");
        final ArrayList<CodegenOperation> opList = (ArrayList<CodegenOperation>) ops.get("operation");

        // iterate over the operation and perhaps modify something
        for (final CodegenOperation co : opList) {
            // Group operations by resource.
            String path = co.path;
            String resourceName = singularize(getResourceName(co.path));
            final Map<String, Object> resource = resources.computeIfAbsent(resourceName, k -> new LinkedHashMap<>());
            populateCrudOperations(resource, co);

            for (final CodegenParameter pathParam : co.pathParams) {
                path = path.replace("{" + pathParam.baseName + "}", "${" + pathParam.paramName + "}");
            }

            if (co.path.endsWith("}")) {
                if ("GET".equalsIgnoreCase(co.httpMethod)) {
                    co.vendorExtensions.put("x-is-fetch-operation", true);
                    addOperationName(co, "Fetch");
                } else if ("POST".equalsIgnoreCase(co.httpMethod)) {
                    addOperationName(co, "Update");
                } else if ("DELETE".equalsIgnoreCase(co.httpMethod)) {
                    resource.put("isDelete", true);
                    addOperationName(co, "Remove");
                }
            } else {
                if ("POST".equalsIgnoreCase(co.httpMethod)) {
                    resource.put("isCreate", true);
                    addOperationName(co, "Create");
                } else if ("GET".equalsIgnoreCase(co.httpMethod)) {
                    addOperationName(co, "Page");
                }
            }

            final ArrayList<CodegenOperation> resourceOperationList =
                    (ArrayList<CodegenOperation>) resource.computeIfAbsent(
                            "operations",
                            k -> new ArrayList<>());

            resourceOperationList.add(co);
            resource.put("resourceName", resourceName);
            resource.put("path", path);
            resource.put("resourcePathParams", co.pathParams);
            co.allParams.removeAll(co.pathParams);
            co.requiredParams.removeAll(co.pathParams);
            co.pathParams = null;
            co.hasParams = !co.allParams.isEmpty();
            co.hasRequiredParams = !co.requiredParams.isEmpty();

            if (co.bodyParam != null) {
                addModel(resource, co.bodyParam.dataType);
            }

            if (co.path.endsWith("}")) {
                final Map<String, Object> dependents = getStringMap(resource, "dependents");
                for (final String dependentPath : (List<String>) co.vendorExtensions.get("x-dependents")) {
                    addDependent(dependents, dependentPath);
                }

                co.responses
                        .stream()
                        .map(response -> response.dataType)
                        .filter(Objects::nonNull)
                        .map(this::getModel)
                        .flatMap(Optional::stream)
                        .forEach(model -> {
//                            model.setName(instanceName);
                            resource.put("responseModel", model);
                        });
            }

            results.put("apiFilename", getResourceName(co.path));
        }

        for (final Object resource : resources.values()) {
            flattenStringMap((Map<String, Object>) resource, "models");
            flattenStringMap((Map<String, Object>) resource, "dependents");
        }

        results.put("resources", resources.values());

        return results;
    }

    @AllArgsConstructor
    private enum Operation {
        // TODO: fix casing
        CREATE("create"),
        FETCH("fetch"),
        UPDATE("update"),
        DELETE("delete");

        private final String prefix;
    }

    private void populateCrudOperations(final Map<String, Object> resource, final CodegenOperation operation) {
        if (operation.nickname.startsWith(Operation.CREATE.prefix)) {
            operation.vendorExtensions.put("x-is-create-operation", true);
            resource.put(Operation.CREATE.name(), operation);
        } else if (operation.nickname.startsWith(Operation.FETCH.prefix)) {
            operation.vendorExtensions.put("x-is-read-operation", true);
            resource.put(Operation.FETCH.name(), operation);
        } else if (operation.nickname.startsWith(Operation.UPDATE.prefix)) {
            operation.vendorExtensions.put("x-is-update-operation", true);
            resource.put(Operation.UPDATE.name(), operation);
        } else if (operation.nickname.startsWith(Operation.DELETE.prefix)) {
            operation.vendorExtensions.put("x-is-delete-operation", true);
            resource.put(Operation.DELETE.name(), operation);
        }
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
        dependent.put("name", singularize(dependentName));
        dependent.put("mountName", StringUtils.underscore(dependentName));
        dependent.put("filename", dependentName);
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
        return PathUtils.getLastPathPart(PathUtils.cleanPath(path));
    }

    private String singularize(final String plural) {
        return plural.substring(0, plural.length() - 1);
    }

    private void addOperationName(final CodegenOperation operation, final String name) {
        operation.vendorExtensions.put("x-name", name);
        operation.vendorExtensions.put("x-name-lower", name.toLowerCase());
    }

    @Override
    public String getName() {
        return "twilio-java";
    }

    @Override
    public String getHelp() {
        return "Generates the twilio-node helper library.";
    }
}
