package com.twilio.oai;

import com.twilio.oai.api.ApiResources;
import com.twilio.oai.api.NodeApiResourceBuilder;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.LanguagePropertyResolver;
import com.twilio.oai.resolver.common.CodegenModelResolver;
import com.twilio.oai.resolver.node.NodeParameterResolver;
import com.twilio.oai.resolver.ruby.RubyParameterResolver;
import org.openapitools.codegen.languages.RubyClientCodegen;
import org.openapitools.codegen.model.ModelsMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.twilio.oai.common.ApplicationConstants.CONFIG_NODE_JSON_PATH;
import static com.twilio.oai.common.ApplicationConstants.CONFIG_RUBY_JSON_PATH;

public class TwilioRubyGenerator extends RubyClientCodegen {
    private final TwilioCodegenAdapter twilioCodegen;
    private final Map<String, String> modelFormatMap = new HashMap<>();

    public TwilioRubyGenerator() {
        super();
        twilioCodegen = new TwilioCodegenAdapter(this, getName());
    }

    @Override
    public void processOpts() {
        super.processOpts();

        twilioCodegen.processOpts();
    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(Map<String, ModelsMap> objs) {
        return new HashMap<>();
    }

    private ApiResources generateResources() {
        final IConventionMapper conventionMapper = new LanguageConventionResolver(CONFIG_RUBY_JSON_PATH);
//        final CodegenModelResolver codegenModelResolver = new CodegenModelResolver(conventionMapper,
//                modelFormatMap,
//                List.of(EnumConstants.RubyDataTypes.values()));
        new RubyParameterResolver(conventionMapper);

        return null;
//        return new NodeApiResourceBuilder(actionTemplate, opList, allModels, directoryStructureService)
//                .updateApiPath()
//                .updateTemplate()
//                .updateOperations(new NodeParameterResolver(conventionMapper))
//                .updateResponseModel(new LanguagePropertyResolver(conventionMapper), codegenModelResolver)
//                .build();
    }

    @Override
    public String getName() {
        return "twilio-ruby";
    }
}

