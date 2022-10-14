package com.twilio.oai.mlambdas;

import com.twilio.oai.StringHelper;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TitleCaseLambda implements Mustache.Lambda {
    private final String delimiter;

    public TitleCaseLambda() {
        this(" ");
    }

    @Override
    public void execute(Template.Fragment fragment, Writer writer) throws IOException {
        final String text = fragment.execute().toLowerCase();

        // Split accepts regex. \Q and \E wrap the delimiter to create a literal regex,
        // so things like "." and "|" aren't treated as their regex equivalents.
        final String[] parts = text.split("\\Q" + delimiter + "\\E");
        writer.write(Arrays.stream(parts).map(StringHelper::camelize).collect(Collectors.joining(delimiter)));
    }
}
