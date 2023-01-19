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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RubyApiResourceBuilder extends FluentApiResourceBuilder {

    List<CodegenParameter> readParams;
    List<Object> componentContextClasses = new ArrayList<>();
    final OpenAPI openApi;

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
        this.updateListPath();
        return apiResourceBuilder;
    }

    private void addCreateSeparator(ApiResourceBuilder apiResourceBuilder) {
        for (CodegenOperation operation : apiResourceBuilder.codegenOperationList) {
            if ((boolean) operation.vendorExtensions.getOrDefault("x-is-create-operation", false)) {
                for (CodegenParameter param : operation.allParams) {
                    param.vendorExtensions.put("separator", ",");
                }
                if (!operation.allParams.isEmpty())
                    operation.allParams.get(operation.allParams.size() - 1).vendorExtensions.put("separator", "");
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

    private void updateListPath() {
        if (listPath != null) listPath = listPath.replace("${", "#{@solution[:").replace("}", "]}");
    }
}
