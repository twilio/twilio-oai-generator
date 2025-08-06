package com.twilio.oai.java;

import com.twilio.oai.TwilioJavaGeneratorModern;
import com.twilio.oai.common.Utility;
import com.twilio.oai.java.processor.JavaOperationProcessor;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;

public class JavaApiResourceBuilder {
    // ApiName, example: Message
    String resourceName;
    // key used for pagination in list operation
    String recordKey;
    TwilioJavaGeneratorModern twilioJavaGenerator;
    final JavaOperationProcessor operationProcessor;
    List<CodegenOperation> operations;
    public JavaApiResourceBuilder(TwilioJavaGeneratorModern twilioJavaGenerator, List<CodegenOperation> operations) {
        this.twilioJavaGenerator = twilioJavaGenerator;
        this.operations = operations;
        this.operationProcessor = JavaOperationProcessor.getInstance();
    }

    // Bug: path should not end with '/'
    // example: /2010-04-01/Accounts.json, otherwise directory structure will not be created properly.
    public JavaApiResourceBuilder resourceName() {
        ResourceCache.setResourceName(""); // Clear
        operations.forEach(co -> {
            List<String> filePathArray = new ArrayList<>(Arrays.asList(co.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
            if (!filePathArray.isEmpty() && StringUtils.isNotBlank(filePathArray.get(filePathArray.size() - 1))) {
                resourceName = filePathArray.get(filePathArray.size() - 1);
            }
        });
        ResourceCache.setResourceName(resourceName);
        return this;
    }

    public JavaApiResourceBuilder recordKey() {
        this.recordKey = Utility.getRecordKey(ResourceCache.allModelsByDefaultGenerator, operations);
        return this;
    }

    public JavaApiResourceBuilder processOperations() {
        if (this.resourceName == null) {
            throw new RuntimeException("Resource name must be set before processing operations.");
        }
        operations.forEach(operation -> operationProcessor.process(operation));
        return this;
    }

    public JavaApiResource build() {
        return new JavaApiResource(this);
    }
}
