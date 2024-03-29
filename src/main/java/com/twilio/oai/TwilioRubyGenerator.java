package com.twilio.oai;

import com.twilio.oai.api.RubyApiResourceBuilder;
import com.twilio.oai.api.RubyApiResources;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.common.CodegenModelResolver;
import com.twilio.oai.resolver.ruby.RubyCaseResolver;
import com.twilio.oai.resolver.ruby.RubyCodegenModelResolver;
import com.twilio.oai.resolver.ruby.RubyParameterResolver;
import com.twilio.oai.resolver.ruby.RubyPropertyResolver;
import com.twilio.oai.resource.IResourceTree;
import com.twilio.oai.resource.ResourceMap;
import com.twilio.oai.template.RubyApiActionTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
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

import static com.twilio.oai.common.ApplicationConstants.CONFIG_RUBY_JSON_PATH;

public class TwilioRubyGenerator extends RubyClientCodegen {
    private final TwilioCodegenAdapter twilioCodegen;
    private final IResourceTree resourceTree = new ResourceMap(new Inflector());
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(additionalProperties, resourceTree, new RubyCaseResolver());
    private final List<CodegenModel> allModels = new ArrayList<>();
    private final Map<String, String> modelFormatMap = new HashMap<>();
    private final RubyApiActionTemplate rubyApiActionTemplate = new RubyApiActionTemplate(this);
    public static final String JSON_INGRESS = "json_ingress";

    public TwilioRubyGenerator() {
        super();
        twilioCodegen = new TwilioCodegenAdapter(this, getName());
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {

        String domain = StringHelper.toSnakeCase(twilioCodegen.getDomainFromOpenAPI(openAPI));
        String version = StringHelper.toSnakeCase(twilioCodegen.getVersionFromOpenAPI(openAPI));
        twilioCodegen.setDomain(domain);
        twilioCodegen.setVersion(version);
        twilioCodegen.setOutputDir(domain, version);

        openAPI.getPaths().forEach(resourceTree::addResource);
        resourceTree.getResources().forEach(resource -> resource.updateFamily(resourceTree));
        setGemName("");
        this.libFolder = "";
        directoryStructureService.configure(openAPI);
        final Map<String, PathItem> pathMap = openAPI.getPaths();
        directoryStructureService.configureAdditionalProps(pathMap, domain, directoryStructureService);
    }

    @Override
    public String apiFilename(final String templateName, final String tag) {
        return rubyApiActionTemplate.apiFilename(templateName, super.apiFilename(templateName, tag));
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

    private RubyApiResources generateResources(final List<CodegenOperation> opList) {
        final IConventionMapper conventionMapper = new LanguageConventionResolver(CONFIG_RUBY_JSON_PATH);
        final RubyCodegenModelResolver codegenModelResolver = new RubyCodegenModelResolver(conventionMapper, modelFormatMap, List.of(EnumConstants.RubyDataTypes.values()));
        RubyApiResourceBuilder rubyApiResourceBuilder = new RubyApiResourceBuilder(rubyApiActionTemplate, opList, allModels, directoryStructureService, openAPI, twilioCodegen.getToggles(JSON_INGRESS));
        rubyApiResourceBuilder.updateApiPath()
                .updateTemplate()
                .updateOperations(new RubyParameterResolver(conventionMapper,codegenModelResolver));
        return rubyApiResourceBuilder.updateResponseModel(new RubyPropertyResolver(conventionMapper), codegenModelResolver)
                .build();
    }

    @Override
    public String toParamName(final String name) {
        return StringHelper.toSnakeCase(twilioCodegen.toParamName(name));
    }

    @Override
    public String getName() {
        return "twilio-ruby";
    }
}

