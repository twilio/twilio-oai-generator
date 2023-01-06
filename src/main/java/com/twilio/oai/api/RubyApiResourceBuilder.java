package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.PathUtils;
import com.twilio.oai.StringHelper;
import com.twilio.oai.template.IApiActionTemplate;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.SupportingFile;

import java.io.File;
import java.util.List;

public class RubyApiResourceBuilder extends FluentApiResourceBuilder {
    public RubyApiResourceBuilder(final IApiActionTemplate template,
                                  final List<CodegenOperation> codegenOperations,
                                  final List<CodegenModel> allModels,
                                  final DirectoryStructureService directoryStructureService) {
        super(template, codegenOperations, allModels, directoryStructureService);
    }

//    @Override
//    public IApiResourceBuilder updateTemplate(){
//        if(directoryStructureService.isVersionLess()){
//            String version = StringHelper.toSnakeCase(PathUtils.getFirstPathPart(codegenOperationList.get(0).path));
//            template.addVersionlessFile(version);
//        }
//        super.updateTemplate();
//        return this;
//    }
}
