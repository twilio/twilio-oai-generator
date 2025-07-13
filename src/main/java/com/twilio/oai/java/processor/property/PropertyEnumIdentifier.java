package com.twilio.oai.java.processor.property;

import com.twilio.oai.java.processor.parameter.enumidentifcation.EnumIdentificationStrategy;
import com.twilio.oai.java.processor.parameter.enumidentifcation.ParameterListEnumStrategy;
import com.twilio.oai.java.processor.parameter.enumidentifcation.ParameterListRefEnumStrategy;
import com.twilio.oai.java.processor.parameter.enumidentifcation.ParameterRefEnumStrategy;
import com.twilio.oai.java.processor.parameter.enumidentifcation.ParameterSingleEnumStrategy;
import com.twilio.oai.java.processor.parameter.enumidentifcation.UrlEncodedListEnumStrategy;
import com.twilio.oai.java.processor.parameter.enumidentifcation.UrlEncodedListRefEnumStrategy;
import com.twilio.oai.java.processor.parameter.enumidentifcation.UrlEncodedRefEnumStrategy;
import com.twilio.oai.java.processor.parameter.enumidentifcation.UrlEncodedSingleEnumStrategy;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;

public class PropertyEnumIdentifier {
    private final List<PropertyIdentificationStrategy> strategies;
    public PropertyEnumIdentifier() {
        strategies = List.of(
                new JsonSingleEnumStrategy(),
                new JsonRefEnumStrategy(),
                new JsonListEnumStrategy(),
                new JsonListRefEnumStrategy()
        );
    }

    public void identify(CodegenProperty codegenProperty) {
        if ("http-method".equals(codegenProperty.dataFormat)) {
            return;
        }
        for (PropertyIdentificationStrategy strategy : strategies) {
            if (strategy.identify(codegenProperty)) {
                System.out.println("Strategy matched: " + strategy.getType() + " Enum Basename: " + codegenProperty.baseName);
                break;
            }
        }
    }
}
