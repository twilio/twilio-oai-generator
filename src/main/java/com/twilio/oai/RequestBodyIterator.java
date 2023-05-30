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
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// TODO: Refactor such that it can be used with c# code as well, For that need to refactor java resolvers.
public class RequestBodyIterator {
    final Resolver<CodegenProperty> codegenPropertyResolver;
    
    final Resolver<CodegenParameter> codegenParameterResolver;
    
    final ApiResourceBuilder apiResourceBuilder;
    
    ContainerResolver containerResolver = new ContainerResolver(Arrays.asList(EnumConstants.JavaDataTypes.values()));

    private final JavaConventionResolver conventionResolver;

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    private String resourceName;

    public RequestBodyIterator(Resolver<CodegenProperty> codegenPropertyResolver, 
                               Resolver<CodegenParameter> codegenParameterResolver, 
                               ApiResourceBuilder apiResourceBuilder) {
        this.codegenPropertyResolver = codegenPropertyResolver;
        this.apiResourceBuilder = apiResourceBuilder;
        this.codegenParameterResolver = codegenParameterResolver;
        this.conventionResolver = new JavaConventionResolver();
    }

    public CodegenParameter iterate(final CodegenParameter codegenParameter) {
//        codegenParameter.dataType = apiResourceBuilder.getApiName() + "." + codegenParameter.dataType;
//        for (CodegenProperty property: codegenParameter.vars) {
//            codegenPropertyResolver.resolve(property, apiResourceBuilder);
//        }
//        return codegenParameter;
        
        return null;
    }
    
    public void dfsParameter(final CodegenParameter codegenParameter) {
        // Only add if model exists
        String unwrappedContainer = containerResolver.unwrapContainerType(codegenParameter);
        final CodegenModel model = apiResourceBuilder.getModel(codegenParameter.dataType);
        containerResolver.rewrapContainerType(codegenParameter, unwrappedContainer);
        
        if (model == null) {
            // If parameter is not a model.
            codegenParameterResolver.resolve(codegenParameter, apiResourceBuilder);
            
        } else if(CodegenUtils.isParameterSchemaEnum(codegenParameter)) {
            conventionResolver.resolveEnumParameter(codegenParameter, resourceName);
            
        } else {
            apiResourceBuilder.addNestedModel(model);
            for (CodegenProperty property: codegenParameter.getVars()) {
                dfsProperty(property);
            }
        }
    }

    public void dfs(CodegenParameter codegenParameter) {
        
    }
    public void dfsProperty(CodegenProperty property) {
        
            String unwrappedContainer = containerResolver.unwrapContainerType(property);
            final CodegenModel model = apiResourceBuilder.getModel(property.dataType);
            containerResolver.rewrapContainerType(property, unwrappedContainer);
            
            if (model == null) {
                codegenPropertyResolver.resolve(property, apiResourceBuilder);
            } else if (CodegenUtils.isPropertySchemaEnum(property)) {
                conventionResolver.resolveEnumProperty(property, resourceName);
                ((JavaApiResourceBuilder)apiResourceBuilder).enums.add(property);
            } else {
                apiResourceBuilder.addNestedModel(model);
                // Get children
                for (CodegenProperty codegenProperty: model.vars) {
                    unwrappedContainer = containerResolver.unwrapContainerType(property);
                    dfsProperty(codegenProperty);
                    containerResolver.rewrapContainerType(property, unwrappedContainer);
                }
            }
    }
}
