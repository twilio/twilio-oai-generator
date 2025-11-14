package com.twilio.oai.java.nestedmodels;

public class MustacheOneOfIface {
    String interfaceClassName;
    String discriminator;

    public MustacheOneOfIface(String interfaceClassName) {
        this.interfaceClassName = interfaceClassName;
    }

    public MustacheOneOfIface(String interfaceClassName, String discriminator) {
        this.interfaceClassName = interfaceClassName;
        this.discriminator = discriminator;
    }
}
