package com.twilio.oai.resolver.python;

import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resolver.LanguagePropertyResolver;
import org.openapitools.codegen.CodegenProperty;

public class PythonPropertyResolver extends LanguagePropertyResolver {

    public PythonPropertyResolver(IConventionMapper mapper) {
        super(mapper);
    }

    @Override
    protected void resolveProperties(CodegenProperty codegenProperty, ApiResourceBuilder apiResourceBuilder) {
        // update _form paramName to from_ for backward compatibility
        if (codegenProperty.name.equals("_from") && codegenProperty.dataFormat != null &&
                codegenProperty.dataFormat.equals(ApplicationConstants.PHONE_NUMBER)) {
            codegenProperty.name = "from_";
        }
        super.resolveProperties(codegenProperty, apiResourceBuilder);
    }
}
