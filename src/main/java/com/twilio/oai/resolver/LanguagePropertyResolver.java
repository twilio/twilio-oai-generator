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
    public static final String ANY_TYPE = "any-type";
    private static final String LIST_OBJECT = "List<Object>";



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

    protected void resolveProperties(CodegenProperty codegenProperty, ApiResourceBuilder apiResourceBuilder) {
        if((codegenProperty.dataType.equalsIgnoreCase(OBJECT) || codegenProperty.dataType.equals(LIST_OBJECT)) && codegenProperty.vendorExtensions.get("x-is-anytype") == null) {
            String objectType = mapper.properties().getString(OBJECT).orElseThrow();

            if (codegenProperty.isAnyType || (codegenProperty.isArray && codegenProperty.items.isAnyType)) {
                objectType = "Object";
                codegenProperty.vendorExtensions.put("x-is-anytype", true);
            }

            else
                codegenProperty.isFreeFormObject = true;

            if (codegenProperty.dataType.equals(LIST_OBJECT)) {
                codegenProperty.dataType = ApplicationConstants.LIST_START + objectType + ApplicationConstants.LIST_END;
                codegenProperty.baseType = objectType;
            } else {
                codegenProperty.dataType = objectType;
            }
        }

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
