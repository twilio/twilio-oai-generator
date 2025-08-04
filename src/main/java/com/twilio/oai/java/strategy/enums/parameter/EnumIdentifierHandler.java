package com.twilio.oai.java.strategy.enums.parameter;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.strategy.enums.property.InlineListPropStrategy;
import com.twilio.oai.java.strategy.enums.property.RefListPropStrategy;
import com.twilio.oai.java.strategy.enums.property.RefPropStrategy;
import com.twilio.oai.java.strategy.enums.property.InlinePropStrategy;
import com.twilio.oai.java.strategy.enums.property.PropertyEnumProcessingStrategy;
import com.twilio.oai.modern.ResourceCache;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;

import static com.twilio.oai.common.ApplicationConstants.X_VARIABLE_NAME;
import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;
import static com.twilio.oai.common.ApplicationConstants.X_DATATYPE;
import static com.twilio.oai.common.ApplicationConstants.DOT;

// It does not do any processing, It will just return if a passed parameter or property is an Enum
public class EnumIdentifierHandler {
    private final List<ParameterEnumProcessingStrategy> parameterStrategies;

    private final List<PropertyEnumProcessingStrategy> propertyStrategies;
    
    public EnumIdentifierHandler() {
        parameterStrategies = List.of(
                new InlineParamStrategy(),
                new RefParamStrategy(),
                new InlineListParamStrategy(),
                new RefListParamStrategy(),
                new FormParamInlineStrategy(),
                new FormParamRefStrategy(),
                new FormParamListStrategy(),
                new FormParamListRefStrategy()
        );

        propertyStrategies = List.of(
                new InlinePropStrategy(),
                new RefPropStrategy(),
                new InlineListPropStrategy(),
                new RefListPropStrategy()
        );
    }
    
    public void identify(CodegenParameter codegenParameter) {
        if ("http-method".equals(codegenParameter.dataFormat)) {
            return;
        }
        for (ParameterEnumProcessingStrategy strategy : parameterStrategies) {
            if (strategy.isStrategyApplicable(codegenParameter)) {
                System.out.println("Strategy matched: " + strategy.getType() + " Enum Basename: " + codegenParameter.baseName);
                break;
            }
        }
    }
    public boolean identify(CodegenProperty codegenProperty) {
        if ("http-method".equals(codegenProperty.dataFormat)) {
            return false;
        }
        for (PropertyEnumProcessingStrategy strategy : propertyStrategies) {
            if (strategy.isStrategyApplicable(codegenProperty)) {
                System.out.println("Strategy matched: " + strategy.getType() + " Enum Basename: " + codegenProperty.baseName);
                return true;
            }
        }
        return false;
    }

    public void identify(CodegenModel codegenModel) {

    }

    /*
        .isEnum = true
        .isEnumRef = true
        .schema.isEnum = true
        .ref = null
        .isContainer = false
        .items = null
        
        codegenParameter.schema._enum = enum array values
     */
    private boolean isParameterSingle(CodegenParameter codegenParameter) {
       if (codegenParameter.getSchema()._enum != null && codegenParameter.isEnum) {
           System.out.println("Identified as single parameter enum: " + codegenParameter.baseName);
           codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.PARAMETER_INLINE);
           
           // Resolve
           codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
           codegenParameter.vendorExtensions.put(X_DATATYPE, 
                   ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(codegenParameter.baseName));
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
        
        
        
        .schema._enum = null
        .schema.items = null
        .schema.ref = it gives ref to the enum
     */
    private boolean isParameterRef(CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema().getRef() != null && codegenParameter.isEnumRef) {
            // Identify
            System.out.println("Identified as single parameter enum ref: " + codegenParameter.baseName);
            codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.PARAMETER_REF);

            /* Resolve
                X_NAME_NAME: variable name
                
            */
            codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
            String enumRefResolved = Utility.getEnumNameFromRef(codegenParameter.getSchema().getRef());
            codegenParameter.vendorExtensions.put(X_DATATYPE,
                    ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(enumRefResolved));
            return true;
        }
        return false;
    }

    /*
        .isEnum = true
        .isEnumRef = false
        .schema.isEnum = true
        .ref = null
        .isContainer = true
        .items = not null
        .schema.ref = null
        .schema.items.ref = it gives ref to the item
        
        .schema._enum = null
        .schema.items._enum = enum array values
     */
    private boolean isParameterArray(CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema()._enum == null &&
                codegenParameter.getSchema().items != null && codegenParameter.getSchema().items._enum != null) {
            // Identify
            System.out.println("Identified as array parameter enum: " + codegenParameter.baseName);
            codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.PARAMETER_LIST_INLINE);


            // Resolve
            codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));

            String enumClassName = StringUtils.toPascalCase(codegenParameter.baseName);
            String enumNonContainerDatatype = ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(enumClassName);
            String resolvedDataType = Utility.replaceDatatypeInContainer(codegenParameter.dataType, enumNonContainerDatatype);
            codegenParameter.vendorExtensions.put(X_DATATYPE, resolvedDataType);
            
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
        
        
        .schema._enum = null
        .schema.items._enum = null
        .schema.items.ref = it gives ref to the item
        
     */
    private boolean isParameterArrayRef(CodegenParameter codegenParameter) {
        if (codegenParameter.getSchema().items != null && codegenParameter.getSchema().items.getRef() != null 
                && codegenParameter.getSchema().items.isEnumRef) {
            System.out.println("Identified as array parameter enum ref: " + codegenParameter.baseName);
            codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.PARAMETER_LIST_REF);

            // Resolve
            codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
            // #/components/schemas/singleReusable or #/components/schemas/content_enum_single_reusable
            // enumRefResolved = singleReusable
            String enumRefResolved = Utility.getEnumNameFromRef(codegenParameter.getSchema().items.getRef());
            // enumNonContainerDatatype = Content.SingleReusable
            String enumNonContainerDatatype = ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(enumRefResolved);
            // resolvedDataType = List<Content.SingleReusable>
            String resolvedDataType = Utility.replaceDatatypeInContainer(codegenParameter.dataType, enumNonContainerDatatype);
            codegenParameter.vendorExtensions.put(X_DATATYPE, resolvedDataType);

            return true;
        }
        return false;
    }
    
    /*
    isEnum = true
    isEnumRef = true
     */
    private boolean isUrlEncodedBodySingle(CodegenParameter codegenParameter) {
        if (codegenParameter._enum != null && codegenParameter.isEnum && !codegenParameter.isContainer) {
            System.out.println("Identified as single url encoded body enum: " + codegenParameter.baseName);
            codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.FORM_PARAM_INLINE);

            // Resolve
            codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
            codegenParameter.vendorExtensions.put(X_DATATYPE,
                    ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(codegenParameter.baseName));
            return true;
        }
        return false;
    }

    private boolean isUrlEncodedBodyRef(CodegenParameter codegenParameter) {
        if (!codegenParameter.isEnum && codegenParameter.isEnumRef) {
            codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.FORM_PARAM_REF);
            codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
            String enumDatatypeResolved = Utility.getEnumNameFromDefaultDatatype(codegenParameter.dataType);
            codegenParameter.vendorExtensions.put(X_DATATYPE, ResourceCache.getResourceName() + DOT + enumDatatypeResolved);
            return true;
        }
        return false;
    }
    

    private boolean isUrlEncodedBodyArray(CodegenParameter codegenParameter) {
        if (codegenParameter.items != null && codegenParameter.items._enum != null && codegenParameter.isEnum) {
            System.out.println("Identified as single url encoded body enum: " + codegenParameter.baseName);
            codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.FORM_PARAM_INLINE);

            // Resolve
            codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));

            // codegenParameter.dataType = List<String>
            // enumExistingDatatype = String
            String enumExistingDatatype = Utility.extractDatatypeFromContainer(codegenParameter.dataType);
            String enumClassName = StringUtils.toPascalCase(codegenParameter.baseName);
            String enumNonContainerDatatype = ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(enumClassName);
            String resolvedDataType = Utility.replaceDatatypeInContainer(codegenParameter.dataType, enumNonContainerDatatype);
            codegenParameter.vendorExtensions.put(X_DATATYPE, resolvedDataType);
            return true;
        }
        return false;
    }

    private boolean isUrlEncodedBodyArrayRef(CodegenParameter codegenParameter) {
        if (codegenParameter.items != null && codegenParameter.items.isEnumRef && !codegenParameter.items.isEnum) {
            codegenParameter.vendorExtensions.put(X_ENUM_TYPE, EnumConstants.OpenApiEnumType.FORM_PARAM_LIST_REF);
            
            codegenParameter.vendorExtensions.put(X_VARIABLE_NAME, StringUtils.toCamelCase(codegenParameter.baseName));
            // codegenParameter.dataType = List<AccountEnumStatus>
            // enumExistingDatatype = AccountEnumStatus
            String enumExistingDatatype = Utility.extractDatatypeFromContainer(codegenParameter.dataType);
            // enumClassName = Status
            String enumClassName = Utility.getEnumNameFromDatatype(enumExistingDatatype);
            // enumNonContainerDatatype = Account.Status
            String enumNonContainerDatatype = ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(enumClassName);
            // resolvedDataType = List<Account.Status>
            String resolvedDataType = Utility.replaceDatatypeInContainer(codegenParameter.dataType, enumNonContainerDatatype);
            codegenParameter.vendorExtensions.put(X_DATATYPE, resolvedDataType);
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
