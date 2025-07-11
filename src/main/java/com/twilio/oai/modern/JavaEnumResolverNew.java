package com.twilio.oai.modern;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.java.nestedmodels.EnumIdentifier;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import static com.twilio.oai.common.ApplicationConstants.X_ENUM_NAME;
import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;
import static com.twilio.oai.common.EnumConstants.OpenApiEnumType;

/*
This will resolve followings in an Enum:
List<CustomEnum> parameterName;
1. parameterName
2. datatype (i.e. List<CustomEnum>)
3. Enum Class and values:
   public enum CustomEnum {
       VALUE1,
       VALUE2,
       VALUE3
   }

parameterName and datatype are stored in CodegenParameter itself as a vendorExtension.
Open for serialization and deserialization.
 */
public class JavaEnumResolverNew {
    EnumIdentifier enumIdentifier = new EnumIdentifier();

    public void resolve(final CodegenProperty codegenProperty) {
        enumIdentifier.identify(codegenProperty);
        
    }
    
    public void resolve(final CodegenParameter codegenParameter) {
        enumIdentifier.identify(codegenParameter);
        // Once enum has been identified, resolve enum
        // name, datatype must be resolved
        
        if (codegenParameter.vendorExtensions.get(X_ENUM_TYPE) == null) {
            return; // Not an enum
        }
        
        if (OpenApiEnumType.PARAMETER_SINGLE.equals(codegenParameter.vendorExtensions.get(X_ENUM_TYPE))
                || OpenApiEnumType.PARAMETER_ARRAY.equals(codegenParameter.vendorExtensions.get(X_ENUM_TYPE))) {
            resolveDirect(codegenParameter);
        } else if (OpenApiEnumType.PARAMETER_SINGLE_REF.equals(codegenParameter.vendorExtensions.get(X_ENUM_TYPE))
                || OpenApiEnumType.PARAMETER_ARRAY_REF.equals(codegenParameter.vendorExtensions.get(X_ENUM_TYPE))) {
            resolveContainer(codegenParameter);
        }
    }

    /*
      Example of enum this method resolves
      INPUT: resource = Account
      Call:
        type: array
        items:
          type: string
          enum:
            - active
            - inactive
            - pending

        OUTPUT: x-name = Call, x-datatype = List<Account.Call>
     */
    private void resolveContainer(final CodegenParameter codegenParameter) {
//        System.out.println(codegenParameter);
//        // Directly defined enum
//        if (codegenParameter.isEnum) {
//            codegenParameter.vendorExtensions.put(X_NAME, StringUtils.toPascalCase(codegenParameter.baseName));
//            // class name would be same as basename
//            // IN: codegenParameter.dataType = List<String>
//            // OUT: List<Resource.BaseName>, BaseName must be converted to pascal case.
//            String resourceDatatype = Utility.replaceDatatypeInContainer(codegenParameter.dataType, 
//                    ResourceCache.getResourceName() + DOT + StringUtils.toPascalCase(codegenParameter.baseName));
//            codegenParameter.vendorExtensions.put(X_DATATYPE, resourceDatatype);
//        }
    }
    /*
      Example of enum this method resolves
      BodyEnum:
        type: string
        enum:
          - 'One'
          - 'Two'
     */
    private void resolveDirect(final CodegenParameter codegenParameter) {
        
        // If CodegenParameter is http-method enum, do not add to allModels
        // CodegenParameter does not have nested objects but can have $ref, thus CodegenPropertyResolver needs to be called.
//        
//        if (codegenParameter.isEnum) {
//            codegenParameter.vendorExtensions.put(X_NAME, StringUtils.toPascalCase(codegenParameter.baseName));
//            // datatype = ResourceName.datatypeWithEnum
//            
//            codegenParameter.vendorExtensions.put(X_DATATYPE, ResourceCache.getResourceName() + DOT + codegenParameter.datatypeWithEnum);
//        }
    }
    
    private void resolveDirectRef(CodegenParameter codegenParameter) {
        if (codegenParameter.isEnumRef) {
            codegenParameter.vendorExtensions.put(X_ENUM_NAME, StringUtils.toPascalCase(codegenParameter.baseName));
            
        }
    }
}
