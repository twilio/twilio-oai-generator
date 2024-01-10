package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.PathUtils;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.ruby.RubyCodegenModelResolver;
import com.twilio.oai.resource.Resource;
import com.twilio.oai.template.IApiActionTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.twilio.oai.DirectoryStructureService.VERSION_RESOURCES;
import static com.twilio.oai.common.ApplicationConstants.DESERIALIZE_VEND_EXT;
import static com.twilio.oai.common.ApplicationConstants.STRING;

public class RubyApiResourceBuilder extends FluentApiResourceBuilder {

    List<CodegenParameter> readParams;
    List<String[]> parentDir = new ArrayList<>();
    boolean hasParents = false;
    final OpenAPI openApi;

    public RubyApiResourceBuilder(final IApiActionTemplate template, final List<CodegenOperation> codegenOperations, final List<CodegenModel> allModels, final DirectoryStructureService directoryStructureService, final OpenAPI openApi) {
        super(template, codegenOperations, allModels, directoryStructureService);
        this.openApi = openApi;
    }

    public RubyApiResourceBuilder(final IApiActionTemplate template, final List<CodegenOperation> codegenOperations, final List<CodegenModel> allModels, final DirectoryStructureService directoryStructureService, final OpenAPI openApi, final Map<String, Boolean> toggleMap) {
        super(template, codegenOperations, allModels, directoryStructureService);
        this.openApi = openApi;
        this.toggleMap = toggleMap;
    }

    public RubyApiResourceBuilder updateResponseModel(Resolver<CodegenProperty> codegenPropertyResolver, RubyCodegenModelResolver codegenModelResolver) {
        final String resourceName = getApiName();

        final List<CodegenModel> allResponseModels = codegenOperationList
                .stream()
                .flatMap(co -> co.responses
                        .stream()
                        .map(response -> response.dataType)
                        .filter(Objects::nonNull)
                        .flatMap(modelName -> getModel(modelName, co).stream())
                        .findFirst()
                        .stream())
                .collect(Collectors.toList());

        allResponseModels.stream().findFirst().ifPresent(firstModel -> {
            responseModel = firstModel;

            allResponseModels.forEach(model -> {
                codegenModelResolver.resolveResponseModel(model, this);

                model.setName(resourceName);
                model.getVars().forEach(variable -> {
                    codegenPropertyResolver.resolve(variable, this);

                    instancePathParams
                            .stream()
                            .filter(param -> param.paramName.equals(variable.name))
                            .filter(param -> param.dataType.equals(STRING))
                            .filter(param -> !param.dataType.equals(variable.dataType))
                            .forEach(param -> param.vendorExtensions.put("x-stringify", true));
                });

                if (model != responseModel) {
                    // Merge any vars from the model that aren't part of the response model.
                    model.getVars().forEach(variable -> {
                        if (responseModel.getVars().stream().noneMatch(v -> v.getName().equals(variable.getName()))) {
                            responseModel.getVars().add(variable);
                        }
                    });
                }
            });

            responseModel.getVars().forEach(variable -> {
                addModel(modelTree, variable.complexType, variable.dataType);

                updateDataType(variable.complexType, variable.dataType, (dataTypeWithEnum, dataType) -> {
                    variable.datatypeWithEnum = dataTypeWithEnum;
                    variable.baseType = dataType;
                });
            });
        });

        modelTree.values().forEach(model -> model.setName(getModelName(model.getClassname())));
        if (responseModel != null) {
            responseModel.getVars().forEach(variable -> {
                if (variable.complexType != null && !variable.complexType.contains(ApplicationConstants.ENUM)) {
                    getModelByClassname(variable.complexType).ifPresent(model -> {
                        // variable.baseType = variable.baseType.replace(variable.datatypeWithEnum, "str");
                        // variable.datatypeWithEnum = "str";
                        model.vendorExtensions.put("part-of-response-model", true);
                    });
                }
            });
        }

        return this.updateVars();
    }

    @Override
    public RubyApiResources build() {
        fetchParentDirectory();
        return new RubyApiResources(this);
    }

    @Override
    public ApiResourceBuilder updateOperations(Resolver<CodegenParameter> codegenParameterIResolver) {
        ApiResourceBuilder apiResourceBuilder = super.updateOperations(codegenParameterIResolver);
        resolveHeaderParams(codegenParameterIResolver);
        createReadParams((RubyApiResourceBuilder) apiResourceBuilder);
        updatePaths();
        updateRequiredPathParams(apiResourceBuilder);
        createContextParamsList(apiResourceBuilder.codegenOperationList);
        categorizeOperations();
        createMaturityDescription(apiResourceBuilder.codegenOperationList);
        updateDependentProperties(apiResourceBuilder.codegenOperationList);
        updateVersionData();
        return apiResourceBuilder;
    }

    @Override
    public RubyApiResourceBuilder updateResponseModel(final Resolver<CodegenProperty> codegenPropertyResolver,
                                                      final Resolver<CodegenModel> codegenModelResolver) {
        return ((RubyApiResourceBuilder) super
                .updateResponseModel(codegenPropertyResolver, codegenModelResolver))
                .updateVars();
    }

    private void resolveHeaderParams(Resolver<CodegenParameter> codegenParameterIResolver) {
        codegenOperationList.forEach(codegenOperation ->
                codegenOperation.headerParams.forEach(e -> codegenParameterIResolver.resolve(e, this)));
    }

    private void createReadParams(RubyApiResourceBuilder apiResourceBuilder) {
        this.readParams = new ArrayList<>();
        for (CodegenOperation operation : apiResourceBuilder.codegenOperationList) {
            if ((boolean) operation.vendorExtensions.getOrDefault("x-is-read-operation", false)) {
                for (CodegenParameter param : operation.allParams) {
                    if (!param.paramName.equals("page_size")) {
                        readParams.add(param);
                    }
                }
            }
        }
    }

    private void updatePaths() {
        boolean isPhoneNumberParams = instancePathParams.stream().anyMatch(param -> Objects.equals(param.dataFormat, "phone-number"));
        if (listPath != null) listPath = listPath.replace("${", "#{@solution[:").replace("}", "]}");
        if (instancePath != null) {
            if (isPhoneNumberParams)
                instancePath = instancePath.replace("${", "#{CGI.escape(@solution[:").replace("}", "]).gsub(\"+\", \"%20\")}");
            else
                instancePath = instancePath.replace("${", "#{@solution[:").replace("}", "]}");
        }
    }

    private void createContextParamsList(List<CodegenOperation> opList) {
        HashSet<String> seenOps = new HashSet<>();
        opList.forEach(operation -> {
            if (!seenOps.contains(operation.path)) {
                List<Object> dependentPropertiesList = new ArrayList<>();
                List<Object> dependentMethods = new ArrayList<>();

                List<Resource> dependents = StreamSupport.stream(directoryStructureService.getResourceTree().getResources().spliterator(), false)
                        .filter(resource -> PathUtils.removeFirstPart(operation.path)
                                .equals(PathUtils.getTwilioExtension(resource.getPathItem(), "parent")
                                        .orElse(null)))
                        .collect(Collectors.toList());
                List<Resource> methodDependents = dependents.stream().filter(dep ->
                                PathUtils.getTwilioExtension(dep.getPathItem(), "pathType").get().equals("instance"))
                        .collect(Collectors.toList());
                dependents.removeIf(methodDependents::contains);
                dependents.addAll(Optional.ofNullable(methodDependents.stream()).orElse(Stream.empty()).filter(dep ->
                        !dep.getName().endsWith("}") && !dep.getName().endsWith("}.json")).collect(Collectors.toList()));

                updateDependents(directoryStructureService, dependents, dependentPropertiesList);
                updateDependents(directoryStructureService, methodDependents, dependentMethods);
                dependentPropertiesList.removeAll(dependentPropertiesList.stream()
                        .map(DirectoryStructureService.ContextResource.class::cast)
                        .filter(dependent -> dependentMethods.stream()
                                .map(DirectoryStructureService.ContextResource.class::cast).anyMatch(
                                        methodDependent -> methodDependent.getMountName().equals(dependent.getMountName()))

                        ).collect(Collectors.toList()));
                if (PathUtils.isInstanceOperation(operation)) {
                    metaAPIProperties.put("contextImportProperties", dependentPropertiesList);
                    metaAPIProperties.put("contextImportMethods", dependentMethods);

                } else {
                    metaAPIProperties.put("listImportProperties", dependentPropertiesList);
                    metaAPIProperties.put("listImportMethods", dependentMethods);
                }
                seenOps.add(operation.path);
            }
        });
    }
    private void updateDependentProperties(List<CodegenOperation> opList){
        for (CodegenOperation operation : opList) {
            Map<String, String> dependentsForOperation = mapOperationsDependents(operation);
            updateContextResourceDependents(dependentsForOperation, metaAPIProperties.get("contextImportMethods"));
            updateContextResourceDependents(dependentsForOperation, metaAPIProperties.get("contextImportProperties"));
        }
    }

    private void updateContextResourceDependents(Map<String, String> dependentsForOperation, Object listofContextResourceObjs){
        List<DirectoryStructureService.ContextResource> listObjs = (List<DirectoryStructureService.ContextResource>) listofContextResourceObjs;
        if(listObjs != null) {
            for (DirectoryStructureService.ContextResource cr : listObjs) {
                 String value = (dependentsForOperation.containsKey(cr.getFilename())) ? dependentsForOperation.get(cr.getFilename())
                        : dependentsForOperation.get(cr.getMountName());
                cr.setDependentProperties(value);
            }
        }
    }

    private Map<String, String> mapOperationsDependents(CodegenOperation operation){
        String dependentParams = "";
        Set<String> seenParams = new HashSet<>();
        HashMap<String, String> depMap = new HashMap<>();
        if (operation.vendorExtensions.containsKey(ApplicationConstants.DEPENDENT_PROPERTIES)){
            HashMap<String, LinkedHashMap<String, Object>> dependentProperties = (HashMap<String, LinkedHashMap<String, Object>>) operation.vendorExtensions.get(ApplicationConstants.DEPENDENT_PROPERTIES);

            for(Map.Entry<String, LinkedHashMap<String, Object>> propertiesDetails : dependentProperties.entrySet()){
                String mountName = propertiesDetails.getKey();
                dependentParams = "";
                LinkedHashMap<String, String> mapping = (LinkedHashMap<String, String>)propertiesDetails.getValue().get("mapping");
                if(!seenParams.contains(mountName)) {
                    for (Map.Entry<String, String> mappingEntry : mapping.entrySet()) {
                        String dependent = mappingEntry.getKey() + ": @solution[:" + mappingEntry.getValue()+ "], ";
                        dependentParams += dependent;
                        depMap.put(mountName, dependentParams);
                    }
                }
                seenParams.add(mountName);
            }
        }
        return depMap;
    }

    private void createMaturityDescription(List<CodegenOperation> opList) {
        Set<String> typesOfProducts = new HashSet<>();
        for (CodegenOperation op : opList) {
            List<String> values = (List<String>) op.vendorExtensions.get("x-maturity");
            if (values != null) typesOfProducts.addAll(values);
        }
        if (typesOfProducts.contains("Beta"))
            metaAPIProperties.put("x-maturity-desc", "PLEASE NOTE that this class contains beta products that are subject to change. Use them with caution.");
        if (typesOfProducts.contains("Preview"))
            metaAPIProperties.put("x-maturity-desc", "PLEASE NOTE that this class contains preview products that are subject to change. Use them with caution. If you currently do not have developer preview access, please contact help@twilio.com.");
    }

    private void updateRequiredPathParams(ApiResourceBuilder apiResourceBuilder) {
        if (!apiResourceBuilder.requiredPathParams.isEmpty()) {
            for (CodegenParameter param : apiResourceBuilder.requiredPathParams) {
                param.vendorExtensions.put("isInstanceParam", !param.paramName.equals("account_sid"));
            }
        }
    }

    private RubyApiResourceBuilder updateVars() {
        if (responseModel != null && responseModel.vars != null)
            for (CodegenProperty property : responseModel.vars) {
                String instanceProperty = (String) property.vendorExtensions.getOrDefault(DESERIALIZE_VEND_EXT, "{value}");
                property.vendorExtensions
                        .put("instance-property", instanceProperty.replace("{value}", "payload['" + property.name + "']"));
            }
        return this;
    }

    private void fetchParentDirectory() {
        String path = codegenOperationList.get(0).path;
        List<String> parentFiles = new ArrayList<>();
        final Resource resource = directoryStructureService.getResourceTree().findResource(path).orElseThrow();
        Optional<Resource> parent = getParent(resource);
        while (parent.isPresent()) {
            Resource parentResource = parent.get();
            Optional<String> pathType = PathUtils.getTwilioExtension(parentResource.getPathItem(), "pathType");
            if (pathType.isPresent()) {
                String pathtype = pathType.get();
                if (pathtype.equals("instance")) {
                    List<Operation> ops = parentResource.getPathItem().readOperations();

                    if (ops.isEmpty() || ops.get(0).getExtensions().containsKey("x-ignore"))
                        parentFiles.add(0, parentResource.getResourceAliases().getClassName() + "List");
                    else
                        parentFiles.add(0, parentResource.getResourceAliases().getClassName() + "Context");
                } else
                    parentFiles.add(0, parentResource.getResourceAliases().getClassName() + "List");
            } else
                parentFiles.add(0, parentResource.getResourceAliases().getClassName());
            parent = getParent(parentResource);
        }

        for (String file : parentFiles) {
            if (file.endsWith("List")) {
                parentDir.add(new String[]{file, "ListResource"});
            } else {
                parentDir.add(new String[]{file, "InstanceContext"});
            }
            hasParents = true;
        }
    }

    private Optional<Resource> getParent(Resource resource) {
        PathItem pathItem = resource.getPathItem();
        Optional<String> optionalParent = PathUtils.getTwilioExtension(pathItem, "parent");
        if (optionalParent.isPresent()) {
            String parentPath = optionalParent.get();
            return directoryStructureService.getResourceTree().findResource(parentPath, false);
        }
        return Optional.empty();
    }

    private void addResources(List<Resource> dependents, DirectoryStructureService directoryStructureService, List<Object> dependentList) {
        dependents.forEach(dependent -> dependent
                .getPathItem()
                .readOperations()
                .forEach(operation -> directoryStructureService.addContextdependents(dependentList,
                        dependent.getName(),
                        operation)));
    }

    private boolean dependentExist(String mountName, List<DirectoryStructureService.ContextResource> versionDependents) {
        for (var versionDependent : versionDependents) {
            if (versionDependent.getMountName().equals(mountName)) {
                return true;
            }
        }
        return false;
    }

    private void updateVersionResources() {
        var versionResources = (List<DirectoryStructureService.DependentResource>) directoryStructureService.getAdditionalProperties().getOrDefault("versionResources", null);
        if (versionResources != null) {
            for (var versionResource : versionResources) {
                //here "hasInstanceOperation" may be either null or false, so only set if it is true
                if (versionResource.getResourceName().equals(getApiName()) && (boolean) metaAPIProperties.getOrDefault("hasInstanceOperation", false)) {
                    versionResource.setInstanceDependent(true);
                }
            }
            var domain = (String) directoryStructureService.getAdditionalProperties().get("domainName");
            versionResources = versionResources.stream().filter(resource -> !(resource.getMountName().equals("account") && domain.equals("Api"))).collect(Collectors.toList());
        }
        directoryStructureService.getAdditionalProperties().put(VERSION_RESOURCES, versionResources);
    }

    private void updateVersionDependents() {
        List<Object> dependentList = new ArrayList<>();
        String pathkey = "/2010-04-01/Accounts/{Sid}.json";
        List<Resource> dependents = directoryStructureService.getResourceTree().dependents(pathkey)
                .stream().filter(dep -> !dep.getName().endsWith("}.json"))
                .collect(Collectors.toList());
        addResources(dependents, directoryStructureService, dependentList);
        var versionDependents = (List<DirectoryStructureService.ContextResource>) directoryStructureService.getAdditionalProperties().getOrDefault("versionDependents", null);
        if (versionDependents == null) return;

        //remove accounts from versionDependents for Api domain
        versionDependents = versionDependents.stream().filter(dependent -> !dependent.getMountName().equals("accounts")).collect(Collectors.toList());
        //add dependents from dependents list if not already present
        for (var dependent : dependentList) {
            if (!dependentExist(((DirectoryStructureService.ContextResource) dependent).getMountName(), versionDependents)) {
                versionDependents.add((DirectoryStructureService.ContextResource) dependent);
            }
        }
        //sort versionDependents alphabetically
        Collections.sort(versionDependents, Comparator.comparing(DirectoryStructureService.ContextResource::getMountName));
        directoryStructureService.getAdditionalProperties().put("versionDependents", versionDependents);
    }

    private ApiResourceBuilder updateVersionData() {
        updateVersionDependents();
        updateVersionResources();
        return this;
    }
}
