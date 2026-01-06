package com.twilio.oai.java.cache;

import com.twilio.oai.java.nestedmodels.MustacheEnum;
import com.twilio.oai.java.nestedmodels.MustacheModel;
import com.twilio.oai.java.nestedmodels.MustacheOneOfIface;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.*;

public class ResourceCache2 {
    
    @Getter
    @Setter
    private String resourceName;

    @Getter
    @Setter
    private String recordKey;

    @Getter
    @Setter
    private Set<CodegenProperty> response = new TreeSet<>((p1, p2) -> p1.baseName.compareTo(p2.baseName));

    @Getter
    private ArrayList<CodegenModel> allModelsByDefaultGenerator = new ArrayList<>();
    @Getter
    private Set<MustacheModel> modelClassesForMustache = new HashSet<>();

    @Getter
    private Set<MustacheOneOfIface> oneOfInterfaces = new HashSet<>();

    @Getter
    private Set<MustacheEnum> enumsClassesForMustache = new HashSet<>();
    
    @Getter
    private Set<MustacheModel> responses = new HashSet<>();
    
    // Note: Key is stored in PascalCase using com.twilio.oai.common.StringUtils.toPascalCase(string_val)
    @Getter
    private Map<String, CodegenModel> allModelsMapByDefaultGenerator = new HashMap<>();
    
    @Getter
    @Setter
    private boolean isV1;

    public void setAllModelsByDefaultGenerator(ArrayList<CodegenModel> allModelsByDefaultGenerator) {
        this.allModelsByDefaultGenerator = new ArrayList<>(allModelsByDefaultGenerator);
    }

    public ArrayList<CodegenModel> getAllModelsByDefaultGenerator() {
        return this.allModelsByDefaultGenerator;
    }

    public Map<String, CodegenModel> getAllModelsMapByDefaultGenerator() {
        return this.allModelsMapByDefaultGenerator;
    }
    public void addToModelClasses(MustacheModel mustacheModel) {
        this.modelClassesForMustache.add(mustacheModel);
    }

    public void addToEnumClasses(MustacheEnum mustacheEnum) {
        this.enumsClassesForMustache.add(mustacheEnum);
    }

    public void addToOneOfInterfaces(MustacheOneOfIface oneOfIFace) {
        this.oneOfInterfaces.add(oneOfIFace);
    }

    public void addToResponse(MustacheModel mustacheModel) {
        this.responses.add(mustacheModel);
    }

    // Clear at group of operation level, at method: postProcessOperationsWithModels
    public void clear() {
        resourceName = "";
        modelClassesForMustache.clear();
        enumsClassesForMustache.clear();
        response.clear();
        responses.clear();
    }

    // No need to clear
    public void clearAllModelsByDefaultGenerator() {
        allModelsByDefaultGenerator.clear();
    }
}
