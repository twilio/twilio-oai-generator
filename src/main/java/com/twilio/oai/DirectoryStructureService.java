package com.twilio.oai;

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
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import lombok.RequiredArgsConstructor;
import org.openapitools.codegen.utils.StringUtils;

import static com.twilio.oai.common.ApplicationConstants.ACCOUNT_SID_FORMAT;
import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;

@RequiredArgsConstructor
public class DirectoryStructureService {
    private final IResourceTree resourceTree;
    private final CaseResolver caseResolver;
    private final Map<String, String> productMap = new HashMap<>();

    public void configure(OpenAPI openAPI, Map<String, Object> additionalProperties) {
        final Map<String, Object> versionResources = PathUtils.getStringMap(additionalProperties, "versionResources");

        openAPI.getPaths().forEach(resourceTree::addResource);
        openAPI.getPaths().forEach((name, path) -> {
            updateAccountSidParam(path);
            path.readOperations().forEach(operation -> {
                // Group operations together by tag. This gives us one file/post-process per resource.
                final String tag = String.join(PATH_SEPARATOR_PLACEHOLDER, resourceTree.ancestors(name, operation));

                if (isVersionLess(additionalProperties)) {
                    productMap.put(tag, PathUtils.getFirstPathPart(name));
                }

                operation.addTagsItem(tag);

                if (!tag.contains(PATH_SEPARATOR_PLACEHOLDER)) {
                    addDependent(versionResources, name);
                }
            });
        });

        PathUtils.flattenStringMap(additionalProperties, "versionResources");
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

    public void addDependent(final Map<String, Object> resourcesMap, final String path) {
        final Resource.ClassName className = getResourceClassName(path);
        final Map<String, Object> versionResource = PathUtils.getStringMap(resourcesMap, className.getName());
        versionResource.put("name", className.getName());
        versionResource.put("mountName", StringUtils.underscore(className.getMountName()));
        versionResource.put("filename", StringUtils.camelize(className.getName(), true));
    }

    public Resource.ClassName getResourceClassName(final String path) {
        return resourceTree.findResource(path).map(Resource::getClassName).orElseThrow();
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

    boolean isVersionLess(Map<String, Object> additionalProperties) {
        return additionalProperties.get("apiVersion").equals("");
    }
}
