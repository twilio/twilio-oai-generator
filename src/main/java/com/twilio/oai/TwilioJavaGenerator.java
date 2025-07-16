package com.twilio.oai;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.JavaTemplateUpdater;
import com.twilio.oai.modern.JavaApiResource;
import com.twilio.oai.modern.JavaApiResourceBuilderNew;
import com.twilio.oai.modern.ResourceCache;
import com.twilio.oai.templating.mustache.ReplaceHyphenLambda;
import com.twilio.oai.resolver.java.JavaCaseResolver;
import com.twilio.oai.resource.ResourceMap;

import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.JavaClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import java.util.*;

import com.google.common.collect.ImmutableMap;
import com.samskivert.mustache.Mustache.Lambda;


public class TwilioJavaGenerator extends JavaClientCodegen {

    private final TwilioCodegenAdapter twilioCodegen;
    JavaTemplateUpdater templateUpdater = new JavaTemplateUpdater();
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
            additionalProperties,
            new ResourceMap(new Inflector()),
            new JavaCaseResolver());
    private final Map<String, String> modelFormatMap = new HashMap<>();

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
        JavaApiResource apiResourceNew = processCodegenOperations(opList);
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

    private JavaApiResource processCodegenOperations(List<CodegenOperation> operations) {
        templateUpdater.addApiTemplate(this, operations);
        JavaApiResourceBuilderNew javaApiResourceBuilder = new JavaApiResourceBuilderNew(this);
        javaApiResourceBuilder.process(operations);
        return javaApiResourceBuilder.build();
    }
}
