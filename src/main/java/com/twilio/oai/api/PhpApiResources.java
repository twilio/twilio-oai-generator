package com.twilio.oai.api;
import org.openapitools.codegen.CodegenParameter;

import java.util.ArrayList;
import java.util.List;

public class PhpApiResources extends ApiResources{
    private String apiListPath;
    private String apiContextPath;
    private List<CodegenParameter> requiredPathParamsList = new ArrayList<>();
    private List<CodegenParameter> requiredPathParamsContext = new ArrayList<>();

    public PhpApiResources(PhpApiResourceBuilder apiResourceBuilder) {
        super(apiResourceBuilder);

        apiListPath = apiResourceBuilder.apiListPath;
        apiContextPath = apiResourceBuilder.apiContextPath;
        requiredPathParamsList = new ArrayList<>(apiResourceBuilder.requiredPathParamsList);
        requiredPathParamsContext = new ArrayList<>(apiResourceBuilder.requiredPathParamsContext);
    }
}
