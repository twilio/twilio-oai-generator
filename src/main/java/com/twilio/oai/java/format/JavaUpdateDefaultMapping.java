package com.twilio.oai.java.format;

import com.twilio.oai.java.format.OpenApiSpecFormatFeatureConstants;
import io.swagger.v3.oas.models.OpenAPI;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        typeMapping.putAll(OpenApiSpecFormatFeatureConstants.getPredefinedTypeMappings());
    }
    
    public void importMapping(Map<String, String> importMapping) {
        // This is an example of how to add a custom import mapping.
        importMapping.putAll(OpenApiSpecFormatFeatureConstants.getPredefinedImportMappings());
    }
    
    public void modelTemplateFiles(Map<String, String> modelTemplateFiles) {
        // Do not generate models
        modelTemplateFiles.clear();
    }

    public void removeReservedWords(Set<String> reservedWords) {
        // Do not generate models
        reservedWords.remove("localdate");
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