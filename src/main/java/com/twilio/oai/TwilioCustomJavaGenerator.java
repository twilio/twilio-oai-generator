package com.twilio.oai;

import com.samskivert.mustache.Mustache;
import com.twilio.oai.api.JavaApiResources;
import com.twilio.oai.tmp.JavaApiResource;
import com.twilio.oai.tmp.JavaApiResourceBuilder;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.templating.mustache.ReplaceHyphenLambda;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.common.CodegenModelResolver;
import com.twilio.oai.resolver.java.JavaCaseResolver;
import com.twilio.oai.resolver.java.JavaParameterResolver;
import com.twilio.oai.resolver.java.JavaPropertyResolver;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resource.ResourceMap;

import com.twilio.oai.template.IApiActionTemplate;
import com.twilio.oai.template.JavaApiActionTemplate;
import com.twilio.oai.tmp.JavaOperationProcessor;
import com.twilio.oai.tmp.TemplateModifier;
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


public class TwilioCustomJavaGenerator extends JavaClientCodegen {

    private final TemplateModifier templateModifier;
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
            additionalProperties,
            new ResourceMap(new Inflector()),
            new JavaCaseResolver());
    private final IConventionMapper conventionMapper = new LanguageConventionResolver(CONFIG_JAVA_JSON_PATH);
    private final List<CodegenModel> allModels = new ArrayList<>();

    public TwilioCustomJavaGenerator() {
        super();
        templateModifier = new TemplateModifier(this);
        sourceFolder = "";
        this.typeMapping.put("string+abc", "CustomType");
        this.importMapping.put("CustomType", "hello.import.CustomType");
        System.out.println(this.typeMapping());
    }

    @Override
    public void processOpts() {
        super.processOpts();
        sourceFolder = "";
        templateModifier.resetPredefinedTemplate();
        this.filesMetadataFilename = "";
        this.versionMetadataFilename = "";
        // Mustache file lookup location
        setTemplateDir("twilio-java-custom");
        setModelPackage("models");
        apiTemplateFiles().put("api.mustache", ".java");
        
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        super.processOpenAPI(openAPI);
        this.setApiPackage("hello");
        this.setOutputDir("hello");

        
        this.openAPI = openAPI;
    }

//    @Override
//    public String toParamName(final String name) {
//        return super.toVarName(twilioCodegen.toParamName(name));
//    }
    

//    @Override
//    public String toApiFilename(final String name) {
//        return directoryStructureService.toApiFilename(super.toApiFilename(name));
//    }

//    @Override
//    public Map<String, ModelsMap> postProcessAllModels(final Map<String, ModelsMap> allModels) {
//        final Map<String, ModelsMap> results = super.postProcessAllModels(allModels);
//
//        Utility.addModelsToLocalModelList(results, this.allModels);
//        directoryStructureService.postProcessAllModels(results, modelFormatMap);
//
//        // Return an empty collection so no model files get generated.
//        return new HashMap<>();
//    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
       // final List<CodegenOperation> opList = directoryStructureService.processOperations(results);
        JavaApiResource apiResource = processCodegenOperations(results.getOperations().getOperation());
        results.put("resources", apiResource);
        return results;
    }

    @Override
    protected ImmutableMap.Builder<String, Mustache.Lambda> addMustacheLambdas() {
        ImmutableMap.Builder<String, Mustache.Lambda> lambdaBuilder = super.addMustacheLambdas();
        lambdaBuilder.put("replacehyphen", new ReplaceHyphenLambda());
        return lambdaBuilder;
    }

    @Override
    public String getName() {
        return EnumConstants.Generator.TWILIO_JAVA_CUSTOM.getValue();
    }

    @Override
    public String getHelp() {
        return "Generates the twilio-java helper library.";
    }

    private JavaApiResource processCodegenOperations(List<CodegenOperation> opList) {
        JavaOperationProcessor operationProcessor = new JavaOperationProcessor();
        JavaApiResourceBuilder javaApiResourceBuilder = new JavaApiResourceBuilder(opList, operationProcessor);
        javaApiResourceBuilder.process();
        return javaApiResourceBuilder.build();
    }
}
