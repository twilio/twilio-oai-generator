package com.twilio.oai;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import io.swagger.v3.oas.models.PathItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.codegen.CodegenOperation;

import static com.twilio.oai.common.ApplicationConstants.PATH_TYPE_EXTENSION_NAME;
import static com.twilio.oai.common.ApplicationConstants.TWILIO_EXTENSION_NAME;
import static com.twilio.oai.common.EnumConstants.PathType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathUtils {

    public static String getFirstPathPart(final String path) {
        return path.replaceAll("^/", "").split("/")[0];
    }

    public static String cleanPathAndRemoveFirstElement(final String path) {
        return cleanPath(removeExtension(removeFirstPart(path)));
    }

    public static String removeBraces(final String path) {
        return path.replaceAll("[{}]", "");
    }

    public static String cleanPath(final String path) {
        return path.replaceAll("/\\{[^}]+}", "");
    }

    public static String removeFirstPart(final String path) {
        return path.replaceFirst("/[^/]+", "");
    }

    public static String removeExtension(final String path) {
        return path.replaceAll("\\.[^/]+$", "");
    }

    public static String removeTrailingPathParam(final String path) {
        return path.replaceFirst("/\\{[^}]+}[^/]*$", "");
    }

    public static String fetchLastElement(final String path, final String delimiter) {
        return path.substring(path.lastIndexOf(delimiter)+1);
    }

    public static Optional<String> getTwilioExtension(final PathItem pathItem, final String extensionKey) {
        return getTwilioExtension(pathItem.getExtensions(), extensionKey);
    }

    public static Optional<String> getTwilioExtension(final Map<String, Object> extensions, final String extensionKey) {
        return Optional
            .ofNullable(extensions)
            .map(ext -> ext.get(TWILIO_EXTENSION_NAME))
            .map(Map.class::cast)
            .map(xTwilio -> xTwilio.get(extensionKey))
            .map(String.class::cast);
    }

    public static boolean isInstanceOperation(final CodegenOperation operation) {
        return operation.vendorExtensions.getOrDefault(PATH_TYPE_EXTENSION_NAME, "").equals(PathType.INSTANCE.getValue());
    }

    public static boolean isInstancePath(final String path) {
        return PathUtils.removeExtension(path).endsWith("}");
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getStringMap(final Map<String, Object> resource, final String key) {
        return (Map<String, Object>) resource.computeIfAbsent(key, k -> new TreeMap<>());
    }

    @SuppressWarnings("unchecked")
    public static void flattenStringMap(final Map<String, Object> resource, final String key) {
        resource.computeIfPresent(key, (k, dependents) -> ((Map<String, Object>) dependents).values());
    }
}
