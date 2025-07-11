package com.twilio.oai.modern;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.codegen.CodegenModel;

import java.util.ArrayList;
import java.util.List;

public class ResourceCache {
    @Getter
    @Setter
    public static String resourceName;
    
    @Getter
    public static List<CodegenModel> allModels = new ArrayList<>();

    public static void setAllModels(List<CodegenModel> allModels) {
        ResourceCache.allModels = new ArrayList<>(allModels);
    }
}
