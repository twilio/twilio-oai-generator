package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.PathUtils;
import com.twilio.oai.resource.Resource;
import com.twilio.oai.template.IApiActionTemplate;
import com.twilio.oai.template.PhpApiActionTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import org.openapitools.codegen.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.*;

public class PhpApiResourceBuilder extends ApiResourceBuilder {
    private final List<Object> contextResourcesList = new ArrayList<>();
    private final List<Object> dependentList = new ArrayList<>();

    public PhpApiResourceBuilder(IApiActionTemplate template, List<CodegenOperation> codegenOperations, List<CodegenModel> allModels) {
        super(template, codegenOperations, allModels);
    }

    @Override
    public PhpApiResourceBuilder updateTemplate() {
        codegenOperationList.stream().forEach(codegenOperation -> {
            template.clean();
            if (super.isInstanceOperation(codegenOperation)) {
                template.add(PhpApiActionTemplate.TEMPLATE_TYPE_CONTEXT);
            } else {
                template.add(PhpApiActionTemplate.TEMPLATE_TYPE_PAGE);
                template.add(PhpApiActionTemplate.TEMPLATE_TYPE_OPTIONS);
            }
            template.add(PhpApiActionTemplate.TEMPLATE_TYPE_LIST);
            template.add(PhpApiActionTemplate.TEMPLATE_TYPE_INSTANCE);
        });
        return this;
    }

    @Override
    public IApiResourceBuilder updateApiPath() {
        super.updateApiPath();
        List<CodegenOperation> opr = codegenOperationList.stream().filter(op -> !isInstanceOperation(op)).collect(Collectors.toList());
        if (!opr.isEmpty()) {
            apiPath = opr.get(0).path;
        }
        String path = apiPath;
        String regex = "/[v1-9]+[^/]+";
        Matcher matcher = Pattern.compile(regex).matcher(apiPath);
        if (matcher.find()) {
            path = PathUtils.removeFirstPart(apiPath);
        }
        path = lowerCasePathParam(path);
        apiPath = replaceBraces(path);

        return this;
    }

    public void setVersionTemplate(final OpenAPI openAPI, String domain, DirectoryStructureService directoryStructureService) {
        List<Resource> context_list = filter_contextResources(openAPI, domain, directoryStructureService);
        setContextResources(context_list, directoryStructureService);
        template.add(PhpApiActionTemplate.TEMPLATE_TYPE_VERSION);
    }

    private List<Resource> filter_contextResources(final OpenAPI openAPI, String domain, DirectoryStructureService directoryStructureService) {
        if (domain.equals("api"))
            getApiDependents(directoryStructureService);

        List<Resource> context_list = new ArrayList<>();
        Map<String, PathItem> pathMap = openAPI.getPaths();

        for (String pathKey : pathMap.keySet()) {
            String pathkey = pathKey;
            if (domain.equals("api")) {
                pathkey = pathKey.split(".json")[0];
            }
            if (pathkey.endsWith("}")) {
                PathItem path = pathMap.get(pathKey);
                Optional<String> parentKey = PathUtils.getTwilioExtension(path, "parent");
                if (!parentKey.isPresent()) {
                    context_list.add(new Resource(null, pathkey, path, null));
                } else {
                    String parentkey = parentKey.get();
                    if (domain.equals("api")) {
                        parentkey = "/2010-04-01" + parentkey;
                    }
                    if (pathMap.containsKey(parentkey)) {
                        path = pathMap.get(parentkey);
                        Optional<String> parentKey2 = PathUtils.getTwilioExtension(path, "parent");
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
        List<Resource> dependents = directoryStructureService.resourceTree.dependents(pathkey)
                .stream().filter(dep -> !dep.getName().endsWith("}.json"))
                .collect(Collectors.toList());
        dependents.forEach(dependent -> dependent
                .getPathItem()
                .readOperations()
                .forEach(operation -> directoryStructureService.addContextdependents(dependentList,
                        dependent.getName(),
                        operation)));
        directoryStructureService.additionalProperties.put("isApiDomain", "true");
        directoryStructureService.additionalProperties.put("apiDependents", dependentList);
    }

    private void setContextResources(List<Resource> context_list, DirectoryStructureService directoryStructureService) {
        context_list.forEach(dependent -> dependent
                .getPathItem()
                .readOperations()
                .forEach(operation -> directoryStructureService.addContextdependents(contextResourcesList,
                        dependent.getName(),
                        operation)));
        directoryStructureService.additionalProperties.put("versionDependents", contextResourcesList);
    }

    private String lowerCasePathParam(String path) {
        return Pattern.compile("\\{([\\w])").matcher(path)
                .replaceAll(match -> "{"+match.group(1).toLowerCase());
    }

    private String replaceBraces(String path) {
        path = path.replaceAll("[{]", "' . \\\\rawurlencode(\\$");
        return path.replaceAll("[}]",") . '");
    }
}
