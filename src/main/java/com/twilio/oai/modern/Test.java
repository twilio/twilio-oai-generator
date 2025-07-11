package com.twilio.oai.modern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static String extractCustomType(String dstring) {
        // Define the regular expression pattern
        Pattern pattern = Pattern.compile("<([^>]+)>");

        // Create a matcher object
        Matcher matcher = pattern.matcher(dstring);

        // Check if the pattern matches
        if (matcher.find()) {
            // Return the captured group which is the custom type
            return matcher.group(1);
        }
        return null; // Return null if no match is found
    }

    public static String replaceCustomType(String input, String replacement) {
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

    public static void main(String[] args) {
        String input = "Set<Dummy>";
        String replacement = "Abc";

        String result = replaceCustomType(input, replacement);
        System.out.println(result); // Output: List<Abc>
    }
}