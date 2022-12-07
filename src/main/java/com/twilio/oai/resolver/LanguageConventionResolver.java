package com.twilio.oai.resolver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.oai.Segments;

import java.util.Map;

public class LanguageConventionResolver implements IConventionMapper {
    public static final String VENDOR_PREFIX = "x-";
    public static final String PREFIXED_COLLAPSIBLE_MAP = "prefixed-collapsible-map";
    public static final String X_PREFIXED_COLLAPSIBLE_MAP = "x-prefixed-collapsible-map";
    public static final String HYPHEN = "-";
    public static final String OBJECT = "object";
    public static final String MIXED = "mixed";
    public static final String MIXED_ARRAY = "mixed[]";

    public static final String LIST_OBJECT = "List<Object>";

    public static final String PHONE_NUMBER_FORMAT = "phone-number";

    public static final String X_IS_PHONE_NUMBER_FORMAT = "x-is-phone-number-format";

    public static final String LIST_PREFIX = "list-";

    private Map<String, Map<String, Object>> conventionalMap ;
    public LanguageConventionResolver(String langConventionFilePath) {
        this.conventionalMap = getConventionalMap(langConventionFilePath);
    }


    @Override
    public Map<String, Object> properties() {
        return conventionalMap.get(Segments.SEGMENT_PROPERTIES.getSegment());
    }

    @Override
    public Map<String, Object> serialize() {
        return conventionalMap.get(Segments.SEGMENT_SERIALIZE.getSegment());
    }

    @Override
    public Map<String, Object> deserialize() {
        return conventionalMap.get(Segments.SEGMENT_DESERIALIZE.getSegment());
    }

    @Override
    public Map<String, Object> libraries() {
        return conventionalMap.get(Segments.SEGMENT_LIBRARY.getSegment());
    }

    @Override
    public Map<String, Object> hydrate() {
        return conventionalMap.get(Segments.SEGMENT_HYDRATE.getSegment());
    }
    @Override
    public Map<String, Object> promotions() {
        return conventionalMap.get(Segments.SEGMENT_PROMOTIONS.getSegment());
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
