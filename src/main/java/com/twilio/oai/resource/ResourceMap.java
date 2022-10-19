package com.twilio.oai.resource;

import com.twilio.oai.Inflector;
import com.twilio.oai.PathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResourceMap implements IResourceTree {
    private final Map<String, Resource> urlResourceMap = new HashMap<>();
    private final Inflector inflector;

    @Override
    public Iterable<Resource> getResources() {
        return urlResourceMap.values();
    }

    @Override
    public List<String> ancestors(final String resourceName, final Operation operation) {
        final Resource resource = findResource(resourceName).orElseThrow();
        final List<String> ancestorList = new ArrayList<>();
        ancestorList.add(resource.getResourceAliases(operation).getClassName());

        Optional<Resource> parent = resource.getParentResource(this);
        while (parent.isPresent()) {
            final Resource parentResource = parent.get();
            ancestorList.add(0, parentResource.getResourceAliases().getClassName());
            parent = parentResource.getParentResource(this);
        }

        return ancestorList;
    }

    /**
     * Returns a list of dependents for the operation as those with a path that directly under the current path.
     */
    @Override
    public List<Resource> dependents(final String name) {
        // First get a list of all the descendants.
        final List<Resource> descendants = urlResourceMap
            .values()
            .stream()
            .filter(resource -> isDescendent(name, resource.getName()))
            .collect(Collectors.toList());

        // Then filter out any that are descendents of any descendants.
        return descendants
            .stream()
            .filter(descendent -> descendants
                .stream()
                .noneMatch(current -> isDescendent(current.getName(), descendent.getName())))
            .collect(Collectors.toList());
    }

    private boolean isDescendent(final String resource, final String descendent) {
        return PathUtils
            .removePathParamIds(descendent)
            .matches(PathUtils.escapeRegex(PathUtils.removePathParamIds(resource) + "/[^{]+"));
    }

    @Override
    public Optional<Resource> findResource(final String name) {
        return findResource(name, true);
    }

    @Override
    public Optional<Resource> findResource(String name, final boolean removeVersion) {
        if (removeVersion) {
            name = PathUtils.removeFirstPart(name);
        }

        return getResourceByTag(generateTag(name));
    }

    @Override
    public Optional<Resource> getResourceByTag(final String tag) {
        return Optional.ofNullable(urlResourceMap.get(tag));
    }

    @Override
    public String addResource(final String name, final PathItem pathItem) {
        final String withoutVersion = PathUtils.removeFirstPart(name);
        final String withoutTrailingParam = PathUtils.removeTrailingPathParam(withoutVersion);
        final String tag = generateTag(withoutVersion);
        urlResourceMap.put(tag, new Resource(generateTag(withoutTrailingParam), name, pathItem, inflector));
        return tag;
    }

    private String generateTag(String url) {
        return PathUtils
            .removeBraces(PathUtils.removeExtension(url))
            .replaceFirst("^/", "")
            .replace("/", Resource.SEPARATOR);
    }
}
