package com.twilio.oai.modern;

import com.twilio.oai.TwilioJavaGenerator;
import com.twilio.oai.java.JavaTemplateFile;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.twilio.oai.common.ApplicationConstants.PATH_SEPARATOR_PLACEHOLDER;

public class JavaApiResourceBuilderNew {

    List<CodegenOperation> operations;
    JavaOperationProcessorNew operationProcessor;
    //private final Map<String, ResponseProcessor> responseProcessors = new HashMap<>();
    JavaTemplateFile javaTemplateFile = new JavaTemplateFile();

    TwilioJavaGenerator twilioJavaGenerator;

    //JavaOpenAPIProcessor javaOpenAPIProcessor;

    CodegenModel responseModel;

    String resourceName;

    public JavaApiResourceBuilderNew(TwilioJavaGenerator twilioJavaGenerator) {
        this.twilioJavaGenerator = twilioJavaGenerator;
        //javaOpenAPIProcessor = new JavaOpenAPIProcessor();
    }

    public void process(List<CodegenOperation> operations, JavaOperationProcessorNew operationProcessor) {
        this.operations = operations;
        this.operationProcessor = operationProcessor;
        fetchResourceName();
        templateModifier();
        for (CodegenOperation codegenOperation: operations) {
            operationProcessor.process(codegenOperation);
        }
    }

    // Bug: path should not end with '/'
    // example: /2010-04-01/Accounts.json, otherwise directory structure will not be created properly.
    private void fetchResourceName() {
        ResourceCache.setResourceName(""); // Clear
        operations.forEach(co -> {
            List<String> filePathArray = new ArrayList<>(Arrays.asList(co.baseName.split(PATH_SEPARATOR_PLACEHOLDER)));
            if (!filePathArray.isEmpty() && StringUtils.isNotBlank(filePathArray.get(filePathArray.size() - 1))) {
                resourceName = filePathArray.get(filePathArray.size() - 1);
            }
        });
        ResourceCache.setResourceName(resourceName);
    }

    private void templateModifier() {
        // This method is a placeholder for any template modifications that might be needed.
        // It can be implemented to modify the templates used for generating Java API resources.
        javaTemplateFile.addApiTemplate(twilioJavaGenerator, operations);
    }


    public JavaApiResourceNew build() {
        return new JavaApiResourceNew(this);
    }
}
