package com.twilio.oai;

import com.twilio.oai.api.ApiResources;
import com.twilio.oai.api.PythonApiResourceBuilder;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.python.PythonCodegenModelResolver;
import com.twilio.oai.resolver.python.PythonCaseResolver;
import com.twilio.oai.resolver.python.PythonParameterResolver;
import com.twilio.oai.resolver.python.PythonPropertyResolver;
import com.twilio.oai.resource.IResourceTree;
import com.twilio.oai.resource.ResourceMap;
import com.twilio.oai.template.PythonApiActionTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.languages.PythonLegacyClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import static com.twilio.oai.common.ApplicationConstants.CONFIG_PYTHON_JSON_PATH;

public class TwilioPythonGenerator extends PythonLegacyClientCodegen {
    private final TwilioCodegenAdapter twilioCodegen;
    private final PythonApiActionTemplate actionTemplate = new PythonApiActionTemplate(this);
    private final IResourceTree resourceTree = new ResourceMap(new Inflector());
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
        additionalProperties,
        resourceTree,
        new PythonCaseResolver());
    private final List<CodegenModel> allModels = new ArrayList<>();
    private final Map<String, String> modelFormatMap = new HashMap<>();
    public static final String JSON_INGRESS = "json_ingress";

    public TwilioPythonGenerator() {
        super();
        twilioCodegen = new TwilioCodegenAdapter(this, getName());
        packageName = "";

        typeMapping.put("array", "List");
    }

    @Override
    public void processOpts() {
        twilioCodegen.processOpts();
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

        directoryStructureService.configure(openAPI);
    }

    @Override
    public String apiFilename(final String templateName, final String tag) {
        final String initFilename = "__init__.py";
        final String filename = actionTemplate.apiFilename(templateName, super.apiFilename(templateName, tag));

        final boolean isInitFile = this.openAPI.getPaths().entrySet().stream().anyMatch(pathsEntry -> {
            final String resourcePath = pathsEntry.getKey();
            final PathItem path = pathsEntry.getValue();
            final boolean isMatchingPath = path
                .readOperations()
                .stream()
                .anyMatch(operation -> operation.getTags().contains(tag));
            final boolean pathHasDependents = !resourceTree.dependents(resourcePath).isEmpty();

            return isMatchingPath && pathHasDependents;
        });

        if (isInitFile && !filename.endsWith(initFilename)) {
            return PathUtils.removeExtension(filename) + File.separator + initFilename;
        }

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
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final List<CodegenOperation> opList = directoryStructureService.processOperations(objs);
        objs.put("resources", generateResources(opList));
        return objs;
    }

    private ApiResources generateResources(final List<CodegenOperation> opList) {
        final IConventionMapper conventionMapper = new LanguageConventionResolver(CONFIG_PYTHON_JSON_PATH);
        final PythonCodegenModelResolver codegenModelResolver = new PythonCodegenModelResolver(conventionMapper,
                                                                                   modelFormatMap,
                                                                                   List.of(EnumConstants.PythonDataTypes.values()));

        PythonApiResourceBuilder pythonApiResourceBuilder = new PythonApiResourceBuilder(actionTemplate, opList, allModels, directoryStructureService, twilioCodegen.getToggles(JSON_INGRESS));
        pythonApiResourceBuilder.updateApiPath()
                                .updateTemplate()
                                .updateOperations(new PythonParameterResolver(conventionMapper, codegenModelResolver));
        return pythonApiResourceBuilder.updateResponseModel(new PythonPropertyResolver(conventionMapper), codegenModelResolver)
                                       .build();
    }

    @Override
    public String getName() {
        return EnumConstants.Generator.TWILIO_PYTHON.getValue();
    }

    @Override
    public String toParamName(final String name) {
        return StringHelper.toSnakeCase(twilioCodegen.toParamName(name));
    }

    @Override
    public String defaultTemplatingEngine() {
        return "twilio-handlebars";
    }
}
