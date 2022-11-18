package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.HttpMethod;
import com.twilio.oai.PathUtils;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.EnumConstants;

import com.twilio.oai.resolver.php.IResolver;
import com.twilio.oai.resource.Resource;
import com.twilio.oai.template.ITemplate;
import com.twilio.oai.template.PHPAPITemplate;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import org.openapitools.codegen.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;

public class PHPAPIResourceBuilder implements IAPIResourceBuilder {
    public final static String API_OPERATION_READ = "Read";
    public final static String API_OPERATION_CREATE = "Create";
    public final static String API_OPERATION_FETCH = "Fetch";
    public final static String API_OPERATION_UPDATE = "Update";
    public final static String API_OPERATION_DELETE = "Delete";
    public final static String META_LIST_PARAMETER_KEY = "x-list-parameters";
    public final static String META_CONTEXT_PARAMETER_KEY = "x-context-parameters";

    private ITemplate phpTemplate;
    private APIResources apiResource ;
    private List<CodegenModel> allModels;
    private final HashMap<String,List<Object>> contextResourcesMap=new HashMap<>();
    private final List<Object> contextResourcesList =new ArrayList<>();

    public PHPAPIResourceBuilder(ITemplate template){
        this.phpTemplate=template;
    }

    public PHPAPIResourceBuilder(ITemplate template, List<CodegenOperation> codegenOperations, List<CodegenModel> allModels) {
        this.phpTemplate = template;
        this.apiResource = new APIResources(codegenOperations, allModels);
        this.allModels = allModels;
    }

    @Override
    public IAPIResourceBuilder setRequiredParams(IResolver<CodegenParameter> codegenParameterIResolver) {
        return this;
    }

    @Override
    public IAPIResourceBuilder setOperations(IResolver<CodegenParameter> codegenParameterIResolver) {
        this.apiResource.getApiOperations().stream().forEach(codegenOperation -> {
            codegenOperation.pathParams = codegenOperation.pathParams.stream().map(item ->
                    codegenParameterIResolver.resolve(item)).collect(Collectors.toList());
            codegenOperation.allParams = codegenOperation.allParams.stream().map(item ->
                    codegenParameterIResolver.resolve(item)).collect(Collectors.toList());
            codegenOperation.queryParams = codegenOperation.queryParams.stream().map(item ->
                    codegenParameterIResolver.resolve(item)).collect(Collectors.toList());
            codegenOperation.bodyParams = codegenOperation.bodyParams.stream().map(item ->
                    codegenParameterIResolver.resolve(item)).collect(Collectors.toList());
            codegenOperation.requiredParams = codegenOperation.requiredParams.stream()
                    .map(item -> codegenParameterIResolver.resolve(item)).collect(Collectors.toList());
            codegenOperation.optionalParams = codegenOperation.optionalParams.stream()
                    .map(item -> codegenParameterIResolver.resolve(item)).collect(Collectors.toList());

            apiResource.setRequiredPathParams(codegenOperation.pathParams);
            codegenOperation.vendorExtensions = mapOperation(codegenOperation);
        });
        return this;
    }

    @Override
    public PHPAPIResourceBuilder setResponseModel(IResolver<CodegenProperty> codegenPropertyIResolver) {
        List<CodegenOperation> apiOperations = this.apiResource.getApiOperations();
        boolean hasInstanceOperations = apiOperations.stream().anyMatch(PathUtils::isInstanceOperation);
        apiOperations.stream().forEach(codegenOperation -> {
            boolean isInstanceOperation = PathUtils.isInstanceOperation(codegenOperation);
            final HttpMethod httpMethod = HttpMethod.fromString(codegenOperation.httpMethod);
            if (isInstanceOperation || (!hasInstanceOperations && httpMethod == HttpMethod.POST)) {
                List<CodegenModel> responseModels = codegenOperation.responses
                        .stream()
                        .map(response -> response.dataType)
                        .filter(Objects::nonNull)
                        .map(modelName -> this.getModel(modelName, allModels))
                        .flatMap(Optional::stream).collect(Collectors.toList());
                responseModels.forEach( item -> {
                    item.vars = item.vars.stream().map(v ->
                            codegenPropertyIResolver.resolve(v)).collect(Collectors.toList());
                    item.allVars = item.allVars.stream().map(v ->
                            codegenPropertyIResolver.resolve(v)).collect(Collectors.toList());
                });
                this.apiResource.setResponseModels(getDistinctResponseModel(responseModels));
            }
        });
        return this;
    }

    private List<CodegenProperty> getDistinctResponseModel(List<CodegenModel> responseModels) {
        Set<CodegenProperty> distinctResponseModels = new LinkedHashSet<>();
        for (CodegenModel codegenModel: responseModels) {
            for (CodegenProperty property: codegenModel.vars) {
                property.nameInCamelCase = StringHelper.camelize(property.nameInSnakeCase);
                if (Arrays
                        .stream(EnumConstants.Operation.values())
                        .anyMatch(value -> value.getValue().equals(property.nameInCamelCase))) {
                    property.nameInCamelCase = "_" + property.nameInCamelCase;
                }
                distinctResponseModels.add(property);
            }
        }
        return new LinkedList<>(distinctResponseModels);
    }

    @Override
    public IAPIResourceBuilder setTemplate() {
        this.apiResource.getApiOperations().stream().forEach(codegenOperation -> {
            phpTemplate.clean();
            final boolean isInstanceOperation = PathUtils.isInstanceOperation(codegenOperation);
            if (isInstanceOperation) {
                phpTemplate.add(PHPAPITemplate.TEMPLATE_TYPE_CONTEXT);
            } else {
                phpTemplate.add(PHPAPITemplate.TEMPLATE_TYPE_LIST);
                phpTemplate.add(PHPAPITemplate.TEMPLATE_TYPE_PAGE);
                phpTemplate.add(PHPAPITemplate.TEMPLATE_TYPE_OPTIONS);
            }
            phpTemplate.add(PHPAPITemplate.TEMPLATE_TYPE_INSTANCE);
        });
        return this;
    }

    @Override
    public APIResources build() {
        String resourceName = getName(this.apiResource.getApiOperations().get(0));
        apiResource.setApiName(resourceName);
        apiResource.setPath(this.apiResource.getApiOperations().get(0).path);
        return apiResource;
    }

    public IAPIResourceBuilder setAdditionalProps(DirectoryStructureService directoryStructureService) {
        this.apiResource.setVersion(directoryStructureService.getApiVersionClass().get());
        return this;
    }

    public void setVersionTemplate(final OpenAPI openAPI, String domain, DirectoryStructureService directoryStructureService){
        List<Resource> context_list = filter_contextResources(openAPI,domain,directoryStructureService);
        setContextResources(context_list,directoryStructureService);
        phpTemplate.add(PHPAPITemplate.TEMPLATE_TYPE_VERSION);
    }

    private List<Resource> filter_contextResources(final OpenAPI openAPI, String domain, DirectoryStructureService directoryStructureService){
        if(domain.equals("api"))
            getApiDependents(directoryStructureService);

        List<Resource> context_list=new ArrayList<>();
        Map<String, PathItem> pathMap =openAPI.getPaths();

        for (String pathKey: pathMap.keySet()){
            String pathkey=pathKey;
            if(domain.equals("api")){
                pathkey=pathKey.split(".json")[0];
            }
            if(pathkey.endsWith("}")) {
                PathItem path = pathMap.get(pathKey);
                Optional<String> parentKey=PathUtils.getTwilioExtension(path,"parent");
                if (!parentKey.isPresent()) {
                    context_list.add(new Resource(null,pathkey,path,null));
                } else{
                    String parentkey=parentKey.get();
                    if(domain.equals("api")){
                        parentkey="/2010-04-01"+parentkey;
                    }
                    if(pathMap.containsKey(parentkey)){
                        path = pathMap.get(parentkey);
                        Optional<String> parentKey2=PathUtils.getTwilioExtension(path,"parent");
                        if (!parentKey2.isPresent()) {
                            context_list.add(new Resource(null, pathkey, path, null));
                        }}}}}
        return context_list;
    }

    private void getApiDependents(DirectoryStructureService directoryStructureService) {
        final List<Object> dependentList = new ArrayList<>();
        String pathkey="/2010-04-01/Accounts/{Sid}.json";
        directoryStructureService.resourceTree.dependents(pathkey)
                .forEach(dependent -> dependent
                .getPathItem()
                .readOperations()
                .forEach(operation -> directoryStructureService.addContextdependents(dependentList,
                        dependent.getName(),
                        operation)));
        directoryStructureService.additionalProperties.put("isApiDomain","true");
        directoryStructureService.additionalProperties.put("apiDependents",dependentList);
    }

    private void setContextResources(List<Resource> context_list, DirectoryStructureService directoryStructureService){
        context_list.forEach(dependent -> dependent
                .getPathItem()
                .readOperations()
                .forEach(operation -> directoryStructureService.addContextdependents(contextResourcesList,
                        dependent.getName(),
                        operation)));
        contextResourcesMap.put("versionDependents",contextResourcesList);
        phpTemplate.addContextResources(contextResourcesMap);
    }

    private String getName(CodegenOperation operation) {
        List<String> filePathArray = new ArrayList<>(Arrays.asList(operation.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
        String resourceName = filePathArray.remove(filePathArray.size()-1);
        return resourceName;
    }

    private Map<String, Object> mapOperation(CodegenOperation operation) {
        Map<String, Object> operationMap = new HashMap<>();
        final HttpMethod httpMethod = HttpMethod.fromString(operation.httpMethod);
        if (PathUtils.isInstanceOperation(operation)) {
            applyInstanceOperation(operation, operationMap, httpMethod);
        } else {
            applyListOperation(operation, operationMap, httpMethod);
        }

        return operationMap;
    }

    private void applyListOperation(CodegenOperation operation, Map<String, Object> operationMap, HttpMethod httpMethod) {
        if (httpMethod == HttpMethod.POST) {
            operationMap.put("x-name", API_OPERATION_CREATE);
            operationMap.put("x-is-create-operation", true);
        } else if (httpMethod == HttpMethod.GET) {
            operationMap.put("x-name", API_OPERATION_READ);
            operationMap.put("x-is-read-operation", true);
        }
        this.apiResource.getMetaProperties().put(META_LIST_PARAMETER_KEY, operation.allParams);
    }

    private void applyInstanceOperation(CodegenOperation operation, Map<String, Object> operationMap, HttpMethod httpMethod) {
        if (httpMethod == HttpMethod.GET) {
            operationMap.put("x-name", API_OPERATION_FETCH);
            operationMap.put("x-is-fetch-operation", true);
        } else if (httpMethod == HttpMethod.POST) {
            operationMap.put("x-name", API_OPERATION_UPDATE);
            operationMap.put("x-is-update-operation", true);
        } else if (httpMethod == HttpMethod.DELETE) {
            operationMap.put("x-name", API_OPERATION_DELETE);
            operation.vendorExtensions.put("x-is-delete-operation", true);
        }
        this.apiResource.getMetaProperties().put(META_CONTEXT_PARAMETER_KEY, operation.allParams);
    }

    private Optional<CodegenModel> getModel(final String modelName, List<CodegenModel> allModels) {
        String[] modelNames = modelName.split("\\\\");
        String extractedModelName = modelNames[modelNames.length-1];
        return allModels.stream().filter(model -> model.getClassname().equals(extractedModelName)).findFirst();
    }



    private boolean isInstanceOperation(CodegenOperation operation) {
        boolean is_fetch = operation.nickname.startsWith(EnumConstants.Operation.FETCH.getValue().toLowerCase(Locale.ROOT));
        boolean is_update = operation.nickname.startsWith(EnumConstants.Operation.UPDATE.getValue().toLowerCase(Locale.ROOT));
        boolean is_delete = operation.nickname.startsWith(EnumConstants.Operation.DELETE.getValue().toLowerCase(Locale.ROOT));
        if(is_fetch || is_update || is_delete) {
            return true;
        }
        return false;
    }
}
