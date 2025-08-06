package com.twilio.oai.java.format;

import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

import java.util.List;
import java.util.stream.Collectors;

/*
 * The `Promoter` class is responsible for adding additional methods to provide flexibility
 * for customers to pass `String` as an input parameter instead of specific objects.
 *
 * Promoters are applied only to inputs such as query, header, and URL-encoded form body parameters.
 * These additional methods are generated alongside the required setter methods.
 *
 * Note:
 * - Promoters cannot be applied to path parameters because path parameters cannot be custom objects
 *   defined in `PROMOTIONS`.
 * - The promoter logic uses the `{{paramName}}` placeholder in the setter method argument to ensure
 *   proper substitution of parameter names.
 */
public class Promoter {
    public static void addPromoter(final CodegenParameter codegenParameter) {
        if (codegenParameter.dataFormat != null && OpenApiSpecFormatFeatureConstants.PROMOTIONS.containsKey(codegenParameter.dataFormat)) {
            Promotion promotion = OpenApiSpecFormatFeatureConstants.PROMOTIONS.get(codegenParameter.dataFormat);
            String promoter = promotion.getPromoter();
            String promoterToBeUsedInMustache = promoter.replace("{}", codegenParameter.paramName);
            codegenParameter.vendorExtensions.put("x-promotion", promoterToBeUsedInMustache);
        }
    }
    
    public static void addPromoter(final CodegenOperation codegenOperation) {
        List<CodegenParameter> setterParameters = codegenOperation.allParams.stream().filter(param -> !param.isPathParam).collect(Collectors.toList());
        setterParameters.forEach(param -> addPromoter(param));
    }
}
