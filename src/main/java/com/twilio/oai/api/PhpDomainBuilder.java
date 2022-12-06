package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.PathUtils;
import com.twilio.oai.resource.Resource;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;

import com.twilio.oai.template.IApiActionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PhpDomainBuilder {
    private final List<Object> contextResourcesList = new ArrayList<>();
    private final List<Object> dependentList = new ArrayList<>();
    private IApiActionTemplate template;

    public PhpDomainBuilder(IApiActionTemplate template) {
        this.template = template;
    }

    public void setVersionTemplate(final OpenAPI openAPI, DirectoryStructureService directoryStructureService) {
        List<Resource> context_list = filter_contextResources(openAPI, directoryStructureService);
        setContextResources(context_list, directoryStructureService);
        template.addSupportVersion();
    }

    private List<Resource> filter_contextResources(final OpenAPI openAPI, DirectoryStructureService directoryStructureService) {
        Object domain = directoryStructureService.getAdditionalProperties().get("domainName");
        if (domain.equals("Api"))
            getApiDependents(directoryStructureService);

        List<Resource> context_list = new ArrayList<>();
        Map<String, PathItem> pathMap = openAPI.getPaths();

        for (String pathKey : pathMap.keySet()) {
            String pathkey = pathKey;
            if (domain.equals("Api")) {
                pathkey = pathKey.split(".json")[0];
            }
            if (pathkey.endsWith("}")) {
                PathItem path = pathMap.get(pathKey);
                Optional<String> parentKey = PathUtils.getTwilioExtension(path, "parent");
                if (!parentKey.isPresent()) {
                    context_list.add(new Resource(null, pathkey, path, null));
                } else {
                    String parentkey = parentKey.get();
                    if (domain.equals("Api")) {
                        parentkey = "/2010-04-01" + parentkey;
                    }
                    if (pathMap.containsKey(parentkey)) {
                        PathItem pathParent = pathMap.get(parentkey);
                        Optional<String> parentKey2 = PathUtils.getTwilioExtension(pathParent, "parent");
                        if (!parentKey2.isPresent()) {
                            context_list.add(new Resource(null, pathkey, path, null));
                        }
                    }
                }
            }
        }
        return context_list;
    }

    private void getApiDependents(DirectoryStructureService directoryStructureService) {
        String pathkey = "/2010-04-01/Accounts/{Sid}.json";
        List<Resource> dependents = directoryStructureService.getResourceTree().dependents(pathkey)
                .stream().filter(dep -> !dep.getName().endsWith("}.json"))
                .collect(Collectors.toList());
        dependents.forEach(dependent -> dependent
                .getPathItem()
                .readOperations()
                .forEach(operation -> directoryStructureService.addContextdependents(dependentList,
                        dependent.getName(),
                        operation)));
        directoryStructureService.getAdditionalProperties().put("isApiDomain", "true");
        directoryStructureService.getAdditionalProperties().put("apiDependents", dependentList);
    }

    private void setContextResources(List<Resource> context_list, DirectoryStructureService directoryStructureService) {
        context_list.forEach(dependent -> dependent
                .getPathItem()
                .readOperations()
                .forEach(operation -> directoryStructureService.addContextdependents(contextResourcesList,
                        dependent.getName(),
                        operation)));
        directoryStructureService.getAdditionalProperties().put("versionDependents", contextResourcesList);
    }
}
