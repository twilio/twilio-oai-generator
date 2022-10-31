package com.twilio.oai.api;

import com.twilio.oai.template.ITemplate;
import com.twilio.oai.template.PHPAPITemplate;
import org.openapitools.codegen.*;

import java.util.*;

public class PHPAPIResourceBuilder extends APIResourceBuilder {

    public PHPAPIResourceBuilder(ITemplate template, List<CodegenOperation> codegenOperations, List<CodegenModel> allModels) {
        super(template, codegenOperations, allModels);
    }

    @Override
    public PHPAPIResourceBuilder setTemplate() {
        super.setTemplate();
        codegenOperationList.stream().forEach(codegenOperation -> {
            phpTemplate.clean();
            if (super.isInstanceOperation(codegenOperation)) {
                phpTemplate.add(PHPAPITemplate.TEMPLATE_TYPE_CONTEXT);
            } else {
                phpTemplate.add(PHPAPITemplate.TEMPLATE_TYPE_LIST);
                phpTemplate.add(PHPAPITemplate.TEMPLATE_TYPE_PAGE);
                phpTemplate.add(PHPAPITemplate.TEMPLATE_TYPE_OPTIONS);
            }
            phpTemplate.add(PHPAPITemplate.TEMPLATE_TYPE_INSTANCE);
        });
        return this;
    }
}
