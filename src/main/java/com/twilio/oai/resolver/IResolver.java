package com.twilio.oai.resolver;

import org.openapitools.codegen.IJsonSchemaValidationProperties;

public interface IResolver <T extends IJsonSchemaValidationProperties>{
    T resolve(T arg);
    void resolveSerialize(T arg);
    void resolveDeSerialize(T arg);
    void resolveProperties(T arg);
    void resolvePrefixedMap(T arg);
}
