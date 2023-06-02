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
    
    final Resolver<CodegenParameter> codegenParameterResolver;
    
    final ApiResourceBuilder apiResourceBuilder;
    
    final private ContainerResolver containerResolver = new ContainerResolver(Arrays.asList(EnumConstants.JavaDataTypes.values()));

    private final JavaConventionResolver conventionResolver;

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    private String resourceName;

    public JsonRequestBodyResolver(Resolver<CodegenProperty> codegenPropertyResolver,
                                   Resolver<CodegenParameter> codegenParameterResolver,
                                   ApiResourceBuilder apiResourceBuilder) {
        this.codegenPropertyResolver = codegenPropertyResolver;
        this.apiResourceBuilder = apiResourceBuilder;
        this.codegenParameterResolver = codegenParameterResolver;
        this.conventionResolver = new JavaConventionResolver();
    }
    
    public void resolve(final CodegenParameter codegenParameter) {
        // Only add if model exists
        Stack<String> containerTypes = new Stack<>();
        codegenParameter.dataType = containerResolver.unwrapContainerType(codegenParameter, containerTypes);
        final CodegenModel model = apiResourceBuilder.getModel(codegenParameter.dataType);
        containerResolver.rewrapContainerType(codegenParameter, containerTypes);
        if (CodegenUtils.isParameterSchemaEnum(codegenParameter)) {
            conventionResolver.resolveEnumParameter(codegenParameter, resourceName);
            ((JavaApiResourceBuilder)apiResourceBuilder).enums.add(codegenParameter);
        } else if(model == null) {
            // If parameter is not a model.
            codegenParameterResolver.resolve(codegenParameter, apiResourceBuilder);
        } else {
            apiResourceBuilder.addNestedModel(model);
            for (CodegenProperty property: codegenParameter.getVars()) {
                resolve(property);
            }
        }
    }

    public void resolve(CodegenProperty property) {
        Stack<String> containerTypes = new Stack<>();
        property.dataType = containerResolver.unwrapContainerType(property, containerTypes);
        final CodegenModel model = apiResourceBuilder.getModel(property.dataType);
        containerResolver.rewrapContainerType(property, containerTypes);
        containerTypes.clear();

        if (model == null) {
            codegenPropertyResolver.resolve(property, apiResourceBuilder);
        } else if (CodegenUtils.isPropertySchemaEnum(property)) {
            conventionResolver.resolveEnumProperty(property, resourceName);
            ((JavaApiResourceBuilder)apiResourceBuilder).enums.add(property);
        } else {
            apiResourceBuilder.addNestedModel(model);
            // Get children
            for (CodegenProperty codegenProperty: model.vars) {
                codegenProperty.dataType = containerResolver.unwrapContainerType(property, containerTypes);
                resolve(codegenProperty);
                containerResolver.rewrapContainerType(property, containerTypes);
            }
        }
    }
}
