package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.resolver.Resolver;
import com.twilio.oai.template.IApiActionTemplate;

import java.util.List;
import java.util.Map;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

public class NodeApiResourceBuilder extends FluentApiResourceBuilder {
    public NodeApiResourceBuilder(final IApiActionTemplate template,
                                  final List<CodegenOperation> codegenOperations,
                                  final List<CodegenModel> allModels,
                                  final DirectoryStructureService directoryStructureService) {
        super(template, codegenOperations, allModels, directoryStructureService);
    }

    public NodeApiResourceBuilder(final IApiActionTemplate template,
                                  final List<CodegenOperation> codegenOperations,
                                  final List<CodegenModel> allModels,
                                  final DirectoryStructureService directoryStructureService,
                                  final Map<String, Boolean> toggleMap) {
        super(template, codegenOperations, allModels, directoryStructureService);
        this.toggleMap = toggleMap;
    }

    @Override
    public ApiResourceBuilder updateOperations(final Resolver<CodegenParameter> codegenParameterIResolver) {
        super.updateOperations(codegenParameterIResolver);

        final String apiName = getApiName();
        final String resourceName = apiName + "Instance";

        for (final CodegenOperation co : codegenOperationList) {
            co.httpMethod = co.httpMethod.toLowerCase();

            if (co.nickname.startsWith("delete")) {
                addOperationName(co, "Remove");
                co.returnType = "boolean";
            } else if (co.nickname.startsWith("list")) {
                addOperationName(co, "Page");
                co.returnType = apiName + "Page";
            } else {
                co.returnType = resourceName;
            }

            dependents.values().forEach(dependent -> {
                if (dependent.getType().equals(resourceName)) {
                    dependent.setType(resourceName + "Import");
                    dependent.setClassName(resourceName + "Import");
                    dependent.setImportName(resourceName + " as " + dependent.getType());
                }
            });
        }

        return this;
    }

    @Override
    public ApiResourceBuilder updateModel(Resolver<CodegenModel> codegenModelResolver) {
        super.updateModel(codegenModelResolver);
        return this;
    }

    @Override
    protected String getModelName(final String classname) {
        return removeEnumName(classname);
    }

    @Override
    protected String getDataTypeName(final String dataType) {
        return removeEnumName(dataType);
    }

    private String removeEnumName(final String dataType) {
        if (dataType != null && dataType.contains(ApplicationConstants.ENUM)) {
            return getApiName() + Utility.removeEnumName(dataType);
        }

        return dataType;
    }
}
