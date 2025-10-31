package com.twilio.oai.resolver.node;

import com.twilio.oai.Segments;
import com.twilio.oai.StringHelper;
import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.ConfigurationSegment;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguagePropertyResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.HashMap;
import java.util.Map;

import static com.twilio.oai.common.ApplicationConstants.VENDOR_PREFIX;

public class NodePropertyResolver extends LanguagePropertyResolver {
    public NodePropertyResolver(IConventionMapper mapper) {
        super(mapper);
    }

    @Override
    public  void resolveProperties(CodegenProperty property, ApiResourceBuilder apiResourceBuilder) {
        super.resolveProperties(property, apiResourceBuilder);
        Map<String, Map<String, Object>> vendorExtensions = new HashMap<>();

        for (Segments segment : Segments.values()) {
            getMapperByType(segment).get(property.dataFormat).ifPresent(value -> {
                Map<String, Object> segmentElements = new HashMap<>();
                segmentElements.put(VENDOR_PREFIX + property.dataFormat,
                                    value.toString().replaceAll("\\{.*}", property.name));
                vendorExtensions.put(segment.getSegment(), segmentElements);
            });
        }

        property.nameInSnakeCase = StringHelper.toSnakeCase(property.name);

        // Merges the vendorExtensions map with the existing property's vendor extensions
        vendorExtensions.forEach(
                (key, value) -> property.getVendorExtensions().merge(key, value, (oldValue, newValue) -> newValue)
        );

        if (apiResourceBuilder.getToggleMap().getOrDefault(EnumConstants.Generator.TWILIO_JAVA_LEGACY.getValue(), Boolean.FALSE) ) {
            resolveIngressModel(property, apiResourceBuilder);
        }
    }

    ConfigurationSegment getMapperByType(Segments segments) {
        switch (segments) {
            case SEGMENT_PROPERTIES:
                return mapper.properties();
            case SEGMENT_LIBRARY:
                return mapper.libraries();
            case SEGMENT_DESERIALIZE:
                return mapper.deserialize();
            case SEGMENT_SERIALIZE:
                return mapper.serialize();
            case SEGMENT_PROMOTIONS:
                return mapper.promotions();
            case SEGMENT_HYDRATE:
                return mapper.hydrate();
        }
        return null;

    }

    @Override
    public void resolveDeSerialize(CodegenProperty codegenProperty) {
        super.resolveDeSerialize(codegenProperty);
    }

    private void resolveIngressModel(CodegenProperty property, ApiResourceBuilder apiResourceBuilder) {
        for (CodegenModel model : apiResourceBuilder.getAllModels()) {
            if(model.getClassname().equals(property.complexType)) {
                // Fetch model from allModels and resolve that recursively
                property.dataType = apiResourceBuilder.getApiName() + "." + property.dataType;
            }
        }
    }
}
