package com.twilio.oai.api;

import com.twilio.oai.template.IAPIActionTemplate;
import com.twilio.oai.template.PHPAPIActionTemplate;
import org.openapitools.codegen.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.*;

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
                phpTemplate.add(PHPAPIActionTemplate.TEMPLATE_TYPE_PAGE);
                phpTemplate.add(PHPAPIActionTemplate.TEMPLATE_TYPE_OPTIONS);
            }
            phpTemplate.add(PHPAPIActionTemplate.TEMPLATE_TYPE_LIST);
            phpTemplate.add(PHPAPIActionTemplate.TEMPLATE_TYPE_INSTANCE);
        });
        return this;
    }

    @Override
    public IAPIResourceBuilder apiPath() {
        super.apiPath();
        String path=apiPath;
        List<CodegenOperation> opr = codegenOperationList.stream().filter(op -> !isInstanceOperation(op)).collect(Collectors.toList());
        if (!opr.isEmpty())
            apiPath = opr.get(0).path;
        Pattern pattern = Pattern.compile("/[v1-9]+[^/]+");
        Matcher matcher = pattern.matcher(apiPath);
        if (matcher.find()) {
            path = apiPath.split("/[v1-9]+[^/]+")[1];
        }
//        path = apiPath.split("/[v1-9]+[^/]+")[1];
        int idx = 0;
        String url = "";
        while (idx < path.length()) {
            if (path.substring(idx).contains("{")) {
                url += "\'" + path.substring(idx, path.indexOf('{', idx)) + "\'";
                idx = path.indexOf('{', idx) + 1;
                String temp = path.substring(idx, path.indexOf('}', idx));
                temp = Character.toLowerCase(temp.charAt(0)) + temp.substring(1);
                url += " . \\rawurlencode($" + temp + ") . ";
                idx = path.indexOf('}', idx) + 1;
            } else {
                url += "\'" + path.substring(idx) + "\'";
                break;
            }
        }
        apiPath = url;
        return this;
    }
}
