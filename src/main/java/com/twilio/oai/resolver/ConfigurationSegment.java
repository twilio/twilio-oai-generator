package com.twilio.oai.resolver;

import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConfigurationSegment {
    private static final Map<String, String> CONVERSIONS = Map.of("uri", "url", "bool", "boolean");

    private final Map<String, Object> configuration;

    public Optional<String> getString(final String name) {
        return get(name).map(String.class::cast);
    }

    public Optional<Map> getMap(final String name) {
        return get(name).map(Map.class::cast);
    }

    public Optional<Object> get(final String name) {
        return name != null ? findKey(name.toLowerCase()).map(configuration::get) : Optional.empty();
    }

    private Optional<String> findKey(final String name) {
        return configuration.keySet().stream().filter(key -> isMatch(name, key)).sorted().findFirst()
            // Remove the last section of the kebab and try again.
            .or(() -> name.contains("-") ? findKey(name.replaceAll("-[^-]*$", "")) : Optional.empty());
    }

    private boolean isMatch(final String name, final String key) {
        final String keyKebab = key
            .replaceAll("[^a-zA-Z\\d]+", "-")
            .replaceAll("([A-Za-z])(\\d)", "$1-$2")
            .replaceAll("-+$", "");
        final String keyWithoutQualifier = key.replaceAll("<.*", "").replace("_", "-");

        if (keyKebab.equals(name) || keyWithoutQualifier.equals(name)) {
            return true;
        }

        return CONVERSIONS
            .entrySet()
            .stream()
            .filter(entry -> name.startsWith(entry.getKey()) && !name.startsWith(entry.getValue()))
            .anyMatch(entry -> isMatch(name.replaceAll("^" + entry.getKey(), entry.getValue()), key));
    }
}
