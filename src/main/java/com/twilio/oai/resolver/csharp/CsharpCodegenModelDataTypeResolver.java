package com.twilio.oai.resolver.csharp;

import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.common.CodegenModelDataTypeResolver;
import org.openapitools.codegen.CodegenProperty;

import java.util.Map;
import java.util.Optional;

// Overriding default behavior and adding "x-jsonConverter" to enum
public class CsharpCodegenModelDataTypeResolver extends CodegenModelDataTypeResolver {

    public CsharpCodegenModelDataTypeResolver(IConventionMapper mapper, Map<String, String> modelFormatMap) {
        super(mapper, modelFormatMap);
    }

    public CodegenProperty resolve(CodegenProperty property) {
        super.resolve(property);
        resolveEnum(property);
        mapper.deserialize()
                .getString(property.dataFormat)
                .ifPresent(dataType -> property.vendorExtensions.put("x-jsonConverter", dataType));

        return property;
    }

    private CodegenProperty resolveEnum(CodegenProperty property) {
        if (property == null) {
            return null;
        }

        OperationStore operationStore = OperationStore.getInstance();
        if (property.complexType == null || !property.complexType.contains("Enum")) {
            if (property.dataType != null) {
                Optional importStm = mapper.libraries().get(StringHelper.toSnakeCase(property.dataType).replaceAll("_", "-"));
                if (!importStm.isEmpty() && importStm.get() instanceof String && importStm.get().equals("Twilio.Types")) {
                    // If the datatype found in libraries(csharp.json) is Twilio.Types, import enum into the resource file.
                    operationStore.setEnumPresentInResource(true);
                }
            }
            return property;
        }
        String className = OperationStore.getInstance().getClassName();
        property.isEnum = true;
        String[] value = property.complexType.split(ApplicationConstants.ENUM);
        property.enumName = value[value.length-1] + ApplicationConstants.ENUM;
        // In case enum is an array
        if (property.items != null) {
            property.items.enumName = value[value.length-1] + ApplicationConstants.ENUM;
        }
        property.dataType = className + ApplicationConstants.RESOURCE + ApplicationConstants.DOT + property.enumName;
        property.vendorExtensions.put("x-jsonConverter", "StringEnumConverter"); // TODO: Remove this.
        operationStore.getEnums().put(property.enumName, property);
        // Import enum into the resource if it contains enum class.
        operationStore.setEnumPresentInResource(true);
        return property;
    }
}
