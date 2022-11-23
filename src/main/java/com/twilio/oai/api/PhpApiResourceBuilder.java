package com.twilio.oai.api;

import com.twilio.oai.PathUtils;
import com.twilio.oai.template.IApiActionTemplate;
import com.twilio.oai.template.PhpApiActionTemplate;
import org.openapitools.codegen.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.*;

public class PhpApiResourceBuilder extends ApiResourceBuilder {

    public PhpApiResourceBuilder(IApiActionTemplate template, List<CodegenOperation> codegenOperations, List<CodegenModel> allModels) {
        super(template, codegenOperations, allModels);
    }

    @Override
    public PhpApiResourceBuilder updateTemplate() {
        codegenOperationList.stream().forEach(codegenOperation -> {
            template.clean();
            if (super.isInstanceOperation(codegenOperation)) {
                template.add(PhpApiActionTemplate.TEMPLATE_TYPE_CONTEXT);
            } else {
                template.add(PhpApiActionTemplate.TEMPLATE_TYPE_PAGE);
                template.add(PhpApiActionTemplate.TEMPLATE_TYPE_OPTIONS);
            }
            template.add(PhpApiActionTemplate.TEMPLATE_TYPE_LIST);
            template.add(PhpApiActionTemplate.TEMPLATE_TYPE_INSTANCE);
        });
        return this;
    }

    @Override
    public IApiResourceBuilder updateApiPath() {
        super.updateApiPath();
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
