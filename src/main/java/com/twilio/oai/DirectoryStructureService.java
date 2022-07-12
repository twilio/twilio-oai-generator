package com.twilio.oai;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resource.IResourceTree;
import com.twilio.oai.resource.ResourceMap;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import org.openapitools.codegen.utils.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;

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

        openAPI.getPaths().forEach((name, path) -> {
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

    public void generate() {

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
}
