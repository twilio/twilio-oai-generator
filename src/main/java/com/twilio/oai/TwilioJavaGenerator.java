package com.twilio.oai;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.modern.JavaApiResourceNew;
import com.twilio.oai.modern.JavaApiResourceBuilderNew;
import com.twilio.oai.modern.JavaOperationProcessorNew;
import com.twilio.oai.modern.ResourceCache;
import com.twilio.oai.templating.mustache.ReplaceHyphenLambda;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.java.JavaCaseResolver;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resource.ResourceMap;

import com.twilio.oai.template.IApiActionTemplate;
import com.twilio.oai.template.JavaApiActionTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.JavaClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import java.util.*;

import com.google.common.collect.ImmutableMap;
import com.samskivert.mustache.Mustache.Lambda;

import static com.twilio.oai.common.ApplicationConstants.*;

public class TwilioJavaGenerator extends JavaClientCodegen {

    public static final String VALUES = "values";
    public static final String JSON_INGRESS = "json_ingress";

    private final TwilioCodegenAdapter twilioCodegen;
    private final IApiActionTemplate apiActionTemplate = new JavaApiActionTemplate(this);
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
            additionalProperties,
            new ResourceMap(new Inflector()),
            new JavaCaseResolver());
    private final Map<String, String> modelFormatMap = new HashMap<>();
    private final IConventionMapper conventionMapper = new LanguageConventionResolver(CONFIG_JAVA_JSON_PATH);
    private final List<CodegenModel> allModels = new ArrayList<>();

    public TwilioJavaGenerator() {
        super();
        twilioCodegen = new TwilioCodegenAdapter(this, getName());
        sourceFolder = "";
    }

    @Override
    public void processOpts() {
        super.processOpts();
        twilioCodegen.processOpts();
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        String domain = twilioCodegen.getDomainFromOpenAPI(openAPI);
        String version = twilioCodegen.getVersionFromOpenAPI(openAPI);
        twilioCodegen.setDomain(domain);
        twilioCodegen.setVersion(version);
        twilioCodegen.setOutputDir(domain, version);
        directoryStructureService.configure(openAPI);
    }

    @Override
    public String toApiFilename(final String name) {
        return directoryStructureService.toApiFilename(super.toApiFilename(name));
    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(final Map<String, ModelsMap> allModels) {
        final Map<String, ModelsMap> results = super.postProcessAllModels(allModels);

        Utility.addModelsToLocalModelList(results, ResourceCache.getAllModelsByDefaultGenerator());
        directoryStructureService.postProcessAllModels(results, modelFormatMap);

        // Return an empty collection so no model files get generated.
        return new HashMap<>();
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        final List<CodegenOperation> opList = directoryStructureService.processOperations(results);
        JavaApiResourceNew apiResourceNew = processCodegenOperations(opList);
        results.put("resources", apiResourceNew);
        return results;
    }

    @Override
    protected ImmutableMap.Builder<String, Lambda> addMustacheLambdas() {
        ImmutableMap.Builder<String, Lambda> lambdaBuilder = super.addMustacheLambdas();
        lambdaBuilder.put("replacehyphen", new ReplaceHyphenLambda());
        return lambdaBuilder;
    }

    @Override
    public String getName() {
        return EnumConstants.Generator.TWILIO_JAVA.getValue();
    }

    @Override
    public String getHelp() {
        return "Generates the twilio-java helper library.";
    }

    private JavaApiResourceNew processCodegenOperations(List<CodegenOperation> operations) {
        JavaApiResourceBuilderNew javaApiResourceBuilder = new JavaApiResourceBuilderNew(this);
        JavaOperationProcessorNew javaOperationProcessorNew = new JavaOperationProcessorNew();
        javaApiResourceBuilder.process(operations, javaOperationProcessorNew);
        return javaApiResourceBuilder.build();
    }
}
