package com.twilio.oai.resolver.ruby;

import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguagePropertyResolver;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;

import java.util.List;

import static com.twilio.oai.common.ApplicationConstants.DESERIALIZE_VEND_EXT;

public class RubyPropertyResolver extends LanguagePropertyResolver  {
    public RubyPropertyResolver(IConventionMapper mapper, List<CodegenModel> allModels) {
        super(mapper, allModels);
    }

    @Override
    protected void resolveDeSerialize(CodegenProperty codegenProperty){
        mapper
                .deserialize()
                .getString(getDataType(codegenProperty))
                .ifPresent(deserialize -> codegenProperty.vendorExtensions.put(DESERIALIZE_VEND_EXT,
                        deserialize));
    }
}
