package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.template.IApiActionTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;

public class PythonApiResourceBuilder extends FluentApiResourceBuilder {
    public PythonApiResourceBuilder(final IApiActionTemplate template,
                                    final List<CodegenOperation> codegenOperations,
                                    final List<CodegenModel> allModels,
                                    final DirectoryStructureService directoryStructureService) {
        super(template, codegenOperations, allModels, directoryStructureService);
    }

    @Override
    public ApiResourceBuilder updateOperations(final Resolver<CodegenParameter> codegenParameterIResolver) {
        super.updateOperations(codegenParameterIResolver);
        updatePaths();
        for (final CodegenOperation co : codegenOperationList) {
            co.httpMethod = co.httpMethod.toLowerCase();
            updateNamespaceSubPart(co);
            if (co.operationId.startsWith("list")) {
                addOperationName(co, "Page");
            }

            for (CodegenParameter cp : co.allParams) {
                if (cp.paramName.equals("_from")) {
                    cp.paramName = "from_";
                } else if (cp.paramName.equals("a2_p_profile_bundle_sid")) {
                    cp.paramName = "a2p_profile_bundle_sid";
                }
            }
        }
        return this;
    }

    private void updateNamespaceSubPart(CodegenOperation co) {
        namespaceSubPart = Arrays
            .stream(co.baseName.split(PATH_SEPARATOR_PLACEHOLDER))
            .map(StringHelper::toSnakeCase)
            .collect(Collectors.joining("."));
    }

    private void updatePaths() {
        if (listPath != null) {
            listPath = listPath.replace("$", "");
        }
        if (instancePath != null) {
            instancePath = instancePath.replace("$", "");
        }
    }

    @Override
    protected String getDataTypeName(final String dataType) {
        if (dataType != null && dataType.contains(ApplicationConstants.ENUM)) {
            return getApiName() + "Instance." + Utility.removeEnumName(dataType);
        }

        return dataType;
    }
}
