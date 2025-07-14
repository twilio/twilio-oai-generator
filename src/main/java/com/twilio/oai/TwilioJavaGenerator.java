package com.twilio.oai;

import com.twilio.oai.api.JavaApiResourceBuilder;
import com.twilio.oai.api.JavaApiResources;
import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.templating.mustache.ReplaceHyphenLambda;
import com.twilio.oai.resolver.LanguageConventionResolver;
import com.twilio.oai.resolver.common.CodegenModelResolver;
import com.twilio.oai.resolver.java.JavaCaseResolver;
import com.twilio.oai.resolver.java.JavaParameterResolver;
import com.twilio.oai.resolver.java.JavaPropertyResolver;
import com.twilio.oai.resolver.IConventionMapper;
import com.twilio.oai.resource.ResourceMap;

import com.twilio.oai.template.IApiActionTemplate;
import com.twilio.oai.template.JavaApiActionTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.JavaClientCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationsMap;

import java.util.*;

import com.google.common.collect.ImmutableMap;
import com.samskivert.mustache.Mustache.Lambda;

import static com.twilio.oai.common.ApplicationConstants.*;

public class TwilioJavaGenerator extends JavaClientCodegen {

    public static final String VALUES = "values";
    public static final String JSON_INGRESS = "json_ingress";

    private final TwilioCodegenAdapter twilioCodegen;
    private final IApiActionTemplate apiActionTemplate = new JavaApiActionTemplate(this);
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
            additionalProperties,
            new ResourceMap(new Inflector()),
            new JavaCaseResolver());
    private final Map<String, String> modelFormatMap = new HashMap<>();
    private final IConventionMapper conventionMapper = new LanguageConventionResolver(CONFIG_JAVA_JSON_PATH);
    private final List<CodegenModel> allModels = new ArrayList<>();

    public TwilioJavaGenerator() {
        super();
        twilioCodegen = new TwilioCodegenAdapter(this, getName());
        sourceFolder = "";
        this.typeMapping.put("string+abc", "CustomType");
        this.importMapping.put("CustomType", "hello.import.CustomType");
        System.out.println(this.typeMapping());
    }

    @Override
    public void processOpts() {
        super.processOpts();
        twilioCodegen.processOpts();
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        String domain = twilioCodegen.getDomainFromOpenAPI(openAPI);
        String version = twilioCodegen.getVersionFromOpenAPI(openAPI);
        twilioCodegen.setDomain(domain);
        twilioCodegen.setVersion(version);
        twilioCodegen.setOutputDir(domain, version);
        directoryStructureService.configure(openAPI);
    }

    @Override
    public String toParamName(final String name) {
        return super.toVarName(twilioCodegen.toParamName(name));
    }

    @Override
    public void postProcessParameter(final CodegenParameter parameter) {
        super.postProcessParameter(parameter);
        if (parameter.dataType.startsWith(LIST_START) && CodegenUtils.isParameterSchemaEnumJava(parameter)) {
            if (parameter.dataType.contains(ENUM)) {
                String lastValue = Utility.removeEnumName(parameter.dataType);
                parameter.dataType = LIST_START + lastValue;
                parameter.vendorExtensions.put(REF_ENUM_EXTENSION_NAME, true);
                parameter.baseType = lastValue.substring(0, lastValue.length() - 1); 
            }
        } else if (CodegenUtils.isParameterSchemaEnumJava(parameter)) {
            parameter.vendorExtensions.put(REF_ENUM_EXTENSION_NAME, true);
            parameter.enumName = parameter.dataType;
            parameter.dataType = Utility.removeEnumName(parameter.dataType);
            parameter.baseType = Utility.removeEnumName(parameter.dataType);
            parameter.isEnum = true;
        } else if (parameter.isEnum) {
            parameter.enumName = parameter.paramName;
        } else {
            if (parameter.isPathParam) {
                parameter.paramName = "Path" + parameter.paramName.substring(0, 1).toUpperCase() + parameter.paramName.substring(1);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);
        if (property.dataType.startsWith(LIST_START) && CodegenUtils.isPropertySchemaEnumJava(property)) {
            String lastValue = Utility.removeEnumName(property.dataType);
            property.dataType = LIST_START + lastValue;
            property.vendorExtensions.put(REF_ENUM_EXTENSION_NAME, true);
            property.complexType = lastValue.substring(0, lastValue.length() - 1);
            property.baseType = lastValue.substring(0, lastValue.length() - 1);
            property.isEnum = true;
            property.allowableValues = property.items.allowableValues;
            property._enum = (List<String>) property.items.allowableValues.get(VALUES);
        } else if (CodegenUtils.isPropertySchemaEnumJava(property)) {
            property.vendorExtensions.put(REF_ENUM_EXTENSION_NAME, true);
            property.dataType = Utility.removeEnumName(property.dataType);
            property.complexType = property.dataType;
            property.baseType = property.dataType;
            property.isEnum = true;
            property._enum = (List<String>) property.allowableValues.get(VALUES);
        } else if (property.isEnum) {
            property.enumName = property.baseName;
        }
        property.isEnum = property.isEnum && property.dataFormat == null;
    }

    @Override
    public String toApiFilename(final String name) {
        return directoryStructureService.toApiFilename(super.toApiFilename(name));
    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(final Map<String, ModelsMap> allModels) {
        final Map<String, ModelsMap> results = super.postProcessAllModels(allModels);

        Utility.addModelsToLocalModelList(results, this.allModels);
        directoryStructureService.postProcessAllModels(results, modelFormatMap);

        // Return an empty collection so no model files get generated.
        return new HashMap<>();
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(final OperationsMap objs, List<ModelMap> allModels) {
        final OperationsMap results = super.postProcessOperationsWithModels(objs, allModels);
        final List<CodegenOperation> opList = directoryStructureService.processOperations(results);
        JavaApiResources apiResources = processCodegenOperations(opList);
        results.put("resources", apiResources);
        return results;
    }

    @Override
    protected ImmutableMap.Builder<String, Lambda> addMustacheLambdas() {
        ImmutableMap.Builder<String, Lambda> lambdaBuilder = super.addMustacheLambdas();
        lambdaBuilder.put("replacehyphen", new ReplaceHyphenLambda());
        return lambdaBuilder;
    }

    @Override
    public String getName() {
        return EnumConstants.Generator.TWILIO_CSHARP.getValue();
    }

    @Override
    public String getHelp() {
        return "Generates the twilio-java helper library.";
    }

    private JavaApiResources processCodegenOperations(List<CodegenOperation> opList) {
        CodegenModelResolver codegenModelResolver = new CodegenModelResolver(conventionMapper, modelFormatMap,
                Arrays.asList(EnumConstants.JavaDataTypes.values()));
        JavaApiResourceBuilder javaApiResourceBuilder= new JavaApiResourceBuilder(apiActionTemplate, opList,
                allModels, twilioCodegen.getToggles(JSON_INGRESS), new JavaPropertyResolver(conventionMapper));
        javaApiResourceBuilder
                .updateApiPath()
                .updateTemplate();
        final Boolean isIngress = twilioCodegen.getToggles(JSON_INGRESS).
                get(EnumConstants.Generator.TWILIO_JAVA.getValue());
        javaApiResourceBuilder.updateOperations(new JavaParameterResolver(conventionMapper))
                .updateResponseModel(new JavaPropertyResolver(conventionMapper), codegenModelResolver);
        if (isIngress) {
           //javaApiResourceBuilder.updateModel(codegenModelResolver);
        }
        return javaApiResourceBuilder.build();
    }
}
