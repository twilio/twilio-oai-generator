package com.twilio.oai.templating.handlebars;

import com.twilio.oai.StringHelper;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public enum TwilioHelpers implements Helper<Object> {
    snake_case {
        public Object apply(final Object value, final Options options) {
            return StringHelper.toSnakeCase(value.toString());
        }
    };
}
