package com.twilio.oai.resolver;

import com.twilio.oai.CodegenUtils;
import com.twilio.oai.StringHelper;
import com.twilio.oai.api.ApiResourceBuilder;
import com.twilio.oai.common.ApplicationConstants;

import com.twilio.oai.resolver.common.CodegenModelResolver;
import lombok.AllArgsConstructor;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;

import java.util.LinkedHashMap;
import java.util.List;

import static com.twilio.oai.common.ApplicationConstants.SERIALIZE_VEND_EXT;

public class LanguageParamResolver extends Resolver<CodegenParameter> {
    protected IConventionMapper mapper;
    protected CodegenModelResolver codegenModelResolver;
    public static final String OBJECT = "object";
    private static final String LIST_OBJECT = "List<Object>";

    public LanguageParamResolver(IConventionMapper mapper) {
        this.mapper = mapper;
    }

    public LanguageParamResolver(IConventionMapper mapper, CodegenModelResolver codegenModelResolver) {
        this.mapper = mapper;
        this.codegenModelResolver = codegenModelResolver;
    }

    @Override
    public CodegenParameter resolve(CodegenParameter codegenParameter, ApiResourceBuilder apiResourceBuilder) {

        CodegenModel codegenModel = apiResourceBuilder.getModel(codegenParameter.dataType);
        if (codegenModel != null && codegenModelResolver !=null && !CodegenUtils.isParameterSchemaEnum(codegenParameter)) { //json body
            codegenModelResolver.resolve(codegenModel, apiResourceBuilder);
            CodegenUtils.mergeVendorExtensionProperty(codegenParameter.vendorExtensions,
                    (LinkedHashMap) codegenModel.getVendorExtensions().get("x-import"), "x-import");
            apiResourceBuilder.addNestedModel(codegenModel);
        }
        else {
            resolveProperties(codegenParameter, apiResourceBuilder);
            resolvePrefixedMap(codegenParameter);
            resolveSerialize(codegenParameter);
        }
        return codegenParameter;
    }

    protected void resolveProperties(CodegenParameter codegenParameter, ApiResourceBuilder apiResourceBuilder) {
        if((codegenParameter.dataType.equalsIgnoreCase(OBJECT) || codegenParameter.dataType.equals(LIST_OBJECT)) && codegenParameter.vendorExtensions.get("x-is-anytype") == null) {
            String objectType = mapper.properties().getString(OBJECT).orElseThrow();

            if (codegenParameter.isAnyType || (codegenParameter.isArray && codegenParameter.items.isAnyType)) {
                objectType = "Object";
                codegenParameter.vendorExtensions.put("x-is-anytype", true);
            }

            else
                codegenParameter.isFreeFormObject = true;

            if (codegenParameter.dataType.equals(LIST_OBJECT)) {
                codegenParameter.dataType = ApplicationConstants.LIST_START + objectType + ApplicationConstants.LIST_END;
                codegenParameter.baseType = objectType;
            } else {
                codegenParameter.dataType = objectType;
            }
        }

        if(codegenParameter.vendorExtensions.get("x-is-anytype") == null) {
            mapper
                    .properties()
                    .getString(codegenParameter.dataFormat)
                    .ifPresent(dataType -> codegenParameter.dataType = dataType);
        }
    }

    protected void resolveSerialize(CodegenParameter codegenParameter) {
        mapper
            .serialize()
            .getString(getDataType(codegenParameter))
            .ifPresent(serialize -> codegenParameter.vendorExtensions.put(SERIALIZE_VEND_EXT,
                                                                          serialize.split("\\(")[0]));
    }

    protected void resolvePrefixedMap(CodegenParameter codegenParameter) {
        if (codegenParameter.dataFormat != null && codegenParameter.dataFormat
                .startsWith(ApplicationConstants.PREFIXED_COLLAPSIBLE_MAP)) {
            String[] split_format_array = codegenParameter.dataFormat.split(LanguageConventionResolver.HYPHEN);
            codegenParameter.vendorExtensions.put(LanguageConventionResolver.X_PREFIXED_COLLAPSIBLE_MAP,
                    split_format_array[split_format_array.length - 1]);
            codegenParameter.dataType = mapper.properties()
                    .getString(ApplicationConstants.PREFIXED_COLLAPSIBLE_MAP).orElse(null);
        }
        codegenParameter.paramName = StringHelper.toFirstLetterLower(codegenParameter.paramName);
    }
}
