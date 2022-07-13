package com.twilio.oai.resource;

import java.util.List;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;

public interface IResourceTree {
    List<String> ancestors(String resourceName, Operation operation);

    Resource findResource(String name, boolean isTag);

    String addResource(String name, PathItem pathItem);
}
