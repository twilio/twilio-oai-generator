package com.twilio.oai.resolver.common;

import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.Resolver;

import java.util.Map;

import org.openapitools.codegen.CodegenProperty;

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
        mapper
            .properties()
            .getString(property.dataFormat)
            .or(() -> mapper.properties().getString(property.dataType))
            .ifPresent(dataType -> property.dataType = dataType);
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
