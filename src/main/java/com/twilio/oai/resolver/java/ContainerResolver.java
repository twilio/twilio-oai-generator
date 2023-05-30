package com.twilio.oai.resolver.java;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.LanguageDataType;
import lombok.RequiredArgsConstructor;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;

@RequiredArgsConstructor
public class ContainerResolver {
    private final List<? extends LanguageDataType> languageDataTypes;

    public String unwrapContainerType(CodegenProperty property) {
        if (!property.isContainer) return "";
        String unwrappedContainer = null;
        for (LanguageDataType dataType : languageDataTypes) {
            if (property.dataType != null && property.dataType.startsWith(dataType.getValue())) {
                unwrappedContainer = dataType.getValue();
                break;
            }
        }
        property.dataType = property.dataType.replace(unwrappedContainer, "");
        property.dataType = property.dataType.substring(0, property.dataType.length() - 1);

        return unwrappedContainer;
    }

    public void rewrapContainerType(CodegenProperty property, String unwrappedContainer) {
        if (!property.isContainer) return;
        property.dataType = unwrappedContainer + property.dataType + ApplicationConstants.LIST_END;
    }

    public String unwrapContainerType(CodegenParameter parameter) {
        if (!parameter.isContainer) return "";
        String unwrappedContainer = null;
        for (LanguageDataType dataType : languageDataTypes) {
            if (parameter.dataType != null && parameter.dataType.startsWith(dataType.getValue())) {
                unwrappedContainer = dataType.getValue();
                break;
            }
        }
        parameter.dataType = parameter.dataType.replace(unwrappedContainer, "");
        parameter.dataType = parameter.dataType.substring(0, parameter.dataType.length() - 1);

        return unwrappedContainer;
    }

    public void rewrapContainerType(CodegenParameter parameter, String unwrappedContainer) {
        if (!parameter.isContainer) return;
        parameter.dataType = unwrappedContainer + parameter.dataType + ApplicationConstants.LIST_END;
    }
}
