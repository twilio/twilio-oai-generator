package com.twilio.oai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenResponse;
import org.openapitools.codegen.utils.ModelUtils;
import org.openapitools.codegen.utils.StringUtils;

public class TwilioTerraformGenerator extends AbstractTwilioGoGenerator {

    @Value
    private static class PropertySchema {
        String propertyName;
        Schema<?> schema;
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
    public void processOpenAPI(final OpenAPI openAPI) {
        super.processOpenAPI(openAPI);

        // We only need to create resources with full CRUD capabilities.
        final boolean allCrudAvailable = openAPI
            .getPaths()
            .values()
            .stream()
            .anyMatch(path -> path.getDelete() != null && path.getPost() != null && path.getGet() != null);

        // If all CRUD operations are not available, do not create the resource.
        if (!allCrudAvailable) {
            System.exit(0);
        }

        openAPI.getPaths().forEach((name, path) -> path.readOperations().forEach(operation -> {
            if (operation.getOperationId().startsWith(ResourceOperation.CREATE.prefix)) {
                // We need to find which property is the sid_key for use after this resource gets created. We'll do
                // that by finding the matching instance path (just like our path, but ends with something like
                // "/{Sid}") and then extracting out the name of the last path param. If the sid_key we find is not
                // part of the operation response body, remove the operation so the resource doesn't get added.
                PathUtils
                    .getInstancePath(name, openAPI.getPaths().keySet())
                    .map(PathUtils::getLastPathPart)
                    .map(PathUtils::removeBraces)
                    .flatMap(param -> getResponsePropertySchema(operation, param))
                    .ifPresentOrElse(propertySchema -> {
                        operation.addExtension("x-sid-key", propertySchema.getPropertyName());

                        if ("integer".equals(propertySchema.getSchema().getType())) {
                            operation.addExtension("x-sid-key-conversion-func", "Int32ToString");
                        }
                    }, () -> path.setPost(null));
            }
        }));
    }

    private Optional<PropertySchema> getResponsePropertySchema(final Operation operation, final String propertyName) {
        return operation
            .getResponses()
            .values()
            .stream()
            .flatMap(response -> response
                .getContent()
                .values()
                .stream()
                .map(MediaType::getSchema)
                .map(schema -> ModelUtils.getReferencedSchema(this.openAPI, schema))
                .map(Schema::getProperties)
                .map(properties -> properties.get(StringUtils.underscore(propertyName)))
                .filter(Objects::nonNull)
                .map(Schema.class::cast)
                .map(schema -> new PropertySchema(propertyName, schema)))
            .findFirst();
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

        removeNonCrudResources(resources);

        resources.values().forEach(resource -> {
            final CodegenOperation createOperation = getCodegenOperation(resource, ResourceOperation.CREATE);

            // Use the parameters for creating the resource as the resource schema.
            resource.put("schema", createOperation.allParams);
            for (final CodegenResponse resp : createOperation.responses) {
                if (resp.is2xx) {
                    final ArrayList<CodegenProperty> properties = getResponseProperties((Schema<?>) resp.schema,
                                                                                        getParamNames(createOperation.allParams));
                    resource.put("responseSchema", properties);
                    break;
                }
            }
        });

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

    private CodegenOperation getCodegenOperation(final Map<String, Object> resources,
                                                 final ResourceOperation resourceOperation) {
        return (CodegenOperation) resources.get(resourceOperation.name());
    }

    private Set<String> getParamNames(final List<CodegenParameter> parameters) {
        return parameters.stream().map(param -> param.baseName).map(this::toSnakeCase).collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    private ArrayList<CodegenProperty> getResponseProperties(final Schema<?> schema, final Set<String> createParams) {
        final ArrayList<CodegenProperty> properties = new ArrayList<>();

        final Map<String, Schema<?>> props = ModelUtils.getReferencedSchema(this.openAPI, schema).getProperties();
        if (props != null) {
            for (final Map.Entry<String, Schema<?>> pair : props.entrySet()) {
                final String key = pair.getKey();
                final Schema<?> value = pair.getValue();
                final CodegenProperty codegenProperty = fromProperty(key, value);
                final String schemaType = buildSchemaType(value,
                                                          codegenProperty.openApiType,
                                                          "SchemaOptional",
                                                          codegenProperty);

                codegenProperty.vendorExtensions.put("x-terraform-schema-type", schemaType);
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

        final String schemaType = property.required ? "SchemaRequired" : "SchemaOptional";
        final String terraformProviderType = buildSchemaType(schema, property.openApiType, schemaType, property);
        property.vendorExtensions.put("x-terraform-schema-type", terraformProviderType);

        return property;
    }

    @Override
    public CodegenParameter fromParameter(final Parameter param, final Set<String> imports) {
        final CodegenParameter parameter = super.fromParameter(param, imports);

        final Schema<?> parameterSchema = param.getSchema();
        final String schemaType = parameter.required ? "SchemaRequired" : "SchemaOptional";
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
