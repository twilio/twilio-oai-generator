package com.twilio.oai.modern;

import com.twilio.oai.java.processor.parameter.enumidentifcation.EnumIdentifierHandler;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import static com.twilio.oai.common.ApplicationConstants.X_ENUM_TYPE;

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
    EnumIdentifierHandler enumIdentifier = new EnumIdentifierHandler();

    public void resolve(final CodegenProperty codegenProperty) {
        enumIdentifier.identify(codegenProperty);
        
    }
    
    public void resolve(final CodegenParameter codegenParameter) {
        enumIdentifier.identify(codegenParameter);
        // Once enum has been identified, resolve enum
        // name, datatype must be resolved
        
        if (codegenParameter.vendorExtensions.get(X_ENUM_TYPE) == null) {
            return; // Not an enum or http-method format enum
        }
        
       
    }
}
