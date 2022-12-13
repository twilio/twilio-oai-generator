package com.twilio.oai.resolver.java;

import com.twilio.oai.Segments;
import com.twilio.oai.StringHelper;
import com.twilio.oai.resolver.LanguagePropertyResolver;
import com.twilio.oai.resolver.IConventionMapper;
import org.openapitools.codegen.CodegenProperty;

import java.util.HashMap;
import java.util.Map;

import static com.twilio.oai.common.ApplicationConstants.VENDOR_PREFIX;

public class JavaPropertyResolver extends LanguagePropertyResolver {
    public JavaPropertyResolver(IConventionMapper mapper) {
        super(mapper);
    }

    @Override
    public  void resolveProperties(CodegenProperty property) {
        super.resolveProperties(property);
        Map<String, Map<String, Object>> vendorExtensions = new HashMap<>();

        for (Segments segment: Segments.values()) {
            Map<String, Object> segmentMap = getMapperByType(segment);
            if (segmentMap.containsKey(property.dataFormat)) {
                Map<String, Object> segmentElements = new HashMap<>();
                segmentElements.put(VENDOR_PREFIX + property.dataFormat, String.format(segmentMap.get(property.dataFormat).toString() , property.name));
                vendorExtensions.put(segment.getSegment(), segmentElements);
            }
        }

        property.nameInSnakeCase = StringHelper.toSnakeCase(property.name);

        // Merges the vendorExtensions map with the existing property's vendor extensions
        vendorExtensions.forEach(
                (key, value) -> property.getVendorExtensions().merge(key, value, (oldValue, newValue) -> newValue)
        );
    }

    Map<String, Object> getMapperByType(Segments segments) {
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
}
