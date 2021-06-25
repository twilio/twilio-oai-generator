package com.twilio.oai;

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

    public static String cleanPath(final String path) {
        return removeExtension(path).replaceFirst("/[^/]+/", "") // Drop the version
            .replaceAll("/\\{[^}]+}", ""); // Drop every path parameter.
    }

    public static String removeBraces(final String path) {
        return path.replaceAll("\\{|}", "");
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
}
