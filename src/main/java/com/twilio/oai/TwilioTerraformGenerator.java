package com.twilio.oai;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.SupportingFile;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;
import org.openapitools.codegen.utils.ModelUtils;

import static com.twilio.oai.common.ApplicationConstants.STRING;
import static com.twilio.oai.common.EnumConstants.Operation;

public class TwilioTerraformGenerator extends AbstractTwilioGoGenerator {

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
                                     StringHelper.camelize(dataType),
                                     innerSchema.getFunc(),
                                     options.name);
            }

            return String.format("As%s(%s)", StringHelper.camelize(dataType), options.name);
        }
    }

    @AllArgsConstructor
    private enum TerraformSchemaOptions {
        COMPUTED("SchemaComputed", "*Computed*"),
        // We assume all optional properties may also be computed in the backend. E.g., unique_name is typically
        // optional, but it will be populated if omitted.
        OPTIONAL("SchemaComputedOptional", "Optional"),
        FORCE_NEW_OPTIONAL("SchemaForceNewOptional", "Optional"),
        FORCE_NEW_REQUIRED("SchemaForceNewRequired", "**Required**"),
        REQUIRED("SchemaRequired", "**Required**");

        private final String name;
        private final String requirement;
    }

    public TwilioTerraformGenerator() {
        super();

        typeMapping.put("object", STRING);
    }

    @Override
    public void processOpts() {
        super.processOpts();

        if (additionalProperties.get("apiVersion").equals("v2010")) {
            additionalProperties.remove("apiVersion");
            additionalProperties.remove("apiVersionClass");
        }

        supportingFiles.add(new SupportingFile("README.mustache", "README.md"));
    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(final Map<String, ModelsMap> allModels) {
        super.postProcessAllModels(allModels);

        // Return an empty collection so no model files get generated.
        return new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);

        final Map<String, Map<String, Object>> resources = new LinkedHashMap<>();

        final OperationMap ops = results.getOperations();
        final List<CodegenOperation> opList = ops.getOperation();

        // Drop list operations since they're not needed in CRUD operations.
        opList.removeIf(co -> co.nickname.startsWith("List"));

        // iterate over the operation and perhaps modify something
        for (final CodegenOperation co : opList) {
            // Group operations by resource.
            final String resourceName = PathUtils.cleanPathAndRemoveFirstElement(co.path).replace("/", ""); // Drop the path separators

            final Map<String, Object> resource = resources.computeIfAbsent(resourceName, k -> new LinkedHashMap<>());
            final Map<String, Object> resourceOperations = (Map<String, Object>) resource.computeIfAbsent("operations",
                    k -> new LinkedHashMap<>());
            final ArrayList<CodegenOperation> resourceOperationList =
                (ArrayList<CodegenOperation>) resourceOperations.computeIfAbsent(
                "operation",
                k -> new ArrayList<>());

            resource.put("name", resourceName);
            resource.put("nameInSnakeCase", StringHelper.toSnakeCase(resourceName));
            resourceOperationList.add(co);

            final String operationType = Utility.populateCrudOperations(co);
            resource.put(operationType, co);

            resource.put("hasUpdate", resource.containsKey(EnumConstants.Operation.UPDATE.name()));

            this.addParamVendorExtensions(co.allParams);
            this.addParamVendorExtensions(co.pathParams);
            this.addParamVendorExtensions(co.optionalParams);
            this.addParamVendorExtensions(co.bodyParams);

            if (!co.optionalParams.isEmpty() || co.requiredParams.stream().anyMatch(p -> !p.isPathParam) ||
                co.allParams.stream().anyMatch(p -> p.paramName.equals("PathAccountSid"))) {
                co.vendorExtensions.put("x-has-optional-params", true);
            }
        }

        // We only need to create resources with full CRUD capabilities.
        removeNonCrudResources(resources);

        for (final Iterator<Map.Entry<String, Map<String, Object>>> i = resources.entrySet().iterator();
            i.hasNext(); ) {
            final Map<String, Object> resource = i.next().getValue();

            final CodegenOperation createOperation = getCodegenOperation(resource, Operation.CREATE);
            final CodegenOperation updateOperation = getCodegenOperation(resource, Operation.UPDATE);
            final CodegenOperation fetchOperation = getCodegenOperation(resource, Operation.FETCH);

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
                    // We need to find the parameter to be used as the Terraform resource ID (as it's not always the
                    // 'sid'). We assume it's the last path parameter for the fetch/update/delete operation.
                    final CodegenParameter idParameter = fetchOperation.pathParams.get(
                            fetchOperation.pathParams.size() - 1);
                    final String idParameterSnakeCase = StringHelper.toSnakeCase(idParameter.paramName);

                    // If the resource ID parameter is not part of the operation response body, remove the resource.
                    if (!getSchemaPropertyNames(schema).contains(idParameterSnakeCase)) {
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

        results.put("resources", resources.values());

        return results;
    }

    private Set<String> getSchemaPropertyNames(final Schema<?> schema) {
        final Schema<?> actualSchema = ModelUtils.getReferencedSchema(this.openAPI, schema);

        final Set<String> propertyNames = Optional
            .ofNullable(actualSchema.getProperties())
            .map(Map::keySet)
            .orElse(new HashSet<>());

        if (actualSchema instanceof ComposedSchema) {
            ModelUtils
                .getInterfaces((ComposedSchema) actualSchema)
                .stream()
                .map(this::getSchemaPropertyNames)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(() -> propertyNames));
        }

        return propertyNames;
    }

    private void removeNonCrudResources(final Map<String, Map<String, Object>> resources) {
        // Remove resources that do not have at least create, fetch, and delete operations.
        var resourceOps = EnumSet.complementOf(EnumSet.of(Operation.UPDATE, Operation.READ));
        resources
            .entrySet()
            .removeIf(resource -> Arrays
                .stream(resourceOps.toArray())
                .anyMatch(resourceOperation -> !resource.getValue().containsKey(resourceOperation.toString())));
    }

    private CodegenOperation getCodegenOperation(final Map<String, Object> resource,
                                                 final Operation resourceOperation) {
        return (CodegenOperation) resource.get(resourceOperation.name());
    }

    private List<CodegenParameter> getResourceSchema(final CodegenOperation createOperation,
                                                     final CodegenOperation updateOperation,
                                                     final CodegenOperation fetchOperation) {
        var createParams = getCreateParams(createOperation, updateOperation);

        List<CodegenParameter> params = updateOperation != null ? updateOperation.allParams : fetchOperation.pathParams;
        var updateParams = getUpdateParams(params);

        final Set<String> createParamName = getParamNames(createParams);
        final Stream<CodegenParameter> updateStream = updateParams
                .stream()
                .filter(param -> !createParamName.contains(param.paramName));

        return Stream.concat(createParams.stream(), updateStream).collect(Collectors.toList());
    }

    private List<CodegenParameter> getCreateParams(final CodegenOperation createOperation,
                                                   final CodegenOperation updateOperation) {
        createOperation.allParams.forEach(createParam -> {
            // Create params that are not part of the update operation params will require to force a new resource to
            // be created.
            final boolean forceNew = updateOperation == null || updateOperation.allParams
                .stream()
                .noneMatch(updateParam -> updateParam.paramName.equals(createParam.paramName));
            final var schemaOptionRequired = forceNew
                ? TerraformSchemaOptions.FORCE_NEW_REQUIRED
                : TerraformSchemaOptions.REQUIRED;
            final var schemaOptionOptional = forceNew
                ? TerraformSchemaOptions.FORCE_NEW_OPTIONAL
                : TerraformSchemaOptions.OPTIONAL;

            addSchemaVendorExtensions(createParam, createParam.required ? schemaOptionRequired : schemaOptionOptional);
        });

        return createOperation.allParams;
    }

    private List<CodegenParameter> getUpdateParams(List<CodegenParameter> params) {
        params.forEach(param -> addSchemaVendorExtensions(param,
                param.isPathParam
                        ? TerraformSchemaOptions.COMPUTED
                        : TerraformSchemaOptions.OPTIONAL));
        return params;
    }

    private void addSchemaVendorExtensions(final CodegenParameter codegenParameter,
                                           final TerraformSchemaOptions schemaOptions) {
        final TerraformSchema terraformSchema = buildTerraformSchema(codegenParameter, schemaOptions);

        codegenParameter.vendorExtensions.put("x-terraform-schema", terraformSchema);
        codegenParameter.vendorExtensions.put("x-name-in-snake-case", StringHelper.toSnakeCase(codegenParameter.baseName));
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
        return buildTerraformSchema(codegenProperty.dataType, schemaOptions, codegenProperty.items);
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
                // some properties of type "object" can be an array or a map. (Eg: errors and links in the studio flows)
                // Letting the type objects be as Strings in the terraform provider and then json encoding in the
                // provider is the current workaround.
                return new TerraformSchema(STRING, schemaOptions, null);
        }
    }

    private void addParamVendorExtensions(final List<CodegenParameter> params) {
        params.forEach(p -> p.vendorExtensions.put("x-name-in-snake-case", StringHelper.toSnakeCase(p.paramName)));
        params.forEach(p -> p.vendorExtensions.put("x-index", params.indexOf(p)));
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
