package com.twilio.oai.java.feature.constructor;

import com.google.common.collect.Lists;
import com.twilio.oai.StringHelper;
import org.checkerframework.checker.units.qual.A;
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
public class UrlencodedBodyConstructorGenerator extends ConstructorGenerator {
    public void apply(CodegenOperation codegenOperation) {
        codegenOperation.vendorExtensions.put("x-java-constructor", true);
        codegenOperation.vendorExtensions.put(SIGNATURE_LIST, generateSignatureListModern(codegenOperation));
    }
    
    public boolean shouldApply(CodegenOperation codegenOperation) {
        // TODO
        //return true;
        // Check if the operation consumes application/x-www-form-urlencoded
        if (codegenOperation.consumes == null || codegenOperation.consumes.isEmpty()) {
            return true;
        }
        boolean shouldApply =  codegenOperation.consumes.stream()
                .anyMatch(mediaType -> mediaType.get("mediaType").equals("application/x-www-form-urlencoded"));
        return shouldApply;
    }
    
    /*
    Steps:
    1. Get required parameters from path, query, header, body except accountSid
    2. Get accountSid parameter from path if exists
    3. Get conditional parameters from body
    4. Get cartesian product of conditional parameters
    5. Filter out similar datatype parameters because they will create same constructors
    6. Combine required and conditional parameters to form the signature list
    7. If accountSid exists, add it to the beginning of each signature
    8. Return the signature list
     */
    public List<List<CodegenParameter>> generateSignatureListModern(final CodegenOperation codegenOperation) {
        // Step 1
        List<CodegenParameter> requiredParams = getRequiredParameters(codegenOperation);
        // Step 2
        CodegenParameter accountSidParam = getAccountSidParameter(codegenOperation);
        // Step 3
        List<List<CodegenParameter>> conditionalParameters = getConditionalParameters(codegenOperation);
        // Step 4
        List<List<CodegenParameter>> cartesianProducts = Lists.cartesianProduct(conditionalParameters);
        // Step 5
        List<List<CodegenParameter>> filteredConditionalCodegenParam = filterConditionalParameters(cartesianProducts);
        // Step 6
        List<List<CodegenParameter>> listOfConstructors =  combineParameters(requiredParams, filteredConditionalCodegenParam, accountSidParam);
        return listOfConstructors;
    }

    private List<CodegenParameter> getRequiredParameters(CodegenOperation codegenOperation) {
        List<CodegenParameter> requiredParams;
        requiredParams = codegenOperation.pathParams.stream()
                .filter(param -> !param.vendorExtensions.containsKey(ACCOUNT_SID_VEND_EXT))
                .collect(Collectors.toList());
        requiredParams.addAll(codegenOperation.queryParams.stream()
                .filter(param -> param.required)
                .collect(Collectors.toList()));
        requiredParams.addAll(codegenOperation.formParams.stream()
                .filter(param -> param.required)
                .collect(Collectors.toList()));
        requiredParams.addAll(codegenOperation.headerParams.stream()
                .filter(param -> param.required)
                .collect(Collectors.toList()));
        return requiredParams;
    }
    private CodegenParameter getAccountSidParameter(CodegenOperation codegenOperation) {
        return codegenOperation.pathParams.stream()
                .filter(param -> param.vendorExtensions.containsKey(ACCOUNT_SID_VEND_EXT))
                .findAny()
                .orElse(null);
    }

    public List<List<CodegenParameter>> getConditionalParameters(CodegenOperation codegenOperation) {
        List<List<String>> conditionalParamDoubleList = new ArrayList<>();
        try {
            conditionalParamDoubleList = (List<List<String>>) ((HashMap<String, Object>) codegenOperation.vendorExtensions.get("x-twilio")).get("conditional");    
        } catch (NullPointerException nullPointerException) {
            // skip list do not exist.
        }
        if (conditionalParamDoubleList == null || conditionalParamDoubleList.isEmpty()) {
            return List.of();
        }
        List<List<CodegenParameter>> conditionalCodegenParam = lookForConditionalParameterInBody(conditionalParamDoubleList, codegenOperation);
        return conditionalCodegenParam;
    }

    /*
     * The `conditionalParameterDoubleList` contains combinations of constructors derived solely from conditional parameters.
     * It is necessary to filter out constructors with similar parameter combinations to ensure uniqueness.
     */
    public List<List<CodegenParameter>> filterConditionalParameters(List<List<CodegenParameter>> conditionalParams) {
        List<List<CodegenParameter>> filteredConditionalCodegenParam = new ArrayList<>();
        HashSet<List<String>> signatureHashSet = new HashSet<>();
        for (List<CodegenParameter> paramList : conditionalParams) {
            List<String> orderedParamList = paramList.stream().map(p -> p.dataType).collect(Collectors.toList());
            if (signatureHashSet.add(orderedParamList)) {
                filteredConditionalCodegenParam.add(paramList);
            }
        }
        return filteredConditionalCodegenParam;
    }

    private List<List<CodegenParameter>> combineParameters(List<CodegenParameter> requiredParams,
                                                           List<List<CodegenParameter>> filteredConditionalCodegenParam,
                                                           CodegenParameter accountSidParam) {
        List<List<CodegenParameter>> signatureList = new ArrayList<>();
        for (List<CodegenParameter> paramList : filteredConditionalCodegenParam) {
            signatureList.add(addAllToList(requiredParams, paramList));
            if (accountSidParam != null) {
                signatureList.add(addAllToList(List.of(accountSidParam), requiredParams, paramList));
            }
        }
        return signatureList;
    }

    public List<List<CodegenParameter>> lookForConditionalParameterInBody(List<List<String>> conditionalParamDoubleList, CodegenOperation codegenOperation) {
        List<List<CodegenParameter>> conditionalCodegenParam = new ArrayList<>();
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
        return conditionalCodegenParam;
    }

    @SafeVarargs
    private final <T> List<T> mergeLists(List<T>... lists) {
        return Arrays.stream(lists).flatMap(List::stream).collect(Collectors.toList());
    }

    private <T> List<T> addAllToList(List<T>... lists) {
        return Arrays.stream(lists).flatMap(List::stream).collect(Collectors.toList());
    }
}