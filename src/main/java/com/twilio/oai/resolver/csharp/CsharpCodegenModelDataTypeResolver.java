package com.twilio.oai.resolver.csharp;

import com.twilio.oai.StringHelper;
import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.common.CodegenModelDataTypeResolver;

import java.util.Map;
import java.util.Optional;

import com.twilio.oai.resolver.common.CodegenModelResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

// Overriding default behavior and adding "x-jsonConverter" to enum
public class CsharpCodegenModelDataTypeResolver extends CodegenModelDataTypeResolver {
    
    private CodegenModelResolver codegenModelResolver;

    public CsharpCodegenModelDataTypeResolver(IConventionMapper mapper, Map<String, String> modelFormatMap) {
        super(mapper, modelFormatMap);
    }

    public void setCodegenModel(CodegenModelResolver codegenModelResolver) {
        this.codegenModelResolver = codegenModelResolver;
    }

    @Override
    public CodegenProperty resolve(CodegenProperty property, ApiResourceBuilder apiResourceBuilder) {
        CodegenModel codegenModel = apiResourceBuilder.getModel(property.dataType);
        if (codegenModel != null) {
            // this is recursion as codegenModelResolver will again call CsharpCodegenModelDataTypeResolver
            codegenModelResolver.resolve(codegenModel, apiResourceBuilder);
            apiResourceBuilder.addNestedModel(codegenModel);
        } else {
            super.resolve(property, apiResourceBuilder);
            resolveEnum(property);
            mapper.deserialize()
                    .getString(property.dataFormat)
                    .ifPresent(dataType -> property.vendorExtensions.put("x-jsonConverter", dataType));
        }
        return property;
    }

    private void resolveEnum(CodegenProperty property) {
        if (property == null) {
            return;
        }

        OperationStore operationStore = OperationStore.getInstance();
        if (property.complexType == null || !property.complexType.contains("Enum")) {
            if (property.dataType != null) {
                Optional<Object> importStm = mapper.libraries().get(StringHelper.toSnakeCase(property.dataType).replace("_", "-"));
                if (importStm.isPresent() && importStm.get() instanceof String && importStm.get().equals("Twilio.Types")) {
                    // If the datatype found in libraries(csharp.json) is Twilio.Types, import enum into the resource file.
                    operationStore.setEnumPresentInResource(true);
                }
            }
            return;
        }
        String className = OperationStore.getInstance().getClassName();
        property.isEnum = true;
        property.enumName = Utility.removeEnumName(property.complexType) + ApplicationConstants.ENUM;
        // In case enum is an array
        if (property.items != null) {
            property.items.enumName = property.enumName;
        }
        property.dataType = className + ApplicationConstants.RESOURCE + ApplicationConstants.DOT + property.enumName;
        property.vendorExtensions.put("x-jsonConverter", "StringEnumConverter"); // TODO: Remove this.
        operationStore.getEnums().put(property.enumName, property);
        // Import enum into the resource if it contains enum class.
        operationStore.setEnumPresentInResource(true);
    }
}
