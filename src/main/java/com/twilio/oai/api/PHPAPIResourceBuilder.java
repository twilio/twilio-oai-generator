package com.twilio.oai.api;

import com.twilio.oai.template.IAPIActionTemplate;
import com.twilio.oai.template.PHPAPIActionTemplate;
import org.openapitools.codegen.*;

import java.util.*;

public class PHPAPIResourceBuilder extends APIResourceBuilder {

    public PHPAPIResourceBuilder(IAPIActionTemplate template, List<CodegenOperation> codegenOperations, List<CodegenModel> allModels) {
        super(template, codegenOperations, allModels);
    }

    @Override
    public PHPAPIResourceBuilder template() {
        super.template();
        codegenOperationList.stream().forEach(codegenOperation -> {
            phpTemplate.clean();
            if (super.isInstanceOperation(codegenOperation)) {
                phpTemplate.add(PHPAPIActionTemplate.TEMPLATE_TYPE_CONTEXT);
            } else {
                phpTemplate.add(PHPAPIActionTemplate.TEMPLATE_TYPE_LIST);
                phpTemplate.add(PHPAPIActionTemplate.TEMPLATE_TYPE_PAGE);
                phpTemplate.add(PHPAPIActionTemplate.TEMPLATE_TYPE_OPTIONS);
            }
            phpTemplate.add(PHPAPIActionTemplate.TEMPLATE_TYPE_INSTANCE);
        });
        return this;
    }
}
