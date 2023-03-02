package com.twilio.oai.templating;

import com.twilio.oai.templating.handlebars.TwilioHelpers;

import java.io.IOException;
import java.util.Map;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;
import com.github.jknack.handlebars.helper.ConditionalHelpers;
import com.github.jknack.handlebars.helper.StringHelpers;
import com.github.jknack.handlebars.io.AbstractTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;
import org.openapitools.codegen.api.TemplatingExecutor;
import org.openapitools.codegen.templating.HandlebarsEngineAdapter;

public class TwilioHandlebarsEngineAdapter extends HandlebarsEngineAdapter {
    @Override
    public String getIdentifier() {
        return "twilio-handlebars";
    }

    @Override
    public String compileTemplate(final TemplatingExecutor executor,
                                  final Map<String, Object> bundle,
                                  final String templateFile) throws IOException {
        final TemplateLoader loader = new AbstractTemplateLoader() {
            @Override
            public TemplateSource sourceAt(final String location) {
                return findTemplate(executor, location);
            }
        };

        final Context context = Context
            .newBuilder(bundle)
            .resolver(MapValueResolver.INSTANCE,
                      JavaBeanValueResolver.INSTANCE,
                      FieldValueResolver.INSTANCE,
                      MethodValueResolver.INSTANCE)
            .build();

        final Handlebars handlebars = new Handlebars(loader);
        StringHelpers.register(handlebars);
        handlebars.registerHelpers(TwilioHelpers.class);
        handlebars.registerHelpers(ConditionalHelpers.class);
        handlebars.registerHelpers(org.openapitools.codegen.templating.handlebars.StringHelpers.class);

        final Template template = handlebars.compile(templateFile);
        return template.apply(context);
    }
}
