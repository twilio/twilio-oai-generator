package com.twilio.oai;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.api.JavaApiResourceBuilder;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.java.ContainerResolver;
import com.twilio.oai.resolver.java.JavaConventionResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.Arrays;
import java.util.Stack;

// TODO: Refactor such that it can be used with c# code as well, For that need to refactor java resolvers.
public class JsonRequestBodyResolver {
    final Resolver<CodegenProperty> codegenPropertyResolver;
    
    final ApiResourceBuilder apiResourceBuilder;
    
    final private ContainerResolver containerResolver = new ContainerResolver(Arrays.asList(EnumConstants.JavaDataTypes.values()));

    private final JavaConventionResolver conventionResolver;

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    private String resourceName;

    public JsonRequestBodyResolver(ApiResourceBuilder apiResourceBuilder, final Resolver<CodegenProperty> codegenPropertyResolver) {
        this.codegenPropertyResolver = codegenPropertyResolver;
        this.apiResourceBuilder = apiResourceBuilder;
        this.conventionResolver = new JavaConventionResolver();
    }
    
    public void resolve(final CodegenParameter codegenParameter, final Resolver<CodegenParameter> codegenParameterResolver) {
        // Only add if model exists
        Stack<String> containerTypes = new Stack<>();
        codegenParameter.dataType = containerResolver.unwrapContainerType(codegenParameter, containerTypes);
        final CodegenModel model = apiResourceBuilder.getModel(codegenParameter.dataType);
        containerResolver.rewrapContainerType(codegenParameter, containerTypes);
        if (CodegenUtils.isParameterSchemaEnumJava(codegenParameter)) {
            conventionResolver.resolveEnumParameter(codegenParameter, resourceName);
            ((JavaApiResourceBuilder)apiResourceBuilder).enums.add(codegenParameter);
        } else if(model == null) {
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
    }

    public void resolve(final CodegenProperty property) {
        Stack<String> containerTypes = new Stack<>();
        property.dataType = containerResolver.unwrapContainerType(property, containerTypes);
        final CodegenModel model = apiResourceBuilder.getModel(property.dataType);
        containerResolver.rewrapContainerType(property, containerTypes);
        containerTypes.clear();

        if (model == null) {
            codegenPropertyResolver.resolve(property, apiResourceBuilder);
        } else if (CodegenUtils.isPropertySchemaEnumJava(property)) {
            conventionResolver.resolveEnumProperty(property, resourceName);
            ((JavaApiResourceBuilder)apiResourceBuilder).enums.add(property);
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
    }
}
