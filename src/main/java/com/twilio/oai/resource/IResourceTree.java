package com.twilio.oai.resource;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;

public interface IResourceTree {
    Iterable<Resource> getResources();

    List<String> ancestors(String resourceName, Operation operation);

    Optional<Resource> findResource(String name);

    Optional<Resource> findResource(String name, boolean removeVersion);

    Optional<Resource> getResourceByTag(String tag);

    String addResource(String name, PathItem pathItem);
}
