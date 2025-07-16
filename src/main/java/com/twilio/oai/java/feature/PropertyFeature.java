package com.twilio.oai.java.feature;

public interface PropertyFeature extends ParameterFeature {
    /**
     * Applies the feature to the given property.
     *
     * @param codegenProperty the property to which the feature will be applied
     */
    void apply(org.openapitools.codegen.CodegenProperty codegenProperty);

    /**
     * Returns the name of the feature.
     *
     * @return the name of the feature
     */
    String getName();
}
