package com.twilio.oai.api;
import com.twilio.oai.java.cache.ResourceCacheContext;
import org.openapitools.codegen.CodegenParameter;

import java.util.ArrayList;
import java.util.List;

public class PhpApiResources extends ApiResources{
    private String apiListPath;
    private String apiContextPath;
    private Boolean isApiV1 = null; // true or NUll

    public PhpApiResources(PhpApiResourceBuilder apiResourceBuilder) {
        super(apiResourceBuilder);

        apiListPath = apiResourceBuilder.apiListPath;
        apiContextPath = apiResourceBuilder.apiContextPath;
        if (ResourceCacheContext.get().isV1()) isApiV1 = true;
    }
}
