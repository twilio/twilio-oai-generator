package com.twilio.oai.common.resolver;

import org.openapitools.codegen.IJsonSchemaValidationProperties;

public interface Resolver <T extends IJsonSchemaValidationProperties>{
    public T resolve(T arg);
}
