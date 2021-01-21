package com.twilio.oai;

import org.openapitools.codegen.CodegenConstants;
import org.openapitools.codegen.languages.GoClientCodegen;
import org.openapitools.codegen.CodegenProperty;
import static org.openapitools.codegen.utils.StringUtils.camelize;
import io.swagger.v3.oas.models.media.Schema;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTwilioGoGenerator extends GoClientCodegen {

    public AbstractTwilioGoGenerator() {
        super();

        embeddedTemplateDir = templateDir = getName();
    }

    @Override
    public void processOpts() {
        super.processOpts();

        additionalProperties.put(CodegenConstants.IS_GO_SUBMODULE, true);

        supportingFiles.clear();
    }

    @Override
    public Map<String, String> createMapping(final String key, final String value) {
        // Optional dependency not needed.
        if (value.equals("github.com/antihax/optional")) {
            return new HashMap<>();
        }

        return super.createMapping(key, value);
    }

    @Override
    public void updateCodegenPropertyEnum(CodegenProperty var) {
        // make sure the inline enums have plain defaults (e.g. string, int, float)
        String enumDefault = null;
        if (var.isEnum && var.defaultValue != null) {
            enumDefault = var.defaultValue;
        }
        super.updateCodegenPropertyEnum(var);
        if (var.isEnum && enumDefault != null) {
            var.defaultValue = enumDefault;
        }
    }

    @Override
    public CodegenProperty fromProperty(String name, Schema p) {
        CodegenProperty prop = super.fromProperty(name, p);
        prop = super.fromProperty(name, p);
        String cc = camelize(prop.name, true);
        if (isReservedWord(cc)) {
            cc = escapeReservedWord(cc);
        }
        prop.nameInCamelCase = cc;
        prop.baseName = prop.baseName.replaceAll("[-+.^:,]","");
        System.out.println("basename="+prop.baseName);
        return prop;
    }

//    @Override
//    public String toParamName(String name) {
//        // params should be lowerCamelCase. E.g. "person Person", instead of
//        // "Person Person".
//        //
//        name = camelize(toVarName(name), true);
//
//        // REVISIT: Actually, for idiomatic go, the param name should
//        // really should just be a letter, e.g. "p Person"), but we'll get
//        // around to that some other time... Maybe.
//        if (isReservedWord(name)) {
//            name = name + "_";
//        }
//
//        name = name.replaceAll("[-+.^:,]","");
//        name = name.replace("<","lesserThan");
//        name = name.replace(">","greaterThan");
//
//        return name;
//    }
}
