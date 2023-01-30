package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.PathUtils;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resource.Resource;
import com.twilio.oai.template.IApiActionTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RubyApiResourceBuilder extends FluentApiResourceBuilder {

    List<CodegenParameter> readParams;
    List<Object> componentContextClasses = new ArrayList<>();
    final OpenAPI openApi;
    private final List<CodegenOperation> instanceOperations = new ArrayList<>();

    public RubyApiResourceBuilder(final IApiActionTemplate template, final List<CodegenOperation> codegenOperations, final List<CodegenModel> allModels, final DirectoryStructureService directoryStructureService, final OpenAPI openApi) {
        super(template, codegenOperations, allModels, directoryStructureService);
        this.openApi = openApi;
    }

    @Override
    public RubyApiResources build() {
        return new RubyApiResources(this);
    }

    @Override
    public ApiResourceBuilder updateOperations(Resolver<CodegenParameter> codegenParameterIResolver) {
        ApiResourceBuilder apiResourceBuilder = super.updateOperations(codegenParameterIResolver);
        this.addCreateSeparator(apiResourceBuilder);
        this.createReadParams((RubyApiResourceBuilder) apiResourceBuilder);
        this.addContextDataForComponents();
        this.updatePaths();
        this.createContextParamsList(apiResourceBuilder.codegenOperationList);
        this.addInstanceOperations();
        this.createMaturityDescription(apiResourceBuilder.codegenOperationList);
        return apiResourceBuilder;
    }

    private void addCreateSeparator(ApiResourceBuilder apiResourceBuilder) {
        for (CodegenOperation operation : apiResourceBuilder.codegenOperationList) {
            if ((boolean) operation.vendorExtensions.getOrDefault("x-is-create-operation", false)) {
                for (CodegenParameter param : operation.allParams) {
                    param.vendorExtensions.put("separator", ",\n\t\t\t\t\t\t");
                }
                if (!operation.allParams.isEmpty())
                    operation.allParams.get(operation.allParams.size() - 1).vendorExtensions.put("separator", "\n\t\t\t\t\t");
            }
        }
    }

    private void createReadParams(RubyApiResourceBuilder apiResourceBuilder) {
        this.readParams = new ArrayList<>();
        for (CodegenOperation operation : apiResourceBuilder.codegenOperationList) {
            if ((boolean) operation.vendorExtensions.getOrDefault("x-is-read-operation", false)) {
                for (CodegenParameter param : operation.allParams) {
                    if (!param.paramName.equals("page_size")) {
                        param.vendorExtensions.put("separator", ",");
                        readParams.add(param);
                    }
                }
            }
        }
    }

    private void createContextParamsList(List<CodegenOperation> opList){
        HashSet<String> seenOps = new HashSet<>();
        opList.forEach(operation -> {
            if(!seenOps.contains(operation.path)) {
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
                if (operation.path.endsWith("}") || operation.path.endsWith("}.json")) {
                    metaAPIProperties.put("contextImportProperties", dependentPropertiesList);
                    metaAPIProperties.put("contextImportMethods", dependentMethods);

                }
                seenOps.add(operation.path);
            }
        });
    }

    private void addContextDataForComponents() {
        List<Resource> dependents = new ArrayList<>();
        dependents = getDependentInfo(dependents);
        if (!dependents.isEmpty())
            dependents.forEach(dependent -> dependent.getPathItem().readOperations().forEach(operation -> directoryStructureService.addContextdependents(componentContextClasses, dependent.getName(), operation)));
    }

    private List<Resource> getDependentInfo(List<Resource> dependents) {
        Object domain = directoryStructureService.getAdditionalProperties().get("domainName");
        Map<String, PathItem> pathMap = openApi.getPaths();
        String apiPathWithoutVersion = apiPath.substring(apiPath.indexOf("/", 1));
        for (String pathKey : pathMap.keySet()) {
            String pathkey = pathKey;
            if (domain.equals("Api")) {
                pathkey = pathKey.split(".json")[0];
                apiPathWithoutVersion = apiPathWithoutVersion.split(".json")[0];
            }
            PathItem path = pathMap.get(pathKey);
            Optional<String> parentKey = PathUtils.getTwilioExtension(path, "parent");
            if (parentKey.isPresent() && (pathKey.endsWith("}") || pathKey.endsWith("}.json"))) {
                String parentKeyValue = domain.equals("Api") ? parentKey.get().split(".json")[0] : parentKey.get();
                if (!parentKeyValue.endsWith("}")) {
                    if (parentKeyValue.equals(apiPathWithoutVersion)) {
                        dependents.add(new Resource(null, pathkey, path, null));
                    }
                }
            }
        }
        return dependents;
    }

    private void updatePaths() {
        if (listPath != null) listPath = listPath.replace("${", "#{@solution[:").replace("}", "]}");
        if (instancePath != null) instancePath = instancePath.replace("${", "#{@solution[:").replace("}", "]}");
    }

    private void addInstanceOperations() {
        codegenOperationList.stream().filter(operation -> !operation.vendorExtensions.containsKey("x-ignore")).forEach(codegenOperation -> {
            Optional<String> pathType = Optional.ofNullable(codegenOperation.vendorExtensions.get("x-path-type").toString());
            if (pathType.isPresent() && !pathType.get().equals("list")) {
                    instanceOperations.add(codegenOperation);
                    codegenOperation.vendorExtensions.put("instanceOperation", true);
                    metaAPIProperties.put("hasInstanceOperation", true);
            }
        });
    }

    private void createMaturityDescription(List<CodegenOperation> opList) {
        Set<String> typesOfProducts = new HashSet<>();
        for (CodegenOperation op : opList) {
            List<String> vals= (List<String>) op.vendorExtensions.get("x-maturity");
            if(vals!= null ) typesOfProducts.addAll(vals);
        }
        if(typesOfProducts.contains("Beta")) metaAPIProperties.put("x-maturity-desc", "PLEASE NOTE that this class contains beta products that are subject to change. Use them with caution.");
        if(typesOfProducts.contains("Preview")) metaAPIProperties.put("x-maturity-desc", "PLEASE NOTE that this class contains preview products that are subject to change. Use them with caution. If you currently do not have developer preview access, please contact help@twilio.com.");
    }

}
