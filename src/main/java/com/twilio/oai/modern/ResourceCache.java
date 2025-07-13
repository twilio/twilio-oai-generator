package com.twilio.oai.modern;

import com.twilio.oai.java.nestedmodels.MustacheEnum;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResourceCache {
    @Getter
    @Setter
    public static String resourceName;
    
    @Getter
    public static List<CodegenModel> allModelsByDefaultGenerator = new ArrayList<>();
    @Getter
    public static Set<CodegenModel> modelClassesForMustache = new HashSet<>();
    @Getter
    public static Set<MustacheEnum> enumsClassesForMustache = new HashSet<>();

    public static void setAllModelsByDefaultGenerator(List<CodegenModel> allModelsByDefaultGenerator) {
        ResourceCache.allModelsByDefaultGenerator = new ArrayList<>(allModelsByDefaultGenerator);
    }

    public static void addToModelClasses(CodegenModel codegenModel) {
        ResourceCache.modelClassesForMustache.add(codegenModel);
    }

    public static void addToEnumClasses(MustacheEnum mustacheEnum) {
        ResourceCache.enumsClassesForMustache.add(mustacheEnum);
    }

    public static void clear() {
        resourceName = null;
        allModelsByDefaultGenerator.clear();
        modelClassesForMustache.clear();
        enumsClassesForMustache.clear();
    }
}
