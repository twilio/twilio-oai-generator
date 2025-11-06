package com.twilio.oai.java.processor.responsebody.paginationremover;

import com.intuit.commons.traverser.Traverser;
import com.intuit.commons.traverser.TraversingIterator;
import com.twilio.oai.common.StringUtils;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.cache.ResourceCache2;
import com.twilio.oai.java.cache.ResourceCacheContext;
import com.twilio.oai.java.processor.traverser.CustomTraverser;
import org.checkerframework.checker.units.qual.C;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.CodegenResponse;

import java.util.Map;

public class Meta extends ResponsePaginationRemover {
    @Override
    public boolean shouldProcess(CodegenOperation codegenOperation) {

        Map<String, CodegenModel> codegenModelMap = ResourceCacheContext.get().getAllModelsMapByDefaultGenerator();
        // Check if in the response body there is a "meta" property
        for (CodegenResponse response: codegenOperation.responses) {
            CodegenModel codegenModel = codegenModelMap.get(StringUtils.toPascalCase(response.baseType));
            for (CodegenProperty codegenProperty: codegenModel.vars) {
                if (codegenProperty.name.equals("meta")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public CodegenProperty getResponse(CodegenOperation codegenOperation) {
        Map<String, CodegenModel> codegenModelMap = ResourceCacheContext.get().getAllModelsMapByDefaultGenerator();
        for (CodegenResponse response: codegenOperation.responses) {
            CodegenModel codegenModel = codegenModelMap.get(StringUtils.toPascalCase(response.baseType));
            for (CodegenProperty codegenProperty: codegenModel.vars) {
                if (!codegenProperty.name.equals("meta")) {
                    return codegenProperty;
                }
            }
        }
        return null;
    }
}
