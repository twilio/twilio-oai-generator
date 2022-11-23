package com.twilio.oai.resolver.php;

import java.util.Map;

public interface IConventionMapper {
    Map<String, Object> properties();
    Map<String, Object> serialize();
    Map<String, Object> deserialize();
    Map<String, Object> libraries();
}
