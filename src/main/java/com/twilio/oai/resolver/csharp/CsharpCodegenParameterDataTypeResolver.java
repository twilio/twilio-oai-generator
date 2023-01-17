package com.twilio.oai.resolver.csharp;

import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
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

    public CodegenParameter resolve(CodegenParameter parameter) {
        super.resolve(parameter);
        // Resolve Enums
        resolveEnum(parameter);
        csharpSerializer.serialize(parameter);
        return parameter;
    }

    private CodegenParameter resolveEnum(CodegenParameter parameter) {
        if (!mapper.properties().getString(parameter.dataFormat).isEmpty()) {
            // If the dataformat found in libraries(csharp.json) is Twilio.Types, import enum into the options file.
            Optional importStm = mapper.libraries().get(StringHelper.toSnakeCase(parameter.dataFormat).replaceAll("_", "-"));
            if (!importStm.isEmpty() && importStm.get() instanceof String && importStm.get().equals("Twilio.Types")) {
                OperationStore.isEnumPresentInOptions = true;
            }
            return parameter;
        }
        if (parameter.dataType.contains(ApplicationConstants.ENUM)) {
            parameter.isEnum = true;
            String[] value = parameter.dataType.split(ApplicationConstants.ENUM);
            parameter.enumName = value[value.length-1] + ApplicationConstants.ENUM;
            setDataType(parameter);
        } else if (parameter.isEnum) {
            setDataType(parameter);
        }
        return parameter;
    }

    private void setDataType(CodegenParameter parameter) {
        // In case enum is an array
        if (parameter.items != null) {
            parameter.items.enumName = parameter.enumName;
        }
        parameter.dataType = OperationStore.className + ApplicationConstants.RESOURCE + ApplicationConstants.DOT + parameter.enumName;
        // Using enums to avoid duplicate enum creation
        OperationStore.enums.putIfAbsent(parameter.enumName, parameter);
        OperationStore.isEnumPresentInResource = true;
    }


}
