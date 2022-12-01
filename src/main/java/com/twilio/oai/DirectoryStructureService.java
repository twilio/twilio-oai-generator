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
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;

import static com.twilio.oai.common.ApplicationConstants.ACCOUNT_SID_FORMAT;
import static com.twilio.oai.common.ApplicationConstants.ARRAY;
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
    }

    @Data
    @Builder
    public static class ContextResource {
        private String paramName;
        private String filename;
        private String mountName;
        private String parent;
    }

    public void configure(final OpenAPI openAPI) {
        final Map<String, Object> versionResources = PathUtils.getStringMap(additionalProperties,
                                                                            ALL_VERSION_RESOURCES);

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
                    addDependent(versionResources, name, operation);
                }

                pathType.ifPresent(type -> Optional
                    .ofNullable(operation.getExtensions())
                    .ifPresentOrElse(ext -> ext.putIfAbsent(PATH_TYPE_EXTENSION_NAME, type),
                                     () -> operation.addExtension(PATH_TYPE_EXTENSION_NAME, type)));
            });
        });
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

    public void addDependent(final Map<String, Object> resourcesMap, final String path, final Operation operation) {
        final Resource.Aliases resourceAliases = getResourceAliases(path, operation);
        final DependentResource dependent = new DependentResource.DependentResourceBuilder()
            .version(PathUtils.getFirstPathPart(path))
            .type(resourceAliases.getClassName() + LIST_INSTANCE)
            .className(resourceAliases.getClassName() + LIST_INSTANCE)
            .importName(resourceAliases.getClassName() + LIST_INSTANCE)
            .mountName(caseResolver.pathOperation(resourceAliases.getMountName()))
            .filename(caseResolver.filenameOperation(resourceAliases.getClassName()))
            .build();
        resourcesMap.put(resourceAliases.getClassName(), dependent);
    }

    public void addContextdependents(final List<Object> resourceList, final String path, final Operation operation){
        final Resource.Aliases resourceAliases = getResourceAliases(path, operation);
        String parent = String.join("\\", resourceTree.ancestors(path, operation));
        String paramName = PathUtils.fetchlastPathParam(path,"{", "}");
        final ContextResource dependent = new ContextResource.ContextResourceBuilder()
                .paramName(paramName)
                .mountName(caseResolver.pathOperation(resourceAliases.getMountName()))
                .filename(caseResolver.filenameOperation(resourceAliases.getClassName()))
                .parent(parent)
                .build();
        if (!resourceList.contains(dependent))
            resourceList.add(dependent);
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

        final List<Object> versionResources = PathUtils
            .getStringMap(additionalProperties, ALL_VERSION_RESOURCES)
            .values()
            .stream()
            .map(DependentResource.class::cast)
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
        return opList
            .stream()
            .filter(co -> co.operationId.toLowerCase().startsWith("list"))
            .map(co -> getModelByClassname(co.returnType))
            .map(Optional::orElseThrow)
            .map(CodegenModel::getAllVars)
            .flatMap(Collection::stream)
            .filter(v -> v.openApiType.equals(ARRAY))
            .map(v -> v.baseName)
            .findFirst()
            .orElse("");
    }

    public Optional<CodegenModel> getModelCoPath(final String className,
                                                 final CodegenOperation codegenOperation,
                                                 final String recordKey) {
        if ((boolean) codegenOperation.vendorExtensions.getOrDefault("x-is-read-operation", false)) {
            return allModels
                .stream()
                .filter(model -> model.getClassname().equals(className))
                .map(CodegenModel::getVars)
                .flatMap(Collection::stream)
                .filter(prop -> prop.baseName.equals(recordKey))
                .map(CodegenProperty::getComplexType)
                .map(this::getModelByClassname)
                .findFirst()
                .orElseThrow();
        }

        return getModelByClassname(className);
    }

    public void addModel(final Map<String, CodegenModel> models, final String classname) {
        getModelByClassname(classname).ifPresent(model -> {
            if (models.putIfAbsent(model.getClassname(), model) == null) {
                model.getVars().forEach(property -> addModel(models, property.dataType));
            }
        });
    }

    public Optional<CodegenModel> getModelByClassname(final String classname) {
        return allModels.stream().filter(model -> model.classname.equals(classname)).findFirst();
    }
}
