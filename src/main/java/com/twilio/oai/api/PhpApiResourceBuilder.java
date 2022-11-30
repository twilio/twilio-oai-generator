package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.PathUtils;
import com.twilio.oai.resource.Resource;
import com.twilio.oai.template.IApiActionTemplate;
import com.twilio.oai.template.PhpApiActionTemplate;
import org.openapitools.codegen.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.*;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;

public class PhpApiResourceBuilder extends ApiResourceBuilder {
    private HashSet<String> pathSet = new HashSet<>();

    protected String apiListPath = "";
    protected String apiContextPath = "";
    protected TreeSet<CodegenParameter> requiredPathParamsList = new TreeSet<>((cp1, cp2) -> cp1.baseName.compareTo(cp2.baseName));
    protected TreeSet<CodegenParameter> requiredPathParamsContext = new TreeSet<>((cp1, cp2) -> cp1.baseName.compareTo(cp2.baseName));

    public PhpApiResourceBuilder(IApiActionTemplate template, List<CodegenOperation> codegenOperations, List<CodegenModel> allModels) {
        super(template, codegenOperations, allModels);
    }

    @Override
    public PhpApiResourceBuilder updateTemplate() {
        codegenOperationList.stream().forEach(codegenOperation -> {
            updateNamespaceSubPart(codegenOperation);
            template.clean();
            if (super.isInstanceOperation(codegenOperation)) {
                requiredPathParamsContext.addAll(codegenOperation.pathParams);
                template.add(PhpApiActionTemplate.TEMPLATE_TYPE_CONTEXT);
            } else {
                requiredPathParamsList.addAll(codegenOperation.pathParams);
                template.add(PhpApiActionTemplate.TEMPLATE_TYPE_PAGE);
                template.add(PhpApiActionTemplate.TEMPLATE_TYPE_OPTIONS);
            }
            template.add(PhpApiActionTemplate.TEMPLATE_TYPE_LIST);
            template.add(PhpApiActionTemplate.TEMPLATE_TYPE_INSTANCE);
        });
        return this;
    }
    @Override
    public PhpApiResources build() {
        return new PhpApiResources(this);
    }

    @Override
    public IApiResourceBuilder updateApiPath() {
        super.updateApiPath();
        List<CodegenOperation> oprList = codegenOperationList.stream().filter(op -> !isInstanceOperation(op)).collect(Collectors.toList());
        List<CodegenOperation> oprContext = codegenOperationList.stream().filter(op -> isInstanceOperation(op)).collect(Collectors.toList());

        if (!oprContext.isEmpty())
            apiContextPath = oprContext.get(0).path;
        if (!oprList.isEmpty())
            apiListPath = oprList.get(0).path;

        apiContextPath = formatPath(apiContextPath);
        apiListPath = formatPath(apiListPath);

        return this;
    }

    @Override
    public ApiResourceBuilder setImports(DirectoryStructureService directoryStructureService) {
        codegenOperationList.stream().forEach(operation -> {
            if (!pathSet.contains(operation.path)) {
                List<Resource> dependents = directoryStructureService.getResourceTree().dependents(operation.path);
                List<Resource> methodDependents = dependents.stream().filter(dep ->
                                PathUtils.getTwilioExtension(dep.getPathItem(), "pathType").get().equals("instance"))
                        .collect(Collectors.toList());
                dependents.removeIf(dep -> methodDependents.contains(dep));
                List<Resource> propertyDependents = dependents;
                List<Object> dependentProperties = new ArrayList<>();
                List<Object> dependentMethods = new ArrayList<>();
                updateDependents(directoryStructureService, methodDependents, dependentMethods);
                updateDependents(directoryStructureService, propertyDependents, dependentProperties);
                operation.vendorExtensions.put("importProperties", dependentProperties);
                operation.vendorExtensions.put("importMethods", dependentMethods);
                pathSet.add(operation.path);
            }
        });
        return this;
    }

    private static void updateDependents(DirectoryStructureService directoryStructureService, List<Resource> resourceList, List<Object> dependentList) {
        resourceList.forEach(dependent -> dependent.getPathItem().readOperations()
                .forEach(opr -> directoryStructureService.addContextdependents(dependentList,
                        dependent.getName(),
                        opr)));
    }

    private String formatPath(String path) {
        String regex = "/[v1-9]+[^/]+";
        Matcher matcher = Pattern.compile(regex).matcher(path);
        if (matcher.find()) {
            path = PathUtils.removeFirstPart(path);
        }
        path = lowerCasePathParam(path);
        return replaceBraces(path);
    }

    private String lowerCasePathParam(String path) {
        return Pattern.compile("\\{([\\w])").matcher(path)
                .replaceAll(match -> "{" + match.group(1).toLowerCase());
    }

    private String replaceBraces(String path) {
        path = path.replaceAll("[{]", "' . \\\\rawurlencode(\\$");
        return path.replaceAll("[}]", ") . '");
    }

    private void updateNamespaceSubPart(CodegenOperation codegenOperation) {
        List<String> filePathArray = new ArrayList<>(Arrays.asList(codegenOperation.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
        filePathArray = filePathArray.subList(0, filePathArray.size() - 1);
        if (!filePathArray.isEmpty()) {
            final String namespacePath = filePathArray
                    .stream()
                    .collect(Collectors.joining("\\"));
            namespaceSubPart = "\\" + namespacePath;
        }
    }
}
