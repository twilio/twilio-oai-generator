package com.twilio.oai.resource;

import com.twilio.oai.Inflector;
import com.twilio.oai.PathUtils;
import com.twilio.oai.StringHelper;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.twilio.oai.common.ApplicationConstants.IGNORE_EXTENSION_NAME;
import static com.twilio.oai.common.ApplicationConstants.IS_PARENT_PARAM_EXTENSION_NAME;
import static com.twilio.oai.common.ApplicationConstants.PATH_TYPE_EXTENSION_NAME;
import static com.twilio.oai.common.EnumConstants.PathType;

@Getter
@RequiredArgsConstructor
public class Resource {
    // Some characters that are not allowed in API paths.
    public static final String SEPARATOR = ":";
    public static final Pattern PARAMETERIZED_PATTERN = Pattern.compile(".*?\\{([^}]+)}");

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

    public void updateFamily(final IResourceTree resourceTree) {
        if (!pathItem.readOperations().isEmpty()) {
            final Operation operation = pathItem.readOperations().get(0);

            // If we have a parent, ensure it has at least one operation and mark any of our path params as parent
            // params if they match the parent's path.
            getParentResource(resourceTree).ifPresent(parentResource -> {
                addIgnoreOperationIfNone(parentResource.pathItem, operation);
                updateParentParams(parentResource.name, name, pathItem.readOperations());
            });
        }
    }

    private void updateParentParams(final String parentPath,
                                    final String dependentPath,
                                    final List<Operation> dependentOperations) {
        final Matcher parentMatcher = PARAMETERIZED_PATTERN.matcher(parentPath);
        final Matcher dependentMatcher = PARAMETERIZED_PATTERN.matcher(dependentPath);
        final Set<String> parentParams = new HashSet<>();

        while (dependentMatcher.find() && parentMatcher.find()) {
            parentParams.add(dependentMatcher.group(1));
        }

        dependentOperations
            .stream()
            .map(Operation::getParameters)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .forEach(param -> param.addExtension(IS_PARENT_PARAM_EXTENSION_NAME,
                                                 parentParams.contains(param.getName())));
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
