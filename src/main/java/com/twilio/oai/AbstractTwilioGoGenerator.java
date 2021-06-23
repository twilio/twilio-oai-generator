package com.twilio.oai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openapitools.codegen.CodegenConstants;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.languages.GoClientCodegen;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.openapitools.codegen.CodegenParameter;

public abstract class AbstractTwilioGoGenerator extends GoClientCodegen {

    protected AbstractTwilioGoGenerator() {
        super();

        embeddedTemplateDir = templateDir = getName();
        typeMapping.put("integer", "int");
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
    public void postProcessParameter(final CodegenParameter parameter) {
        // Make sure required non-path params get into the options block.
        parameter.required = parameter.isPathParam;

        if (parameter.paramName.equals("PathAccountSid")) {
            parameter.required = false;
            parameter.vendorExtensions.put("x-is-account-sid", true);
        }
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        openAPI.getPaths().forEach((name, path) -> path.readOperations().forEach(operation -> {
            List<Parameter> parameters = operation.getParameters();
            if (parameters != null) {
                for (Parameter p : parameters) {
                    String in = p.getIn();
                    String paramName = p.getName();
                    if (in.equals("path") && paramName.equals("AccountSid")) {
                        p.setName("PathAccountSid");
                    }
                }
            }
        }));
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
