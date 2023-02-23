package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.PathUtils;
import com.twilio.oai.resource.Resource;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PhpDomainBuilder {
    private static final ThreadLocal<List<Object>> contextResourcesList = ThreadLocal.withInitial(ArrayList::new);
    private static final ThreadLocal<List<Object>> dependentList = ThreadLocal.withInitial(ArrayList::new);

    public void setVersionTemplate(final OpenAPI openAPI, DirectoryStructureService directoryStructureService) {
        filter_contextResources(openAPI, directoryStructureService);
        if (directoryStructureService.isVersionLess()) return;
        setContextResources(directoryStructureService, null);
    }

    public static void setContextResources(DirectoryStructureService directoryStructureService, String version) {
        if (version == null) {
            directoryStructureService.getAdditionalProperties().put("versionDependents", contextResourcesList.get());
            return;
        }
        directoryStructureService.getAdditionalProperties().put("versionDependents",
                contextResourcesList.get().stream()
                        .map(DirectoryStructureService.ContextResource.class::cast)
                        .filter(contextResource -> contextResource.getVersion().equals(version))
                        .collect(Collectors.toList())
        );
    }

    private void filter_contextResources(final OpenAPI openAPI, DirectoryStructureService directoryStructureService) {
        Object domain = directoryStructureService.getAdditionalProperties().get("domainName");
        if (domain.equals("Api"))
            getApiDependents(directoryStructureService);

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
                if (!parentKey.isPresent()) {
                    dependents.add(new Resource(null, pathkey, path, null));
                } else {
                    String parentkey = parentKey.get();
                    if (domain.equals("Api")) {
                        parentkey = "/2010-04-01" + parentkey;
                    }
                    if (pathMap.containsKey(parentkey)) {
                        PathItem pathParent = pathMap.get(parentkey);
                        Optional<String> parentKey2 = PathUtils.getTwilioExtension(pathParent, "parent");
                        if (!parentKey2.isPresent()) {
                            dependents.add(new Resource(null, pathkey, path, null));
                        }
                    }
                }
            }
        }

        addResources(dependents, directoryStructureService, contextResourcesList.get());
    }

    private void getApiDependents(DirectoryStructureService directoryStructureService) {
        String pathkey = "/2010-04-01/Accounts/{Sid}.json";
        List<Resource> dependents = directoryStructureService.getResourceTree().dependents(pathkey)
                .stream().filter(dep -> !dep.getName().endsWith("}.json"))
                .collect(Collectors.toList());
        addResources(dependents, directoryStructureService, dependentList.get());
        directoryStructureService.getAdditionalProperties().put("isApiDomain", "true");
        directoryStructureService.getAdditionalProperties().put("apiDependents", dependentList.get());
    }

    private void addResources(List<Resource> dependents, DirectoryStructureService directoryStructureService, List<Object> dependentList) {
        dependents.forEach(dependent -> dependent
                .getPathItem()
                .readOperations()
                .forEach(operation -> directoryStructureService.addContextdependents(dependentList,
                        dependent.getName(),
                        operation)));
    }
}
