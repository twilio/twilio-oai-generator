package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.template.IApiActionTemplate;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;

import java.util.List;

public class RubyApiResourceBuilder extends FluentApiResourceBuilder {
    public RubyApiResourceBuilder(final IApiActionTemplate template, final List<CodegenOperation> codegenOperations, final List<CodegenModel> allModels, final DirectoryStructureService directoryStructureService) {
        super(template, codegenOperations, allModels, directoryStructureService);
    }
}