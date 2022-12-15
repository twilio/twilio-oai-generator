package com.twilio.oai;

import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.CaseResolver;
import com.twilio.oai.resource.IResourceTree;
import com.twilio.oai.resource.Resource;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;

import static com.twilio.oai.common.ApplicationConstants.ACCOUNT_SID_FORMAT;
import static com.twilio.oai.common.ApplicationConstants.ENUM_VARS;
import static com.twilio.oai.common.ApplicationConstants.LIST_INSTANCE;
import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;
import static com.twilio.oai.common.ApplicationConstants.PATH_TYPE_EXTENSION_NAME;

@RequiredArgsConstructor
public class DirectoryStructureService {
    public static final String VERSION_RESOURCES = "versionResources";
    public static final String ALL_VERSION_RESOURCES = VERSION_RESOURCES + "All";
    @Getter
    private final Map<String, Object> additionalProperties;
    @Getter
    private final IResourceTree resourceTree;
    private final CaseResolver caseResolver;

    @Getter
    private boolean isVersionLess = false;
    private final Map<String, String> productMap = new HashMap<>();

    private final List<CodegenModel> allModels = new ArrayList<>();

    @Data
    @Builder
    public static class DependentResource {
        private String version;
        private String type;
        private String className;
        private String importName;
        private String mountName;
        private String filename;
        private String param;
        private boolean instanceDependent;
    }

    @Data
    @Builder
    public static class ContextResource {
        private String version;
        private List<String> params;
        private String filename;
        private String mountName;
        private String parent;
    }

    public void configure(final OpenAPI openAPI) {
        final Map<String, DependentResource> versionResources = getVersionResourcesMap();

        isVersionLess = additionalProperties.get("apiVersion").equals("");

        openAPI.getPaths().forEach(resourceTree::addResource);
        openAPI.getPaths().forEach((name, path) -> {
            final Optional<String> pathType = PathUtils.getTwilioExtension(path, "pathType");
            updateAccountSidParam(path);
            path.readOperations().forEach(operation -> {
                // Group operations together by tag. This gives us one file/post-process per resource.
                final String tag = String.join(PATH_SEPARATOR_PLACEHOLDER, resourceTree.ancestors(name, operation));

                if (isVersionLess) {
                    productMap.put(tag, PathUtils.getFirstPathPart(name));
                }

                operation.setTags(null);
                operation.addTagsItem(tag);

                if (!tag.contains(PATH_SEPARATOR_PLACEHOLDER)) {
                    final DependentResource dependent = generateDependent(name, operation);
                    versionResources.put(dependent.getFilename(), dependent);
                }

                pathType.ifPresent(type -> Optional
                    .ofNullable(operation.getExtensions())
                    .ifPresentOrElse(ext -> ext.putIfAbsent(PATH_TYPE_EXTENSION_NAME, type),
                                     () -> operation.addExtension(PATH_TYPE_EXTENSION_NAME, type)));
            });
        });
    }

    @SuppressWarnings("unchecked")
    public Map<String, DependentResource> getVersionResourcesMap() {
        return (Map<String, DependentResource>) additionalProperties.computeIfAbsent(ALL_VERSION_RESOURCES,
                                                                                     k -> new TreeMap<>());
    }

    public void configureResourceFamily(OpenAPI openAPI) {
        openAPI.getPaths().forEach(resourceTree::addResource);
        resourceTree.getResources().forEach(resource -> resource.updateFamily(resourceTree));
    }

    // If account sid is present in path param, it is stored in x-is-account-sid.
    private void updateAccountSidParam(final PathItem pathMap) {
        pathMap
            .readOperations()
            .stream()
            .map(Operation::getParameters)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .filter(param -> param.getIn().equals("path") &&
                (ACCOUNT_SID_FORMAT.equals(param.getSchema().getPattern())))
            .forEach(param -> {
                param.required(false);
                param.addExtension("x-is-account-sid", true);
            });
    }

    public DependentResource generateDependent(final String path, final Operation operation) {
        final Resource.Aliases resourceAliases = getResourceAliases(path, operation);
        return new DependentResource.DependentResourceBuilder()
            .version(PathUtils.getFirstPathPart(path))
            .type(resourceAliases.getClassName() + LIST_INSTANCE)
            .className(resourceAliases.getClassName() + LIST_INSTANCE)
            .importName(resourceAliases.getClassName() + LIST_INSTANCE)
            .mountName(caseResolver.pathOperation(resourceAliases.getMountName()))
            .filename(caseResolver.filenameOperation(resourceAliases.getClassName()))
            .build();
    }

    public void addContextdependents(final List<Object> resourceList, final String path, final Operation operation) {
        final Resource.Aliases resourceAliases = getResourceAliases(path, operation);
        String parent = String.join("\\", resourceTree.ancestors(path, operation));
        List<String> params = fetchNonParentPathParams(operation);

        final ContextResource dependent = new ContextResource.ContextResourceBuilder()
                .version(PathUtils.getFirstPathPart(path))
                .params(params)
                .mountName(caseResolver.pathOperation(resourceAliases.getMountName()))
                .filename(caseResolver.filenameOperation(resourceAliases.getClassName()))
                .parent(parent)
                .build();
        if (!resourceList.contains(dependent))
            resourceList.add(dependent);
    }

    private List<String> fetchNonParentPathParams(Operation operation){
        if(operation != null){
            List<Parameter> pathParams = Optional.ofNullable(operation.getParameters())
                    .stream().flatMap(Collection::stream)
                    .filter(param -> Objects.nonNull(param.getIn())).filter(PathUtils::isPathParam)
                    .collect(Collectors.toList());
            List<String> params = pathParams.stream().filter(parameter -> Objects.isNull(parameter.getExtensions()))
                    .map(Parameter::getName).collect(Collectors.toList());
            params.addAll(pathParams.stream().filter(parameter -> Objects.nonNull(parameter.getExtensions()))
                    .filter(parameter -> !PathUtils.isParentParam(parameter))
                    .map(Parameter::getName).collect(Collectors.toList()));
            return params;
        }
        return null;
    }

    private Resource.Aliases getResourceAliases(final String path, final Operation operation) {
        return resourceTree
            .findResource(path)
            .map(resource -> operation == null ? resource.getResourceAliases() : resource.getResourceAliases(operation))
            .orElseThrow();
    }

    public Optional<String> getApiVersionClass() {
        return Optional
            .of(additionalProperties.get("apiVersionClass"))
            .map(String.class::cast)
            .map(version -> version.isEmpty() ? null : version);
    }

    public void postProcessAllModels(final Map<String, ModelsMap> models, final Map<String, String> modelFormatMap) {
        Utility.addModelsToLocalModelList(models, allModels);
        Utility.setComplexDataMapping(allModels, modelFormatMap);
        allModels.forEach(model -> model.setClassname(model.getClassname().replace("Enum", "")));
    }

    public List<CodegenOperation> processOperations(final OperationsMap results) {
        final OperationMap ops = results.getOperations();
        final List<CodegenOperation> operations = ops.getOperation();
        final CodegenOperation firstOperation = operations.stream().findFirst().orElseThrow();
        final String version = PathUtils.getFirstPathPart(firstOperation.path);

        additionalProperties.put("version", version);
        additionalProperties.put("apiVersionPath", getRelativeRoot(firstOperation.baseName));
        additionalProperties.put("apiFilename",
                                 caseResolver.pathOperation(getResourceAliases(firstOperation.path,
                                                                               null).getClassName()));

        if (isVersionLess) {
            additionalProperties.put("apiVersion", caseResolver.productOperation(version));
            additionalProperties.put("apiVersionClass", StringHelper.camelize(version));
        }

        final List<DependentResource> versionResources = getVersionResourcesMap()
            .values()
            .stream()
            .filter(resource -> resource.getVersion().equals(version))
            .collect(Collectors.toList());
        additionalProperties.put(VERSION_RESOURCES, versionResources);

        if (additionalProperties.get("apiVersion").equals("v2010")) {
            final String name = "Account";
            versionResources.add(new DependentResource.DependentResourceBuilder()
                                     .type(name + "Context")
                                     .className(name + LIST_INSTANCE)
                                     .importName(name + "Context")
                                     .mountName(caseResolver.pathOperation(name))
                                     .filename(caseResolver.filenameOperation(name))
                                     .param(caseResolver.pathOperation(name + "Sid"))
                                     .build());
        }

        allModels.forEach(item -> item.getVendorExtensions().remove(ENUM_VARS));

        return operations;
    }

    protected String getRelativeRoot(final String tag) {
        return Arrays
            .stream(tag.split(PATH_SEPARATOR_PLACEHOLDER))
            .map(part -> "..")
            .collect(Collectors.joining(File.separator));
    }

    /**
     * Replaces the path separator placeholder with the actual separator and applies case converter to each path part.
     */
    public String toApiFilename(final String name) {
        final List<String> pathParts = new ArrayList<>();

        final List<String> nameParts = new ArrayList<>(Arrays.asList(name.split(PATH_SEPARATOR_PLACEHOLDER)));
        final String filename = nameParts.remove(nameParts.size() - 1);

        if (productMap.containsKey(name)) {
            pathParts.add(caseResolver.productOperation(productMap.get(name)));
        }

        pathParts.addAll(nameParts.stream().map(caseResolver::pathOperation).collect(Collectors.toList()));
        pathParts.add(caseResolver.filenameOperation(filename));

        return String.join(File.separator, pathParts);
    }

    public String getRecordKey(final List<CodegenOperation> opList) {
        return Utility.getRecordKey(allModels, opList);
    }

    public Optional<CodegenModel> getModelCoPath(final String className,
                                                 final CodegenOperation codegenOperation,
                                                 final String recordKey) {
        return Utility.getModel(allModels, className, recordKey, codegenOperation);
    }
}
