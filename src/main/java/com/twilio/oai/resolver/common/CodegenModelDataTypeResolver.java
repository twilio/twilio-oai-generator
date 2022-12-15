package com.twilio.oai.resolver.common;

import com.twilio.oai.Segments;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.IConventionMapper;
import org.openapitools.codegen.CodegenProperty;

import java.util.Map;

import static com.twilio.oai.common.ApplicationConstants.LIST_START;
import static com.twilio.oai.common.ApplicationConstants.OBJECT;

public class CodegenModelDataTypeResolver extends Resolver<CodegenProperty> {

    private final IConventionMapper mapper;
    private final CodegenModelComplexResolver codegenModelComplexResolver;
    private final Map<String, String> modelFormatMap;

    public CodegenModelDataTypeResolver(final IConventionMapper mapper, final Map<String, String> modelFormatMap) {
        this.mapper = mapper;
        this.modelFormatMap = modelFormatMap;
        codegenModelComplexResolver = new CodegenModelComplexResolver(mapper, modelFormatMap);
    }

    public CodegenProperty resolve(CodegenProperty property) {
        // Exceptional case, data format does not exist for Object type.
        assignDataTypeObjectForNullDataFormat(property);
        assignDataType(property);

        if (property.complexType != null && modelFormatMap.containsKey(property.complexType)) {
            codegenModelComplexResolver.resolve(property);
        }

        return property;
    }

    private void assignDataType(CodegenProperty property) {
        String propertyFieldName = Segments.SEGMENT_PROPERTIES.getSegment();

        if (mapper.properties().containsKey(property.dataFormat)) {
            property.dataType = (String)mapper.properties().get(property.dataFormat);
        } else if (mapper.properties().containsKey(property.dataType)) {
            property.dataType = (String)mapper.properties().get(property.dataType);
        }
    }

    private void assignDataTypeObjectForNullDataFormat(CodegenProperty property){
        if (property.dataFormat == null) {
            if (property.dataType.equals(OBJECT)) {
                property.dataType = "object";
            } else if (property.dataType.equals(LIST_START + OBJECT )) {
                property.dataType = "List<object";
            }
        }
    }
}
