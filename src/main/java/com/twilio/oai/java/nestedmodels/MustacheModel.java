package com.twilio.oai.java.nestedmodels;

import com.twilio.oai.common.Utility;
import org.checkerframework.checker.units.qual.A;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Models can be created out of json request body or json response body.
public class MustacheModel {
    String className;
    
    // Used in either builder or setter
    List<CodegenProperty> allProperties;
    
    // Used in constructor
    List<CodegenProperty> mandatoryProperties;

    List<CodegenProperty> optionalProperties;

    public MustacheModel(CodegenProperty codegenProperty, CodegenModel codegenModel) {
        this.className = codegenModel.classname;
        
        this.allProperties = new ArrayList<>(codegenModel.vars);
        
        this.mandatoryProperties = codegenModel.vars.stream()
                .filter(codegenProperty1 -> codegenProperty1.required)
                .collect(Collectors.toList());
        
        this.optionalProperties = codegenModel.vars.stream()
                .filter(codegenProperty1 -> !codegenProperty1.required)
                .collect(Collectors.toList());
    }

    public MustacheModel(CodegenParameter codegenParameter, CodegenModel codegenModel) {
        this.className = codegenModel.classname;

        this.allProperties = new ArrayList<>(codegenModel.vars);

        this.mandatoryProperties = codegenModel.vars.stream()
                .filter(codegenProperty1 -> codegenProperty1.required)
                .collect(Collectors.toList());

        this.optionalProperties = codegenModel.vars.stream()
                .filter(codegenProperty1 -> !codegenProperty1.required)
                .collect(Collectors.toList());
    }

    public MustacheModel(String className, List<CodegenProperty> mandatoryProperties, List<CodegenProperty> allProperties) {
        this.className = className;
        this.mandatoryProperties = new ArrayList<>(mandatoryProperties);
        this.allProperties = new ArrayList<>(allProperties);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MustacheModel that = (MustacheModel) obj;
        return className != null && className.equals(that.className);
    }

    @Override
    public int hashCode() {
        return className != null ? className.hashCode() : 0;
    }
}
