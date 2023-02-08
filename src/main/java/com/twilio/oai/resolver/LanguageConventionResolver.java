package com.twilio.oai.resolver;

import com.twilio.oai.Segments;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.common.ApplicationConstants;

public class LanguageConventionResolver implements IConventionMapper {
    public static final String X_PREFIXED_COLLAPSIBLE_MAP = "x-prefixed-collapsible-map";
    public static final String HYPHEN = "-";
    public static final String MIXED = "mixed";
    public static final String MIXED_ARRAY = "mixed[]";

    public static final String LIST_OBJECT = "List<Object>";

    private final Map<String, Map<String, Object>> conventionalMap;

    public LanguageConventionResolver(String langConventionFilePath) {
        this.conventionalMap = getConventionalMap(langConventionFilePath);
    }

    @Override
    public ConfigurationSegment properties() {
        return getSegment(Segments.SEGMENT_PROPERTIES);
    }

    @Override
    public ConfigurationSegment serialize() {
        return getSegment(Segments.SEGMENT_SERIALIZE);
    }

    @Override
    public ConfigurationSegment deserialize() {
        return getSegment(Segments.SEGMENT_DESERIALIZE);
    }

    @Override
    public ConfigurationSegment libraries() {
        return getSegment(Segments.SEGMENT_LIBRARY);
    }

    @Override
    public ConfigurationSegment hydrate() {
        return getSegment(Segments.SEGMENT_HYDRATE);
    }

    @Override
    public ConfigurationSegment promotions() {
        return getSegment(Segments.SEGMENT_PROMOTIONS);
    }

    private ConfigurationSegment getSegment(final Segments segmentType) {
        return new ConfigurationSegment(conventionalMap.get(segmentType.getSegment()));
    }

    private Map<String, Map<String, Object>> getConventionalMap(String conventionalFilePath) {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(conventionalFilePath), new TypeReference<>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
