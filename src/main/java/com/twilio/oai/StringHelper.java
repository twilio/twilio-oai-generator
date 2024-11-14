package com.twilio.oai;

import java.util.Arrays;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class StringHelper {

    private static final String SNAKE_SEPARATOR = "_";
    private static final String SNAKE_REPLACER = "$1" + SNAKE_SEPARATOR + "$2";

    public String toSnakeCase(final String inputWord) {
        return inputWord
            .replaceAll("[^a-zA-Z\\d]+", SNAKE_SEPARATOR)
            .replaceAll("([a-z])([A-Z])", SNAKE_REPLACER)
            .replaceAll("(\\d[A-Z]*)([A-Z])", SNAKE_REPLACER)
            .replaceAll("([A-Z])([A-Z][a-z])", SNAKE_REPLACER)
            .toLowerCase();
    }

    public String camelize(final String inputWord) {
        return StringHelper.camelize(inputWord, false);
    }

    public String camelize(final String inputWord, final boolean lowercaseFirstLetter) {
        final String camelized = Arrays
            .stream(toSnakeCase(inputWord).replaceAll("(\\d)([a-z])", SNAKE_REPLACER).split(SNAKE_SEPARATOR))
            .map(StringHelper::toFirstLetterCaps)
            .collect(Collectors.joining());

        return lowercaseFirstLetter ? toFirstLetterLower(camelized) : camelized;
    }

    public String toFirstLetterCaps(final String input) {
        return convertFirstChar(input, String::toUpperCase);
    }

    public String toFirstLetterLower(final String input) {
        return convertFirstChar(input, String::toLowerCase);
    }

    private String convertFirstChar(final String inputWord, final UnaryOperator<String> firstCharFunction) {
        return StringUtils.isBlank(inputWord)
            ? inputWord
            : firstCharFunction.apply(inputWord.substring(0, 1)) + inputWord.substring(1);
    }

    public static String convertUrlToCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        Pattern pattern = Pattern.compile("\\{([a-z0-9_]+)\\}");
        Matcher matcher = pattern.matcher(input);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String original = matcher.group(1);
            String[] parts = original.split("_");
            StringBuilder camelCase = new StringBuilder();
            for (int i = 0; i < parts.length; i++) {
                camelCase.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1));
            }
            matcher.appendReplacement(result, "{" + camelCase.toString() + "}");
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
