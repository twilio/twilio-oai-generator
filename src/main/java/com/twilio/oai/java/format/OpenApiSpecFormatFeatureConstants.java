package com.twilio.oai.java.format;

import java.util.HashMap;
import java.util.Map;

/*
Must be kept in sync with src/main/resources/config/java.json
 */
public class OpenApiSpecFormatFeatureConstants {

    /*
     * key (format) -> The key is the format defined in the OpenAPI Spec.
     * inputType -> Customer will provide inputType.
     * promoter -> Promoter will use this to convert the inputType to the desired type. 
     * 
     * This can be applied to setters. For example, if a query parameter is of the format "phone-number",
     * two setters will be created: one for `PhoneNumber` and another for `String`.
     * The promoter logic will be applied to the `String` setter. 
     */
    public final static Map<String, Promotion> PROMOTIONS = Map.of(
        "uri", new Promotion("String", "Promoter.uriFromString({})"),
        "phone-number", new Promotion("String", "Promoter.phoneNumberFromString({})"),
        "twiml", new Promotion("String", "Promoter.twimlFromString({})")
    );

    /*
     * key (format) -> The key is the format defined in the OpenAPI Spec.
     * value -> deserializer class name in twilio-java
     */
    public final static Map<String, String> DESERIALIZER = Map.of(
            // Covers Hydrate mentioned in java.json
            "date-time-rfc-2822", "com.twilio.converter.RFC2822Deserializer",
            "date-time", "com.twilio.converter.ISO8601Deserializer",
            "date", "com.twilio.converter.LocalDateDeserializer",
            // Covers deserialize mentioned in java.json
            "currency", "com.twilio.converter.CurrencyDeserializer"
    );

    /*
     * key -> The key is the combination of type+format defined in the OpenAPI Spec.
     * value -> The value is the Java type that should be used for that combination.
     */
    final static Map<String, String> getPredefinedTypeMappings() {
        final Map<String, String> predefinedTypeMappings = new HashMap<>();
        // The following two mappings may seem unnecessary, but they are essential.
        // They address a bug that incorrectly converts Map<String, String> to MapStringString.
        predefinedTypeMappings.put("Map<String, String>", "Map<String, String>");
        predefinedTypeMappings.put("Map<String, Object>", "Map<String, Object>");

        predefinedTypeMappings.put("string+phone-number", "com.twilio.type.PhoneNumber");
        predefinedTypeMappings.put("com.twilio.type.PhoneNumber", "com.twilio.type.PhoneNumber");
        predefinedTypeMappings.put("string+endpoint", "com.twilio.type.Endpoint");
        predefinedTypeMappings.put("com.twilio.type.Endpoint", "com.twilio.type.Endpoint");
        predefinedTypeMappings.put("string+twiml", "com.twilio.type.Twiml");
        predefinedTypeMappings.put("com.twilio.type.Twiml", "com.twilio.type.Twiml");
        
        
        predefinedTypeMappings.put("string+uri", "URI");
        predefinedTypeMappings.put("string+url", "URI");
        predefinedTypeMappings.put("string+currency", "Currency");
        predefinedTypeMappings.put("string+date-time", "ZonedDateTime");
        predefinedTypeMappings.put("string+date", "LocalDate");
        //predefinedTypeMappings.put("LocalDate", "LocalDate");
        predefinedTypeMappings.put("string+http-method", "HttpMethod");
        predefinedTypeMappings.put("string+date-time-rfc-2822", "ZonedDateTime");
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
        return predefinedTypeMappings;
    }

    /*
     * key -> Java datatype
     * value -> Java import statement for key
     */
    public static Map<String, String> getPredefinedImportMappings() {

        Map<String, String> predefinedImportMappings = new HashMap<>();
        predefinedImportMappings.put("PhoneNumber", "com.twilio.type.PhoneNumber");
        predefinedImportMappings.put("URI", "java.net.URI");
        predefinedImportMappings.put("Currency", "java.util.Currency");
        predefinedImportMappings.put("HttpMethod", "com.twilio.http.HttpMethod");
        predefinedImportMappings.put("Endpoint", "com.twilio.type.Endpoint");
        predefinedImportMappings.put("ZonedDateTime", "java.time.ZonedDateTime");

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

        return predefinedImportMappings;
    }
}


