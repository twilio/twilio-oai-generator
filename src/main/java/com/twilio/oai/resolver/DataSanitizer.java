package com.twilio.oai.resolver;

import org.openapitools.codegen.IJsonSchemaValidationProperties;

public interface DataSanitizer<T extends IJsonSchemaValidationProperties> {
    void sanitize(T data);
}
