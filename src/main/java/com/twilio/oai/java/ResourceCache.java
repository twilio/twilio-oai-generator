package com.twilio.oai.java;

import com.twilio.oai.java.nestedmodels.MustacheEnum;
import com.twilio.oai.java.nestedmodels.MustacheModel;;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ResourceCache {
    @Getter
    @Setter
    public static String resourceName;
    
    @Getter
    @Setter
    public static Set<CodegenProperty> response = new TreeSet<>((p1, p2) -> p1.baseName.compareTo(p2.baseName));

    @Getter
    public static List<CodegenModel> allModelsByDefaultGenerator = new ArrayList<>();
    @Getter
    public static Set<MustacheModel> modelClassesForMustache = new HashSet<>();
    
    @Getter
    public static Set<MustacheEnum> enumsClassesForMustache = new HashSet<>();

    public static void setAllModelsByDefaultGenerator(List<CodegenModel> allModelsByDefaultGenerator) {
        ResourceCache.allModelsByDefaultGenerator = new ArrayList<>(allModelsByDefaultGenerator);
    }

    public static void addToModelClasses(MustacheModel mustacheModel) {
        ResourceCache.modelClassesForMustache.add(mustacheModel);
    }

    public static void addToEnumClasses(MustacheEnum mustacheEnum) {
        ResourceCache.enumsClassesForMustache.add(mustacheEnum);
    }

    public static void clear() {
        resourceName = null;
        modelClassesForMustache.clear();
        enumsClassesForMustache.clear();
        response.clear();
    }
    public static void clearAllModelsByDefaultGenerator() {
        allModelsByDefaultGenerator.clear();
    }
}
