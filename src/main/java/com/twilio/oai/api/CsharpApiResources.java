package com.twilio.oai.api;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.resolver.csharp.OperationStore;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.ArrayList;
import java.util.List;

public class CsharpApiResources extends ApiResources {
    @Getter @Setter private String authMethod;
    @Getter @Setter private String restClientMethodName;
    @Getter @Setter private String clientName;

    List<CodegenProperty> createResponseModels;
    List<CodegenProperty> updateResponseModels;
    List<CodegenProperty> listResponseModels;
    List<CodegenProperty> patchResponseModels;
    List<CodegenProperty> fetchResponseModels;
    List<IJsonSchemaValidationProperties> enums = new ArrayList<>(OperationStore.getInstance().getEnums().values());

    public String resourceConstant = ApplicationConstants.RESOURCE;

    public CsharpApiResources(CsharpApiResourceBuilder apiResourceBuilder) {
        super(apiResourceBuilder);
        this.authMethod = apiResourceBuilder.authMethod;
        this.createResponseModels = apiResourceBuilder.createResponseModels;
        this.updateResponseModels = apiResourceBuilder.updateResponseModels;
        this.patchResponseModels = apiResourceBuilder.patchResponseModels;
        this.listResponseModels = apiResourceBuilder.listResponseModels;
        this.fetchResponseModels = apiResourceBuilder.fetchResponseModels;
    }
}
