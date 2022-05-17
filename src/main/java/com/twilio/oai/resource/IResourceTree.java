package com.twilio.oai.resource;

import io.swagger.v3.oas.models.PathItem;

import java.util.List;

public interface IResourceTree {
     List<String> ancestors(String resourceName);
     Resource findResource(String name, boolean isTag);
     String addResource(String name, PathItem pathItem);
}
