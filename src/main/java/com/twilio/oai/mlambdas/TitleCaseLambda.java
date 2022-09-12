package com.twilio.oai.mlambdas;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import com.twilio.oai.common.Utility;

import java.io.IOException;
import java.io.Writer;

public class TitleCaseLambda implements Mustache.Lambda {

    private String delimiter;

    public TitleCaseLambda() {
        this(" ");
    }

    public TitleCaseLambda(String delimiter) {
        this.delimiter = delimiter;
    }

    private static String titleCase(final String input) {
        String[] splits = input.split("((?<=[-0-9]))");
        StringBuilder sb = new StringBuilder();
        for (String word: splits) {
            word = Utility.toFirstLetterCaps(word);
            sb.append(word);
        }
        return sb.toString().replaceAll("-", "");
    }

    @Override
    public void execute(Template.Fragment fragment, Writer writer) throws IOException {
        String text = fragment.execute();
        if (delimiter == null) {
            writer.write(titleCase(text));
            return;
        }

        // Split accepts regex. \Q and \E wrap the delimiter to create a literal regex,
        // so things like "." and "|" aren't treated as their regex equivalents.
        String[] parts = text.split("\\Q" + delimiter + "\\E");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            writer.write(titleCase(part));
            if (i != parts.length - 1) {
                writer.write(delimiter);
            }
        }
    }
}
