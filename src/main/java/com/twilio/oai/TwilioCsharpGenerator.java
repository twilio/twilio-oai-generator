package com.twilio.oai;

import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.languages.CSharpClientCodegen;

import java.util.List;
import java.util.*;
import java.util.Map;

public class TwilioCsharpGenerator extends CSharpClientCodegen {

    public TwilioCsharpGenerator() {
        super();

        apiNameSuffix = "";

        // Find the templates in the local resources dir.
        embeddedTemplateDir = templateDir = getName();
        sourceFolder = "";
    }

    @Override
    public void processOpts() {
        super.processOpts();

        apiTestTemplateFiles.clear();
        apiDocTemplateFiles.clear();
        supportingFiles.clear();
        apiTemplateFiles.clear();
        apiTemplateFiles.put("apiResource.mustache", "Resource.cs");
        modelTemplateFiles.clear();
        modelTestTemplateFiles.clear();
        modelDocTemplateFiles.clear();

    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {

    }

    @Override
    public void postProcessParameter(final CodegenParameter parameter) {
        super.postProcessParameter(parameter);
    }

    @Override
    public Map<String, Object> postProcessOperationsWithModels(final Map<String, Object> objs,
                                                               final List<Object> allModels) {
        final Map<String, Object> results = super.postProcessOperationsWithModels(objs, allModels);
        return results;
    }

    @Override
    public String getName() {
        return "twilio-csharp";
    }

    @Override
    public String getHelp() {
        return "Generates the twilio-csharp helper library.";
    }

}
