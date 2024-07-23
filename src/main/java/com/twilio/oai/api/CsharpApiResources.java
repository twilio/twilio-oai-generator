package com.twilio.oai.api;

import com.twilio.oai.common.ApplicationConstants;
import com.twilio.oai.resolver.csharp.OperationStore;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.ArrayList;
import java.util.List;

public class CsharpApiResources extends ApiResources {
    @Getter @Setter private String authMethod;
<<<<<<< HEAD
    @Getter @Setter private String resourceSetPrefix;
    @Getter @Setter private String domainClass;
    @Getter @Setter private String restClientMethodName;
    @Getter @Setter private String clientName;
    @Getter @Setter private String requestName;
=======
    @Getter @Setter private String restClientPrefix;
    @Getter @Setter private String resourceSetPrefix;
    @Getter @Setter private String domainClassPrefix;
>>>>>>> a326080 (orgs api uptake for twilio csharp)
    List<IJsonSchemaValidationProperties> enums = new ArrayList<>(OperationStore.getInstance().getEnums().values());

    public String resourceConstant = ApplicationConstants.RESOURCE;

    public CsharpApiResources(ApiResourceBuilder apiResourceBuilder) {
        super(apiResourceBuilder);
    }
}
