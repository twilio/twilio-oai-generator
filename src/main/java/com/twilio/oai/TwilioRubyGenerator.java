package com.twilio.oai;

import com.twilio.oai.api.ApiResources;
import com.twilio.oai.api.RubyApiResourceBuilder;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.ruby.RubyCaseResolver;
import com.twilio.oai.resource.IResourceTree;
import com.twilio.oai.resource.ResourceMap;
import com.twilio.oai.template.RubyApiActionTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.languages.RubyClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwilioRubyGenerator extends RubyClientCodegen {
    private final TwilioCodegenAdapter twilioCodegen;
    private final IResourceTree resourceTree = new ResourceMap(new Inflector());
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
            additionalProperties,
            resourceTree,
            new RubyCaseResolver());
    private final List<CodegenModel> allModels = new ArrayList<>();
    private final Map<String, String> modelFormatMap = new HashMap<>();
    private final RubyApiActionTemplate rubyApiActionTemplate = new RubyApiActionTemplate(this);

    public TwilioRubyGenerator() {
        super();
        twilioCodegen = new TwilioCodegenAdapter(this, getName());
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        final String domain = twilioCodegen.getDomainFromOpenAPI(openAPI);
        twilioCodegen.setDomain(StringHelper.camelize(domain, true));
        openAPI.getPaths().forEach(resourceTree::addResource);
        resourceTree.getResources().forEach(resource -> resource.updateFamily(resourceTree));
        setGemName("");
        this.libFolder = "";
        directoryStructureService.configure(openAPI);
    }

    @Override
    public String apiFilename(final String templateName, final String tag) {
        String filename = rubyApiActionTemplate.apiFilename(templateName, super.apiFilename(templateName, tag));
        return filename;
    }

    @Override
    public String toApiFilename(final String name) {
        return directoryStructureService.toApiFilename(name);
    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(final Map<String, ModelsMap> allModels) {
        final Map<String, ModelsMap> results = super.postProcessAllModels(allModels);

        Utility.addModelsToLocalModelList(results, this.allModels);
        directoryStructureService.postProcessAllModels(results, modelFormatMap);

        // Return an empty collection so no model files get generated.
        return new HashMap<>();
    }

    @Override
    public void processOpts() {
        super.processOpts();
        twilioCodegen.processOpts();
    }


    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        final List<CodegenOperation> opList = directoryStructureService.processOperations(results);
        results.put("resources", generateResources(opList));
        return results;
    }

    private ApiResources generateResources(final List<CodegenOperation> opList) {
        updateApiVersion(directoryStructureService);
        return new RubyApiResourceBuilder(rubyApiActionTemplate, opList, allModels, directoryStructureService)
                .updateApiPath()
                .updateTemplate()
                .build();
    }

    private void updateApiVersion(DirectoryStructureService directoryStructureService){
        String apiVersionClass = (String)directoryStructureService.getAdditionalProperties().get("apiVersionClass");
        directoryStructureService.getAdditionalProperties().put("apiVersionClass",StringHelper.toSnakeCase(apiVersionClass));
    }

    @Override
    public String getName() {
        return "twilio-ruby";
    }
}