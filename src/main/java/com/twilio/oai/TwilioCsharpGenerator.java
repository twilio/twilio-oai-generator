package com.twilio.oai;

import com.google.common.collect.ImmutableMap;
import com.samskivert.mustache.Mustache;
import com.twilio.oai.api.CsharpApiResourceBuilder;
import com.twilio.oai.api.CsharpApiResources;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.LanguageDataType;
import com.twilio.oai.common.Utility;
import com.twilio.oai.templating.mustache.TitleCaseLambda;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.common.CodegenModelResolver;
import com.twilio.oai.resolver.common.CodegenParameterResolver;
import com.twilio.oai.resolver.csharp.CSharpCaseResolver;
import com.twilio.oai.resolver.csharp.CsharpCodegenModelDataTypeResolver;
import com.twilio.oai.resolver.csharp.CsharpCodegenParameterDataTypeResolver;
import com.twilio.oai.resolver.csharp.CsharpSerializer;
import com.twilio.oai.resolver.csharp.OperationStore;
import com.twilio.oai.resource.ResourceMap;
import com.twilio.oai.template.CsharpApiActionTemplate;
import com.twilio.oai.template.IApiActionTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenSecurity;
import org.openapitools.codegen.languages.CSharpClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.twilio.oai.common.ApplicationConstants.CONFIG_CSHARP_JSON_PATH;

@Slf4j
public class TwilioCsharpGenerator extends CSharpClientCodegen {

    private final TwilioCodegenAdapter twilioCodegen;
    private final String BEARER_TOKEN_PREFIX = "BearerToken";
    private final String NO_AUTH_PREFIX = "NoAuth";
    private final String EMPTY_STRING = "";
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
        additionalProperties,
        new ResourceMap(new Inflector()),
        new CSharpCaseResolver());
    private final Map<String, String> modelFormatMap = new HashMap<>();
    private final List<CodegenModel> allModels = new ArrayList<>();
    private final IConventionMapper conventionMapper = new LanguageConventionResolver(CONFIG_CSHARP_JSON_PATH);
    private final IApiActionTemplate apiActionTemplate = new CsharpApiActionTemplate(this);

    public TwilioCsharpGenerator() {
        super();
        twilioCodegen = new TwilioCodegenAdapter(this, getName());
        sourceFolder = "";
        packageName = "";
    }

    private CsharpApiResources processCodegenOperations(List<CodegenOperation> opList) {
        OperationStore operationStore = OperationStore.getInstance();
        operationStore.clear();

        /*
         * Info: There are 2 types of resolver CodegenModelResolver, CodegenParameterResolver.
         * A model has multiple CodegenProperty objects.
         * CodegenModelResolver internally uses CodegenModelDataTypeResolver and CodegenModelComplexResolver for resolving datatype.
         * CodegenParameterResolver internally uses CodegenParameterDataTypeResolver for resolving datatype.
         * CsharpCodegenModelDataTypeResolver is used by overriding CodegenModelDataTypeResolver.
         */
        List<? extends LanguageDataType> languageDataType = Arrays.asList(EnumConstants.CsharpDataTypes.values());

        // Model Resolver
        CsharpCodegenModelDataTypeResolver csharpCodegenModelDataTypeResolver = new CsharpCodegenModelDataTypeResolver(conventionMapper, modelFormatMap);
        CodegenModelResolver codegenModelResolver = new CodegenModelResolver(languageDataType, csharpCodegenModelDataTypeResolver);
        csharpCodegenModelDataTypeResolver.setCodegenModel(codegenModelResolver);

        // Parameter Resolver
        CsharpSerializer csharpSerializer = new CsharpSerializer(conventionMapper);
        CsharpCodegenParameterDataTypeResolver csharpCodegenParameterDataTypeResolver = new CsharpCodegenParameterDataTypeResolver(conventionMapper, csharpSerializer);
        CodegenParameterResolver codegenParameterResolver = new CodegenParameterResolver(languageDataType, csharpCodegenParameterDataTypeResolver, codegenModelResolver);


        return new CsharpApiResourceBuilder(apiActionTemplate, opList, this.allModels)
                .updateApiPath()
                .updateTemplate()
                .updateOperations(codegenParameterResolver)
                .updateResponseModel(null, codegenModelResolver)
                .setImports(directoryStructureService)
                .build();
    }

    @Override
    public void processOpts() {
        super.processOpts();
        twilioCodegen.processOpts();
        filesMetadataFilename = "";
        versionMetadataFilename = "";
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        String domain = StringHelper.camelize(twilioCodegen.getDomainFromOpenAPI(openAPI));
        String version = StringHelper.camelize(twilioCodegen.getVersionFromOpenAPI(openAPI));
        twilioCodegen.setDomain(domain);
        twilioCodegen.setVersion(version);
        twilioCodegen.setOutputDir(domain, version);

        directoryStructureService.configure(openAPI);
    }

    @Override
    public String toParamName(final String name) {
        return super.toVarName(twilioCodegen.toParamName(name));
    }

    @Override
    public String toApiFilename(final String name) {
        return directoryStructureService.toApiFilename(super.toApiFilename(name));
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        final List<CodegenOperation> opList = directoryStructureService.processOperations(results);
        CsharpApiResources apiResources = processCodegenOperations(opList);
        apiResources.setAuthMethod(processAuthMethods(opList));
        apiResources.setRestClientPrefix(setRestClientPrefix(apiResources.getAuthMethod()));
        apiResources.setResourceSetPrefix(setResourceSetPrefix(apiResources.getAuthMethod()));
        apiResources.setDomainClassPrefix(fetchDomainClassPrefix(apiResources.getAuthMethod()));
        results.put("resources", apiResources);
        return results;
    }

    private String fetchDomainClassPrefix(String authMethod) {
        if(authMethod == BEARER_TOKEN_PREFIX) return "OrgsTokenAuth";
        else if(authMethod == NO_AUTH_PREFIX) return NO_AUTH_PREFIX;
        return EMPTY_STRING;
    }

    private String setResourceSetPrefix(String authMethod){
        return authMethod == BEARER_TOKEN_PREFIX ? authMethod : EMPTY_STRING;
    }

    private String setRestClientPrefix(String authMethod){
        return authMethod == EMPTY_STRING ? "I" : EMPTY_STRING;
    }

    private String processAuthMethods(List<CodegenOperation> opList) {
        if(opList != null){
            List<CodegenSecurity> authMethods = opList.get(0).authMethods;
            if(authMethods != null){
                for(CodegenSecurity c : authMethods){
                    if(c.isOAuth == true){
                        return "BearerToken";
                    }
                }
            }
            else return "NoAuth";
        }
        return "";
    }

    @Override
    protected ImmutableMap.Builder<String, Mustache.Lambda> addMustacheLambdas() {
        ImmutableMap.Builder<String, Mustache.Lambda> lambdaBuilder = super.addMustacheLambdas();
        lambdaBuilder.put("titlecasewithnumbers", new TitleCaseLambda());
        return lambdaBuilder;
    }

    @Override
    public void postProcessParameter(CodegenParameter parameter) {
        super.postProcessParameter(parameter);
        if (parameter.isPathParam) {
            parameter.paramName = "Path" + parameter.paramName;
        }
    }

    // Sanitizing URL path similar to java codegen.
    @Override
    public String sanitizeTag(String tag) {
        tag = StringHelper.camelize(org.openapitools.codegen.utils.StringUtils.underscore(this.sanitizeName(tag)));
        if (tag.matches("^\\d.*")) {
            tag = "Class" + tag;
        }

        return tag;
    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(final Map<String, ModelsMap> allModels) {
        final Map<String, ModelsMap> results = super.postProcessAllModels(allModels);

        Utility.addModelsToLocalModelList(results, this.allModels);
        directoryStructureService.postProcessAllModels(results, modelFormatMap);
        return new HashMap<>();
    }

    @Override
    public String getName() {
        return EnumConstants.Generator.TWILIO_CSHARP.getValue();
    }

    @Override
    public String getHelp() {
        return "Generates the twilio-csharp helper library.";
    }
}
