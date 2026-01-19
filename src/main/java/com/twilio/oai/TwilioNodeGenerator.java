package com.twilio.oai;

import com.twilio.oai.api.ApiResources;
import com.twilio.oai.api.NodeApiResourceBuilder;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.LanguagePropertyResolver;
import com.twilio.oai.resolver.node.NodeCaseResolver;
import com.twilio.oai.resolver.node.NodeCodegenModelResolver;
import com.twilio.oai.resolver.node.NodeParameterResolver;
import com.twilio.oai.resource.IResourceTree;
import com.twilio.oai.resource.ResourceMap;
import com.twilio.oai.template.NodeApiActionTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.languages.TypeScriptNodeClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import static com.twilio.oai.common.ApplicationConstants.CONFIG_NODE_JSON_PATH;

public class TwilioNodeGenerator extends TypeScriptNodeClientCodegen {
    private final TwilioCodegenAdapter twilioCodegen;
    private final NodeApiActionTemplate actionTemplate = new NodeApiActionTemplate(this);
    private final IResourceTree resourceTree = new ResourceMap(new Inflector());
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
        additionalProperties,
        resourceTree,
        new NodeCaseResolver());

    private final List<CodegenModel> allModels = new ArrayList<>();
    private final Map<String, String> modelFormatMap = new HashMap<>();

    public static final String JSON_INGRESS = "json_ingress";

    public TwilioNodeGenerator() {
        super();
        twilioCodegen = new TwilioCodegenAdapter(this, getName());
    }

    @Override
    public void processOpts() {
        super.processOpts();

        twilioCodegen.processOpts();
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        String domain = StringHelper.camelize(twilioCodegen.getDomainFromOpenAPI(openAPI), true);
        String version = StringHelper.camelize(twilioCodegen.getVersionFromOpenAPI(openAPI), true);
        twilioCodegen.setDomain(domain);
        twilioCodegen.setVersion(version);
        twilioCodegen.setIsV1ApiStandard(openAPI);
        twilioCodegen.setOutputDir(domain, version);

        openAPI.getPaths().forEach(resourceTree::addResource);
        resourceTree.getResources().forEach(resource -> resource.updateFamily(resourceTree));

        directoryStructureService.configure(openAPI);
    }

    @Override
    public String apiFilename(final String templateName, final String tag) {
        return actionTemplate.apiFilename(templateName, super.apiFilename(templateName, tag));
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
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        final List<CodegenOperation> opList = directoryStructureService.processOperations(results);

        results.put("resources", generateResources(opList));
        return results;
    }

    private ApiResources generateResources(final List<CodegenOperation> opList) {
        final IConventionMapper conventionMapper = new LanguageConventionResolver(CONFIG_NODE_JSON_PATH);
        final NodeCodegenModelResolver nodeCodegenModelResolver = new NodeCodegenModelResolver(conventionMapper,
                                                                                   modelFormatMap,
                                                                                   List.of(EnumConstants.NodeDataTypes.values()));

        NodeApiResourceBuilder nodeApiResourceBuilder = new NodeApiResourceBuilder(actionTemplate, opList, allModels, directoryStructureService
                , twilioCodegen.getToggles(JSON_INGRESS));

        nodeApiResourceBuilder.updateApiPath()
            .updateTemplate()
            .updateOperations(new NodeParameterResolver(conventionMapper, nodeCodegenModelResolver));

        nodeApiResourceBuilder.updateResponseModel(new LanguagePropertyResolver(conventionMapper), nodeCodegenModelResolver);

        return nodeApiResourceBuilder.build();
    }

    @Override
    public String getName() {
        return EnumConstants.Generator.TWILIO_NODE.getValue();
    }

    @Override
    public String getHelp() {
        return "Generates the twilio-node helper library.";
    }

    @Override
    public String toParamName(final String name) {
        return Arrays
            .stream(twilioCodegen.toParamName(name).split("\\."))
            .map(input -> StringHelper.camelize(input, true))
            .collect(Collectors.joining("."));
    }
}
