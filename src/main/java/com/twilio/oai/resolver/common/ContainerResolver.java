package com.twilio.oai.resolver.common;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.LanguageDataType;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;

// TODO: Add support for multiple container
public class ContainerResolver {
    private List<? extends LanguageDataType> languageDataTypes;

    public ContainerResolver(List<? extends LanguageDataType> languageDataTypes) {
        this.languageDataTypes = languageDataTypes;
    }

    public String unWrapContainerType(CodegenParameter parameter) {
        String unwrappedContainer = getContainer(parameter.dataType);
        if (StringUtils.isBlank(unwrappedContainer)) return null;
        parameter.dataType = parameter.dataType.replace(unwrappedContainer, "");
        parameter.dataType = parameter.dataType.substring(0, parameter.dataType.length()-1);
        return unwrappedContainer;
    }

    public void reWrapContainerType(CodegenParameter parameter, String unwrappedContainer) {
        if (StringUtils.isBlank(unwrappedContainer)) return;
        parameter.dataType = unwrappedContainer + parameter.dataType + ApplicationConstants.LIST_END;
    }

    public String unWrapContainerType(CodegenProperty property) {
        String unwrappedContainer = getContainer(property.dataType);
        if (StringUtils.isBlank(unwrappedContainer)) return null;
        property.dataType = property.dataType.replace(unwrappedContainer, "");
        property.dataType = property.dataType.substring(0, property.dataType.length()-1);
        return unwrappedContainer;
    }

    public void reWrapContainerType(CodegenProperty parameter, String unwrappedContainer) {
        if (StringUtils.isBlank(unwrappedContainer)) return;
        parameter.dataType = unwrappedContainer + parameter.dataType + ApplicationConstants.LIST_END;
    }

    private String getContainer(String dataType) {
        String unwrappedContainer = null;
        for (LanguageDataType languageDataType: languageDataTypes) {
            if (dataType != null && dataType.startsWith(languageDataType.getValue())) {
                unwrappedContainer = languageDataType.getValue();
                break;
            }
        }
        return unwrappedContainer;
    }

    public String getUnwrappedDataType(String dataType) {
        String unwrappedContainer = getContainer(dataType);
        if (StringUtils.isBlank(unwrappedContainer)) return dataType;
        dataType = dataType.replace(unwrappedContainer, "");
        dataType = dataType.substring(0, dataType.length()-1);
        return dataType;
    }
}
