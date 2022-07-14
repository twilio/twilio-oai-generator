package com.twilio.oai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.languages.CSharpClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;
import org.openapitools.codegen.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TwilioCsharpGenerator extends CSharpClientCodegen {

    private DirectoryStructureService directoryStructureService = new DirectoryStructureService(EnumConstants.Generator.TWILIO_CSHARP);
    String initialApiPackage;
    private final List<CodegenModel> allModels = new ArrayList<>();
    private  Map<String, String> modelFormatMap = new HashMap<>();

    public TwilioCsharpGenerator() {
        super();

        apiNameSuffix = "";
        initialApiPackage = apiPackage;
        // Find the templates in the local resources dir.
        embeddedTemplateDir = templateDir = getName();
        sourceFolder = "";
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
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        directoryStructureService.configure(openAPI, additionalProperties);
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
        property.isEnum =  property.isEnum && property.dataFormat == null;
    }

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

        System.out.println("======= Operation =======");
        final ArrayList<CodegenOperation> opList = getAllOperations(results);
        for (final CodegenOperation co : opList) {
            String path = co.path;

            String[] filePathArray = co.baseName.split(ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER);
            String resourceName = filePathArray[filePathArray.length-1];
            final Map<String, Object> resource = resources.computeIfAbsent(resourceName, k -> new LinkedHashMap<>());
            populateCrudOperations(resource, co);

            List<String> packagePaths = Arrays.asList(Arrays.copyOfRange(filePathArray,0 , filePathArray.length-1))
                    .stream().collect(Collectors.toList());

            // Add operations key to resource
            final ArrayList<CodegenOperation> resourceOperationList = (ArrayList<CodegenOperation>) resource.computeIfAbsent("operations", k -> new ArrayList<>());
            resourceOperationList.add(co);

            co.allParams.stream().map(ConventionResolver::resolveParamTypes).map(item -> StringUtils.camelize(item.paramName)).collect(Collectors.toList());

            System.out.println(co.operationId + "path: --> " + packagePaths);
            if (packagePaths.isEmpty()) {
                resource.put("packageSubPart", "");
            } else {
                String packagePath = packagePaths.stream().collect(Collectors.joining("."));
                resource.put("packageSubPart", "."+packagePath);
            }
            if (co.operationId.equals("DeleteCall")) {
                System.out.println("Path is: " +path);
            }


            results.put("recordKey", getRecordKey(opList, this.allModels));
            resource.put("path", path);
            resource.put("resourceName", resourceName);
            resource.put("resourceConstant", "Resource");
        }


        results.put("resources", resources.values());
        return results;
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
        setObjectFormatMap(this.allModels);
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

    private void setObjectFormatMap(final List<CodegenModel> allModels) {
        allModels.forEach(item -> {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(item.modelJson);
                if (jsonNode.get("type").textValue().equals("object") && jsonNode.has("format")) {
                    modelFormatMap.put(item.classFilename, jsonNode.get("format").textValue());
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
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
