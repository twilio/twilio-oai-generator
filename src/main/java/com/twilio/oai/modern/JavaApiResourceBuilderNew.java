package com.twilio.oai.modern;

import com.twilio.oai.TwilioJavaGenerator;
import com.twilio.oai.java.processor.JavaOperationProcessor;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;

public class JavaApiResourceBuilderNew {
    JavaOperationProcessor operationProcessor;
    TwilioJavaGenerator twilioJavaGenerator;
    String resourceName;

    public JavaApiResourceBuilderNew(TwilioJavaGenerator twilioJavaGenerator) {
        this.twilioJavaGenerator = twilioJavaGenerator;
        this.operationProcessor = JavaOperationProcessor.getInstance();
    }

    public void process(List<CodegenOperation> operations) {
        fetchResourceName(operations);
        operations.forEach(operation -> operationProcessor.process(operation));
    }

    // Bug: path should not end with '/'
    // example: /2010-04-01/Accounts.json, otherwise directory structure will not be created properly.
    private void fetchResourceName(List<CodegenOperation> operations) {
        ResourceCache.setResourceName(""); // Clear
        operations.forEach(co -> {
            List<String> filePathArray = new ArrayList<>(Arrays.asList(co.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
            if (!filePathArray.isEmpty() && StringUtils.isNotBlank(filePathArray.get(filePathArray.size() - 1))) {
                resourceName = filePathArray.get(filePathArray.size() - 1);
            }
        });
        ResourceCache.setResourceName(resourceName);
    }

    public JavaApiResource build() {
        return new JavaApiResource(this);
    }
}
