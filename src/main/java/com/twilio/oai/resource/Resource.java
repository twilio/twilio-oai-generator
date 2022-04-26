package com.twilio.oai.resource;

import com.twilio.oai.Inflector;
import com.twilio.oai.PathUtils;
import com.twilio.oai.TwilioJavaGenerator;
import io.swagger.v3.oas.models.PathItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

public class Resource {
    @Getter @Setter(AccessLevel.PROTECTED) private String path;
    @Getter @Setter(AccessLevel.PROTECTED) private PathItem pathItem;
    @Getter @Setter (AccessLevel.PROTECTED) private String className;
    private Inflector inflector;
    public Resource(String name, PathItem pathItem, Inflector inflector) {
        this.path = name;
        this.pathItem = pathItem;
        this.inflector = inflector;
        this.className = getClassName(TwilioJavaGenerator.PATH_SEPARATOR_PLACEHOLDER);
    }

    public Resource getParentResource(IResourceTree resourceTree) {
        for(Map.Entry entrySet: pathItem.getExtensions().entrySet()) {
            if (entrySet.getKey().equals("x-twilio")) {
                if (((Map<?, ?>) entrySet.getValue()).containsKey("parent")) {
                    String parent =((Map<?, String>) entrySet.getValue()).get("parent");
                    return resourceTree.findResource(parent, false);
                }
            }
        }
        return null;
    }

    private String getClassName(String tagDelimiter) {
        for(Map.Entry entrySet: pathItem.getExtensions().entrySet()) {
            if (entrySet.getKey().equals("x-twilio")) {
                if (((Map<?, ?>) entrySet.getValue()).containsKey("className")) {
                    return fetchClassNameFromTwilioVendExt((Map<?, String>) entrySet.getValue());
                }
            }
        }
        return inflector.singular(PathUtils.fetchLastElement(path, tagDelimiter));
    }

    public static String fetchClassNameFromTwilioVendExt(Map<?, String> vendExt) {
        return Arrays.stream(vendExt.get("className").split("_"))
                .map(TwilioJavaGenerator::capitalize).collect(Collectors.joining());
    }

    public String resourceName() {
        return this.path;
    }
}
