package com.twilio.oai.java.feature.constructor;

import com.twilio.oai.StringHelper;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ConstructorGenerator {
    abstract void apply(CodegenOperation codegenOperation);
    abstract boolean shouldApply(CodegenOperation codegenOperation);

    public List<List<CodegenParameter>> getConditionalParameters(CodegenOperation codegenOperation) {
        List<List<CodegenParameter>> conditionalCodegenParam = new ArrayList<>();

        if (codegenOperation.vendorExtensions.containsKey("x-twilio")) {
            HashMap<String, Object> twilioVendorExtension = (HashMap<String, Object>) codegenOperation.vendorExtensions.get("x-twilio");
            if (twilioVendorExtension.containsKey("conditional")) {
                List<List<String>> conditionalParamDoubleList = (List<List<String>>) twilioVendorExtension.get("conditional");

                // Look for conditional parameters in body
                // Add to conditionalCodegenParam from conditionalParamDoubleList
                lookForConditionalParameterInBody(conditionalParamDoubleList, conditionalCodegenParam, codegenOperation);

                // Remove similar datatype parameters because they will create same constructors
                List<List<CodegenParameter>> filteredConditionalCodegenParam = new ArrayList<>();
                for (List<CodegenParameter> cpList : conditionalCodegenParam) {
                    if (cpList.size() <= 1 || !cpList.get(0).dataType.equals(cpList.get(1).dataType)) {
                        filteredConditionalCodegenParam.add(cpList);
                    }
                }
                conditionalCodegenParam = filteredConditionalCodegenParam;            }
        }

        return conditionalCodegenParam;
    }

    /*
  The `conditionalParameterDoubleList` contains combinations of constructors derived solely from conditional parameters.
  It is necessary to filter out constructors with similar parameter combinations to ensure uniqueness.
 */
    public List<List<CodegenParameter>> filterConditionalParametersByDatatype(List<List<CodegenParameter>> conditionalParameterDoubleList) {
        List<List<CodegenParameter>> filteredConditionalCodegenParam = new ArrayList<>();
        HashSet<List<String>> signatureHashSet = new HashSet<>();
        for (List<CodegenParameter> paramList : conditionalParameterDoubleList) {
            List<String> orderedParamList = paramList.stream().map(p -> p.dataType).collect(Collectors.toList());
            if (signatureHashSet.add(orderedParamList)) {
                filteredConditionalCodegenParam.add(paramList);
            }
        }
        return filteredConditionalCodegenParam;
    }

    // Look for conditional parameters in body
    abstract void lookForConditionalParameterInBody(List<List<String>> conditionalParamDoubleList, 
                                                List<List<CodegenParameter>> conditionalCodegenParam, CodegenOperation codegenOperation);
}
