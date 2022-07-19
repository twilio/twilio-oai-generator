package com.twilio.oai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resource.IResourceTree;
import com.twilio.oai.resource.ResourceMap;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import org.openapitools.codegen.utils.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class DirectoryStructureService {

    public static final String ACCOUNT_SID_FORMAT = "^AC[0-9a-fA-F]{32}$";
    private EnumConstants.Generator generator;
    private static final String PATH_SEPARATOR_PLACEHOLDER = "1234567890";
    private final Inflector inflector = new Inflector();
    private IResourceTree resourceTree;

    public DirectoryStructureService(EnumConstants.Generator generator) {
        this.generator = generator;
    }

    public void configure(OpenAPI openAPI, Map<String, Object> additionalProperties) {
        resourceTree = new ResourceMap(inflector, PATH_SEPARATOR_PLACEHOLDER);

        extendOpenAPI(openAPI).getPaths().forEach((name, path) -> {
            resourceTree.addResource(name, path);
        });
        openAPI.getPaths().forEach((name, path) -> {
            updateAccountSidParam(name, path);
            path.readOperations().forEach(operation -> {
                // Group operations together by tag. This gives us one file/post-process per resource.
                String tag = String.join(PATH_SEPARATOR_PLACEHOLDER, resourceTree.ancestors(name, operation));
                operation.addTagsItem(tag);
            });
            Matcher m = ApplicationConstants.serverUrlPattern.matcher(path.getServers().get(0).getUrl());
            if (m.find()) {
                additionalProperties.put("domainName", StringUtils.camelize(m.group(1)));
            }
        });
    }

    // If account sid is present in path param, it is stored in x-is-account-sid.
    private void updateAccountSidParam(final String path, final PathItem pathMap) {
        pathMap.readOperations().stream().map(io.swagger.v3.oas.models.Operation::getParameters)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(param -> param.getIn().equals("path") && ( ACCOUNT_SID_FORMAT.equals(param.getSchema().getPattern())))
                .forEach(param -> {
                    param.required(false);
                    param.addExtension("x-is-account-sid", true);
                });
    }

    private OpenAPI extendOpenAPI(OpenAPI openAPI) {
        Paths newPaths = new Paths();
        openAPI.getPaths().forEach((name, path) -> {
            if (hasOperatorWithClassName(path)) {
                for(Map.Entry<String, PathItem> newPathItem : extractMultiPathItemFromOperatorWithClassName(name, path).entrySet()) {
                    newPaths.addPathItem(newPathItem.getKey(), newPathItem.getValue());
                }
            }
            else {
                newPaths.addPathItem(name, path);
            }
        });
        openAPI.paths(newPaths);
        return openAPI;
    }

    private Map<String, PathItem> extractMultiPathItemFromOperatorWithClassName(String name, PathItem path)  {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, PathItem> pathItemMap = new HashMap<>();
        try {
            PathItem pathItemOperatorClassVendExt = objectMapper
                    .readValue(objectMapper.writeValueAsString(path), PathItem.class);
            PathItem pathItemClassVendExt = objectMapper
                    .readValue(objectMapper.writeValueAsString(path), PathItem.class);
            resetOperationMethodCalls(pathItemOperatorClassVendExt, pathItemClassVendExt);
            for (Map.Entry<PathItem.HttpMethod, io.swagger.v3.oas.models.Operation> entryMapOperation: path.readOperationsMap().entrySet()) {
                if (isClassName(entryMapOperation.getValue())) {
                    String[] urls = name.split("/");
                    String className = Arrays.stream(((Map<String, String>)entryMapOperation.getValue()
                                    .getExtensions().get("x-twilio")).get("className").split("_")).
                            map(StringUtils::camelize).collect(Collectors.joining());
                    urls[urls.length-1] = className + ".json";
                    String urlPath = String.join("/", urls);
                    entryMapOperation.getValue().getExtensions().put("parentUrl", name);
                    pathItemOperatorClassVendExt.operation(entryMapOperation.getKey(), entryMapOperation.getValue());
                    pathItemMap.put(urlPath, pathItemOperatorClassVendExt);
                } else {
                    pathItemClassVendExt.operation(entryMapOperation.getKey(), entryMapOperation.getValue());
                    pathItemMap.put(name, pathItemClassVendExt);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return pathItemMap;
    }

    private void resetOperationMethodCalls(PathItem pathItemOperatorClassVendExt, PathItem pathItemClassVendExt) {
        pathItemClassVendExt.put(null);
        pathItemClassVendExt.get(null);
        pathItemClassVendExt.post(null);
        pathItemClassVendExt.delete(null);
        pathItemClassVendExt.patch(null);
        pathItemOperatorClassVendExt.put(null);
        pathItemOperatorClassVendExt.get(null);
        pathItemOperatorClassVendExt.post(null);
        pathItemOperatorClassVendExt.delete(null);
        pathItemOperatorClassVendExt.patch(null);
    }

    private boolean hasOperatorWithClassName(PathItem path) {
        for (io.swagger.v3.oas.models.Operation operation: path.readOperations()) {
            if (isClassName(operation)) {
                return true;
            }
        }
        return false;
    }

    private boolean isClassName(io.swagger.v3.oas.models.Operation operation) {
        return (operation.getExtensions() != null && operation.getExtensions().containsKey("x-twilio")) &&
                ((Map<String, String>) operation.getExtensions().get("x-twilio")).containsKey("className");
    }
}
