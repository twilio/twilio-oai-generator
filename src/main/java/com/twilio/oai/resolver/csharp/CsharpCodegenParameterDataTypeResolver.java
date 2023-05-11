package com.twilio.oai.resolver.csharp;

import com.twilio.oai.StringHelper;
import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.common.CodegenParameterDataTypeResolver;
import org.openapitools.codegen.CodegenParameter;

import java.util.Optional;

public class CsharpCodegenParameterDataTypeResolver extends CodegenParameterDataTypeResolver {

    private final CsharpSerializer csharpSerializer;
    public CsharpCodegenParameterDataTypeResolver(IConventionMapper mapper, CsharpSerializer csharpSerializer) {
        super(mapper);
        this.csharpSerializer = csharpSerializer;
    }

    @Override
    public CodegenParameter resolve(CodegenParameter parameter, ApiResourceBuilder apiResourceBuilder) {
        super.resolve(parameter, apiResourceBuilder);
        resolveEnum(parameter);
        csharpSerializer.serialize(parameter);
        return parameter;
    }

    private void resolveEnum(CodegenParameter parameter) {
        if (mapper.properties().getString(parameter.dataFormat).isPresent()) {
            // If the dataformat found in libraries(csharp.json) is Twilio.Types, import enum into the options file.
            Optional<Object> importStm = mapper.libraries().get(StringHelper.toSnakeCase(parameter.dataFormat).replace("_", "-"));
            if (importStm.isPresent() && importStm.get() instanceof String && importStm.get().equals("Twilio.Types")) {
                OperationStore.getInstance().setEnumPresentInOptions(true);
            }
            return;
        }
        if (parameter.dataType.contains(ApplicationConstants.ENUM)) {
            parameter.isEnum = true;
            parameter.enumName = Utility.removeEnumName(parameter.dataType) + ApplicationConstants.ENUM;
        }
        if (parameter.isEnum) {
            setDataType(parameter);
        }
    }

    private void setDataType(CodegenParameter parameter) {
        // In case enum is an array
        if (parameter.items != null) {
            parameter.items.enumName = parameter.enumName;
        }
        parameter.dataType = OperationStore.getInstance().getClassName() + ApplicationConstants.RESOURCE + ApplicationConstants.DOT + parameter.enumName;
        // Using enums to avoid duplicate enum creation
        OperationStore.getInstance().getEnums().putIfAbsent(parameter.enumName, parameter);
        OperationStore.getInstance().setEnumPresentInResource(true);
    }
}
