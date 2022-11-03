package com.twilio.oai;

import java.util.Arrays;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class StringHelper {

    public String camelize(final String inputWord) {
        return StringHelper.camelize(inputWord, false);
    }

    public String camelize(final String inputWord, final boolean lowercaseFirstLetter) {
        final String camelized = Arrays
            .stream(inputWord
                        .replaceAll("([a-z])([A-Z])", "$1_$2")
                        .replaceAll("(\\d)([A-Za-z])", "$1_$2")
                        .replaceAll("([A-Z])([A-Z][a-z])", "$1_$2")
                        .split("[_.-]"))
            .map(String::toLowerCase)
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

    public boolean existInSetIgnoreCase(final String item, final Set<String> set) {
        return set.stream().anyMatch(target -> target.equalsIgnoreCase(item));
    }
}
