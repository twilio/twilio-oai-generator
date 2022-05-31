package com.twilio.oai;

import com.google.common.collect.Streams;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class PathUtils {

    public static Optional<String> getInstancePath(final String targetPath, final Collection<String> allPaths) {
        return allPaths
            .stream()
            .map(PathUtils::removeExtension)
            .filter(p -> removePathParamIds(p)
                .matches(escapeRegex(removeExtension(removePathParamIds(targetPath))) + "/\\{[^/]+"))
            .findFirst();
    }

    public static String getLastPathPart(final String path) {
        final String[] pathParts = path.split("/");
        return pathParts[pathParts.length - 1];
    }

    public static String cleanPathAndRemoveFirstElement(final String path) {
        return removeExtension(path).replaceFirst("/[^/]+/", "").replaceAll("/\\{[^}]+}", ""); // Drop the version
    }

    public static String removeBraces(final String path) {
        return path.replaceAll("\\{|}", "");
    }

    public static String cleanPath(final String path) {
        return path.replaceAll("/\\{[^}]+}", "");
    }
    public static String removeExtension(final String path) {
        return path.replaceAll("\\.[^/]+$", "");
    }

    public static String removePathParamIds(final String path) {
        return path.replaceAll("\\{[^}]+", "{");
    }

    public static String escapeRegex(final String regex) {
        return regex.replace("{", "\\{");
    }

    public static String fetchLastElement(final String path, final String delimiter) {
        return Streams.findLast(Arrays.stream(path.split(delimiter))).get();
    }
}
