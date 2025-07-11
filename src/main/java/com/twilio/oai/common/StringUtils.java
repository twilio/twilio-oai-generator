package com.twilio.oai.common;

import org.apache.commons.text.CaseUtils;
import org.apache.commons.text.WordUtils;

public class StringUtils {

    public static String toCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String normalized = input.replaceAll("[^a-zA-Z0-9]", " ");
        normalized = normalized.replaceAll("(?<=[a-z])(?=[A-Z])", " ");
        normalized = normalized.replaceAll("(?<=[A-Z])(?=[A-Z][a-z])", " ");
        String pascalCase = WordUtils.capitalizeFully(normalized).replaceAll(" ", "");
        return Character.toLowerCase(pascalCase.charAt(0)) + pascalCase.substring(1);
    }
    
//    public static String toPascalCase(String input) {
//        if (input == null || input.isEmpty()) {
//            return input;
//        }
//        String normalized = input.replaceAll("[^a-zA-Z0-9]", " ");
//        return WordUtils.capitalizeFully(normalized).replaceAll(" ", "");
//    }

    public static String toPascalCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String normalized = input.replaceAll("[^a-zA-Z0-0]", " ");
        normalized = normalized.replaceAll("(?<=[a-z])(?=[A-Z])", " ");
        normalized = normalized.replaceAll("(?<=[A-Z])(?=[A-Z][a-z])", " ");
        return WordUtils.capitalizeFully(normalized).replaceAll(" ", "");
    }

    public static void main(String[] args) {
        String input = "hello-world-example";
        String pascalCase = toPascalCase(input);
        System.out.println("Pascal Case Output: " + pascalCase); // Output: helloWorldExample
    }
}
