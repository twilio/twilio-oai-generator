package com.twilio.oai.common;

public final class ParameterResolverFactory {

    public static Resolver getInstance(EnumConstants.Generator generator) {

        if (generator.getValue().equals(EnumConstants.Generator.TWILIO_CSHARP.getValue())) {
            return new CsharpResolver(EnumConstants.Generator.TWILIO_CSHARP);
        }
        return new CsharpResolver(EnumConstants.Generator.TWILIO_CSHARP);
    }
}
