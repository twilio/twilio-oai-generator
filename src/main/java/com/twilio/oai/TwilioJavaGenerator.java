package com.twilio.oai;

import com.twilio.oai.api.JavaApiResourceBuilder;
import com.twilio.oai.api.JavaApiResources;
import com.twilio.oai.common.EnumConstants;
import com.twilio.oai.common.Utility;
import com.twilio.oai.mlambdas.ReplaceHyphenLambda;
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

    private static final String VALUES = "values";

    private final TwilioCodegenAdapter twilioCodegen;
    private final IApiActionTemplate apiActionTemplate;
    private final DirectoryStructureService directoryStructureService = new DirectoryStructureService(
        additionalProperties,
        new ResourceMap(new Inflector()),
        new JavaCaseResolver());
    private final Map<String, String> modelFormatMap = new HashMap<>();
    private final IConventionMapper conventionMapper = new LanguageConventionResolver(CONFIG_JAVA_JSON_PATH);
    private final List<CodegenModel> allModels = new ArrayList<>();

    public TwilioJavaGenerator() {
        super();
        apiActionTemplate = new JavaApiActionTemplate(this);
        twilioCodegen = new TwilioCodegenAdapter(this, getName());
        sourceFolder = "";
    }

    @Override
    public void processOpts() {
        super.processOpts();
        twilioCodegen.processOpts();
    }

    @Override
    public void processOpenAPI(final OpenAPI openAPI) {
        final String domain = twilioCodegen.getDomainFromOpenAPI(openAPI);
        twilioCodegen.setDomain(domain);
        directoryStructureService.configure(openAPI);
    }

    @Override
    public String toParamName(final String name) {
        return super.toVarName(twilioCodegen.toParamName(name));
    }

    @Override
    public void postProcessParameter(final CodegenParameter parameter) {
        super.postProcessParameter(parameter);
        if (parameter.dataType.startsWith(LIST_START) && parameter.dataType.contains("Enum")) {
            parameter.vendorExtensions.put(REF_ENUM_EXTENSION_NAME, true);
            String[] value = parameter.dataType.split("Enum");
            String lastValue = value[value.length-1];
            parameter.dataType = LIST_START+lastValue;
            parameter.baseType = lastValue.substring(0, lastValue.length()-1);
        } else if(parameter.dataType.contains("Enum")) {
             parameter.vendorExtensions.put(REF_ENUM_EXTENSION_NAME, true);
            String[] value = parameter.dataType.split("Enum");
            parameter.dataType = value[value.length-1];
            parameter.baseType = value[value.length-1];
        }
        else if (parameter.isEnum) {
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
        if (property.dataType.startsWith(LIST_START) && property.dataType.contains("Enum")) {
            property.vendorExtensions.put(REF_ENUM_EXTENSION_NAME, true);
            String[] value = property.dataType.split("Enum");
            String lastValue = value[value.length-1];
            property.dataType = LIST_START + lastValue;
            property.complexType = lastValue.substring(0, lastValue.length()-1);
            property.baseType = lastValue.substring(0, lastValue.length()-1);
             property.isEnum = true;
            property.allowableValues = property.items.allowableValues;
            property._enum = (List<String>) property.items.allowableValues.get(VALUES);
        } else if (property.dataType.contains("Enum")) {
            property.vendorExtensions.put(REF_ENUM_EXTENSION_NAME, true);
            String[] value = property.dataType.split("Enum");
            property.dataType = value[value.length - 1];
            property.complexType = property.dataType;
            property.baseType = property.dataType;
            property.isEnum = true;
            property._enum = (List<String>) property.allowableValues.get(VALUES);
        } else if (property.isEnum ) {
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
        return EnumConstants.Generator.TWILIO_JAVA.getValue();
    }

    @Override
    public String getHelp() {
        return "Generates the twilio-java helper library.";
    }

    private JavaApiResources processCodegenOperations(List<CodegenOperation> opList) {
        CodegenModelResolver codegenModelResolver = new CodegenModelResolver(conventionMapper, modelFormatMap,
            Arrays.asList(EnumConstants.JavaDataTypes.values()));
        return new JavaApiResourceBuilder(apiActionTemplate, opList, this.allModels, conventionMapper)
            .updateApiPath()
            .updateTemplate()
            .updateOperations(new JavaParameterResolver(conventionMapper))
            .updateResponseModel(new JavaPropertyResolver(conventionMapper), codegenModelResolver)
            .build();
    }
}
