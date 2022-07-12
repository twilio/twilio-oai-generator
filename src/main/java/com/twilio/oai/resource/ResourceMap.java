package com.twilio.oai.resource;

import com.twilio.oai.Inflector;
import com.twilio.oai.PathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResourceMap implements IResourceTree {
    private final Map<String, Resource> urlResourceMap = new HashMap<>();
    private final Inflector inflector;
    private final String pathDelimiter;

    @Override
    public List<String> ancestors(final String resourceName, final Operation operation) {
        List<String> ancestorList = new ArrayList<>();
        Resource resource = findResource(removeVersion(resourceName).toUpperCase(Locale.ROOT), false);
        String className = resource.getClassName(operation);
        resource = resource.getParentResource(this);
        while (resource != null) {
            ancestorList.add(0, resource.getClassName()); //TODO: Need to integrate it with java and csharp .toLowerCase(Locale.ROOT));
            resource = resource.getParentResource(this);
        }
        ancestorList.add(className);
        return ancestorList;
    }

    @Override
    public Resource findResource(String name, boolean isTag) {
        if (isTag) {
            return urlResourceMap.get(name);
        }
        String tag = generateTag(name);
        return urlResourceMap.get(tag);
    }

    @Override
    public String addResource(String name, PathItem pathItem) {
        String withoutVersion = removeVersion(name);
        String tag = generateTag(withoutVersion);
        urlResourceMap.put(tag, new Resource(PathUtils.cleanPathAndRemoveFirstElement(name).replace("/", pathDelimiter), pathItem, inflector));
        return tag;
    }

    private String removeVersion(final String path) {
        return path.replaceFirst("/[^/]+", "");
    }

    private String generateTag(String url) {
        return url.substring(1).replaceAll("\\{|\\}|\\.", "").replace("/", pathDelimiter).toUpperCase();
    }
}
