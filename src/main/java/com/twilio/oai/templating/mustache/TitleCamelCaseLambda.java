package com.twilio.oai.templating.mustache;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import com.twilio.oai.StringHelper;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.Writer;

@RequiredArgsConstructor
public class TitleCamelCaseLambda implements Mustache.Lambda {
    private final String delimiter;

    public TitleCamelCaseLambda() {
        this(" ");
    }

    @Override
    public void execute(Template.Fragment fragment, Writer writer) throws IOException {
        final String text = StringHelper.camelize(fragment.execute(), true);
        writer.write(text);
    }
}
