package com.twilio.oai;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.java.ContainerResolver;
import com.twilio.oai.resolver.php.PhpConventionResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.Arrays;
import java.util.Stack;

public class PhpJsonRequestBodyResolver extends JsonRequestBodyResolver {

    private final ContainerResolver containerResolver = new ContainerResolver(Arrays.asList(EnumConstants.JavaDataTypes.values()));

    public PhpJsonRequestBodyResolver(ApiResourceBuilder apiResourceBuilder, final Resolver<CodegenProperty> codegenPropertyResolver) {
        super(apiResourceBuilder, codegenPropertyResolver, new PhpConventionResolver());
    }

    @Override
    public void resolve(final CodegenParameter codegenParameter, final Resolver<CodegenParameter> codegenParameterResolver) {
        Stack<String> containerTypes = new Stack<>();
        codegenParameter.dataType = containerResolver.unwrapContainerType(codegenParameter, containerTypes);
        final CodegenModel model = apiResourceBuilder.getModel(codegenParameter.dataType);
        // currently supporting required and conditional parameters only for request body object
        if(model != null)
            model.vendorExtensions.put("x-constructor-required", true);
        containerResolver.rewrapContainerType(codegenParameter, containerTypes);
    }
}
