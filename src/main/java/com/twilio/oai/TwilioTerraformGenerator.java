package com.twilio.oai;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenResponse;
import org.openapitools.codegen.utils.ModelUtils;
import org.openapitools.codegen.utils.StringUtils;

public class TwilioTerraformGenerator extends AbstractTwilioGoGenerator {

    public TwilioTerraformGenerator() {
        super();

        typeMapping.put("object", "string");
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        super.processOpenAPI(openAPI);

        openAPI.getPaths().forEach((name, path) -> path.readOperations().forEach(operation -> {
            if (operation.getOperationId().startsWith("Create")) {
                // We need to find which property is the sid_key for use after this resource gets created. We'll do
                // that by finding the matching instance path (just like our path, but ends with something like
                // "/{Sid}") and then extracting out the name of the last path param. If the sid_key we find is not
                // part of the operation response body, remove the operation so the resource doesn't get added.
                PathUtils
                    .getInstancePath(name, openAPI.getPaths().keySet())
                    .map(PathUtils::getLastPathPart)
                    .map(PathUtils::removeBraces)
                    .filter(param -> containsResponseProperty(operation, param))
                    .ifPresentOrElse(param -> operation.addExtension("x-sid-key", param), () -> path.setPost(null));
            }
        }));
    }

    private boolean containsResponseProperty(final Operation operation, final String propertyName) {
        return operation
            .getResponses()
            .values()
            .stream()
            .anyMatch(response -> response
                .getContent()
                .values()
                .stream()
                .map(MediaType::getSchema)
                .map(schema -> ModelUtils.getReferencedSchema(this.openAPI, schema))
                .map(Schema::getProperties)
                .map(Map::keySet)
                .anyMatch(properties -> properties.contains(StringUtils.underscore(propertyName))));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> postProcessOperationsWithModels(final Map<String, Object> objs, final List<Object> allModels) {
        final Map<String, Object> results = super.postProcessOperationsWithModels(objs, allModels);

        final Map<String, Map<String, Object>> resources = new LinkedHashMap<>();

        final Map<String, Object> ops = (Map<String, Object>) results.get("operations");
        final ArrayList<CodegenOperation> opList = (ArrayList<CodegenOperation>) ops.get("operation");

        // Drop list operations since they're not needed in CRUD operations.
        opList.removeIf(co -> co.nickname.startsWith("List"));

        // iterate over the operation and perhaps modify something
        for (final CodegenOperation co : opList) {

            // Group operations by resource.
            final String resourceName = co.path
                    .replaceFirst("/[^/]+", "") // Drop the version
                    .replaceAll("/\\{.+?}", "") // Drop every path parameter
                    .replace(".json", "") // Drop the JSON extension
                    .replace("/", ""); // Drop the path separators

            final Map<String, Object> resource = resources.computeIfAbsent(resourceName, k -> new LinkedHashMap<>());
            final Map<String, Object> resourceOperations = (Map<String, Object>) resource.computeIfAbsent("operations", k -> new LinkedHashMap<>());
            final ArrayList<CodegenOperation> resourceOperationList = (ArrayList<CodegenOperation>) resourceOperations.computeIfAbsent("operation", k -> new ArrayList<>());

            resource.put("name", resourceName);
            resourceOperationList.add(co);

            populateCrudOperations(resource, co.nickname);
            co.vendorExtensions.put("x-is-create-operation", co.nickname.startsWith("Create"));
            co.vendorExtensions.put("x-is-read-operation", co.nickname.startsWith("Fetch"));
            co.vendorExtensions.put("x-is-update-operation", co.nickname.startsWith("Update"));
            co.vendorExtensions.put("x-is-delete-operation", co.nickname.startsWith("Delete"));

            this.addParamVendorExtensions(co.allParams);
            this.addParamVendorExtensions(co.optionalParams);
            this.addParamVendorExtensions(co.bodyParams);


            final Set<String> requestParams = new LinkedHashSet<>();
            for (CodegenParameter param : co.allParams) {
                requestParams.add(this.toSnakeCase(param.baseName));
            }

            if (!co.optionalParams.isEmpty() || co.path.contains("/2010-04-01/")) {
                co.vendorExtensions.put("x-has-optional-params", true);
            }

            // Use the parameters for creating the resource as the resource schema.
            if (co.nickname.startsWith("Create")) {
                resource.put("schema", co.allParams);
                for (CodegenResponse resp : co.responses) {
                    if (resp.is2xx) {
                        ArrayList<CodegenProperty> properties = getResponseProperties((Schema) resp.schema, requestParams);
                        resource.put("responseSchema", properties);
                        break;
                    }
                }
            }
        }

        final String inputSpecPattern = ".+?_(.+?)_(v[0-9]+)\\.(.+)";
        final String inputSpecOriginal = getInputSpec();
        // /path/to/spec/twilio_api_v2010.yaml -> twilio_api_v2010
        final String inputSpec = inputSpecOriginal.substring(inputSpecOriginal.lastIndexOf("/") + 1);

        // twilio_api_v2010 -> api_v2010 -> apiV2010
        final String productVersion = StringUtils.camelize(inputSpec
                .replaceAll(inputSpecPattern, "$1_$2"));
        // twilio_api_v2010 -> rest/api/v2010
        final String clientPath = inputSpec
                .replaceAll(inputSpecPattern, "rest/$1/$2");

        results.put("productVersion", productVersion);
        results.put("clientPath", clientPath);
        results.put("resources", resources.values());

        return results;
    }

    private void populateCrudOperations(Map<String, Object> resource, String operationName) {
        if (operationName.startsWith("Create")) {
            resource.put("hasCreate", true);
        }
        if (operationName.startsWith("Fetch")) {
            resource.put("hasRead", true);
        }
        if (operationName.startsWith("Update")) {
            resource.put("hasUpdate", true);
        }
        if (operationName.startsWith("Delete")) {
            resource.put("hasDelete", true);
        }

        resource.put("hasAllCrudOps", (Boolean) resource.getOrDefault("hasCreate", false) && (Boolean) resource.getOrDefault("hasRead", false) && (Boolean) resource.getOrDefault("hasUpdate", false) && (Boolean) resource.getOrDefault("hasDelete", false));
    }

    private ArrayList<CodegenProperty> getResponseProperties(Schema schema, Set<String> requestParams) {
        ArrayList<CodegenProperty> properties = new ArrayList<CodegenProperty>();

        Map<String, Schema> props = ModelUtils.getReferencedSchema(this.openAPI, schema).getProperties();
        if (props != null) {
            for (Map.Entry<String, Schema> pair : props.entrySet()) {
                String key = pair.getKey();
                Schema value = pair.getValue();
                CodegenProperty codegenProperty = fromProperty(key, value);
                String schemaType = BuildSchemaType(value, codegenProperty.openApiType, "SchemaComputed", codegenProperty, null);

                codegenProperty.vendorExtensions.put("x-terraform-schema-type", schemaType);
                codegenProperty.vendorExtensions.put("x-name-in-snake-case", this.toSnakeCase(codegenProperty.baseName));

                if (!requestParams.contains(key)) {
                    properties.add(codegenProperty);
                }
            }
        }

        return properties;
    }

    private String BuildSchemaType(Schema schema, String itemsType, String schemaType, CodegenProperty codegenProperty, CodegenParameter codegenParameter) {
        String terraformProviderType = "";
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
                ArraySchema arraySchema = (ArraySchema) schema;
                Schema innerSchema = unaliasSchema(getSchemaItems(arraySchema), importMapping);
                if (codegenProperty != null) {
                    CodegenProperty innerCodegenProperty = fromProperty(codegenProperty.name, innerSchema);
                    if (innerSchema != null) {
                        terraformProviderType += String.format("%s, %s)", BuildSchemaType(innerSchema, innerCodegenProperty.openApiType, schemaType, innerCodegenProperty, null), schemaType);
                    }
                }
                break;
            case "string":
            case "object":
            default:
                // At the time of writing terraform SDK doesn't support dynamic types and this is incovenient since
                // some properties of type "object" can be array or a map. (Eg: errors and links in the studio flows)
                // Letting the type objects be as Strings in the terraform provider and then json encoding in the provider
                // is the current workaround
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
     * Configures a friendly name for the generator.  This will be used by the generator
     * to select the library with the -g flag.
     *
     * @return the friendly name for the generator
     */
    @Override
    public String getName() {
        return "terraform-provider-twilio";
    }

    /**
     * Returns human-friendly help for the generator.  Provide the consumer with help
     * tips, parameters here
     *
     * @return A string value for the help message
     */
    @Override
    public String getHelp() {
        return "Generates a Terraform provider (beta).";
    }

    @Override
    public CodegenProperty fromProperty(String name, Schema p) {
        CodegenProperty property = super.fromProperty(name, p);
        String schemaType = property.required ? "SchemaRequired" : "SchemaOptional";
        String terraformProviderType = BuildSchemaType(p, property.openApiType, schemaType, property, null);
        property.vendorExtensions.put("x-terraform-schema-type", terraformProviderType);
        return property;
    }

    @Override
    public CodegenParameter fromParameter(Parameter param, Set<String> imports) {
        CodegenParameter parameter = super.fromParameter(param, imports);
        Schema parameterSchema = param.getSchema();
        String schemaType = parameter.required ? "SchemaRequired" : "SchemaOptional";
        String terraformProviderType = BuildSchemaType(parameterSchema, parameterSchema.getType(), schemaType, null, parameter);
        parameter.vendorExtensions.put("x-terraform-schema-type", terraformProviderType);
        return parameter;
    }
}
