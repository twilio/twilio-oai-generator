package com.twilio.oai.resolver;

import org.openapitools.codegen.IJsonSchemaValidationProperties;

public interface ISchemaResolver<T extends IJsonSchemaValidationProperties>{
    T resolve(T arg);
}
