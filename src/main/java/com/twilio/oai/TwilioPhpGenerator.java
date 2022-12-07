package com.twilio.oai;

import com.twilio.oai.api.PhpApiResourceBuilder;
import com.twilio.oai.api.PhpApiResources;
import com.twilio.oai.api.PhpDomainBuilder;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.php.*;
import com.twilio.oai.resource.ResourceMap;
import com.twilio.oai.template.IApiActionTemplate;
import com.twilio.oai.template.PhpApiActionTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.languages.PhpClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import java.util.*;

public class TwilioPhpGenerator extends PhpClientCodegen {

    private String PHP_CONVENTIONAL_MAP_PATH = "config/" + EnumConstants.Generator.TWILIO_PHP.getValue() + ".json";
    private final TwilioCodegenAdapter twilioCodegen;
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
            additionalProperties,
            new ResourceMap(new Inflector()),
            new PhpCaseResolver());
    private final List<CodegenModel> allModels = new ArrayList<>();
    private final Map<String, String> modelFormatMap = new HashMap<>();
    private IConventionMapper conventionMapper = new LanguageConventionResolver(PHP_CONVENTIONAL_MAP_PATH);
    private IApiActionTemplate phpApiActionTemplate = new PhpApiActionTemplate(this);

    public TwilioPhpGenerator() {
        super();
        twilioCodegen = new TwilioCodegenAdapter(this, getName());
    }

    @Override
    public String toApiFilename(final String name) {

        return directoryStructureService.toApiFilename(super.toApiFilename(name));
    }

    @Override
    public void processOpts() {
        super.processOpts();
        twilioCodegen.processOpts();
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        final String domain = StringHelper.camelize(twilioCodegen.getDomainFromOpenAPI(openAPI));
        final String version = StringHelper.camelize((String) additionalProperties.get("apiVersionClass"));
        twilioCodegen.setDomain(domain);
        twilioCodegen.setOutputDir(domain, version);
        setSrcBasePath("");

        directoryStructureService.configureResourceFamily(openAPI);
        directoryStructureService.configure(openAPI);
        if (!directoryStructureService.isVersionLess()) {
            new PhpDomainBuilder(phpApiActionTemplate)
                    .setVersionTemplate(openAPI, directoryStructureService);
        }
    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(Map<String, ModelsMap> allModels) {
        final Map<String, ModelsMap> results = super.postProcessAllModels(allModels);

        Utility.addModelsToLocalModelList(results, this.allModels);
        Utility.setComplexDataMapping(this.allModels, this.modelFormatMap);

        // Return an empty collection so no model files get generated.
        return new HashMap<>();
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        final List<CodegenOperation> opList = directoryStructureService.processOperations(results);
        PhpApiResources apiResources = processCodegenOperations(opList);
        results.put("resources", apiResources);
        return results;
    }

    @Override
    public String getName() {
        return EnumConstants.Generator.TWILIO_PHP.getValue();
    }

    @Override
    public String toParamName(final String name) {
        return StringHelper.camelize(twilioCodegen.toParamName(name));
    }

    private PhpApiResources processCodegenOperations(List<CodegenOperation> opList) {
        return new PhpApiResourceBuilder(phpApiActionTemplate, opList, this.allModels)
                .updateApiPath()
                .updateAdditionalProps(directoryStructureService)
                .updateOperations(new PhpParameterResolver(conventionMapper))
                .updateResponseModel(new PhpPropertyResolver(conventionMapper))
                .updateTemplate()
                .setImports(directoryStructureService)
                .build();
    }
}
