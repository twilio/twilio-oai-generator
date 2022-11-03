package com.twilio.oai.resource;

import com.twilio.oai.Inflector;
import com.twilio.oai.PathUtils;
import com.twilio.oai.StringHelper;

import java.util.Map;
import java.util.Optional;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.twilio.oai.common.ApplicationConstants.IGNORE_EXTENSION_NAME;
import static com.twilio.oai.common.ApplicationConstants.PATH_TYPE_EXTENSION_NAME;
import static com.twilio.oai.common.ApplicationConstants.TWILIO_EXTENSION_NAME;
import static com.twilio.oai.common.EnumConstants.PathType;

@Getter
@RequiredArgsConstructor
public class Resource {
    // Some characters that are not allowed in API paths.
    public static final String SEPARATOR = ":";

    private final String listTag;
    private final String name;
    private final PathItem pathItem;
    private final Inflector inflector;

    @Data
    @RequiredArgsConstructor
    public static class Aliases {
        private final String className;
        private final String mountName;
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
            pathItem.getGet().addExtension(PATH_TYPE_EXTENSION_NAME, PathType.LIST.getValue());
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

    public Aliases getResourceAliases(final Operation operation) {
        final Optional<Aliases> className = getResourceNamesFromExtensions(operation.getExtensions());

        return className.orElseGet(this::getResourceAliases);
    }

    public Aliases getResourceAliases() {
        final Optional<Aliases> className = getResourceNamesFromExtensions(pathItem.getExtensions());

        return className.orElseGet(() -> {
            final String mountName = StringHelper.camelize(PathUtils.fetchLastElement(listTag, SEPARATOR));
            return new Aliases(inflector.singular(mountName), mountName);
        });
    }

    private Optional<Aliases> getResourceNamesFromExtensions(final Map<String, Object> extensions) {
        final Optional<String> classNameOpt = getTwilioExtension(extensions, "className");
        final Optional<String> mountNameOpt = getTwilioExtension(extensions, "mountName");

        if (classNameOpt.isPresent() || mountNameOpt.isPresent()) {
            final String mountName = mountNameOpt.orElse(PathUtils.fetchLastElement(listTag, SEPARATOR));
            final String className = classNameOpt.orElse(inflector.singular(mountName));

            return Optional.of(new Aliases(className, mountName));
        }

        return Optional.empty();
    }

    private Optional<String> getTwilioExtension(final Map<String, Object> extensions, final String extensionName) {
        return PathUtils.getTwilioExtension(extensions, extensionName).map(StringHelper::camelize);
    }
}
