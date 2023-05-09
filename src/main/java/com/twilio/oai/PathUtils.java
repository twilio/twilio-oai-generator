package com.twilio.oai;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

import static com.twilio.oai.common.ApplicationConstants.IS_PARENT_PARAM_EXTENSION_NAME;
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

    public static boolean isPathPrefix(final String path, final String prefix) {
        return removeExtension(removePathParamIds(path)).startsWith(removeExtension(removePathParamIds(prefix)));
    }

    public static String removePathParamIds(final String path) {
        return path.replaceAll("\\{[^}]+}", "{}");
    }

    public static String fetchLastElement(final String path, final String delimiter) {
        return path.substring(path.lastIndexOf(delimiter) + 1);
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


    public static Optional<Object> getDependentsTwilioExtension(final Map<String, Object> extensions, final String extensionKey) {
        return (Optional<Object>) Optional
                .ofNullable(extensions)
                .map(ext -> ext.get(TWILIO_EXTENSION_NAME))
                .map(Map.class::cast)
                .map(xTwilio -> xTwilio.get(extensionKey));
    }

    public static boolean isInstanceOperation(final CodegenOperation operation) {
        if (operation.vendorExtensions.containsKey(PATH_TYPE_EXTENSION_NAME)) {
            return operation.vendorExtensions
                    .get(PATH_TYPE_EXTENSION_NAME)
                    .equals(PathType.INSTANCE.getValue());
        } else {
            String[] paths = operation.path.split(".json");
            String lastElement = paths[paths.length - 1];
            return lastElement.charAt(lastElement.length() - 1) == '}';
        }
    }

    public static boolean isParentParam(final CodegenParameter param) {
        return (boolean) param.vendorExtensions.getOrDefault(IS_PARENT_PARAM_EXTENSION_NAME, false);
    }

    public static boolean isParentParam(final Parameter parameter) {
        return (boolean) parameter.getExtensions().getOrDefault(IS_PARENT_PARAM_EXTENSION_NAME, false);
    }

    public static boolean isPathParam(final Parameter parameter) {
        return parameter.getIn().equals("path");
    }
}
