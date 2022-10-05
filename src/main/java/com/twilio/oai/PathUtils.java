package com.twilio.oai;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Streams;
import io.swagger.v3.oas.models.PathItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.codegen.CodegenOperation;

import static com.twilio.oai.resource.Resource.TWILIO_EXTENSION_NAME;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathUtils {

    public static String getLastPathPart(final String path) {
        final String[] pathParts = path.split("/");
        return pathParts[pathParts.length - 1];
    }

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

    public static String removePathParamIds(final String path) {
        return removeExtension(path).replaceAll("\\{[^}]+", "{");
    }

    public static String removeTrailingPathParam(final String path) {
        return path.replaceFirst("/\\{[^}]+}[^/]*$", "");
    }

    public static String escapeRegex(final String regex) {
        return regex.replace("{", "\\{");
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
}
