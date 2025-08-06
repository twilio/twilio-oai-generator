package com.twilio.oai.java.feature.constructor;

import com.google.common.collect.Lists;
import com.twilio.oai.StringHelper;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.twilio.oai.api.JavaApiResourceBuilder.SIGNATURE_LIST;
import static com.twilio.oai.common.ApplicationConstants.ACCOUNT_SID_VEND_EXT;

/*
 This class builds the constructor parameters from mandatory fields from parameter(path, query, header), request body and conditional fields
 from request body.
 */
public class UrlencodedBodyConstructorGenerator implements ConstructorGenerator {
    public void apply(CodegenOperation codegenOperation) {
        codegenOperation.vendorExtensions.put("x-java-constructor", true);
        codegenOperation.vendorExtensions.put(SIGNATURE_LIST, generateSignatureListModern(codegenOperation));
        System.out.println(codegenOperation.vendorExtensions.get(SIGNATURE_LIST));
    }
    
    public boolean shouldApply(CodegenOperation codegenOperation) {
        // Check if the operation consumes application/x-www-form-urlencoded
        if (codegenOperation.consumes == null || codegenOperation.consumes.isEmpty()) {
            return false;
        }
        return codegenOperation.consumes.stream()
                .anyMatch(mediaType -> mediaType.get("mediaType").equals("application/x-www-form-urlencoded"));
    }

    public List<List<CodegenParameter>> generateSignatureListModern(final CodegenOperation codegenOperation) {
        List<CodegenParameter> requiredParams = codegenOperation.requiredParams;
        
        CodegenParameter accountSidParam = null;
        Optional<CodegenParameter> optionalParam = codegenOperation.pathParams.stream()
                .filter(param -> param.vendorExtensions.containsKey(ACCOUNT_SID_VEND_EXT)).findAny();
        if(optionalParam.isPresent()){
            accountSidParam = optionalParam.get();
        }
        
        List<List<CodegenParameter>> conditionalParameterDoubleList = getConditionalParameters(codegenOperation);
        conditionalParameterDoubleList = Lists.cartesianProduct(conditionalParameterDoubleList);
        // conditionalParameterDoubleList contains list of constructors only from conditional parameters
        // We need to filter out similar constructors.
        List<List<CodegenParameter>> filteredConditionalCodegenParam = filterConditionalParametersByDatatype(conditionalParameterDoubleList);

        // Combine required and conditional parameters to form the signature list
        List<List<CodegenParameter>> signatureList = new ArrayList<>();
        for(List<CodegenParameter> paramList : filteredConditionalCodegenParam){
            signatureList.add(addAllToList(codegenOperation.requiredParams, paramList));
            if( accountSidParam != null) {
                signatureList.add(addAllToList(List.of(accountSidParam), requiredParams, paramList));
            }
        }
        return signatureList;
    }

    @SafeVarargs
    private final <T> List<T> mergeLists(List<T>... lists) {
        return Arrays.stream(lists).flatMap(List::stream).collect(Collectors.toList());
    }
    
    /*
      The `conditionalParameterDoubleList` contains combinations of constructors derived solely from conditional parameters.
      It is necessary to filter out constructors with similar parameter combinations to ensure uniqueness.
     */
    private List<List<CodegenParameter>> filterConditionalParametersByDatatype(List<List<CodegenParameter>> conditionalParameterDoubleList) {
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
    
    private List<List<CodegenParameter>> getConditionalParameters(CodegenOperation codegenOperation) {
        List<List<CodegenParameter>> conditionalCodegenParam = new ArrayList<>();
        
        if (codegenOperation.vendorExtensions.containsKey("x-twilio")) {
            HashMap<String, Object> twilioVendorExtension = (HashMap<String, Object>) codegenOperation.vendorExtensions.get("x-twilio");
            if (twilioVendorExtension.containsKey("conditional")) {
                List<List<String>> conditionalParamDoubleList = (List<List<String>>) twilioVendorExtension.get("conditional");

                // Look for conditional parameters in body
                for (List<String> conditionalParamList : conditionalParamDoubleList) {
                    List<CodegenParameter> foundParameters = new ArrayList<>();
                    for (String cp : conditionalParamList) {
                        CodegenParameter matchedParam = null;
                        for (CodegenParameter formParam : codegenOperation.formParams) {
                            if (!formParam.required
                                    && StringHelper.camelize(formParam.baseName, true)
                                    .equals(StringHelper.camelize(cp, true))) {
                                matchedParam = formParam;
                                break;
                            }
                        }
                        if (matchedParam == null) {
                            throw new IllegalArgumentException("Parameter not found: " + cp);
                        }
                        foundParameters.add(matchedParam);
                    }
                    conditionalCodegenParam.add(foundParameters);
                }
                
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

    public ArrayList<List<CodegenParameter>> generateSignatureList(final CodegenOperation co) {
        CodegenParameter accountSidParam = null;
        List<List<CodegenParameter>> conditionalCodegenParam = new ArrayList<>();
        Optional<CodegenParameter> optionalParam = co.allParams.stream()
                .filter(param -> param.vendorExtensions.containsKey("x-account-sid")).findAny();
        if (optionalParam.isPresent()) {
            accountSidParam = optionalParam.get();
        }

        if (co.vendorExtensions.containsKey("x-twilio")) {
            HashMap<String, Object> twilioVendorExtension = (HashMap<String, Object>) co.vendorExtensions.get("x-twilio");
            if (twilioVendorExtension.containsKey("conditional")) {
                List<List<String>> conditionalParams = (List<List<String>>) twilioVendorExtension.get("conditional");
                conditionalCodegenParam = conditionalParams.stream().map(
                        paramList -> paramList.stream().map(
                                cp -> co.optionalParams.stream().filter(
                                        op -> op.paramName.equals(cp)
                                ).findAny().get()
                        ).collect(Collectors.toList())).collect(Collectors.toList());
                conditionalCodegenParam = conditionalCodegenParam.stream()
                        .filter(cpList -> (cpList.size() <= 1 || !cpList.get(0).dataType.equals(cpList.get(1).dataType)))
                        .collect(Collectors.toList());
            }
        }

        conditionalCodegenParam = Lists.cartesianProduct(conditionalCodegenParam);
        List<List<CodegenParameter>> filteredConditionalCodegenParam = new ArrayList<>();
        HashSet<List<String>> signatureHashSet = new HashSet<>();
        for (List<CodegenParameter> paramList : conditionalCodegenParam) {
            List<String> orderedParamList = paramList.stream().map(p -> p.dataType).collect(Collectors.toList());
            if (!signatureHashSet.contains(orderedParamList)) {
                filteredConditionalCodegenParam.add(paramList);
                signatureHashSet.add(orderedParamList);
            }
        }

        ArrayList<List<CodegenParameter>> signatureList = new ArrayList<>();
        for (List<CodegenParameter> paramList : filteredConditionalCodegenParam) {
            signatureList.add(addAllToList(co.requiredParams, paramList));
            if (accountSidParam != null) {
                signatureList.add(addAllToList(List.of(accountSidParam), co.requiredParams, paramList));
            }
        }
        return signatureList;
    }

    private <T> List<T> addAllToList(List<T>... lists) {
        return Arrays.stream(lists).flatMap(List::stream).collect(Collectors.toList());
    }
}