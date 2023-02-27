package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.PathUtils;
import com.twilio.oai.resource.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;

public class PhpDomainBuilder {
    private final List<Object> contextResourcesList = new ArrayList<>();

    public static void setVersionTemplate(final OpenAPI openAPI,
                                          final DirectoryStructureService directoryStructureService) {
        setVersionTemplate(openAPI, directoryStructureService, null);
    }

    public static void setVersionTemplate(final OpenAPI openAPI,
                                          final DirectoryStructureService directoryStructureService,
                                          final String version) {
        final PhpDomainBuilder builder = new PhpDomainBuilder();
        builder.filterContextResources(openAPI, directoryStructureService);
        builder.setContextResources(directoryStructureService, version);
    }

    private void setContextResources(final DirectoryStructureService directoryStructureService, final String version) {
        directoryStructureService
            .getAdditionalProperties()
            .put("versionDependents",
                 contextResourcesList
                     .stream()
                     .map(DirectoryStructureService.ContextResource.class::cast)
                     .filter(contextResource -> version == null || contextResource.getVersion().equals(version))
                     .collect(Collectors.toList()));
    }

    private void filterContextResources(final OpenAPI openAPI,
                                        final DirectoryStructureService directoryStructureService) {
        Object domain = directoryStructureService.getAdditionalProperties().get("domainName");
        if (domain.equals("Api")) {
            getApiDependents(directoryStructureService);
        }

        List<Resource> dependents = new ArrayList<>();
        Map<String, PathItem> pathMap = openAPI.getPaths();

        for (String pathKey : pathMap.keySet()) {
            String pathkey = pathKey;
            if (domain.equals("Api")) {
                pathkey = pathKey.split(".json")[0];
            }
            if (pathkey.endsWith("}")) {
                PathItem path = pathMap.get(pathKey);
                Optional<String> parentKey = PathUtils.getTwilioExtension(path, "parent");
                if (parentKey.isEmpty()) {
                    dependents.add(new Resource(null, pathkey, path, null));
                } else {
                    String parentkey = parentKey.get();
                    if (domain.equals("Api")) {
                        parentkey = "/2010-04-01" + parentkey;
                    }
                    if (pathMap.containsKey(parentkey)) {
                        PathItem pathParent = pathMap.get(parentkey);
                        Optional<String> parentKey2 = PathUtils.getTwilioExtension(pathParent, "parent");
                        if (parentKey2.isEmpty()) {
                            dependents.add(new Resource(null, pathkey, path, null));
                        }
                    }
                }
            }
        }

        addResources(dependents, directoryStructureService, contextResourcesList);
    }

    private void getApiDependents(final DirectoryStructureService directoryStructureService) {
        String pathkey = "/2010-04-01/Accounts/{Sid}.json";
        List<Resource> dependents = directoryStructureService
            .getResourceTree()
            .dependents(pathkey)
            .stream()
            .filter(dep -> !dep.getName().endsWith("}.json"))
            .collect(Collectors.toList());

        final List<Object> dependentList = new ArrayList<>();
        addResources(dependents, directoryStructureService, dependentList);
        directoryStructureService.getAdditionalProperties().put("isApiDomain", "true");
        directoryStructureService.getAdditionalProperties().put("apiDependents", dependentList);
    }

    private void addResources(final List<Resource> dependents,
                              final DirectoryStructureService directoryStructureService,
                              final List<Object> dependentList) {
        dependents.forEach(dependent -> dependent
            .getPathItem()
            .readOperations()
            .forEach(operation -> directoryStructureService.addContextdependents(dependentList,
                                                                                 dependent.getName(),
                                                                                 operation)));
    }
}
