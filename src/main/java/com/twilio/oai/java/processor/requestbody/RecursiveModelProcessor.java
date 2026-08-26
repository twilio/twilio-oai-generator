package com.twilio.oai.java.processor.requestbody;

import com.twilio.oai.common.Utility;
import com.twilio.oai.java.cache.ResourceCacheContext;
import com.twilio.oai.java.format.Deserializer;
import com.twilio.oai.java.processor.enums.EnumProcessorFactory;
import com.twilio.oai.java.processor.model.ModelProcessorFactory;
import com.twilio.oai.java.processor.model.parameter.ParamModelProcessorManager;
import com.twilio.oai.resolver.common.CodegenModelOneOf;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecursiveModelProcessor {
    EnumProcessorFactory enumProcessorFactory = EnumProcessorFactory.getInstance();
    ModelProcessorFactory modelProcessorFactory = ModelProcessorFactory.getInstance();
    ParamModelProcessorManager paramModelProcessorManager = ParamModelProcessorManager.getInstance();

    public void processBody(CodegenOperation codegenOperation) {
        // codegenOperation.bodyParam.vars.get(3).ref: #/components/schemas/types
        
        codegenOperation.bodyParam.vars.forEach(property -> processModelRecursively(property));
        
    }

    public void process(CodegenProperty codegenProperty) {
        processModelRecursively(codegenProperty);
    }
    
    public void process(CodegenParameter codegenParameter) {
        CodegenModel codegenModel = ResourceCacheContext.get().getAllModelsByDefaultGenerator().stream()
                .filter(model -> model.classname.equalsIgnoreCase(codegenParameter.dataType))
                .findFirst()
                .orElse(null);
        if (codegenModel == null) return;
        if (null != codegenModel.oneOf && !codegenModel.oneOf.isEmpty()) {
            CodegenModelOneOf.getInstance().resolve(codegenModel);
        }
        
        paramModelProcessorManager.applyProcessor(codegenParameter, codegenModel);

        if (codegenModel.vars != null && !codegenModel.vars.isEmpty()) {
            for (CodegenProperty modelProperty : codegenModel.vars) {
                processModelRecursively(modelProperty);
            }
        }
    }

    // Only in case of oneOf or allOf
    public void processModelRecursively(CodegenParameter codegenParameter, CodegenModel codegenModel) {
        
        
        paramModelProcessorManager.applyProcessor(codegenParameter, codegenModel);

        if (codegenModel.vars != null && !codegenModel.vars.isEmpty()) {
            for (CodegenProperty modelProperty : codegenModel.vars) {
                processModelRecursively(modelProperty);
            }
        }
    }

    public void processResponse(final CodegenOperation codegenOperation) {
        // Make sure to filter pagination models.
        return ;
    }

    
    // DFS based recursive logic
    public void processModelRecursively(final CodegenProperty codegenProperty) {
        CodegenModel codegenModel = Utility.getModelFromOpenApiType(codegenProperty);
        /*
        This code block has access to all the codegenProperty for a nested model.
        Add your logic to process the property.
        ------------------  Start ------------------ 
        */
        
        if (isEnum(codegenProperty)) {
            // Logic 1: Enum Logic
            processEnum(codegenProperty);
            return;
        }
        if (codegenModel == null) {
            // For non ref models, CodegenModel will be present. Non model nor enum.
            // Logic 3: Normal variable logic
            Deserializer.addDeserializer(codegenProperty);
            Deserializer.addSerializer(codegenProperty);

            return;
        }

        if (null != codegenModel.oneOf && !codegenModel.oneOf.isEmpty()) {
            Map<String, List<VariantProperty>> variantPropsByName = collectVariantPropertiesByName(codegenModel);
            CodegenModelOneOf.getInstance().resolve(codegenModel);
            widenConflictingPropertiesToObject(codegenModel, variantPropsByName);
        }

        // Logic 2: nested model logic
        modelProcessorFactory.applyProcessor(codegenProperty, codegenModel);

        /*
        ------------------ End ------------------
         */



        // A Model has been identified, look for child models
        if (codegenModel.vars != null && !codegenModel.vars.isEmpty()) {
            for (CodegenProperty modelProperty : codegenModel.vars) {
                processModelRecursively(modelProperty);
            }
        }
    }

    private Map<String, List<VariantProperty>> collectVariantPropertiesByName(CodegenModel model) {
        Map<String, List<VariantProperty>> propsByName = new LinkedHashMap<>();
        List<CodegenModel> variantModels = getVariantModels(model);
        for (CodegenModel variant : variantModels) {
            if (variant.vars == null) continue;
            for (CodegenProperty prop : variant.vars) {
                propsByName.computeIfAbsent(prop.getName(), k -> new ArrayList<>())
                    .add(new VariantProperty(prop, variant.classname));
            }
        }
        return propsByName;
    }

    private static class VariantProperty {
        final CodegenProperty property;
        final String variantClassName;
        VariantProperty(CodegenProperty property, String variantClassName) {
            this.property = property;
            this.variantClassName = variantClassName;
        }
    }

    private void widenConflictingPropertiesToObject(CodegenModel model, Map<String, List<VariantProperty>> variantPropsByName) {
        for (CodegenProperty resolvedProp : model.vars) {
            List<VariantProperty> variants = variantPropsByName.get(resolvedProp.getName());
            if (variants == null || variants.size() <= 1) continue;

            Set<String> distinctComplexTypes = new HashSet<>();
            for (VariantProperty v : variants) {
                if (v.property.complexType != null) {
                    distinctComplexTypes.add(v.property.complexType);
                }
            }
            if (distinctComplexTypes.size() <= 1) continue;

            resolvedProp.dataType = "Map<String, Object>";
            resolvedProp.complexType = null;
        }
    }

    private List<CodegenModel> getVariantModels(CodegenModel model) {
        List<CodegenModel> variantModels = new ArrayList<>();
        if (model.interfaceModels != null && !model.interfaceModels.isEmpty()) {
            variantModels = model.interfaceModels;
        } else if (model.oneOf != null && !model.oneOf.isEmpty()) {
            List<CodegenModel> allModels = ResourceCacheContext.get().getAllModelsByDefaultGenerator();
            for (String oneOfName : model.oneOf) {
                Utility.getModelByClassname(allModels, oneOfName).ifPresent(variantModels::add);
            }
        }
        return variantModels;
    }

    private boolean isEnum(CodegenProperty codegenProperty) {
        return enumProcessorFactory.isEnum(codegenProperty);
    }

    private void processEnum(CodegenProperty codegenProperty) {
        enumProcessorFactory.applyProcessor(codegenProperty);
    }
}
