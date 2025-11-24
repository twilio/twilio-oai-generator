package com.twilio.oai.java.feature.datamodels;

import com.twilio.oai.java.cache.ResourceCacheContext;
import com.twilio.oai.java.nestedmodels.MustacheOneOfIface;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;

import java.util.Map;
import java.util.Set;

/*
 This class has 2 responsibilities
 1. Identifying oneOf interfaces and ignore it so that concrete classes shouldn't generate.
 2. Identify the concrete classes from interfaces and create relationship between concrete class and interface.
*/
public class OneOf implements DataModel {
    Map<String, CodegenModel> allModelsMap = ResourceCacheContext.get().getAllModelsByDefaultGenerator().stream()
            .collect(java.util.stream.Collectors.toMap(m -> m.name, m -> m));
    
    @Override
    public void apply(final CodegenOperation codegenOperation) {
        
    }

    private void resolveOneOf(CodegenOperation codegenOperation) {
        
    }

    @Override
    public boolean shouldApply(final CodegenModel codegenModel) {
        if (codegenModel == null || codegenModel.oneOf == null || codegenModel.oneOf.isEmpty()) {
            return false;
        }
        return true;
    }
    
    public void addInterfaceMustache(CodegenModel codegenModel) {
        MustacheOneOfIface oneOfIface = new MustacheOneOfIface(codegenModel.classname);
        ResourceCacheContext.get().addToOneOfInterfaces(oneOfIface);
        
    }

    public void addImplMustache() {

    }
}
