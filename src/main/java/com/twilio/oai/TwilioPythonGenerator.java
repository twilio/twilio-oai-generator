package com.twilio.oai;

import com.twilio.oai.api.ApiResources;
import com.twilio.oai.api.PythonApiResourceBuilder;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.LanguageParamResolver;
import com.twilio.oai.resolver.LanguagePropertyResolver;
import com.twilio.oai.resolver.common.CodegenModelResolver;
import com.twilio.oai.resolver.python.PythonCaseResolver;
import com.twilio.oai.resource.IResourceTree;
import com.twilio.oai.resource.Resource;
import com.twilio.oai.resource.ResourceMap;
import com.twilio.oai.template.PythonApiActionTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.languages.PythonClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import static com.twilio.oai.common.ApplicationConstants.CONFIG_PYTHON_JSON_PATH;

public class TwilioPythonGenerator extends PythonClientCodegen {
    private final TwilioCodegenAdapter twilioCodegen;
    private final PythonApiActionTemplate actionTemplate = new PythonApiActionTemplate(this);
    private final IResourceTree resourceTree = new ResourceMap(new Inflector());
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
        additionalProperties,
        resourceTree,
        new PythonCaseResolver());
    private final List<CodegenModel> allModels = new ArrayList<>();
    private final Map<String, String> modelFormatMap = new HashMap<>();

    public TwilioPythonGenerator() {
        super();
        twilioCodegen = new TwilioCodegenAdapter(this, getName());
        packageName = "";
    }

    @Override
    public void processOpts() {
        twilioCodegen.processOpts();
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        final String domain = twilioCodegen.getDomainFromOpenAPI(openAPI);
        twilioCodegen.setDomain(StringHelper.toSnakeCase(domain));

        openAPI.getPaths().forEach(resourceTree::addResource);
        resourceTree.getResources().forEach(resource -> resource.updateFamily(resourceTree));

        directoryStructureService.configure(openAPI);
    }

    @Override
    public String apiFileFolder() {
        return outputFolder + File.separatorChar + packagePath() + File.separatorChar + apiPackage();
    }

    @Override
    public String apiFilename(final String templateName, final String tag) {
        Paths pathMap = this.openAPI.getPaths();
        Boolean isInitFile = false;
        for (Map.Entry<String, PathItem> pathsEntry : pathMap.entrySet()) {
            String resourcePath = pathsEntry.getKey();
            PathItem path = pathsEntry.getValue();
            // See if the resource's tag exists in any operation tags, and if the associated resource has dependents
            Operation curOp = path.getGet() != null ? path.getGet() : path.getPost() != null ? path.getPost() :
                    path.getHead() != null ? path.getHead() : path.getPut() != null ? path.getPut() :
                    path.getDelete() != null ? path.getDelete() : path.getPatch() != null ? path.getPatch() :
                    path.getOptions() != null ? path.getOptions() : path.getTrace();
            if (curOp != null) {
                List<String> opTags = curOp.getTags();
                if (opTags.contains(tag)) {
                    List<Resource> dependents = resourceTree.dependents(resourcePath);
                    if (dependents.size() > 0) {
                        isInitFile = true;
                        break;
                    }
                }
            }
        }
        String result = actionTemplate.apiFilename(templateName, super.apiFilename(templateName, tag));
        if (isInitFile) {
            result = result.substring(0, result.length()-3) + File.separator + "__init__.py";
        }
        return result;
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
        final CodegenModelResolver codegenModelResolver = new CodegenModelResolver(conventionMapper,
                                                                                   modelFormatMap,
                                                                                   List.of(EnumConstants.NodeDataTypes.values()));

        return new PythonApiResourceBuilder(actionTemplate, opList, allModels, directoryStructureService)
            .updateApiPath()
            .updateTemplate()
            .updateOperations(new LanguageParamResolver(conventionMapper))
            .updateResponseModel(new LanguagePropertyResolver(conventionMapper), codegenModelResolver)
            .build();
    }

    @Override
    public String getName() {
        return EnumConstants.Generator.TWILIO_PYTHON.getValue();
    }
}
