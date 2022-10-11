package com.twilio.oai;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.openapitools.codegen.CodegenConstants;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.languages.GoClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;

import static org.openapitools.codegen.utils.StringUtils.camelize;

public abstract class AbstractTwilioGoGenerator extends GoClientCodegen {

    public static final String VENDOR_EXTENSION_ACCOUNT_SID = "x-is-account-sid";

    private final TwilioCodegenAdapter twilioCodegen;

    protected AbstractTwilioGoGenerator() {
        super();

        twilioCodegen = new TwilioCodegenAdapter(this, getName());

        typeMapping.put("integer", "int");
    }

    @Override
    public void processOpts() {
        super.processOpts();

        twilioCodegen.processOpts();

        if (additionalProperties().get("apiVersion").equals("")) {
            // Exit gracefully if given an OpenAPI document that doesn't contain a versioned API. The Go clients do
            // not currently support this.
            System.exit(0);
        }

        additionalProperties.put(CodegenConstants.IS_GO_SUBMODULE, true);
        additionalProperties.put(CodegenConstants.ENUM_CLASS_PREFIX, true);
    }

    @Override
    public Map<String, String> createMapping(final String key, final String value) {
        // Optional dependency not needed.
        if (value.equals("github.com/antihax/optional")) {
            return new HashMap<>();
        }

        return super.createMapping(key, value);
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        super.processOpenAPI(openAPI);

        openAPI
            .getPaths()
            .values()
            .stream()
            .map(PathItem::readOperations)
            .flatMap(Collection::stream)
            .map(Operation::getParameters)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .filter(param -> param.getIn().equals("path") && param.getName().equals("AccountSid"))
            .forEach(param -> {
                param.setName("PathAccountSid");
                param.required(false);
                param.in("query");
                param.addExtension(VENDOR_EXTENSION_ACCOUNT_SID, true);
            });
    }

    @Override
    public void updateCodegenPropertyEnum(final CodegenProperty property) {
        // make sure the inline enums have plain defaults (e.g. string, int, float)
        String enumDefault = null;
        if (property.isEnum && property.defaultValue != null) {
            enumDefault = property.defaultValue;
        }
        super.updateCodegenPropertyEnum(property);
        if (property.isEnum && enumDefault != null) {
            property.defaultValue = enumDefault;
        }
    }

    @Override
    public String toParamName(String name) {
        name = name.replaceAll("[-+.^:,]", "");
        name = twilioCodegen.toParamName(name);
        return super.toVarName(name);
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationMap objectMap = objs.getOperations();
        final List<CodegenOperation> operations = objectMap.getOperation();
        final List<Map<String, String>> imports = (List<Map<String, String>>) objs.get("imports");

        // HTTP method verb conversion (e.g. PUT => Put).
        operations.forEach(operation -> operation.httpMethod = camelize(operation.httpMethod.toLowerCase()));

        if (imports != null) {
            // Remove model imports to avoid error.
            imports.removeIf(item -> item.get("import").startsWith(apiPackage()));
        }

        return objs;
    }

    private Map<String, ModelsMap> filterOutEnumResults(Map<String, ModelsMap> results) {
        HashMap<String, ModelsMap> resultMap = new HashMap<>();
        results.forEach((key, value) -> {
            if (!key.contains("_enum_")) {
                resultMap.put(key, value);
            }
        });
        return resultMap;
    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(final Map<String, ModelsMap> allModels) {
        return filterOutEnumResults(super.postProcessAllModels(allModels));
    }

    @Override
    protected ApiResponse findMethodResponse(final ApiResponses responses) {
        final ApiResponse response = super.findMethodResponse(responses);

        if (response != null) {
            return response;
        }

        // If we found no 2XX or default responses, look for 3XX.
        return responses
            .keySet()
            .stream()
            .filter(responseCode -> responseCode.startsWith("3"))
            .findFirst()
            .map(responses::get)
            .orElse(null);
    }

    @Override
    public String toApiName(final String name) {
        return super.toApiName(name + "_api");
    }
}
