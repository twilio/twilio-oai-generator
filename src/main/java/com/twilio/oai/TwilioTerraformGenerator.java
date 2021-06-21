package com.twilio.oai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.AllArgsConstructor;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.utils.ModelUtils;
import org.openapitools.codegen.utils.StringUtils;

public class TwilioTerraformGenerator extends AbstractTwilioGoGenerator {

    private static final String SCHEMA_COMPUTED = "SchemaComputed";
    // We assume all optional properties may also be computed in the backend. E.g., unique_name is typically optional
    // but it will be populated if omitted.
    private static final String SCHEMA_OPTIONAL = "SchemaComputedOptional";
    private static final String SCHEMA_REQUIRED = "SchemaRequired";

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
            final String resourceName = co.path.replaceFirst("/[^/]+", "") // Drop the version
                .replaceAll("/\\{[^}]+?}", "") // Drop every path parameter
                .replace(".json", "") // Drop the JSON extension
                .replace("/", ""); // Drop the path separators

            final Map<String, Object> resource = resources.computeIfAbsent(resourceName, k -> new LinkedHashMap<>());
            final Map<String, Object> resourceOperations = (Map<String, Object>) resource.computeIfAbsent("operations",
                                                                                                          k -> new LinkedHashMap<>());
            final ArrayList<CodegenOperation> resourceOperationList =
                (ArrayList<CodegenOperation>) resourceOperations.computeIfAbsent(
                "operation",
                k -> new ArrayList<>());

            resource.put("name", resourceName);
            resourceOperationList.add(co);

            populateCrudOperations(resource, co);

            this.addParamVendorExtensions(co.allParams);
            this.addParamVendorExtensions(co.optionalParams);
            this.addParamVendorExtensions(co.bodyParams);

            if (!co.optionalParams.isEmpty() || co.path.contains("/2010-04-01/")) {
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

            // Use the parameters for creating the resource as the resource schema.
            resource.put("schema", createOperation.allParams);

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

                    final Set<String> createParams = getParamNames(createOperation.allParams);
                    final Set<String> updateParams = getParamNames(updateOperation.formParams);

                    resource.put("responseSchema", getResponseProperties(properties, createParams, updateParams));

                    // We need to update te resource after creating it if there are any params which can only be set
                    // during the update operation.
                    createOperation.vendorExtensions.put("x-update-after-create",
                                                         !createParams.containsAll(updateParams));

                    // We need to find the parameter to be used as the Terraform resource ID (as it's not always the
                    // 'sid'). We assume it's the last path parameter for the fetch/update/delete operation.
                    final CodegenParameter idParameter = updateOperation.pathParams.get(
                        updateOperation.pathParams.size() - 1);
                    final String idParameterSnakeCase = toSnakeCase(idParameter.paramName);

                    // If the resource ID parameter is not part of the operation response body, remove the resource.
                    if (!properties.containsKey(idParameterSnakeCase)) {
                        i.remove();
                    }

                    createOperation.vendorExtensions.put("x-resource-id", idParameter.paramName);
                    createOperation.vendorExtensions.put("x-resource-id-in-snake-case", idParameterSnakeCase);

                    if ("int32".equals(idParameter.dataType)) {
                        createOperation.vendorExtensions.put("x-resource-id-conversion-func", "Int32ToString");
                    }
                });
        }

        // Exit if there are no resources to generate.
        if (resources.isEmpty()) {
            System.exit(0);
        }

        final String inputSpecPattern = ".+?_(.+?)_(v[0-9]+)\\.(.+)";
        final String inputSpecOriginal = getInputSpec();
        // /path/to/spec/twilio_api_v2010.yaml -> twilio_api_v2010
        final String inputSpec = inputSpecOriginal.substring(inputSpecOriginal.lastIndexOf("/") + 1);

        // twilio_api_v2010 -> api_v2010 -> apiV2010
        final String productVersion = StringUtils.camelize(inputSpec.replaceAll(inputSpecPattern, "$1_$2"));
        // twilio_api_v2010 -> rest/api/v2010
        final String clientPath = inputSpec.replaceAll(inputSpecPattern, "rest/$1/$2");

        results.put("productVersion", productVersion);
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
        resources
            .entrySet()
            .removeIf(resource -> Arrays
                .stream(ResourceOperation.values())
                .anyMatch(resourceOperation -> !resource.getValue().containsKey(resourceOperation.name())));
    }

    private CodegenOperation getCodegenOperation(final Map<String, Object> resource,
                                                 final ResourceOperation resourceOperation) {
        return (CodegenOperation) resource.get(resourceOperation.name());
    }

    private Set<String> getParamNames(final List<CodegenParameter> parameters) {
        return parameters.stream().map(param -> param.paramName).map(this::toSnakeCase).collect(Collectors.toSet());
    }

    private ArrayList<CodegenProperty> getResponseProperties(final Map<String, Schema<?>> props,
                                                             final Set<String> createParams,
                                                             final Set<String> updateParams) {
        final ArrayList<CodegenProperty> properties = new ArrayList<>();

        if (props != null) {
            for (final Map.Entry<String, Schema<?>> pair : props.entrySet()) {
                final String key = pair.getKey();
                final Schema<?> value = pair.getValue();
                final CodegenProperty codegenProperty = fromProperty(key, value);
                final String schemaType = updateParams.contains(key) ? SCHEMA_OPTIONAL : SCHEMA_COMPUTED;
                final String terraformSchemaType = buildSchemaType(value,
                                                                   codegenProperty.openApiType,
                                                                   schemaType,
                                                                   codegenProperty);

                codegenProperty.vendorExtensions.put("x-terraform-schema-type", terraformSchemaType);
                codegenProperty.vendorExtensions.put("x-name-in-snake-case",
                                                     this.toSnakeCase(codegenProperty.baseName));

                if (!createParams.contains(key)) {
                    properties.add(codegenProperty);
                }
            }
        }

        return properties;
    }

    @Override
    public CodegenProperty fromProperty(final String name, final Schema schema) {
        final CodegenProperty property = super.fromProperty(name, schema);

        final String schemaType = property.required ? SCHEMA_REQUIRED : SCHEMA_OPTIONAL;
        final String terraformProviderType = buildSchemaType(schema, property.openApiType, schemaType, property);
        property.vendorExtensions.put("x-terraform-schema-type", terraformProviderType);

        return property;
    }

    @Override
    public CodegenParameter fromParameter(final Parameter param, final Set<String> imports) {
        final CodegenParameter parameter = super.fromParameter(param, imports);

        final Schema<?> parameterSchema = param.getSchema();
        final String schemaType = parameter.required ? SCHEMA_REQUIRED : SCHEMA_OPTIONAL;
        final String terraformProviderType = buildSchemaType(parameterSchema,
                                                             parameterSchema.getType(),
                                                             schemaType,
                                                             null);
        parameter.vendorExtensions.put("x-terraform-schema-type", terraformProviderType);

        return parameter;
    }

    private String buildSchemaType(final Schema<?> schema,
                                   final String itemsType,
                                   final String schemaType,
                                   final CodegenProperty codegenProperty) {
        String terraformProviderType;
        switch (itemsType) {
            case "number":
                terraformProviderType = String.format("AsFloat(%s)", schemaType);
                break;
            case "integer":
                terraformProviderType = String.format("AsInt(%s)", schemaType);
                break;
            case "boolean":
                terraformProviderType = String.format("AsBool(%s)", schemaType);
                break;
            case "array":
                terraformProviderType = "AsList(";
                final ArraySchema arraySchema = (ArraySchema) schema;
                final Schema<?> innerSchema = unaliasSchema(getSchemaItems(arraySchema), importMapping);
                if (codegenProperty != null) {
                    final CodegenProperty innerCodegenProperty = fromProperty(codegenProperty.name, innerSchema);
                    if (innerSchema != null) {
                        terraformProviderType += String.format("%s, %s)",
                                                               buildSchemaType(innerSchema,
                                                                               innerCodegenProperty.openApiType,
                                                                               schemaType,
                                                                               innerCodegenProperty),
                                                               schemaType);
                    }
                }
                break;
            case "string":
            case "object":
            default:
                // At the time of writing terraform SDK doesn't support dynamic types and this is inconvenient since
                // some properties of type "object" can be array or a map. (Eg: errors and links in the studio flows)
                // Letting the type objects be as Strings in the terraform provider and then json encoding in the
                // provider is the current workaround.
                terraformProviderType = String.format("AsString(%s)", schemaType);
                break;
        }

        return terraformProviderType;
    }

    private void addParamVendorExtensions(final List<CodegenParameter> params) {
        params.forEach(p -> p.vendorExtensions.put("x-name-in-snake-case", this.toSnakeCase(p.paramName)));
        params.forEach(p -> p.vendorExtensions.put("x-util-name", p.isFreeFormObject ? "Object" : "String"));
    }

    private String toSnakeCase(final String string) {
        return string.replaceAll("([a-z\\d])([A-Z])", "$1_$2").toLowerCase();
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
