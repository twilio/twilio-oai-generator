package com.twilio.oai.resource;

import com.twilio.oai.Inflector;
import com.twilio.oai.PathUtils;
import io.swagger.v3.oas.models.PathItem;

import java.util.*;

public class ResourceMap implements IResourceTree {
    private Map<String, Resource> urlResourceMap = new HashMap<>();
    private Inflector inflector;
    private String pathDelimiter;
    public ResourceMap(Inflector inflector, String pathDelimiter) {
        this.inflector = inflector;
        this.pathDelimiter = pathDelimiter;
    }

    @Override
    public List<String> ancestors(String resourceName) {
        List<String> ancestorList = new ArrayList<>();
        Resource resource = findResource(resourceName.toUpperCase(Locale.ROOT), false);
        String className = resource.getClassName();
        resource = resource.getParentResource(this);
        while (resource != null) {
            ancestorList.add(0, resource.getClassName().toLowerCase(Locale.ROOT));
            resource = resource.getParentResource(this);
        }
        ancestorList.add(className);
        return ancestorList;
    }

    @Override
    public Resource findResource(String name, boolean isTag) {
        if (isTag)  {
            return  urlResourceMap.get(name);
        }
        String tag = generateTag(name);
        return urlResourceMap.get(tag);
    }

    @Override
    public String addResource(String name, PathItem pathItem) {
        String withoutVersion = "/"+name.replaceFirst("/[^/]+/", "");
        String tag = generateTag(withoutVersion);
        urlResourceMap.put(tag, new Resource(PathUtils.cleanPathAndRemoveFirstElement(name).replace("/", pathDelimiter), pathItem, inflector));
        return tag;
    }

    private String generateTag(String url) {
        return url.substring(1).replaceAll("\\{|\\}|\\.", "").replace("/", pathDelimiter).toUpperCase();
    }
}
