package com.twilio.oai.resolver.python;

import com.twilio.oai.CodegenUtils;
import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.common.CodegenModelDataTypeResolver;
import com.twilio.oai.resolver.common.CodegenModelResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.Map;
import java.util.function.BiConsumer;

// Overriding default behavior and handling the enum
public class PythonCodegenModelDataTypeResolver extends CodegenModelDataTypeResolver {

    protected Map<String, String> modelFormatMap;
    private CodegenModelResolver codegenModelResolver;

    public PythonCodegenModelDataTypeResolver(IConventionMapper mapper, Map<String, String> modelFormatMap) {
        super(mapper, modelFormatMap);
        this.modelFormatMap = modelFormatMap;
    }

    public void setCodegenModel(CodegenModelResolver codegenModelResolver) {
        this.codegenModelResolver = codegenModelResolver;
    }

    @Override
    public CodegenProperty resolve(CodegenProperty property, ApiResourceBuilder apiResourceBuilder) {
        property =  super.resolve(property, apiResourceBuilder);
        CodegenModel codegenModel = apiResourceBuilder.getModel(property.dataType);
        if (codegenModel != null && !CodegenUtils.isPropertySchemaEnum(property) && !modelFormatMap.containsKey(property.dataType)) {
            // this is recursion as codegenModelResolver will again call PythonCodegenModelDataTypeResolver
            codegenModelResolver.resolve(codegenModel, apiResourceBuilder);
            apiResourceBuilder.addNestedModel(codegenModel);
        } else {
            super.resolve(property, apiResourceBuilder);
            resolveProperty(property, apiResourceBuilder);
        }
        return property;
    }

    public void resolveResponseModel(CodegenProperty property, ApiResourceBuilder apiResourceBuilder) {
        super.resolve(property, apiResourceBuilder);
    }

    protected void resolveProperty(CodegenProperty property, ApiResourceBuilder apiResourceBuilder) {
        if (property.name.contains("from") && property.dataFormat != null &&
                property.dataFormat.equals(ApplicationConstants.PHONE_NUMBER)) {
            property.name = "from_";
            property.vendorExtensions.put("json-name", "from");
        } else {
            property.vendorExtensions.put("json-name", property.name);
        }
        updateDataType(property.baseType, property.dataType, apiResourceBuilder, (dataTypeWithEnum, dataType) -> {
            property.datatypeWithEnum = dataTypeWithEnum;
            property.dataType = dataType;
        });
    }

    private void updateDataType(final String baseType,
                                final String dataType,
                                final ApiResourceBuilder apiResourceBuilder,
                                final BiConsumer<String, String> consumer) {
        consumer.accept(baseType, removeEnumName(dataType, apiResourceBuilder));

        if (baseType != null && dataType != null) {
            final String datatypeWithEnum = removeEnumName(baseType, apiResourceBuilder);
            consumer.accept(datatypeWithEnum, dataType.replaceFirst(baseType, datatypeWithEnum));
        }
    }

    private String removeEnumName(final String dataType, ApiResourceBuilder apiResourceBuilder) {
        if (dataType != null && dataType.contains(ApplicationConstants.ENUM)) {
            return '"' + apiResourceBuilder.getApiName() + "Instance." + Utility.removeEnumName(dataType) + '"';
        }

        return dataType;
    }
}
