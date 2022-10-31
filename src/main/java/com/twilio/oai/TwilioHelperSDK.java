package com.twilio.oai;

public enum TwilioHelperSDK {
    TWILIO_PHP("twilio-php");

    private final String name;

    private TwilioHelperSDK(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
