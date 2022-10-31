package com.twilio.oai;

import com.twilio.oai.api.APIResources;
import com.twilio.oai.api.IAPIResourceBuilder;
import com.twilio.oai.api.PHPAPIResourceBuilder;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.php.*;
import com.twilio.oai.resource.ResourceMap;
import com.twilio.oai.template.PHPAPITemplate;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.languages.PhpClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import java.util.*;
import java.util.stream.Collectors;

public class TwilioPhpGenerator extends PhpClientCodegen {
    private String PHP_CONVENTIONAL_MAP_PATH = "config/"+ TwilioHelperSDK.TWILIO_PHP.getName() + ".json";
    private final TwilioCodegenAdapter twilioCodegen;
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
            additionalProperties,
            new ResourceMap(new Inflector()),
            new PhpCaseResolver());
    private final List<CodegenModel> allModels = new ArrayList<>();
    private final Map<String, String> modelFormatMap = new HashMap<>();
    private IAPIResourceBuilder phpAPIResource = null;
    private IConventionMapper conventionMapper =  new LanguageConventionResolver(PHP_CONVENTIONAL_MAP_PATH);

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
        final String domain = twilioCodegen.getDomainFromOpenAPI(openAPI);
        twilioCodegen.setDomain(domain);
        directoryStructureService.configure(openAPI);
    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(Map<String, ModelsMap> allModels) {
        final Map<String, ModelsMap> results = super.postProcessAllModels(allModels);
        for (final ModelsMap mods : results.values()) {
            final List<ModelMap> modList = mods.getModels();
            modList
                    .stream()
                    .map(ModelMap::getModel)
                    .map(CodegenModel.class::cast)
                    .collect(Collectors.toCollection(() -> this.allModels));
        }
        return new HashMap<>();
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        final List<CodegenOperation> opList = directoryStructureService.processOperations(results);
        results.put("resources", processCodegenOperations(opList));
        return results;
    }

    @Override
    public String getName() {
        return TwilioHelperSDK.TWILIO_PHP.getName();
    }

    private APIResources processCodegenOperations(List<CodegenOperation> opList) {
         return new PHPAPIResourceBuilder(new PHPAPITemplate(this), opList, this.allModels)
                .setAdditionalProps(directoryStructureService)
                .setTemplate()
                .setOperations(new PhpParameterResolver(conventionMapper))
                 .setResponseModel(new PhpPropertyResolver(conventionMapper))
                .build();
    }
}
