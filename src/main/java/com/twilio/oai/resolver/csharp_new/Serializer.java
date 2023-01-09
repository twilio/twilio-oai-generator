package com.twilio.oai.resolver.csharp_new;

import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.LanguageDataType;
import org.openapitools.codegen.CodegenParameter;

import java.util.List;

// Resolving datatype to string (Serializable)
public abstract class Serializer {

    List<? extends LanguageDataType> languageDataTypes;
    CodegenParameter serialize(CodegenParameter parameter) {
        unwrapContainer(parameter);
        serializeNonContainer(parameter);
        wrapContainer(parameter);
        return parameter;
    }

    CodegenParameter serializeNonContainer(CodegenParameter parameter) {
        return parameter;
    }

    protected CodegenParameter unwrapContainer(CodegenParameter parameter) {
        String currentContainerType = "";
        boolean isContainerType = false;

        for (LanguageDataType dataType: languageDataTypes) {
            if (parameter.dataType != null && parameter.dataType.startsWith(dataType.getValue())) {
                currentContainerType = dataType.getValue();
                break;
            }
        }

        if (isContainerType) {
            parameter.dataType = parameter.dataType.replace(currentContainerType, "");
            parameter.dataType = parameter.dataType.substring(0, parameter.dataType.length()-1);
        }
        return parameter;
    }

    protected CodegenParameter wrapContainer(CodegenParameter parameter) {

        return parameter;
    }
}
