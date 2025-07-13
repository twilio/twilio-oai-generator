package com.twilio.oai.java.processor.request;

import com.twilio.oai.common.ApplicationConstants;

import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.X_IS_MODEL;

import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import org.commonmark.node.Code;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

public class RecursiveTraversor {

    public void traverse(CodegenProperty property) {
        if (processEnumProperty(property)) return;
        processModelProperty(property);
    }

    private boolean processEnumProperty(CodegenProperty property) {
        // Add logic to process the property
        System.out.println("Processing property: " + property.baseName);
        return false;
    }
    
    private void processModelProperty(CodegenProperty property) {
        CodegenModel codegenModel = Utility.getModelFromOpenApiType(property.openApiType);
        if (codegenModel == null) return;
        type(property);
        variableName(property);
        dataType(property);

        // Model has been identified
        if (codegenModel.vars != null && !codegenModel.vars.isEmpty()) {
            for (CodegenProperty modelProperty : codegenModel.vars) {
                traverse(modelProperty);
            }
        }
    }

    private void type(final CodegenProperty property) {
        property.vendorExtensions.put(X_IS_MODEL, true);
    }

    private void variableName(CodegenProperty property) {
        property.vendorExtensions.put(X_DATATYPE, StringUtils.toCamelCase(property.baseName));
    }
    // basetype
    private void dataType(CodegenProperty property) {
        // Need to identify whether the property is defined using ref or directly defined property
        System.out.println(property.dataType);
    }
}
