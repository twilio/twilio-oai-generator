package com.twilio.oai;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.ReservedKeyword;
import com.twilio.oai.common.Serializer;
import com.twilio.oai.common.Utility;
import com.twilio.oai.csharp.CSharpCaseResolver;
import com.twilio.oai.mlambdas.TitleCaseLambda;
import com.twilio.oai.resolver.csharp.CSharpResolver;
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
import io.swagger.v3.oas.models.OpenAPI;
import lombok.extern.slf4j.Slf4j;
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
import org.openapitools.codegen.utils.StringUtils;

@Slf4j
public class TwilioCsharpGenerator extends CSharpClientCodegen {

    private final TwilioCodegenAdapter twilioCodegen;
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(new ResourceMap(
        new Inflector()), new CSharpCaseResolver());
    private final List<CodegenModel> allModels = new ArrayList<>();
    private final Map<String, String> modelFormatMap = new HashMap<>();

    CSharpResolver resolver = new CSharpResolver();

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

        apiTestTemplateFiles.clear();
        apiDocTemplateFiles.clear();
        apiTemplateFiles.clear();
        apiTemplateFiles.put("Resource.mustache", "Resource.cs");
        apiTemplateFiles.put("Options.mustache", "Options.cs");
        modelTemplateFiles.clear();
        modelTestTemplateFiles.clear();
        modelDocTemplateFiles.clear();

        filesMetadataFilename = "";
        versionMetadataFilename = "";
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        final String domain = StringUtils.camelize(twilioCodegen.getDomainFromOpenAPI(openAPI));
        final String version = (String) additionalProperties.get("apiVersionClass");
        twilioCodegen.setDomain(domain);
        twilioCodegen.setOutputDir(domain, version);

        directoryStructureService.configure(openAPI, additionalProperties);
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
        final Map<String, Map<String, Object>> resources = new LinkedHashMap<>();

        List<CodegenModel> responseModels = new ArrayList<>();
        String[] tagMap = ((Map<String, String>) objs.get("operations")).get("classname").split(ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER);
        String className = tagMap[tagMap.length-1];

        resolver.setClassName(className);

        final ArrayList<CodegenOperation> opList = getAllOperations(results);
        final Map<String, IJsonSchemaValidationProperties> enums = new HashMap<>();
        resolver.setEnums(enums);

        for (final CodegenOperation co : opList) {
            String path = co.path;
            String[] filePathArray = co.baseName.split(ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER);
            String resourceName = filePathArray[filePathArray.length - 1];
            Map<String, Object> resource = resources.computeIfAbsent(resourceName, k -> new LinkedHashMap<>());
            populateCrudOperations(co);
            resolveCodeOperationParams(co, opList, results, responseModels);

            // Add operations key to resource
            final ArrayList<CodegenOperation> resourceOperationList = (ArrayList<CodegenOperation>) resource.computeIfAbsent("operations", k -> new ArrayList<>());
            resourceOperationList.add(co);

            boolean arrayParamsPresent = hasArrayParams(co.allParams);

            Serializer.serialize(co.allParams);
            Serializer.serialize(co.pathParams);
            generateGetParams(co);
            generatePackage(co, resource);
            requestBodyArgument(co);

            resource.put("path", path);
            resource.put("resourceName", resourceName);
            resource.put("resourceConstant", "Resource");
            handleImports(resource, enums, arrayParamsPresent);
        }

        for (final Map<String, Object> resource : resources.values()) {
            resource.put("responseModel", getDistinctResponseModel(responseModels));
            PathUtils.flattenStringMap(resource, "models");
        }

        List<IJsonSchemaValidationProperties> enumList = new ArrayList<>(resolver.getEnums().values());
        results.put("enums", enumList);
        results.put("resources", resources.values());
        return results;
    }

    private void generatePackage(final CodegenOperation co, final Map<String, Object> resource) {
        String[] filePathArray = co.baseName.split(ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER);

        // Generate packages for domains without version
        if (directoryStructureService.isVersionLess(this.additionalProperties)) {
            resource.put("apiVersionClass", StringUtils.camelize(PathUtils.getFirstPathPart(co.path)));
        }

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

    private void generateGetParams(CodegenOperation co) {
        LinkedList<CodegenParameter> headerParams = new LinkedList<>();
        for (CodegenParameter codegenParameter: co.allParams) {
            if (!codegenParameter.isPathParam && !codegenParameter.isHeaderParam) {
                headerParams.add(codegenParameter);
            }
        }

        co.vendorExtensions.put("x-getparams", headerParams);
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

    private void requestBodyArgument(final CodegenOperation co) {
        List<CodegenParameter> conditionalParameters = new ArrayList<>();
        List<CodegenParameter> optionalParameters = new ArrayList<>();

        HashMap<String, CodegenParameter> optionalParamMap = new HashMap<>();
        for (CodegenParameter codegenParameter: co.optionalParams) {
            optionalParamMap.put(codegenParameter.paramName, codegenParameter);
        }

        // Process Conditional Parameters
        if (co.vendorExtensions.containsKey("x-twilio")) {
            HashMap<String, Object> twilioVendorExtension = (HashMap<String, Object>) co.vendorExtensions.get("x-twilio");
            if (twilioVendorExtension != null && twilioVendorExtension.containsKey("conditional")) {
                List<List<String>> conditionalParamList = (List<List<String>>) twilioVendorExtension.get("conditional");
                for (List<String> conditionalParams: conditionalParamList) {
                    for (String conditionalParam: conditionalParams) {
                        if (optionalParamMap.containsKey(StringUtils.camelize(conditionalParam))) {
                            conditionalParameters.add(optionalParamMap.get(StringUtils.camelize(conditionalParam)));
                            optionalParamMap.remove(StringUtils.camelize(conditionalParam));
                        }
                    }
                }
            }
        }

        // Remove path param from Optional Parameters as they appear before conditional and optional Parameter
        for (CodegenParameter pathParam: co.pathParams) {
            optionalParamMap.remove(pathParam.paramName);
        }

        // Process Optional Parameters
        for (CodegenParameter optionalParam: co.optionalParams) {
            if (optionalParamMap.containsKey(optionalParam.paramName)) {
                optionalParameters.add(optionalParam);
            }
        }

        rearrangeBeforeAfter(co.requiredParams);
        rearrangeBeforeAfter(co.pathParams);
        rearrangeBeforeAfter(conditionalParameters);
        rearrangeBeforeAfter(optionalParameters);

        // Add to vendor extension
        LinkedHashMap<String, CodegenParameter> requestBodyArgument = new LinkedHashMap<>();
        co.requiredParams.forEach(parameter -> requestBodyArgument.put(parameter.paramName, parameter));
        co.pathParams.forEach(parameter -> requestBodyArgument.put(parameter.paramName, parameter));
        conditionalParameters.forEach(parameter -> requestBodyArgument.put(parameter.paramName, parameter));
        optionalParameters.forEach(parameter -> requestBodyArgument.put(parameter.paramName, parameter));

        co.vendorExtensions.put("x-request-body-param", new ArrayList<>(requestBodyArgument.values()));
    }

    void rearrangeBeforeAfter(final List<CodegenParameter> parameters) {
        for (int index = 0; index < parameters.size(); index++) {
            CodegenParameter codegenParameter = parameters.get(index);
            if (!org.apache.commons.lang3.StringUtils.isBlank(codegenParameter.paramName) && codegenParameter.paramName.endsWith("Before")) {
                String paramName = codegenParameter.paramName.replace("Before", "");
                if (index > 0 && paramName.equals(parameters.get(index-1).paramName)) {
                    CodegenParameter codegenParameterToRearrange = parameters.get(index-1);
                    parameters.set(index, codegenParameterToRearrange);
                    parameters.set(index-1, codegenParameter);
                }
            }
        }
    }

    @Override
    protected ImmutableMap.Builder<String, Mustache.Lambda> addMustacheLambdas() {
        ImmutableMap.Builder<String, Mustache.Lambda> lambdaBuilder = super.addMustacheLambdas();
        lambdaBuilder.put("titlecasewithnumbers", new TitleCaseLambda());
        return lambdaBuilder;
    }

    private Optional<CodegenModel> getModelCoPath(final String modelName, CodegenOperation codegenOperation, String recordKey) {
        if (codegenOperation.vendorExtensions.containsKey("x-is-read-operation") && (boolean)codegenOperation.vendorExtensions.get("x-is-read-operation")) {
            Optional<CodegenModel> coModel = allModels.stream().filter(model -> model.getClassname().equals(modelName)).findFirst();
            if (coModel.isEmpty()) {
                return Optional.empty();
            }
            CodegenProperty property = coModel.get().vars.stream().filter(prop -> prop.baseName.equals(recordKey)).findFirst().get();
            return allModels.stream().filter(model -> model.getClassname().equals(property.complexType)).findFirst();
        }
        return allModels.stream().filter(model -> model.getClassname().equals(modelName)).findFirst();
    }


    private List<CodegenProperty> getDistinctResponseModel(List<CodegenModel> responseModels) {
        Set<CodegenProperty> distinctResponseModels = new LinkedHashSet<>();
        for (CodegenModel codegenModel: responseModels) {
            for (CodegenProperty property: codegenModel.vars) {
                ReservedKeyword.Csharp[] reservedKeyWords = ReservedKeyword.Csharp.values();
                Optional<ReservedKeyword.Csharp> result = Arrays.stream(reservedKeyWords)
                        .filter(value -> value.getValue().equals(property.nameInCamelCase))
                        .findAny();
                if (result.isPresent()) {
                    property.nameInCamelCase = "_" + property.nameInCamelCase;
                }
                distinctResponseModels.add(property);
            }
        }
        return new LinkedList<>(distinctResponseModels);
    }

    private void resolveCodeOperationParams(final CodegenOperation co, ArrayList<CodegenOperation> opList, OperationsMap results, List<CodegenModel> responseModels) {
        resolver.resolve(co.pathParams);
        resolver.resolve(co.queryParams);
        resolver.resolve(co.optionalParams);
        resolver.resolve(co.requiredParams);
        resolver.resolve(co.allParams);
        resolver.resolve(co.headerParams);

        if (!co.requiredParams.isEmpty()) {
            co.vendorExtensions.put("x-required-param-exist", true);
        }

        String recordKey = getRecordKey(opList, this.allModels);
        co.vendorExtensions.put("x-non-path-params", getNonPathParams(co.allParams));

        // Used in GetHeaderParams
        List<CodegenParameter> headerParams = getHeaderParams(co);
        if (headerParams.size() > 0) {
            co.vendorExtensions.put("x-header-params-exists", true);
            co.vendorExtensions.put("x-header-params", headerParams);
        }

        // Resolve Response Model
        for (CodegenResponse response : co.responses) {
            String modelName = response.dataType;
            Optional<CodegenModel> responseModel = getModelCoPath(modelName,co, recordKey);
            if (responseModel.isEmpty()) {
                continue;
            }

            resolver.resolve(responseModel.get());
            responseModels.add(responseModel.get()); // Check for DeleteCall (delete operation)
        }

        results.put("recordKey", recordKey);
    }

    // Sanitizing URL path similar to java codegen.
    @Override
    public String sanitizeTag(String tag) {
        tag = org.openapitools.codegen.utils.StringUtils.camelize(org.openapitools.codegen.utils.StringUtils.underscore(this.sanitizeName(tag)));
        if (tag.matches("^\\d.*")) {
            tag = "Class" + tag;
        }

        return tag;
    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(final Map<String, ModelsMap> allModels) {
        final Map<String, ModelsMap> results = super.postProcessAllModels(allModels);

        for (final ModelsMap mods : results.values()) {
            final List<ModelMap> modList = mods.getModels();

            // Add all the models to the local models list.
            modList
                    .stream()
                    .map(ModelMap::getModel)
                    .map(CodegenModel.class::cast)
                    .collect(Collectors.toCollection(() -> this.allModels));
        }
        Utility.setComplexDataMapping(this.allModels, this.modelFormatMap);
        resolver.setModelFormatMap(modelFormatMap);
        // Return an empty collection so no model files get generated.
        return new HashMap<>();
    }


    private String getRecordKey(List<CodegenOperation> opList, List<CodegenModel> models) {
        String recordKey =  "";
        for (CodegenOperation co: opList) {
            for(CodegenModel model: models) {
                if(model.name.equals(co.returnType)) {
                    recordKey = model.allVars
                            .stream()
                            .filter(v -> v.openApiType.equals("array"))
                            .collect(Collectors.toList()).get(0).baseName;
                }
            }
        }
        return recordKey;
    }

    private ArrayList<CodegenOperation> getAllOperations(final Map<String, Object> results) {
        final Map<String, Object> ops = PathUtils.getStringMap(results, "operations");
        return (ArrayList<CodegenOperation>) ops.get("operation");
    }

    private void populateCrudOperations(final CodegenOperation operation) {
        if (operation.nickname.startsWith(EnumConstants.Operation.CREATE.getValue())) {
            operation.vendorExtensions.put("x-is-create-operation", true);
        } else if (operation.nickname.startsWith(EnumConstants.Operation.FETCH.getValue())) {
            operation.vendorExtensions.put("x-is-fetch-operation", true);
        } else if (operation.nickname.startsWith(EnumConstants.Operation.UPDATE.getValue())) {
            operation.vendorExtensions.put("x-is-update-operation", true);
        } else if (operation.nickname.startsWith(EnumConstants.Operation.DELETE.getValue())) {
            operation.vendorExtensions.put("x-is-delete-operation", true);
        } else {
            operation.vendorExtensions.put("x-is-read-operation", true);
        }
    }

    @Override
    public String getName() {
        return EnumConstants.Generator.TWILIO_CSHARP.getValue();
    }

    @Override
    public String getHelp() {
        return "Generates the twilio-csharp helper library.";
    }

    private void handleImports(Map<String, Object> resource, Map<String, IJsonSchemaValidationProperties> enums,
                               boolean arrayParamsPresent) {
        if(resolver.isHasEnumsInOptions()){
            resource.put("hasEnumsInOptions", true);
            resolver.setHasEnumsInOptions(false);
        }
        if(enums.size() > 0 || resolver.isHasEnumsInResource()){
            resource.put("hasEnumsInResource", true);
            resolver.setHasEnumsInResource(false);
        }
        if (arrayParamsPresent) {
            resource.put("hasArrayParams", true);
        }
    }
}
