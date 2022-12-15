package com.twilio.oai.resolver;

public interface IConventionMapper {
    ConfigurationSegment properties();

    ConfigurationSegment serialize();

    ConfigurationSegment deserialize();

    ConfigurationSegment libraries();

    ConfigurationSegment hydrate();

    ConfigurationSegment promotions();
}
