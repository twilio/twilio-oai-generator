package com.twilio.oai.java.processor.enums;

import com.twilio.oai.common.EnumConstants.OpenApiEnumType;

public interface EnumProcessor<T> {
    boolean shouldProcess(T schema);
    void process(T schema);
    OpenApiEnumType getType();
}
