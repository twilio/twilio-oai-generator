package com.twilio.oai.resolver;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.ApplicationConstants;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.java.ContainerResolver;
import lombok.AllArgsConstructor;
import org.openapitools.codegen.CodegenProperty;

import java.util.Arrays;
import java.util.Stack;

import static com.twilio.oai.common.ApplicationConstants.DESERIALIZE_VEND_EXT;
import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_VEND_EXT;

@AllArgsConstructor
public class LanguagePropertyResolver extends Resolver<CodegenProperty> {
    protected IConventionMapper mapper;
    public static final String OBJECT = "object";

    @Override
    public CodegenProperty resolve(CodegenProperty codegenProperty, ApiResourceBuilder apiResourceBuilder) {
        ContainerResolver containerResolver = new ContainerResolver(Arrays.asList(EnumConstants.JavaDataTypes.values()));
        Stack<String> containerTypes = new Stack();
        codegenProperty.dataType = containerResolver.unwrapContainerType(codegenProperty, containerTypes);
        resolveProperties(codegenProperty, apiResourceBuilder);
        resolveSerialize(codegenProperty);
        resolveDeSerialize(codegenProperty);
        resolvePrefixedMap(codegenProperty);
        containerResolver.rewrapContainerType(codegenProperty, containerTypes);
        return codegenProperty;
    }

    protected void handleAnyType(CodegenProperty codegenProperty, ApiResourceBuilder apiResourceBuilder) {
        return;
    }

    protected void resolveProperties(CodegenProperty codegenProperty, ApiResourceBuilder apiResourceBuilder) {
        handleAnyType(codegenProperty, apiResourceBuilder);
        if(codegenProperty.vendorExtensions.get("x-is-anytype") == null) {
            mapper
                    .properties()
                    .getString(codegenProperty.dataFormat)
                    .or(() -> mapper.properties().getString(codegenProperty.dataType))
                    .ifPresent(dataType -> codegenProperty.dataType = dataType);
        }
    }

    protected void resolveSerialize(CodegenProperty codegenProperty) {
        mapper
            .serialize()
            .getString(getDataType(codegenProperty))
            .ifPresent(serialize -> {
                serialize = serialize.replace("{value}", codegenProperty.name);
                if (!codegenProperty.name.equals(serialize)) {
                    // Save only which require custom serialization
                    codegenProperty.vendorExtensions.put(SERIALIZE_VEND_EXT, serialize);
                }
            });
    }
    protected void resolveDeSerialize(CodegenProperty codegenProperty) {
        mapper
            .deserialize()
            .getString(getDataType(codegenProperty))
            .ifPresent(deserialize -> codegenProperty.vendorExtensions.put(DESERIALIZE_VEND_EXT,
                                                                           deserialize.split("\\(")[0]));
    }

    protected void resolvePrefixedMap(CodegenProperty codegenProperty) {
        if (codegenProperty.dataFormat != null && codegenProperty.dataFormat.startsWith(ApplicationConstants.PREFIXED_COLLAPSIBLE_MAP)) {
            String[] split_format_array = codegenProperty.dataFormat.split(LanguageConventionResolver.HYPHEN);
            codegenProperty.vendorExtensions.put(LanguageConventionResolver.X_PREFIXED_COLLAPSIBLE_MAP, split_format_array[split_format_array.length - 1]);
            codegenProperty.dataType = mapper.properties().getString(ApplicationConstants.PREFIXED_COLLAPSIBLE_MAP).orElseThrow();
        }
    }
}
