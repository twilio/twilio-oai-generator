package com.twilio.oai.common;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;

import static com.twilio.oai.common.ApplicationConstants.ARRAY;
import static com.twilio.oai.common.ApplicationConstants.OBJECT;

@UtilityClass
public class Utility {

    private static final int OVERFLOW_CHECKER = 32;
    public static final int BASE_SIXTEEN = 16;
    private static final int BIG_INTEGER_CONSTANT = 1;

    public void setComplexDataMapping(final List<CodegenModel> allModels, Map<String, String> modelFormatMap) {
        allModels.forEach(item -> {
            if (item.getFormat() != null && OBJECT.equalsIgnoreCase(item.getDataType())) {
                modelFormatMap.put(item.classname, item.getFormat());
            }
        });
    }

    public void addModelsToLocalModelList(final Map<String, ModelsMap> modelMap, List<CodegenModel> localModels){
        for (final ModelsMap mods : modelMap.values()) {
            final List<ModelMap> modList = mods.getModels();
            modList
                    .stream()
                    .map(ModelMap::getModel)
                    .map(CodegenModel.class::cast)
                    .collect(Collectors.toCollection(() -> localModels));
        }
    }

    public String removeEnumName(final String dataType) {
        return dataType == null
            ? null
            : Arrays.stream(dataType.split(ApplicationConstants.ENUM)).reduce((first, second) -> second).orElseThrow();
    }

    public String getMd5(String input){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger bigInteger = new BigInteger(BIG_INTEGER_CONSTANT, messageDigest);
            String hashText = bigInteger.toString(BASE_SIXTEEN);
            while (hashText.length() < OVERFLOW_CHECKER) {
                hashText = "0" + hashText;
            }
            return hashText;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String populateCrudOperations(final CodegenOperation operation) {
        final EnumConstants.Operation method = Arrays
            .stream(EnumConstants.Operation.values())
            .filter(item -> operation.operationId.toLowerCase().startsWith(item.getValue().toLowerCase()))
            .findFirst()
            .orElse(EnumConstants.Operation.READ);

        operation.vendorExtensions.put("x-is-" + method.name().toLowerCase() + "-operation", true);

        String summary = operation.notes;
        if (summary == null || summary.isEmpty()) {
            summary = method.name().toLowerCase();
        }

        operation.vendorExtensions.put("x-generate-comment", summary);

        return method.name();
    }

    public String getRecordKey(final List<CodegenModel> models, final List<CodegenOperation> codegenOperationList) {
        return codegenOperationList
            .stream()
            .filter(co -> co.operationId.toLowerCase().startsWith("list"))
            .map(co -> getModelByClassname(models, co.returnBaseType).orElse(null))
            .filter(Objects::nonNull)
            .map(CodegenModel::getAllVars)
            .flatMap(Collection::stream)
            .filter(v -> v.openApiType.equals(ARRAY))
            .map(v -> v.baseName)
            .findFirst()
            .orElse(null);
    }

    public Optional<CodegenModel> getModel(final List<CodegenModel> models,
                                           final String className,
                                           final String recordKey,
                                           final CodegenOperation codegenOperation) {
        if (recordKey != null &&
            (boolean) codegenOperation.vendorExtensions.getOrDefault("x-is-read-operation", false)) {
            return models
                .stream()
                .filter(model -> model.getClassname().equals(className))
                .map(CodegenModel::getVars)
                .flatMap(Collection::stream)
                .filter(prop -> prop.baseName.equals(recordKey))
                .map(CodegenProperty::getComplexType)
                .map(classname -> getModelByClassname(models, classname))
                .findFirst()
                .orElse(null);
        }

        return getModelByClassname(models, className);
    }

    public Optional<CodegenModel> getModelByClassname(final List<CodegenModel> models, final String classname) {
        return models.stream().filter(model -> model.classname.equals(classname)).findFirst();
    }

    public Optional<CodegenModel> getModelByName(final List<CodegenModel> models, final String modelname) {
        return models.stream().filter(model -> model.name.equals(modelname)).findFirst();
    }

    public static void resolveContentType(CodegenOperation co) {
        if(co.bodyParam != null) {
            LinkedHashMap conType = co.bodyParam.getContent();
            if (conType != null && conType.containsKey("application/json")) {
                co.vendorExtensions.put("x-is-json-type", true);
            }
        }
    }

    public static String extractDatatypeFromContainer(String input) {
        // Define the regular expression pattern
        Pattern pattern = Pattern.compile("<([^>]+)>");

        // Create a matcher object
        Matcher matcher = pattern.matcher(input);

        // Check if the pattern matches
        if (matcher.find()) {
            // Return the captured group which is the custom type
            return matcher.group(1);
        }
        return null; // Return null if no match is found
    }

    public static String replaceDatatypeInContainer(String input, String replacement) {
        // Define the regular expression pattern to extract the custom type
        Pattern pattern = Pattern.compile("<([^>]+)>");
        Matcher matcher = pattern.matcher(input);

        // If a match is found, perform the replacement
        if (matcher.find()) {
            // Extract the custom type
            String customType = matcher.group(1);
            // Replace the custom type with the provided replacement string
            return input.replace(customType, replacement);
        }
        // Return the input unchanged if no match is found
        return input;
    }
    
    public static String getEnumNameFromRef(final String ref) {
        String schemaName = ref.replaceFirst("#/components/schemas/", "");
        String[] enumNameArray = ref.split("_enum_");
        return "";
    }
}
