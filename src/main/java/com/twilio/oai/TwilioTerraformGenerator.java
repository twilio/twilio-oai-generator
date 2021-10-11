package com.twilio.oai;

import java.util.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.swagger.v3.oas.models.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.openapitools.codegen.*;
import org.openapitools.codegen.utils.ModelUtils;
import org.openapitools.codegen.utils.StringUtils;

public class TwilioTerraformGenerator extends AbstractTwilioGoGenerator {

    private final Map<String, CodegenModel> allModels = new HashMap<String, CodegenModel>();

    @Value
    private static class TerraformSchema {
        String dataType;
        TerraformSchemaOptions options;
        TerraformSchema innerSchema;

        public String getFullType() {
            if (innerSchema != null) {
                return String.format("%s(%s)", dataType, innerSchema.getFullType());
            }

            return dataType;
        }

        public String getFunc() {
            if (innerSchema != null) {
                return String.format("As%s(%s, %s)",
                                     StringUtils.camelize(dataType),
                                     innerSchema.getFunc(),
                                     options.name);
            }

            return String.format("As%s(%s)", StringUtils.camelize(dataType), options.name);
        }
    }

    @AllArgsConstructor
    private enum TerraformSchemaOptions {
        COMPUTED("SchemaComputed", "*Computed*"),
        // We assume all optional properties may also be computed in the backend. E.g., unique_name is typically
        // optional but it will be populated if omitted.
        OPTIONAL("SchemaComputedOptional", "Optional"),
        FORCE_NEW_OPTIONAL("SchemaForceNewOptional", "Optional"),
        FORCE_NEW_REQUIRED("SchemaForceNewRequired", "**Required**"),
        REQUIRED("SchemaRequired", "**Required**");

        private final String name;
        private final String requirement;
    }

    @AllArgsConstructor
    private enum ResourceOperation {
        CREATE("Create"),
        FETCH("Fetch"),
        UPDATE("Update"),
        DELETE("Delete");

        private final String prefix;
    }

    public TwilioTerraformGenerator() {
        super();

        typeMapping.put("object", "string");
    }

    @Override
    public void processOpts() {
        super.processOpts();

        supportingFiles.add(new SupportingFile("README.mustache", "README.md"));
    }

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
                    .forEach(model -> this.allModels.put(model.getClassname(), model));
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

        final Map<String, Object> ops = (Map<String, Object>) results.get("operations");
        final ArrayList<CodegenOperation> opList = (ArrayList<CodegenOperation>) ops.get("operation");

        // Drop list operations since they're not needed in CRUD operations.
        opList.removeIf(co -> co.nickname.startsWith("List"));

        // iterate over the operation and perhaps modify something
        for (final CodegenOperation co : opList) {
            // Group operations by resource.
            final String resourceName = PathUtils.cleanPath(co.path).replace("/", ""); // Drop the path separators

            final Map<String, Object> resource = resources.computeIfAbsent(resourceName, k -> new LinkedHashMap<>());
            final Map<String, Object> resourceOperations = (Map<String, Object>) resource.computeIfAbsent("operations",
                    k -> new LinkedHashMap<>());
            final ArrayList<CodegenOperation> resourceOperationList =
                (ArrayList<CodegenOperation>) resourceOperations.computeIfAbsent(
                "operation",
                k -> new ArrayList<>());

            resource.put("name", resourceName);
            resource.put("nameInSnakeCase", toSnakeCase(resourceName));
            resourceOperationList.add(co);

            populateCrudOperations(resource, co);
            resource.put("hasUpdate", resource
                    .containsKey(ResourceOperation.UPDATE.name()));

            this.addParamVendorExtensions(co.allParams);
            this.addParamVendorExtensions(co.pathParams);
            this.addParamVendorExtensions(co.optionalParams);
            this.addParamVendorExtensions(co.bodyParams);

            if (!co.optionalParams.isEmpty() || co.requiredParams.stream().anyMatch(p -> !p.isPathParam) ||
                co.path.contains("/2010-04-01/")) {
                co.vendorExtensions.put("x-has-optional-params", true);
            }
        }

        // We only need to create resources with full CRUD capabilities.
        removeNonCrudResources(resources);

        for (final Iterator<Map.Entry<String, Map<String, Object>>> i = resources.entrySet().iterator();
            i.hasNext(); ) {
            final Map<String, Object> resource = i.next().getValue();

            final CodegenOperation createOperation = getCodegenOperation(resource, ResourceOperation.CREATE);
            final CodegenOperation updateOperation = getCodegenOperation(resource, ResourceOperation.UPDATE);
            final CodegenOperation fetchOperation = getCodegenOperation(resource, ResourceOperation.FETCH);

            // Use the parameters for creating the resource as the resource schema.
            resource.put("schema", getResourceSchema(createOperation, updateOperation, fetchOperation));

            // We need to update the resource after creating it if there are any params which can only be set during
            // the update operation.
            final Set<String> createParams = getParamNames(createOperation.allParams);
            if (updateOperation != null) {
                final Set<String> updateParams = getParamNames(updateOperation.formParams);
                createOperation.vendorExtensions.put("x-update-after-create", !createParams.containsAll(updateParams));
            }

            createOperation.responses
                .stream()
                .filter(r -> r.is2xx)
                .map(r -> r.schema)
                .map(Schema.class::cast)
                .findFirst()
                .ifPresent(schema -> {
                    final Map<String, Schema<?>> properties = ModelUtils
                        .getReferencedSchema(this.openAPI, schema)
                        .getProperties();

                    // We need to find the parameter to be used as the Terraform resource ID (as it's not always the
                    // 'sid'). We assume it's the last path parameter for the fetch/update/delete operation.
                    final CodegenParameter idParameter = fetchOperation.pathParams.get(
                            fetchOperation.pathParams.size() - 1);
                    final String idParameterSnakeCase = toSnakeCase(idParameter.paramName);

                    // If the resource ID parameter is not part of the operation response body, remove the resource.
                    if (!properties.containsKey(idParameterSnakeCase)) {
                        i.remove();
                    }

                    createOperation.vendorExtensions.put("x-resource-id", idParameter.paramName);
                    createOperation.vendorExtensions.put("x-resource-id-in-snake-case", idParameterSnakeCase);

                    if ("int".equals(idParameter.dataType)) {
                        createOperation.vendorExtensions.put("x-resource-id-conversion-func", "IntToString");
                    }
                });

            fetchOperation.pathParams.forEach(param -> {
                // Ensure we're able to properly convert the Terraform resource ID parts to the correct type.
                if ("int".equals(param.dataType)) {
                    param.vendorExtensions.put("x-conversion-func", "StringToInt");
                }
            });
        }

        // Exit if there are no resources to generate.
        if (resources.isEmpty()) {
            System.exit(0);
        }

        final String inputSpecPattern = ".+?_(.+?)_(v[0-9]+)\\.(.+)";
        final String inputSpecOriginal = getInputSpec();
        // /path/to/spec/twilio_api_v2010.yaml -> twilio_api_v2010.yaml
        final String inputSpec = inputSpecOriginal.substring(inputSpecOriginal.lastIndexOf("/") + 1);

        final String product = inputSpec.replaceAll(inputSpecPattern, "$1");
        final String productVersion = inputSpec.replaceAll(inputSpecPattern, "$2");
        final String clientPath = String.format("rest/%s/%s", product, productVersion);

        results.put("product", product);
        results.put("productVersion", productVersion);
        results.put("clientService", StringUtils.camelize(product + "_" + productVersion));
        results.put("clientPath", clientPath);
        results.put("resources", resources.values());

        return results;
    }

    private void populateCrudOperations(final Map<String, Object> resource, final CodegenOperation operation) {
        if (operation.nickname.startsWith(ResourceOperation.CREATE.prefix)) {
            operation.vendorExtensions.put("x-is-create-operation", true);
            resource.put(ResourceOperation.CREATE.name(), operation);
        } else if (operation.nickname.startsWith(ResourceOperation.FETCH.prefix)) {
            operation.vendorExtensions.put("x-is-read-operation", true);
            resource.put(ResourceOperation.FETCH.name(), operation);
        } else if (operation.nickname.startsWith(ResourceOperation.UPDATE.prefix)) {
            operation.vendorExtensions.put("x-is-update-operation", true);
            resource.put(ResourceOperation.UPDATE.name(), operation);
        } else if (operation.nickname.startsWith(ResourceOperation.DELETE.prefix)) {
            operation.vendorExtensions.put("x-is-delete-operation", true);
            resource.put(ResourceOperation.DELETE.name(), operation);
        }
    }

    private void removeNonCrudResources(final Map<String, Map<String, Object>> resources) {
        // Remove resources that do not have at least create, read and delete operations
        var resourceOps = EnumSet.complementOf(EnumSet.of(ResourceOperation.UPDATE));
        resources
            .entrySet()
            .removeIf(resource -> Arrays
                .stream(resourceOps.toArray())
                .anyMatch(resourceOperation -> !resource.getValue().containsKey(resourceOperation.toString())));
    }

    private CodegenOperation getCodegenOperation(final Map<String, Object> resource,
                                                 final ResourceOperation resourceOperation) {
        return (CodegenOperation) resource.get(resourceOperation.name());
    }

    private List<Object> getResourceSchema(final CodegenOperation createOperation,
                                           final CodegenOperation updateOperation,
                                           final CodegenOperation fetchOperation) {
        var createParams = getCreateParams(createOperation, updateOperation != null);

        List<CodegenParameter> params = updateOperation != null ? updateOperation.allParams : fetchOperation.pathParams;
        var updateParams = getUpdateParams(params);

        final Set<String> createParamName = getParamNames(createParams);
        final Stream<CodegenParameter> updateStream = updateParams
                .stream()
                .filter(param -> !createParamName.contains(param.paramName));
        final List<CodegenParameter> operationParams = Stream.concat(createParams.stream(), updateStream).collect(Collectors.toList());

        final Set<String> operationParamName = getParamNames(operationParams);
        final Stream<CodegenProperty> filteredProperties = getResourceProperties(
                this.allModels.get(createOperation.returnType))
                .stream()
                .filter(prop -> !operationParamName.contains(prop.name));

        return Stream.concat(operationParams.stream(), filteredProperties).collect(Collectors.toList());
    }

    private List<CodegenParameter> getCreateParams(CodegenOperation createOperation, boolean hasUpdate) {
        var createParams = createOperation.allParams;

        // Resources without an update will be destroyed and force created on update
        var schemaOptionRequired = !hasUpdate ?
                TerraformSchemaOptions.FORCE_NEW_REQUIRED : TerraformSchemaOptions.REQUIRED;
        var schemaOptionOptional = !hasUpdate ?
                TerraformSchemaOptions.FORCE_NEW_OPTIONAL : TerraformSchemaOptions.OPTIONAL;

        createParams.forEach(param -> addSchemaVendorExtensions(param,
                param.required
                        ? schemaOptionRequired
                        : schemaOptionOptional));
        return createParams;
    }

    private List<CodegenParameter> getUpdateParams(List<CodegenParameter> params) {
        params.forEach(param -> addSchemaVendorExtensions(param,
                param.isPathParam
                        ? TerraformSchemaOptions.COMPUTED
                        : TerraformSchemaOptions.OPTIONAL));
        return params;
    }

    private List<CodegenProperty> getResourceProperties(CodegenModel resourceModel) {
        resourceModel.allVars.forEach(prop -> addSchemaVendorExtensions(prop, TerraformSchemaOptions.COMPUTED));
        return resourceModel.allVars;
    }

    private void addSchemaVendorExtensions(final CodegenParameter codegenParameter,
                                           final TerraformSchemaOptions schemaOptions) {
        final TerraformSchema terraformSchema = buildTerraformSchema(codegenParameter, schemaOptions);

        codegenParameter.vendorExtensions.put("x-terraform-schema", terraformSchema);
        codegenParameter.vendorExtensions.put("x-name-in-snake-case", this.toSnakeCase(codegenParameter.baseName));
    }

    private void addSchemaVendorExtensions(final CodegenProperty codegenProperty,
                                           final TerraformSchemaOptions schemaOptions) {
        final TerraformSchema terraformSchema = buildTerraformSchema(codegenProperty, schemaOptions);

        codegenProperty.vendorExtensions.put("x-terraform-schema", terraformSchema);
        codegenProperty.vendorExtensions.put("x-name-in-snake-case", this.toSnakeCase(codegenProperty.baseName));
    }

    private Set<String> getParamNames(final List<CodegenParameter> parameters) {
        return parameters.stream().map(param -> param.paramName).collect(Collectors.toSet());
    }

    private TerraformSchema buildTerraformSchema(final CodegenParameter codegenParameter,
                                                 final TerraformSchemaOptions schemaOptions) {
        return buildTerraformSchema(codegenParameter.dataType, schemaOptions, codegenParameter.items);
    }

    private TerraformSchema buildTerraformSchema(final CodegenProperty codegenProperty,
                                                 final TerraformSchemaOptions schemaOptions) {
        return buildTerraformSchema(codegenProperty.datatypeWithEnum, schemaOptions, codegenProperty.items);
    }

    private TerraformSchema buildTerraformSchema(final String dataType,
                                                 final TerraformSchemaOptions schemaOptions,
                                                 final CodegenProperty items) {
        if (dataType.startsWith("[]")) {
            return new TerraformSchema("list", schemaOptions, buildTerraformSchema(items, schemaOptions));
        }

        switch (dataType) {
            case "float32":
                return new TerraformSchema("float", schemaOptions, null);
            case "int":
            case "bool":
                return new TerraformSchema(dataType, schemaOptions, null);
            default:
                // At the time of writing terraform SDK doesn't support dynamic types and this is inconvenient since
                // some properties of type "object" can be array or a map. (Eg: errors and links in the studio flows)
                // Letting the type objects be as Strings in the terraform provider and then json encoding in the
                // provider is the current workaround.
                return new TerraformSchema("string", schemaOptions, null);
        }
    }

    private void addParamVendorExtensions(final List<CodegenParameter> params) {
        params.forEach(p -> p.vendorExtensions.put("x-name-in-snake-case", this.toSnakeCase(p.paramName)));
        params.forEach(p -> p.vendorExtensions.put("x-util-name", p.isFreeFormObject ? "Object" : "String"));
        params.forEach(p -> p.vendorExtensions.put("x-index", params.indexOf(p)));
    }

    private String toSnakeCase(final String string) {
        return string.replaceAll("[^a-zA-Z0-9]+", "_").replaceAll("([a-z\\d])([A-Z])", "$1_$2").toLowerCase();
    }

    /**
     * Configures a friendly name for the generator. This will be used by the generator to select the library with the
     * -g flag.
     *
     * @return the friendly name for the generator
     */
    @Override
    public String getName() {
        return "terraform-provider-twilio";
    }

    /**
     * Returns human-friendly help for the generator. Provide the consumer with help tips, parameters here
     *
     * @return A string value for the help message
     */
    @Override
    public String getHelp() {
        return "Generates a Terraform provider (pilot).";
    }
}
