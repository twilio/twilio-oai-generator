package com.twilio.oai.resource;

import com.twilio.oai.Inflector;
import com.twilio.oai.PathUtils;
import com.twilio.oai.TwilioJavaGenerator;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Resource {
    // Some characters that are not allowed in API paths.
    public static final String SEPARATOR = ":";
    public static final String TWILIO_EXTENSION_NAME = "x-twilio";
    public static final String IGNORE_EXTENSION_NAME = "x-ignore";

    private final String listTag;
    private final String name;
    private final PathItem pathItem;
    private final Inflector inflector;

    @Data
    @RequiredArgsConstructor
    public static class ClassName {
        private final String name;
        private final String mountName;

        public ClassName(final String name) {
            this(name, name);
        }
    }

    public Optional<Resource> getParentResource(final IResourceTree resourceTree) {
        return PathUtils
            .getTwilioExtension(pathItem, "parent")
            .flatMap(parent -> resourceTree.findResource(parent, false));
    }

    public void updateFamily(final OpenAPI openAPI, final IResourceTree resourceTree) {
        if (!pathItem.readOperations().isEmpty()) {
            final Operation operation = pathItem.readOperations().get(0);

            // If we have a parent, ensure it has at least one operation.
            getParentResource(resourceTree).ifPresent(parentResource -> addIgnoreOperationIfNone(parentResource.pathItem,
                                                                                                 operation));

            // If we are in instance operation, ensure we have a list operation with at least one operation.
            if (PathUtils.isInstancePath(name)) {
                resourceTree
                    .getResourceByTag(listTag)
                    .ifPresentOrElse(listResource -> addIgnoreOperationIfNone(listResource.pathItem, operation), () -> {
                        final String missingName = PathUtils.removeTrailingPathParam(name);
                        final PathItem missingPath = createMissingPathItem(operation);

                        openAPI.path(missingName, missingPath);
                    });
            }
        }
    }

    /**
     * Add a new operation to the given PathItem if it has no operations. Certain paths exists which have no operations
     * and are only placeholders. We need these paths to exist as resources during post-processing.
     */
    private void addIgnoreOperationIfNone(final PathItem pathItem, final Operation operation) {
        if (pathItem.readOperations().isEmpty()) {
            pathItem.setGet(new Operation());
            pathItem.getGet().addExtension(IGNORE_EXTENSION_NAME, true);

            // Copy the parameters from the given operation. These will be used to fill out the path params in case
            // the path of this resource is parameterized.
            pathItem.getGet().setParameters(operation.getParameters());
        }
    }

    private PathItem createMissingPathItem(final Operation operation) {
        final PathItem missingPath = new PathItem();
        missingPath.addExtension(TWILIO_EXTENSION_NAME, pathItem.getExtensions().get(TWILIO_EXTENSION_NAME));
        addIgnoreOperationIfNone(missingPath, operation);
        return missingPath;
    }

    public ClassName getClassName(final Operation operation) {
        final Optional<String> className = getClassNameFromExtensions(operation.getExtensions());

        return className.map(ClassName::new).orElseGet(this::getClassName);
    }

    public ClassName getClassName() {
        final Optional<String> className = getClassNameFromExtensions(pathItem.getExtensions());

        return className.map(ClassName::new).orElseGet(() -> {
            final String mountName = PathUtils.fetchLastElement(listTag, SEPARATOR);
            return new ClassName(inflector.singular(mountName), mountName);
        });
    }

    private Optional<String> getClassNameFromExtensions(final Map<String, Object> extensions) {
        return Optional
            .ofNullable(extensions)
            .map(ext -> ext.get(TWILIO_EXTENSION_NAME))
            .map(Map.class::cast)
            .map(xTwilio -> xTwilio.get("className"))
            .map(String.class::cast)
            .map(this::transformClassName);
    }

    private String transformClassName(final String className) {
        return Arrays.stream(className.split("_")).map(TwilioJavaGenerator::capitalize).collect(Collectors.joining());
    }
}
