package com.twilio.oai.resolver.php;

import org.openapitools.codegen.IJsonSchemaValidationProperties;

public interface IResolver<T extends IJsonSchemaValidationProperties> {
    T resolve(T ars);
}
