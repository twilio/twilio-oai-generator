package com.twilio.oai.template;

import com.twilio.oai.StringHelper;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import org.openapitools.codegen.CodegenConfig;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaApiActionTemplate extends AbstractApiActionTemplate{
    public static final String API_TEMPLATE = "api";
    private static final Map<String, List<String>> operationTemplateMap =  Stream.of(
            new AbstractMap.SimpleEntry<>("create", Arrays.asList("creator.mustache", "Creator.java")),
            new AbstractMap.SimpleEntry<>("fetch", Arrays.asList("fetcher.mustache", "Fetcher.java")),
            new AbstractMap.SimpleEntry<>("delete", Arrays.asList("deleter.mustache", "Deleter.java")),
            new AbstractMap.SimpleEntry<>("read", Arrays.asList("reader.mustache", "Reader.java")),
            new AbstractMap.SimpleEntry<>("update", Arrays.asList("updater.mustache", "Updater.java")),
            new AbstractMap.SimpleEntry<>("api", Arrays.asList("api.mustache", ".java")))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    public JavaApiActionTemplate(CodegenConfig defaultCodegen) {
        super(defaultCodegen);
    }

    @Override
    public Map<String, List<String>> mapping() {
        return operationTemplateMap;
    }

    @Override
    public void addSupportVersion() {

    }
}
