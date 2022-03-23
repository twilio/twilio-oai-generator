package com.twilio.oai;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ConventionMap {
    final static String SEGMENT_HYDRATE = "hydrate";
    final static String SEGMENT_SERIALIZE = "serialize";
    final static String SEGMENT_DESERIALIZE = "deserialize";
    final static String SEGMENT_LIBRARY = "library";
    final static String SEGMENT_TYPES = "types";
    final Map<String, Map<String, Object>> conventionalMapping = new HashMap<>();

    public ConventionMap() {
        conventionalMapping.put(SEGMENT_HYDRATE, new HashMap<>());
        conventionalMapping.put(SEGMENT_SERIALIZE, new HashMap<>());
        conventionalMapping.put(SEGMENT_DESERIALIZE, new HashMap<>());
        conventionalMapping.put(SEGMENT_LIBRARY, new HashMap<>());
        conventionalMapping.put(SEGMENT_TYPES, new HashMap<>());
        init();
    }

    public void add(String segment, String key, Object value) {
        conventionalMapping.get(segment).put(key, value);
    }

    public Map<String, Object> get(String segment) {
        return conventionalMapping.get(segment);
    }

    public Object get(String segment, String key) {
        return conventionalMapping.get(segment).get(key);
    }

    private void init() {
        initHydrate();
        initSerialize();
        initDeserialize();
        initLibrary();
        initTypes();
    }

    private void initHydrate() {
        this.add(SEGMENT_HYDRATE,"date", "DateConverter.localDateFromString(%s)");
        this.add(SEGMENT_HYDRATE,"date-time-rfc-2822", "DateConverter.rfc2822DateTimeFromString(%s)");
        this.add(SEGMENT_HYDRATE,"date-time", "DateConverter.iso8601DateTimeFromString(%s)");
    }

    private void initSerialize() {
        this.add(SEGMENT_SERIALIZE,"endpoint","{%s}.getEndpoint()");
        this.add(SEGMENT_SERIALIZE,"iso-country-code","%s");
        this.add(SEGMENT_SERIALIZE,"object","Converter.mapToJson(%s)");
        this.add(SEGMENT_SERIALIZE,"date","DateConverter.dateStringFromLocalDate(%s)");
        this.add(SEGMENT_SERIALIZE,"date-time","%s.toInstant().toString()");
        this.add(SEGMENT_SERIALIZE,"date-or-time","%s.toInstant().toString()");
        this.add(SEGMENT_SERIALIZE,"string","%s");
        this.add(SEGMENT_SERIALIZE,"sid","%s");
    }

    private void initDeserialize() {
        this.add(SEGMENT_DESERIALIZE,"currency", "com.twilio.converter.CurrencyDeserializer");
    }

    private void initLibrary() {
        this.add(SEGMENT_LIBRARY, "currency", Arrays.asList("java.util.Currency"));
        this.add(SEGMENT_LIBRARY,"date", Arrays.asList( "java.time.LocalDate", "com.twilio.converter.DateConverter"));
        this.add(SEGMENT_LIBRARY,"date-time", Arrays.asList("java.time.ZonedDateTime", "com.twilio.converter.DateConverter"));
        this.add(SEGMENT_LIBRARY, "date-or-time", Arrays.asList("java.time.ZonedDateTime", "com.twilio.converter.DateConverter"));
        this.add(SEGMENT_LIBRARY, "date-time-range", Arrays.asList("java.time.ZonedDateTime", "com.twilio.converter.DateConverter"));
        this.add(SEGMENT_LIBRARY, "decimal", Arrays.asList("java.math.BigDecimal"));
        this.add(SEGMENT_LIBRARY,"endpoint", Arrays.asList("com.twilio.type.Endpoint"));
        this.add(SEGMENT_LIBRARY,"http-method", Arrays.asList("com.twilio.http.HttpMethod"));
        this.add(SEGMENT_LIBRARY,"url", Arrays.asList("java.net.URI"));
        this.add(SEGMENT_LIBRARY,"ice-server", Arrays.asList("com.twilio.type.IceServer"));
        this.add(SEGMENT_LIBRARY,"subscribe-rule", Arrays.asList("com.twilio.type.SubscribeRule"));
        this.add(SEGMENT_LIBRARY,"recording-rule", Arrays.asList("com.twilio.type.RecordingRule"));
        this.add(SEGMENT_LIBRARY,"phone-number-capabilities", Arrays.asList("com.twilio.type.PhoneNumberCapabilities"));
        this.add(SEGMENT_LIBRARY,"feedback-issue", Arrays.asList("com.twilio.type.FeedbackIssue"));
        this.add(SEGMENT_LIBRARY,"object", Arrays.asList("java.util.Map", "com.twilio.converter.Converter"));
        this.add(SEGMENT_LIBRARY, "phone-number-price", Arrays.asList("com.twilio.type.PhoneNumberPrice"));
        this.add(SEGMENT_LIBRARY, "outbound-sms-price", Arrays.asList("com.twilio.type.OutboundSmsPrice"));
        this.add(SEGMENT_LIBRARY, "inbound-sms-price", Arrays.asList("com.twilio.type.InboundSmsPrice"));
        this.add(SEGMENT_LIBRARY, "outbound-prefix_price", Arrays.asList("com.twilio.type.OutboundPrefixPrice"));
        this.add(SEGMENT_LIBRARY, "inbound-call_price", Arrays.asList("com.twilio.type.InboundCallPrice"));
        this.add(SEGMENT_LIBRARY, "outbound-call_price", Arrays.asList("com.twilio.type.OutboundCallPrice"));
        this.add(SEGMENT_LIBRARY, "outbound-call_price_with_origin", Arrays.asList("com.twilio.type.OutboundCallPriceWithOrigin"));
        this.add(SEGMENT_LIBRARY, "outbound-prefix_price_with_origin", Arrays.asList("com.twilio.type.OutboundPrefixPriceWithOrigin"));
        this.add(SEGMENT_LIBRARY, "prefixed_collapsible_map", Arrays.asList("com.twilio.converter.PrefixedCollapsibleMap", "java.util.Map"));
        this.add(SEGMENT_LIBRARY, "twiml", Arrays.asList("com.twilio.type.Twiml"));
    }

    private void initTypes() {
        this.add(SEGMENT_TYPES, "currency", "Currency");
        this.add(SEGMENT_TYPES,"date", "LocalDate");
        this.add(SEGMENT_TYPES,"date-time-rfc-2822", "ZonedDateTime");
        this.add(SEGMENT_TYPES,"date-time", "ZonedDateTime");
    }
}
