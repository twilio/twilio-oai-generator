package com.twilio.oai.api;

import com.twilio.oai.DirectoryStructureService;
import com.twilio.oai.PathUtils;
import com.twilio.oai.java.cache.ResourceCacheContext;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

import static com.twilio.oai.common.ApplicationConstants.IGNORE_EXTENSION_NAME;
import static java.util.function.Predicate.not;

public class FluentApiResources extends ApiResources {
    private String instanceName;
    private String instancePath;
    private String listPath;
    private List<CodegenParameter> instancePathParams;
    private List<CodegenParameter> listPathParams;
    private CodegenModel responseModel;
    private Collection<CodegenModel> models;
    private Collection<DirectoryStructureService.DependentResource> dependents;

    public FluentApiResources(final FluentApiResourceBuilder apiResourceBuilder) {
        super(apiResourceBuilder);
        instanceName = apiName + "Instance";
        instancePath = apiResourceBuilder.instancePath;
        listPath = apiResourceBuilder.listPath;
        instancePathParams = apiResourceBuilder.instancePathParams;
        listPathParams = apiResourceBuilder.listPathParams;
        responseModel = apiResourceBuilder.responseModel;
        models = apiResourceBuilder.modelTree.values();
        dependents = apiResourceBuilder.dependents.values();
    }

    public List<CodegenOperation> getOperations() {
        return Stream
            .concat(apiOperations.stream().filter(PathUtils::isInstanceOperation),
                    apiOperations.stream().filter(not(PathUtils::isInstanceOperation)))
            .filter(co -> !Optional
                .ofNullable(co.vendorExtensions.get(IGNORE_EXTENSION_NAME))
                .map(Boolean.class::cast)
                .orElse(false))
            .collect(Collectors.toList());
    }

    /**
     * Returns true if this resource has multiple distinct response models for V1 APIs.
     * Used by templates to determine whether to generate operation-specific response interfaces.
     */
    public boolean getHasMultipleResponseModels() {
        boolean isApiV1 = ResourceCacheContext.get() != null && ResourceCacheContext.get().isV1();
        return isApiV1 && responseInstanceModels != null && responseInstanceModels.size() > 1;
    }

    /**
     * Returns the collection of response instance models.
     * For V1 APIs with multiple response models, this contains all distinct response types.
     */
    public Collection<CodegenModel> getResponseInstanceModels() {
        return responseInstanceModels;
    }

    /**
     * Returns the collection of nested/complex models used in the resource.
     * These are models referenced within response models or parameters.
     */
    public Collection<CodegenModel> getModels() {
        return models;
    }
}
