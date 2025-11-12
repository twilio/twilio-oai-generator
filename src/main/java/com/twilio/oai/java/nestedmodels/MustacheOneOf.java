package com.twilio.oai.java.nestedmodels;

// Implementation of OneOf interface for Mustache templates
public class MustacheOneOf {
    String className;
    String discriminator;

    public MustacheOneOf(String className, String discriminator) {
        this.className = className;
        this.discriminator = discriminator;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MustacheOneOf that = (MustacheOneOf) obj;
        return className != null && className.equals(that.className);
    }

    @Override
    public int hashCode() {
        return className != null ? className.hashCode() : 0;
    }
}
