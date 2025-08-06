package com.twilio.oai;

import com.google.common.collect.ImmutableMap;
import com.samskivert.mustache.Mustache.Lambda;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.JavaApiResource;
import com.twilio.oai.java.JavaApiResourceBuilder;
import com.twilio.oai.java.JavaTemplateUpdater;
import com.twilio.oai.java.format.JavaUpdateDefaultMapping;
import com.twilio.oai.modern.ResourceCache;
import com.twilio.oai.resolver.java.JavaCaseResolver;
import com.twilio.oai.resource.ResourceMap;
import com.twilio.oai.templating.mustache.ReplaceHyphenLambda;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.languages.JavaClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TwilioJavaGeneratorModern extends JavaClientCodegen {

    JavaUpdateDefaultMapping javaUpdateDefaultMapping = new JavaUpdateDefaultMapping();
    private final TwilioCodegenAdapter twilioCodegen;
    JavaTemplateUpdater templateUpdater = new JavaTemplateUpdater();
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
            additionalProperties,
            new ResourceMap(new Inflector()),
            new JavaCaseResolver());
    private final Map<String, String> modelFormatMap = new HashMap<>();

    public TwilioJavaGeneratorModern() {
        super();
        this.additionalProperties.put("serializationLibrary", "jackson");
        twilioCodegen = new TwilioCodegenAdapter(this, getName());
        sourceFolder = "";
    }
    @Override
    public String toExampleValue(Schema schema) {
        return super.toExampleValue(schema);
    }
    @Override
    public void processOpts() {
        super.processOpts();
        this.modelTemplateFiles.clear();
        javaUpdateDefaultMapping.typeMapping(this.typeMapping);
        javaUpdateDefaultMapping.importMapping(this.importMapping);
        twilioCodegen.processOpts();
    }

    // Run once per spec
    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        String domain = twilioCodegen.getDomainFromOpenAPI(openAPI);
        String version = twilioCodegen.getVersionFromOpenAPI(openAPI);
        twilioCodegen.setDomain(domain);
        twilioCodegen.setVersion(version);
        twilioCodegen.setOutputDir(domain, version);
        javaUpdateDefaultMapping.removePropertiesFromCustomModels(openAPI);
        directoryStructureService.configure(openAPI);
    }

    @Override
    public String toApiFilename(final String name) {
        return directoryStructureService.toApiFilename(super.toApiFilename(name));
    }
    
    // DO NOT REMOVE this method even though it is not override.
    @Override
    public String getTypeDeclaration(Schema schema) {
        return super.getTypeDeclaration(schema);
    }

    // Run once per spec.
    @Override
    public Map<String, ModelsMap> postProcessAllModels(final Map<String, ModelsMap> allModels) {
        final Map<String, ModelsMap> results = super.postProcessAllModels(allModels);
        ResourceCache.clearAllModelsByDefaultGenerator();
        // Update allModels from Default generator in ResourceCache.
        Utility.addModelsToLocalModelList(results, ResourceCache.getAllModelsByDefaultGenerator());
        directoryStructureService.postProcessAllModels(results, modelFormatMap);

        // Return an empty collection so no model files get generated.
        return new HashMap<>();
    }

    @Override
    public String getSchemaType(Schema p) {
        String schemaType = super.getSchemaType(p);
        return schemaType;
    }
    
    // Run once per operation groups
    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        ResourceCache.clear();
        final List<CodegenOperation> opList = directoryStructureService.processOperations(results);
        JavaApiResource apiResource = processCodegenOperations(opList);
        results.put("resources", apiResource);
        return results;
    }

    @Override
    protected ImmutableMap.Builder<String, Lambda> addMustacheLambdas() {
        ImmutableMap.Builder<String, Lambda> lambdaBuilder = super.addMustacheLambdas();
        lambdaBuilder.put("replacehyphen", new ReplaceHyphenLambda());
        return lambdaBuilder;
    }

    @Override
    public String toParamName(final String name) {
        return super.toVarName(twilioCodegen.toParamName(name));
    }

    @Override
    public String getName() {
        return EnumConstants.Generator.TWILIO_JAVA_MODERN.getValue();
    }

    @Override
    public String getHelp() {
        return "Generates the twilio-java-modern helper library.";
    }

    private JavaApiResource processCodegenOperations(List<CodegenOperation> operations) {
        JavaApiResource apiResource = new JavaApiResourceBuilder(this, operations)
                .resourceName()
                .recordKey()
                .processOperations()
                .build();
        return apiResource;
    }
}
