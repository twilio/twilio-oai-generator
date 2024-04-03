package com.twilio.oai;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.java.ContainerResolver;
import com.twilio.oai.resolver.php.PhpContainerResolver;
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
        super(apiResourceBuilder, codegenPropertyResolver);
    }

    /**
     * Resolves the codegen parameter using model and recursively resolving its properties
     * @param codegenParameter the parameter to be resolved
     * @param codegenParameterResolver the language based parameter resolver
     */
    @Override
    public void resolve(final CodegenParameter codegenParameter, final Resolver<CodegenParameter> codegenParameterResolver) {
        // Only add if model exists
        Stack<String> containerTypes = new Stack<>();
        codegenParameter.dataType = containerResolver.unwrapContainerType(codegenParameter, containerTypes);
        final CodegenModel model = apiResourceBuilder.getModel(codegenParameter.dataType);
        containerResolver.rewrapContainerType(codegenParameter, containerTypes);
        if (CodegenUtils.isParameterSchemaEnumJava(codegenParameter)) {
            if(!codegenParameter.dataType.contains("Enum")) // if Enum is not present in name, adding exclusively to resolve by parameter resolver
                codegenParameter.dataType = "Enum" + codegenParameter.dataType;
            codegenParameterResolver.resolve(codegenParameter, apiResourceBuilder);
        } else if(model == null) {
            // If parameter is not a model.
            codegenParameterResolver.resolve(codegenParameter, apiResourceBuilder);
        } else {
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

    /**
     * Recursively resolves the codegen property using model
     * @param property the codegen property to be resolved
     */
    @Override
    public void resolve(final CodegenProperty property) {
        Stack<String> containerTypes = new Stack<>();
        property.dataType = containerResolver.unwrapContainerType(property, containerTypes);
        final CodegenModel model = apiResourceBuilder.getModel(property.dataType);
        containerResolver.rewrapContainerType(property, containerTypes);
        containerTypes.clear();
        if (CodegenUtils.isPropertySchemaEnumJava(property)) {
            if(!property.dataType.contains("Enum")) // if Enum is not present in name, adding exclusively to resolve by property resolver
                property.dataType = "Enum" + property.dataType;
            codegenPropertyResolver.resolve(property, apiResourceBuilder);
        } else if(model == null) {
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
    }
}
