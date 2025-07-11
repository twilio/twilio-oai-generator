package com.twilio.oai.java.nestedmodels;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import static com.twilio.oai.common.ApplicationConstants.X_ENUM_NAME;
import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;
import static com.twilio.oai.common.ApplicationConstants.X_ENUM_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.DOT;
public class EnumIdentifier {
    
    public void identify(CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema() == null) return;
        if (isParameterSingle(codegenParameter)) {
            return;
        } else if (isParameterArray(codegenParameter)) {
            return;
        } else if (isParameterRef(codegenParameter)) {
            return;
        } else if (isParameterArrayRef(codegenParameter)) {
            return;
        }
        return;
    }
    public void identify(CodegenProperty codegenProperty) {
        System.out.println(codegenProperty);
    }

    public void identify(CodegenModel codegenModel) {

    }

    /*
        .isEnum = true
        .isEnumRef = true
        codegenParameter.schema.isEnum = true
        codegenParameter.ref = null
        codegenParameter.isContainer = false
        codegenParameter.items = null
        
        codegenParameter.schema._enum = enum array values
     */
    private boolean isParameterSingle(CodegenParameter codegenParameter) {
       if (codegenParameter.getSchema()._enum != null && codegenParameter.isEnum) {
           System.out.println("Identified as single parameter enum: " + codegenParameter.baseName);
           codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.PARAMETER_SINGLE);
           
           // Resolve
           codegenParameter.vendorExtensions.put(X_ENUM_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
           codegenParameter.vendorExtensions.put(X_ENUM_DATATYPE, 
                   ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(codegenParameter.dataType));
           return true;
       }
       return false;
    }

    /*
        isEnum = false
        isEnumRef = true
        schema.isEnum = false
        ref = null
        isContainer = false
        items =  null
        
        
        
        codegenParameter.schema._enum = null
        codegenParameter.schema.items = null
        codegenParameter.schema.ref = it gives ref to the enum
     */
    private boolean isParameterRef(CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema().getRef() != null && codegenParameter.isEnumRef) {
            // Identify
            System.out.println("Identified as single parameter enum ref: " + codegenParameter.baseName);
            codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.PARAMETER_SINGLE_REF);

            /* Resolve
                X_NAME_NAME: variable name
                
            */
            codegenParameter.vendorExtensions.put(X_ENUM_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
            return true;
        }
        return false;
    }

    /*
        codegenParameter.isEnum = true
        codegenParameter.isEnumRef = false
        codegenParameter.schema.isEnum = true
        codegenParameter.ref = null
        codegenParameter.isContainer = true
        codegenParameter.items = not null
        codegenParameter.schema.ref = null
        codegenParameter.schema.items.ref = it gives ref to the item
        
        codegenParameter.schema._enum = null
        codegenParameter.schema.items._enum = enum array values
     */
    private boolean isParameterArray(CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema()._enum == null &&
                codegenParameter.getSchema().items != null && codegenParameter.getSchema().items._enum != null) {
            // Identify
            System.out.println("Identified as array parameter enum: " + codegenParameter.baseName);
            codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.PARAMETER_ARRAY);


            // Resolve
            codegenParameter.vendorExtensions.put(X_ENUM_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
            return true;
        }
        return false;
    }
    
    /*
        isEnum = false
        isEnumRef = false
        schema.isEnum = false
        ref = null
        isContainer = true
        items = not null
        
        
        codegenParameter.schema._enum = null
        codegenParameter.schema.items._enum = null
        codegenParameter.schema.items.ref = it gives ref to the item
        
     */
    private boolean isParameterArrayRef(CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema().items != null && codegenParameter.getSchema().items.getRef() != null 
                && codegenParameter.getSchema().items.isEnumRef) {
            System.out.println("Identified as array parameter enum ref: " + codegenParameter.baseName);
            codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.PARAMETER_ARRAY_REF);

            // Resolve
            codegenParameter.vendorExtensions.put(X_ENUM_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
            
            
            
            //codegenParameter.vendorExtensions.put(X_ENUM_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
            return true;
        }
        return false;
    }
    /*

     */
    private boolean isPropertySingle(CodegenProperty codegenProperty) {
        return false;
    }
    /*

     */
    private boolean isReusableSingle(CodegenProperty codegenProperty) {
        return false;
    }
    /*

     */
    private void isReusableArray(CodegenProperty codegenProperty) {
        
    }
}
