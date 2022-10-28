package com.twilio.oai;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.google.common.collect.Streams;
import io.swagger.v3.oas.models.PathItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.codegen.CodegenOperation;

import static com.twilio.oai.resource.Resource.TWILIO_EXTENSION_NAME;

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
        return Streams.findLast(Arrays.stream(path.split(delimiter))).get();
    }

    public static Optional<String> getTwilioExtension(final PathItem pathItem, final String extensionKey) {
        return Optional
            .ofNullable(pathItem.getExtensions())
            .map(ext -> ext.get(TWILIO_EXTENSION_NAME))
            .map(Map.class::cast)
            .map(xTwilio -> xTwilio.get(extensionKey))
            .map(String.class::cast);
    }

    public static boolean isInstanceOperation(final CodegenOperation operation) {
        return isInstancePath(operation.path);
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
