package com.twilio.oai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenResponse;
import org.openapitools.codegen.utils.ModelUtils;
import org.openapitools.codegen.utils.StringUtils;

public class TwilioTerraformGenerator extends AbstractTwilioGoGenerator {

    public TwilioTerraformGenerator() {
        super();

        typeMapping.put("object", "string");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> postProcessOperationsWithModels(final Map<String, Object> objs, final List<Object> allModels) {
        final Map<String, Object> results = super.postProcessOperationsWithModels(objs, allModels);

        final Map<String, Map<String, Object>> resources = new HashMap<>();

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

            final Map<String, Object> resource = resources.computeIfAbsent(resourceName, k -> new HashMap<>());
            final Map<String, Object> resourceOperations = (Map<String, Object>) resource.computeIfAbsent("operations", k -> new HashMap<>());
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


            Set<String> requestParams = new HashSet<>();
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
                        ArrayList<Object> properties = getResponseProperties(co.path, resp.code, requestParams);
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
        resource.put("readOnly", (!((Boolean) resource.getOrDefault("hasCreate", false)) && !((Boolean) resource.getOrDefault("hasUpdate", false)) && !((Boolean) resource.getOrDefault("hasDelete", false))));
    }

    private ArrayList<Object> getResponseProperties(String coPath, String statusCode, Set<String> requestParams) {
        Map<String, ApiResponse> responses = this.openAPI.getPaths().get(coPath).getPost().getResponses();
        ApiResponse response = responses.get(statusCode);
        Schema schema = response.getContent().get("application/json").getSchema();
        ArrayList<Object> properties = new ArrayList<>();
        ModelUtils.getReferencedSchema(this.openAPI, schema).getProperties().forEach((k, v) -> {
            if (!requestParams.contains(k)) {
                properties.add(k);
            }
        });

        return properties;
    }

    private void addParamVendorExtensions(final List<CodegenParameter> params) {
        params.forEach(p -> p.vendorExtensions.put("x-name-in-snake-case", this.toSnakeCase(p.baseName)));
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
}
