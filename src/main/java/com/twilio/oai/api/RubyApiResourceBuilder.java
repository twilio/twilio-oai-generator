package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.PathUtils;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resource.Resource;
import com.twilio.oai.template.IApiActionTemplate;
import io.swagger.v3.oas.models.PathItem;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

import java.util.*;

public class RubyApiResourceBuilder extends FluentApiResourceBuilder {
    private static final List<Object> dependentList = new ArrayList<>();
    public RubyApiResourceBuilder(final IApiActionTemplate template, final List<CodegenOperation> codegenOperations, final List<CodegenModel> allModels, final DirectoryStructureService directoryStructureService) {
        super(template, codegenOperations, allModels, directoryStructureService);
    }

    @Override
    public ApiResourceBuilder updateOperations(Resolver<CodegenParameter> codegenParameterIResolver) {
        super.updateOperations(codegenParameterIResolver);
        return super.updateOperations(codegenParameterIResolver);
    }

    public static void configureAdditionalProps(Map<String, PathItem> pathMap, String domain, DirectoryStructureService directoryStructureService ){

        List<Resource> dependents = new ArrayList<>();
        if (domain.equals("api")){
            directoryStructureService.getAdditionalProperties().put("isApiDomain", "true");
        }
        for (String pathKey : pathMap.keySet()) {
            String pathKeyCache = domain.equals("api") ? pathKey.split(".json")[0] : pathKey;
            if (pathKeyCache.endsWith("}")) {
                PathItem currPath = pathMap.get(pathKey);
                Optional<String> parentKey = PathUtils.getTwilioExtension(currPath, "parent");
                if (!parentKey.isPresent()) {
                    dependents.add(new Resource(null, pathKeyCache, currPath, null));
                } else {
                    String currParentKey = domain.equals("api")? "/2010-04-01" + parentKey.get() : parentKey.get();

                    if (pathMap.containsKey(currParentKey)) {
                        PathItem pathParent = pathMap.get(currParentKey);
                        Optional<String> parentKey2 = PathUtils.getTwilioExtension(pathParent, "parent");
                        if (!parentKey2.isPresent()) {
                            dependents.add(new Resource(null, pathKeyCache, currPath, null));
                        }
                    }
                }
            }
        }
        dependents.forEach(dependent -> dependent
                .getPathItem()
                .readOperations()
                .forEach(operation -> directoryStructureService.addContextdependents(dependentList,
                        dependent.getName(),
                        operation)));
        directoryStructureService.getAdditionalProperties().put("versionDependents",
                dependentList
        );

    }


}
