package com.twilio.oai;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import org.openapitools.codegen.CodegenConstants;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.languages.GoClientCodegen;

public abstract class AbstractTwilioGoGenerator extends GoClientCodegen {

    protected AbstractTwilioGoGenerator() {
        super();

        embeddedTemplateDir = templateDir = getName();
    }

    @Override
    public void processOpts() {
        super.processOpts();

        additionalProperties.put(CodegenConstants.IS_GO_SUBMODULE, true);
        additionalProperties.put(CodegenConstants.ENUM_CLASS_PREFIX, true);

        supportingFiles.clear();
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
                param.addExtension("x-is-account-sid", true);
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
        name = name.replace("<", "Before");
        name = name.replace(">", "After");
        name = super.toVarName(name);
        return name;
    }
}
