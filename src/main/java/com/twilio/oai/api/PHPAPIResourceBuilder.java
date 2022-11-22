package com.twilio.oai.api;

import com.twilio.oai.PathUtils;
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
        List<CodegenOperation> opr = codegenOperationList.stream().filter(op -> !isInstanceOperation(op)).collect(Collectors.toList());
        if (!opr.isEmpty()) {
            apiPath = opr.get(0).path;
        }
        String path = apiPath;
        String regex = "/[v1-9]+[^/]+";
        Matcher matcher = Pattern.compile(regex).matcher(apiPath);
        if (matcher.find()) {
            path = PathUtils.removeFirstPart(apiPath);
        }
        path = lowerCasePathParam(path);
        apiPath = replaceBraces(path);

        return this;
    }

    private String lowerCasePathParam(String path) {
        return Pattern.compile("\\{([\\w])").matcher(path)
                .replaceAll(match -> "{"+match.group(1).toLowerCase());
    }

    private String replaceBraces(String path) {
        path = path.replaceAll("[{]", "' . \\\\rawurlencode(\\$");
        return path.replaceAll("[}]",") . '");
    }
}
