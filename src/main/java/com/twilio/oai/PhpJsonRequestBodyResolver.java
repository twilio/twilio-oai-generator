package com.twilio.oai;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.java.ContainerResolver;
import com.twilio.oai.resolver.php.PhpContainerResolver;
import com.twilio.oai.resolver.php.PhpConventionResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.Arrays;
import java.util.Stack;

import static java.lang.Character.toLowerCase;

public class PhpJsonRequestBodyResolver extends JsonRequestBodyResolver {

    public static final String MODEL_DATATYPE = "MODEL_DATATYPE";
    private final ContainerResolver containerResolver = new PhpContainerResolver(Arrays.asList(EnumConstants.PhpDataTypes.values()));

    public PhpJsonRequestBodyResolver(ApiResourceBuilder apiResourceBuilder, final Resolver<CodegenProperty> codegenPropertyResolver) {
        super(apiResourceBuilder, codegenPropertyResolver, new PhpConventionResolver());
    }

    @Override
    public void resolve(final CodegenParameter codegenParameter, final Resolver<CodegenParameter> codegenParameterResolver) {
        // Only add if model exists
        Stack<String> containerTypes = new Stack<>();
        codegenParameter.dataType = containerResolver.unwrapContainerType(codegenParameter, containerTypes);
        final CodegenModel model = apiResourceBuilder.getModel(codegenParameter.dataType);
        containerResolver.rewrapContainerType(codegenParameter, containerTypes);
            if(model == null) {
            // If parameter is not a model.
            codegenParameterResolver.resolve(codegenParameter, apiResourceBuilder);
        } else {
            // Example: datatype resolution: CreateMessagesRequest -> Message.CreateMessagesRequest
            codegenParameterResolver.resolve(codegenParameter, apiResourceBuilder);
            for (CodegenProperty property: model.getVars()) {
                resolve(property);
            }
            for (CodegenProperty property: codegenParameter.vars) {
                resolve(property);
            }
            apiResourceBuilder.addNestedModel(model);
        }
        if(!codegenParameter.vendorExtensions.containsKey(MODEL_DATATYPE)) {
            int indexOfNull = codegenParameter.dataType.indexOf("|");
            if (indexOfNull == -1)
                indexOfNull = codegenParameter.dataType.length();
            codegenParameter.vendorExtensions.put(MODEL_DATATYPE, codegenParameter.dataType.substring(0, indexOfNull));
        }
    }

    @Override
    public void resolve(final CodegenProperty property) {
        Stack<String> containerTypes = new Stack<>();
        property.dataType = containerResolver.unwrapContainerType(property, containerTypes);
        final CodegenModel model = apiResourceBuilder.getModel(property.dataType);
        containerResolver.rewrapContainerType(property, containerTypes);
        containerTypes.clear();

            if (model == null) {
            codegenPropertyResolver.resolve(property, apiResourceBuilder);
        } else {
            // Get children
            for (CodegenProperty codegenProperty: model.vars) {
                codegenProperty.dataType = containerResolver.unwrapContainerType(codegenProperty, containerTypes);
                resolve(codegenProperty);
                containerResolver.rewrapContainerType(codegenProperty, containerTypes);
            }

            for (CodegenProperty codegenProperty: property.vars) {
                codegenProperty.dataType = containerResolver.unwrapContainerType(codegenProperty, containerTypes);
                resolve(codegenProperty);
                containerResolver.rewrapContainerType(codegenProperty, containerTypes);
            }
            apiResourceBuilder.addNestedModel(model);
        }
            if(!property.vendorExtensions.containsKey(MODEL_DATATYPE)) {
                int indexOfNull = property.dataType.indexOf("|");
                if (indexOfNull == -1)
                    indexOfNull = property.dataType.length();
                property.vendorExtensions.put(MODEL_DATATYPE, property.dataType.substring(0, indexOfNull));
            }
    }
}
