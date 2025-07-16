package com.twilio.oai.java.feature;

import org.openapitools.codegen.CodegenParameter;

public interface ParameterFeature {
    /**
     * Applies the feature to the given parameter.
     *
     * @param codegenParameter the parameter to which the feature will be applied
     */
    void apply(CodegenParameter codegenParameter);

    /**
     * Returns the name of the feature.
     *
     * @return the name of the feature
     */
    String getName();
}
