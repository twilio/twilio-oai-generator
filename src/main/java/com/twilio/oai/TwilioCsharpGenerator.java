package com.twilio.oai;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.CsharpResolver;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.ParameterResolverFactory;
import com.twilio.oai.common.Utility;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class TwilioCsharpGenerator extends CSharpClientCodegen {

    private DirectoryStructureService directoryStructureService = new DirectoryStructureService(EnumConstants.Generator.TWILIO_CSHARP);
    String initialApiPackage;
    private final List<CodegenModel> allModels = new ArrayList<>();
    private  Map<String, String> modelFormatMap = new HashMap<>();

    private CsharpResolver resolver = (CsharpResolver) ParameterResolverFactory.getInstance(EnumConstants.Generator.TWILIO_CSHARP);


    public TwilioCsharpGenerator() {
        super();

        apiNameSuffix = "";
        initialApiPackage = apiPackage;
        // Find the templates in the local resources dir.
        embeddedTemplateDir = templateDir = getName();
        sourceFolder = "";
        packageName = "";
    }

    @Override
    public void processOpts() {
        super.processOpts();

        String[] inputSpecs = inputSpec.split("_");
        final String version = inputSpecs[inputSpecs.length-1].replaceAll("\\.[^/]+$", "");
        final String domain = String.join("", Arrays.copyOfRange(inputSpecs, 1, inputSpecs.length-1));

        apiPackage = version.toUpperCase(); // Place the API files in the version folder.
        additionalProperties.put("apiVersion", Utility.toFirstLetterCaps(version));
        additionalProperties.put("apiVersionClass", version.toUpperCase());
        additionalProperties.put("domain", StringUtils.camelize(domain));
        additionalProperties.put("domainPackage", Utility.toFirstLetterCaps(domain));

        apiTestTemplateFiles.clear();
        apiDocTemplateFiles.clear();
        supportingFiles.clear();
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
        directoryStructureService.configure(openAPI, additionalProperties);
    }

    private List<CodegenParameter> getNonPathParams(List<CodegenParameter> allParams) {
        return allParams.stream().filter(param -> !param.isPathParam).collect(Collectors.toList());
    }
    @Override
    public void postProcessParameter(final CodegenParameter parameter) {
        super.postProcessParameter(parameter);
    }

    @Override
    public String toParamName(String name) {
        name = name.replace("<", "Before");
        name = name.replace(">", "After");
        name = super.toVarName(name);
        return name;
    }

    @Override
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);
    }

    @Override
    public String toApiFilename(final String name) {
        String[] split = super.toApiFilename(name).split(ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER);
        String apiFileName =  Arrays.stream(Arrays.copyOfRange(split, 0, split.length - 1))
                .collect(Collectors.joining("/")) + "/"+split[split.length-1];
        return apiFileName;
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        final Map<String, Map<String, Object>> resources = new LinkedHashMap<>();

        List<CodegenModel> responseModels = new ArrayList<>();
        String recordKey = null;
        String tagMap[] = ((Map<String, String>) objs.get("operations")).get("classname").split(ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER);
        String className = tagMap[tagMap.length-1];
        resolver.setClassName(className);

        final ArrayList<CodegenOperation> opList = getAllOperations(results);
        final Map<String, IJsonSchemaValidationProperties> enums = new HashMap<>();
        resolver.setEnums(enums);

        for (final CodegenOperation co : opList) {
            String path = co.path;

            String[] filePathArray = co.baseName.split(ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER);
            String resourceName = filePathArray[filePathArray.length - 1];
            final Map<String, Object> resource = resources.computeIfAbsent(resourceName, k -> new LinkedHashMap<>());
            populateCrudOperations(resource, co);
            updateCodeOperationParams(co);
            List<String> packagePaths = Arrays.asList(Arrays.copyOfRange(filePathArray, 0, filePathArray.length - 1))
                    .stream().collect(Collectors.toList());

            // Add operations key to resource
            final ArrayList<CodegenOperation> resourceOperationList = (ArrayList<CodegenOperation>) resource.computeIfAbsent("operations", k -> new ArrayList<>());
            resourceOperationList.add(co);

            if (co.operationId.equals("CreateCall") || co.operationId.equals("DeleteCall") || co.operationId.equals("FetchCall")) {
                //System.out.println("All Params for: " + co.operationId + " are: -------------------> +" + co.allParams);
            }
            if (co.requiredParams.size() > 0) {
                co.vendorExtensions.put("x-required-param-exist", true);
            }

            // Options instance variables
            co.allParams.stream().map(resolver::resolveParameter).map(item -> StringUtils.camelize(item.paramName)).collect(Collectors.toList());

            if (packagePaths.isEmpty()) {
                resource.put("packageSubPart", "");
            } else {
                String packagePath = packagePaths.stream().collect(Collectors.joining("."));
                resource.put("packageSubPart", "." + packagePath);
            }
            recordKey = getRecordKey(opList, this.allModels);
            co.vendorExtensions.put("x-non-path-params", getNonPathParams(co.allParams));

            for (CodegenResponse response : co.responses) {
                String modelName = response.dataType;
                Optional<CodegenModel> responseModel = getModelCoPath(modelName,co, recordKey);
                if (!responseModel.isPresent()) {
                    continue;
                }
                resolver.resolveParameter(responseModel.get());
                responseModels.add(responseModel.get());
            }

            requestBodyArgument(co);

            results.put("recordKey", recordKey);
            resource.put("path", path);
            resource.put("resourceName", resourceName);
            resource.put("resourceConstant", "Resource");
        }

        for (final Map<String, Object> resource : resources.values()) {
            resource.put("responseModel", getDistinctResponseModel(responseModels));
            flattenStringMap(resource, "models");
        }

        List<IJsonSchemaValidationProperties> enumList = new ArrayList<>(resolver.getEnums().values());
        results.put("enums", enumList);
        results.put("resources", resources.values());
        return results;
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
            if (optionalParamMap.containsKey(pathParam.paramName)) {
                optionalParamMap.remove(pathParam.paramName);
            }
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
        HashSet<CodegenParameter> requestBodyArgument = new LinkedHashSet<>();
        requestBodyArgument.addAll(co.requiredParams);
        requestBodyArgument.addAll(co.pathParams);
        requestBodyArgument.addAll(conditionalParameters);
        requestBodyArgument.addAll(optionalParameters);

        co.vendorExtensions.put("x-request-body-param", new ArrayList<>(requestBodyArgument));
        int requiredCnt = 0;
        for (CodegenParameter parameter: requestBodyArgument) {
            if (parameter.required) {
                requiredCnt++;
            }
        }
        if (requiredCnt != co.requiredParams.size()) {
            System.out.println("Exception Occurred ...............");
        }
    }

    // TODO: Need to be corrected in open api spec by yps
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

    private void flattenStringMap(final Map<String, Object> resource, final String key) {
        resource.computeIfPresent(key, (k, dependents) -> ((Map<String, Object>) dependents).values());
    }

    private Optional<CodegenModel> getModelCoPath(final String modelName, CodegenOperation codegenOperation, String recordKey) {
        if (codegenOperation.vendorExtensions.containsKey("x-is-read-operation") && (boolean)codegenOperation.vendorExtensions.get("x-is-read-operation")) {
            Optional<CodegenModel> coModel = allModels.stream().filter(model -> model.getClassname().equals(modelName)).findFirst();
            if (!coModel.isPresent()) {
                return Optional.empty();
            }
            CodegenProperty property = coModel.get().vars.stream().filter(prop -> prop.baseName.equals(recordKey)).findFirst().get();
            Optional<CodegenModel> complexModel = allModels.stream().filter(model -> model.getClassname().equals(property.complexType)).findFirst();
            return complexModel;
        }
        return allModels.stream().filter(model -> model.getClassname().equals(modelName)).findFirst();
    }


    private List<CodegenProperty> getDistinctResponseModel(List<CodegenModel> responseModels) {
        Set<CodegenProperty> distinctResponseModels = new LinkedHashSet<>();
        for (CodegenModel codegenModel: responseModels) {
            for (CodegenProperty property: codegenModel.vars) {
                if (property.isEnum) {

                }
                distinctResponseModels.add(property);
            }
        }
        List<CodegenProperty> response = new LinkedList<>();
        response.addAll(distinctResponseModels);
        return response;
    }

    private void updateCodeOperationParams(final CodegenOperation co) {
        resolver.resolveParameter(co.pathParams);
        resolver.resolveParameter(co.queryParams);
        resolver.resolveParameter(co.optionalParams);
        resolver.resolveParameter(co.requiredParams);
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
        final Map<String, Object> ops = getStringMap(results, "operations");
         return (ArrayList<CodegenOperation>) ops.get("operation");
    }

    private void populateCrudOperations(final Map<String, Object> resource, final CodegenOperation operation) {
        if (operation.nickname.startsWith(EnumConstants.Operation.CREATE.getValue())) {
            resource.put("hasCreate", true);
            operation.vendorExtensions.put("x-is-create-operation", true);
            resource.put(EnumConstants.Operation.CREATE.name(), operation);
        } else if (operation.nickname.startsWith(EnumConstants.Operation.FETCH.getValue())) {
            resource.put("hasFetch", true);
            resource.put(EnumConstants.Operation.FETCH.name(), operation);
            operation.vendorExtensions.put("x-is-fetch-operation", true);
        } else if (operation.nickname.startsWith(EnumConstants.Operation.UPDATE.getValue())) {
            resource.put("hasUpdate", true);
            operation.vendorExtensions.put("x-is-update-operation", true);
            resource.put(EnumConstants.Operation.UPDATE.name(), operation);
        } else if (operation.nickname.startsWith(EnumConstants.Operation.DELETE.getValue())) {
            resource.put("hasDelete", true);
            operation.vendorExtensions.put("x-is-delete-operation", true);
            resource.put(EnumConstants.Operation.DELETE.name(), operation);
        } else {
            resource.put("hasRead", true);
            operation.vendorExtensions.put("x-is-read-operation", true);
            resource.put(EnumConstants.Operation.READ.name(), operation);
        }
        if(operation.notes!=null && !operation.notes.isEmpty())
            operation.vendorExtensions.put("x-generate-comment", operation.notes);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getStringMap(final Map<String, Object> resource, final String key) {
        return (Map<String, Object>) resource.computeIfAbsent(key, k -> new HashMap<>());
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
