package com.twilio.oai;

import com.twilio.oai.api.CsharpApiResourceBuilder;
import com.twilio.oai.api.CsharpApiResources;
import com.twilio.oai.api.JavaApiResourceBuilder;
import com.twilio.oai.api.JavaApiResources;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Serializer;
import com.twilio.oai.common.Utility;
import com.twilio.oai.mlambdas.TitleCaseLambda;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.common.CodegenModelResolver;
import com.twilio.oai.resolver.csharp.CSharpCaseResolver;
import com.twilio.oai.resolver.csharp.CSharpResolver;
import com.twilio.oai.resolver.csharp_new.CsharpEnumResolver;
import com.twilio.oai.resolver.csharp_new.CsharpParameterResolver;
import com.twilio.oai.resolver.csharp_new.CsharpPropertyResolver;
import com.twilio.oai.resolver.csharp_new.CsharpSerializer;
import com.twilio.oai.resolver.csharp_new.OperationCache;
import com.twilio.oai.resolver.java.JavaParameterResolver;
import com.twilio.oai.resolver.java.JavaPropertyResolver;
import com.twilio.oai.resource.ResourceMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.samskivert.mustache.Mustache;
import com.twilio.oai.template.CsharpApiActionTemplate;
import com.twilio.oai.template.IApiActionTemplate;
import com.twilio.oai.template.JavaApiActionTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenResponse;
import org.openapitools.codegen.IJsonSchemaValidationProperties;
import org.openapitools.codegen.languages.CSharpClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import static com.twilio.oai.common.ApplicationConstants.CONFIG_CSHARP_JSON_PATH;
import static com.twilio.oai.common.ApplicationConstants.CONFIG_JAVA_JSON_PATH;

@Slf4j
public class TwilioCsharpGenerator extends CSharpClientCodegen {

    private final TwilioCodegenAdapter twilioCodegen;
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
        additionalProperties,
        new ResourceMap(new Inflector()),
        new CSharpCaseResolver());
    private final Map<String, String> modelFormatMap = new HashMap<>();
    private final List<CodegenModel> allModels = new ArrayList<>();

    CSharpResolver resolver = new CSharpResolver();

    private final IConventionMapper conventionMapper = new LanguageConventionResolver(CONFIG_CSHARP_JSON_PATH);
    private final IApiActionTemplate apiActionTemplate = new CsharpApiActionTemplate(this);

    private CsharpApiResources processCodegenOperations(List<CodegenOperation> opList) {
        CodegenModelResolver codegenModelResolver = new CodegenModelResolver(conventionMapper, modelFormatMap,
                Arrays.asList(EnumConstants.CsharpDataTypes.values()));
        CsharpSerializer csharpSerializer = new CsharpSerializer(Arrays.asList(EnumConstants.CsharpDataTypes.values()));
        CsharpEnumResolver csharpEnumResolver = new CsharpEnumResolver();
        return new CsharpApiResourceBuilder(apiActionTemplate, opList, this.allModels, csharpEnumResolver, csharpSerializer)
                .updateApiPath()
                .updateTemplate()
                .updateOperations(new CsharpParameterResolver(conventionMapper))
                .updateResponseModel(new CsharpPropertyResolver(conventionMapper), codegenModelResolver)
                .setImports(directoryStructureService)
                .build();
    }

    public TwilioCsharpGenerator() {
        super();

        twilioCodegen = new TwilioCodegenAdapter(this, getName());

        sourceFolder = "";
        packageName = "";
    }

    @Override
    public void processOpts() {
        super.processOpts();

        twilioCodegen.processOpts();

//        apiTestTemplateFiles.clear();
//        apiDocTemplateFiles.clear();
//        apiTemplateFiles.clear();
        //apiTemplateFiles.put("Resource.mustache", "Resource.cs");
        //apiTemplateFiles.put("Options.mustache", "Options.cs");
//        modelTemplateFiles.clear();
//        modelTestTemplateFiles.clear();
//        modelDocTemplateFiles.clear();

        filesMetadataFilename = "";
        versionMetadataFilename = "";
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        final String domain = StringHelper.camelize(twilioCodegen.getDomainFromOpenAPI(openAPI));
        final String version = (String) additionalProperties.get("apiVersionClass");
        twilioCodegen.setDomain(domain);
        twilioCodegen.setOutputDir(domain, version);

        directoryStructureService.configure(openAPI);
    }

    private List<CodegenParameter> getNonPathParams(List<CodegenParameter> allParams) {
        return allParams.stream().filter(param -> !param.isPathParam).collect(Collectors.toList());
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
        results.put("resources", apiResources);
        // End
        return results;

        // start
/*        final String recordKey = directoryStructureService.getRecordKey(opList);
        results.put("recordKey", recordKey);

        final Map<String, Map<String, Object>> resources = new LinkedHashMap<>();
        final List<CodegenModel> responseModels = new ArrayList<>();

        final Map<String, IJsonSchemaValidationProperties> enums = new HashMap<>();
        resolver.setEnums(enums);

        for (final CodegenOperation co : opList) {
            String[] filePathArray = co.baseName.split(ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER);
            String resourceName = filePathArray[filePathArray.length - 1];
            resolver.setClassName(resourceName);
            Map<String, Object> resource = resources.computeIfAbsent(resourceName, k -> new LinkedHashMap<>());

            Utility.populateCrudOperations(co);
            resolveCodeOperationParams(co, recordKey, responseModels);

            // Add operations key to resource
            final List<CodegenOperation> resourceOperationList = Utility.getOperations(resource);
            resourceOperationList.add(co);

            boolean arrayParamsPresent = hasArrayParams(co.allParams);

            Serializer.serialize(co.allParams);
            Serializer.serialize(co.pathParams);
            generateGetParams(co);
            generatePackage(co, resource);
            requestBodyArgument(co);

            // Remove PageSize from optionalParams
            co.optionalParams = co.optionalParams.stream().filter(parameter -> !parameter.paramName.equals("PageSize")).collect(Collectors.toList());
            resource.put("path", co.path);
            resource.put("resourceName", resourceName);
            resource.put("resourceConstant", "Resource");
            handleImports(resource, enums, arrayParamsPresent);
        }

        for (final Map<String, Object> resource : resources.values()) {
            resource.put("responseModel", getDistinctResponseModel(responseModels));
        }

        List<IJsonSchemaValidationProperties> enumList = new ArrayList<>(resolver.getEnums().values());
        results.put("enums", enumList);
        results.put("resources", resources.values());*/


    }

    private void generatePackage(final CodegenOperation co, final Map<String, Object> resource) {
        String[] filePathArray = co.baseName.split(ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER);

        final String[] packagePaths = Arrays.copyOfRange(filePathArray, 0, filePathArray.length - 1);
        if (packagePaths.length > 0) {
            String packagePath = String.join(".", packagePaths);
            resource.put("packageSubPart", "." + packagePath);
        }
    }

    private boolean hasArrayParams(List<CodegenParameter> allParams) {
        for (CodegenParameter item : allParams) {
            if (item.isArray) {
                return true;
            }
        }
        return false;
    }

    private List<CodegenParameter> getHeaderParams(final CodegenOperation co) {
        LinkedList<CodegenParameter> headerParams = new LinkedList<>();

        for (CodegenParameter codegenParameter: co.allParams) {
            if (codegenParameter.isHeaderParam) {
                headerParams.add(codegenParameter);
            }
        }
        return headerParams;
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
