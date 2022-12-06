package com.twilio.oai.resolver.common;

import com.twilio.oai.Segments;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.resolver.IConventionMapper;
import org.openapitools.codegen.CodegenProperty;

import java.util.HashMap;
import java.util.Map;

import static com.twilio.oai.common.ApplicationConstants.LIST_START;
import static com.twilio.oai.common.ApplicationConstants.OBJECT;

public class CodegenModelDataTypeResolver implements Resolver<CodegenProperty> {

    private IConventionMapper mapper;
    private CodegenModelComplexResolver codegenModelComplexResolver;
    private  Map<String, String> modelFormatMap = new HashMap<>();

    public CodegenModelDataTypeResolver() {
        codegenModelComplexResolver = new CodegenModelComplexResolver();
    }

    public CodegenProperty resolve(CodegenProperty property) {
        assignDataType(property);
        // Exceptional case, data format does not exist for Object type.
        assignDataTypeObjectForNullDataFormat(property);

        if (property.complexType != null && modelFormatMap.containsKey(property.complexType)) {
            codegenModelComplexResolver.setModelFormatMap(modelFormatMap);
            codegenModelComplexResolver.setMapper(mapper);
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

    public void setMapper(IConventionMapper mapper) {
        this.mapper = mapper;
    }

    public void setModelFormatMap(final Map<String, String> modelFormatMap) {
        this.modelFormatMap = new HashMap<>(modelFormatMap);
    }
}
