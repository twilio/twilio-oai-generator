package com.twilio.oai.java;

import io.swagger.v3.oas.models.OpenAPI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class JavaUpdateDefaultMapping {
    HashSet<String> customFormatWithProperties = new HashSet<>();
    
    /*
        If a property in the OpenAPI specification is defined as
        date_generated:
          type: string
          format: date-time
        The expected datatype for the above is: OffsetDateTime
        The expected import is: java.time.OffsetDateTime
        
        Example:
        typeMapping.put("string+date-time", "OffsetDateTime");
        importMapping.put("OffsetDateTime", "java.time.OffsetDateTime");
        
        class AbstractJavaCodegen, method: public String getSchemaType(Schema p) at this place dataType is set
     */
    // TODO: Anytype is pending
    public void typeMapping(Map<String, String> typeMapping) {
        Map<String, String> predefinedTypeMappings = new HashMap<>();
        predefinedTypeMappings.put("string+phone-number", "PhoneNumber");
        predefinedTypeMappings.put("string+uri", "URI");
        predefinedTypeMappings.put("string+url", "URI");
        predefinedTypeMappings.put("string+currency", "Currency");
        predefinedTypeMappings.put("string+date-time", "LocalDate");
        predefinedTypeMappings.put("string+endpoint", "Endpoint");
        predefinedTypeMappings.put("string+http-method", "HttpMethod");
        predefinedTypeMappings.put("string+twiml", "com.twilio.type.Twiml");

        predefinedTypeMappings.put("object+ice-server", "IceServer");
        predefinedTypeMappings.put("object+subscribe-rule", "SubscribeRule");
        predefinedTypeMappings.put("object+recording-rule", "RecordingRule");
        predefinedTypeMappings.put("object+inbound-call-price", "InboundCallPrice");
        predefinedTypeMappings.put("object+inbound-sms-price", "InboundSmsPrice");
        predefinedTypeMappings.put("object+outbound-call-price", "OutboundCallPrice");
        predefinedTypeMappings.put("object+outbound-call-price-with-origin", "OutboundCallPriceWithOrigin");
        predefinedTypeMappings.put("object+outbound-prefix-price", "OutboundPrefixPrice");
        predefinedTypeMappings.put("object+outbound-sms-price", "OutboundSmsPrice");
        predefinedTypeMappings.put("object+phone-number-capabilities", "PhoneNumberCapabilities");
        predefinedTypeMappings.put("object+phone-number-price", "PhoneNumberPrice");
        predefinedTypeMappings.put("object+prefixed-collapsible-map", "Map<String, Object>");
        predefinedTypeMappings.put("object+prefixed-collapsible-map-AddOns", "Map<String, Object>");
        predefinedTypeMappings.put("object+outbound-prefix-price-with-origin", "OutboundPrefixPriceWithOrigin");
        predefinedTypeMappings.put("object+string-map", "Map<String, String>");
        predefinedTypeMappings.put("object+uri-map", "Map<String, String>");

        // Add all predefined mappings to the typeMapping
        
        
        typeMapping.putAll(predefinedTypeMappings);
    }
    
    public void importMapping(Map<String, String> importMapping) {
        // This is an example of how to add a custom import mapping.
        Map<String, String> predefinedImportMappings = new HashMap<>();
        predefinedImportMappings.put("PhoneNumber", "com.twilio.type.PhoneNumber");
        predefinedImportMappings.put("URI", "java.net.URI");
        predefinedImportMappings.put("Currency", "java.util.Currency");
        predefinedImportMappings.put("HttpMethod", "com.twilio.http.HttpMethod");
        predefinedImportMappings.put("Endpoint", "com.twilio.type.Endpoint");
        predefinedImportMappings.put("Endpoint", "com.twilio.type.Endpoint");

        predefinedImportMappings.put("IceServer", "com.twilio.type.IceServer");
        predefinedImportMappings.put("SubscribeRule", "com.twilio.type.SubscribeRule");
        predefinedImportMappings.put("RecordingRule", "com.twilio.type.RecordingRule");
        predefinedImportMappings.put("PhoneNumberCapabilities", "com.twilio.type.PhoneNumberCapabilities");
        predefinedImportMappings.put("FeedbackIssue", "com.twilio.type.FeedbackIssue");
        predefinedImportMappings.put("PhoneNumberPrice", "com.twilio.type.PhoneNumberPrice");
        predefinedImportMappings.put("OutboundSmsPrice", "com.twilio.type.OutboundSmsPrice");
        predefinedImportMappings.put("InboundSmsPrice", "com.twilio.type.InboundSmsPrice");
        predefinedImportMappings.put("OutboundPrefixPrice", "com.twilio.type.OutboundPrefixPrice");
        predefinedImportMappings.put("InboundCallPrice", "com.twilio.type.InboundCallPrice");
        predefinedImportMappings.put("OutboundCallPrice", "com.twilio.type.OutboundCallPrice");
        predefinedImportMappings.put("OutboundCallPriceWithOrigin", "com.twilio.type.OutboundCallPriceWithOrigin");
        predefinedImportMappings.put("OutboundPrefixPriceWithOrigin", "com.twilio.type.OutboundPrefixPriceWithOrigin");
        predefinedImportMappings.put("PrefixedCollapsibleMap", "com.twilio.converter.PrefixedCollapsibleMap");
        //predefinedImportMappings.put("Map<String, Object>", "java.util.Map");
        
        importMapping.putAll(predefinedImportMappings);
    }
    
    public void modelTemplateFiles(Map<String, String> modelTemplateFiles) {
        // Do not generate models
        modelTemplateFiles.clear();
    }
    
    /*
    If a parameter or property in the OpenAPI specification has properties defined, then generator considers it as a complex type.
    So to avoid that, we remove the properties from the custom models, So that typeMapping can be used to map the type to a custom class.
    
    Alternative is to remove properties from the OpenAPI specification itself, but that might be used by doc team.
    
    Example:
    dummyName:
      type: object
      format: ice-server
      properties:
        credential:
          type: string
        username:
          type: string
        url:
          type: string
        urls:
          type: string
     */
    public void removePropertiesFromCustomModels(OpenAPI openAPI) {
        customFormatWithProperties.add("ice-server");
        customFormatWithProperties.add("subscribe-rule");
        customFormatWithProperties.add("recording-rule");
        customFormatWithProperties.add("inbound-call-price");
        customFormatWithProperties.add("inbound-sms-price");
        customFormatWithProperties.add("outbound-call-price");
        customFormatWithProperties.add("outbound-call-price-with-origin");
        customFormatWithProperties.add("outbound-prefix-price");
        customFormatWithProperties.add("outbound-sms-price");
        customFormatWithProperties.add("phone-number-capabilities");
        customFormatWithProperties.add("phone-number-price");
        customFormatWithProperties.add("prefixed-collapsible-map");
        customFormatWithProperties.add("prefixed-collapsible-map-AddOns");
        customFormatWithProperties.add("outbound-prefix-price-with-origin");
        customFormatWithProperties.add("string-map");
        customFormatWithProperties.add("uri-map");

        openAPI.getComponents().getSchemas()
                .forEach((name, schema) -> {
                    if (customFormatWithProperties.contains(schema.getFormat())) {
                        if (schema.getProperties() != null) {
                            schema.setProperties(null);
                        }
                    }
                });
    }
}
/*
"endpoint": "",

    "object": ["java.util.Map", "com.twilio.converter.Converter"],

    "prefixed_collapsible_map": ["", "java.util.Map"],
    "twiml": "com.twilio.type.Twiml"
 */
