package com.twilio.oai.resolver;

import org.openapitools.codegen.IJsonSchemaValidationProperties;

public interface Resolver <T extends IJsonSchemaValidationProperties>{
    T resolve(T arg);
}
