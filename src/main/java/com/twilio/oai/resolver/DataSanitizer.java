package com.twilio.oai.resolver;

import org.openapitools.codegen.IJsonSchemaValidationProperties;

public interface DataSanitizer <T extends IJsonSchemaValidationProperties>{
    public  void sanitize(T data);
}
