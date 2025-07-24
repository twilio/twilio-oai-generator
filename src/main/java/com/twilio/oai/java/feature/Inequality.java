package com.twilio.oai.java.feature;

import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Main aim is to identify inequality fields in the parameters, so that serialization can be handled correctly. 
 */
public class Inequality {
    public void process(CodegenOperation codegenOperation) {
        Map<String, List<CodegenParameter>> groupedItems = new HashMap<>();

        codegenOperation.queryParams.forEach(param -> groupItems(param, groupedItems));
        addVendorExtension(groupedItems);
        groupedItems.clear();

        codegenOperation.pathParams.forEach(param -> groupItems(param, groupedItems));
        addVendorExtension(groupedItems);
        groupedItems.clear();
        
        codegenOperation.headerParams.forEach(param -> groupItems(param, groupedItems));
        addVendorExtension(groupedItems);
        groupedItems.clear();

        codegenOperation.formParams.forEach(param -> groupItems(param, groupedItems));
        addVendorExtension(groupedItems);
        groupedItems.clear();
    }

    private void groupItems (CodegenParameter codegenParameter, Map<String, List<CodegenParameter>> groupedItems) {
        String baseName = getBaseName(codegenParameter);
        if (!groupedItems.containsKey(baseName)) {
            groupedItems.put(baseName, new ArrayList<>());
        }
        groupedItems.get(baseName).add(codegenParameter);
    }

    private void addVendorExtension(Map<String, List<CodegenParameter>> groupedItems) {
        for (Map.Entry<String, List<CodegenParameter>> entry : groupedItems.entrySet()) {
            List<CodegenParameter> values = entry.getValue();
            if (values.size() > 1) {
                // Add a vendor extension to indicate that there are multiple parameters with the same base name
                String baseName = entry.getKey();
                for (CodegenParameter param : values) {
                    if (baseName.equals(param.baseName)) {
                        param.vendorExtensions.put("x-inequality-main", true);
                    } else {
                        param.vendorExtensions.put("x-has-before-or-after", true);
                    }
                }
            }
        }
    }
    
    private String getBaseName(CodegenParameter codegenParameter) {
        if (codegenParameter.baseName.endsWith(">") || codegenParameter.baseName.endsWith("<")) {
            return codegenParameter.baseName.substring(0, codegenParameter.baseName.length() - 1);
        }
        return codegenParameter.baseName;
    }
}
