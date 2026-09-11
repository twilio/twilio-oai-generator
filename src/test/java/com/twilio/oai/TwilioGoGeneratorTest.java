package com.twilio.oai;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Covers the flattened-`oneOf` handling in {@link TwilioGoGenerator}.
 *
 * <p>Flattening a `oneOf` unions every variant's `required` list onto the merged struct, so a field
 * required by a single variant was serialized -- as `""`, `0` or a zero timestamp -- for every other
 * variant, and the request matched the wrong variant.
 */
public class TwilioGoGeneratorTest {

    private TwilioGoGenerator generator;

    @Before
    public void setUp() {
        generator = new TwilioGoGenerator();
    }

    private CodegenProperty property(final String baseName, final boolean required, final String goType) {
        final CodegenProperty property = new CodegenProperty();
        property.baseName = baseName;
        property.name = baseName;
        property.required = required;
        property.vendorExtensions.put("x-go-base-type", goType);
        return property;
    }

    private CodegenModel model(final String classname, final CodegenProperty... properties) {
        final CodegenModel model = new CodegenModel();
        model.classname = classname;
        model.allVars = Arrays.asList(properties);
        return model;
    }

    private Map<String, ModelsMap> asModelsMap(final CodegenModel... models) {
        final Map<String, ModelsMap> allModels = new HashMap<>();
        for (final CodegenModel model : models) {
            final ModelMap modelMap = new ModelMap();
            modelMap.setModel(model);
            final ModelsMap modelsMap = new ModelsMap();
            modelsMap.setModels(java.util.Collections.singletonList(modelMap));
            allModels.put(model.classname, modelsMap);
        }
        return allModels;
    }

    @Test
    public void relaxesFieldsRequiredByOnlySomeVariants() {
        final CodegenModel union = model("KnowledgeSourceTypes",
            property("type", true, "string"),
            property("content", true, "string"),
            property("url", true, "string"));
        union.oneOf = new java.util.LinkedHashSet<>(Arrays.asList("TextSourceDetails", "WebSourceDetails"));

        final CodegenModel text = model("TextSourceDetails",
            property("type", true, "string"), property("content", true, "string"));
        final CodegenModel web = model("WebSourceDetails",
            property("type", true, "string"), property("url", true, "string"));

        generator.relaxOneOfVariantRequired(asModelsMap(union, text, web));

        final Map<String, CodegenProperty> byName = new HashMap<>();
        union.allVars.forEach(v -> byName.put(v.baseName, v));

        // `type` is required by every variant -- it is the discriminator and must stay required.
        assertTrue("discriminator must remain required", byName.get("type").required);
        assertFalse(Boolean.TRUE.equals(byName.get("type").vendorExtensions.get("x-go-omit-empty")));

        // `content` and `url` are conditionally required, so they must become omittable.
        assertFalse("content is required by one variant only", byName.get("content").required);
        assertFalse("url is required by one variant only", byName.get("url").required);
        assertTrue((Boolean) byName.get("content").vendorExtensions.get("x-go-omit-empty"));
        assertTrue((Boolean) byName.get("url").vendorExtensions.get("x-go-omit-empty"));
    }

    @Test
    public void keepsFieldRequiredWhenEveryVariantRequiresIt() {
        final CodegenModel union = model("TypingIndicatorRequest",
            property("channel", true, "string"), property("from", true, "string"));
        union.oneOf = new java.util.LinkedHashSet<>(Arrays.asList("WhatsApp", "Rcs"));

        final CodegenModel whatsApp = model("WhatsApp",
            property("channel", true, "string"), property("from", true, "string"));
        final CodegenModel rcs = model("Rcs",
            property("channel", true, "string"), property("from", true, "string"));

        generator.relaxOneOfVariantRequired(asModelsMap(union, whatsApp, rcs));

        union.allVars.forEach(v -> assertTrue(v.baseName + " required by all variants", v.required));
    }

    /** An unresolvable variant must leave the model alone rather than relax a genuinely required field. */
    @Test
    public void leavesModelUntouchedWhenAVariantCannotBeResolved() {
        final CodegenModel union = model("PartiallyKnown",
            property("type", true, "string"), property("content", true, "string"));
        union.oneOf = new java.util.LinkedHashSet<>(Arrays.asList("Known", "Missing"));

        final CodegenModel known = model("Known",
            property("type", true, "string"), property("content", true, "string"));

        generator.relaxOneOfVariantRequired(asModelsMap(union, known));

        union.allVars.forEach(v -> assertTrue("unresolved variant must not relax anything", v.required));
    }

    @Test
    public void leavesNonUnionModelsAlone() {
        final CodegenModel plain = model("DataMappingToTraits",
            property("type", true, "string"), property("mappings", true, "[]MappingTraitItem"));

        generator.relaxOneOfVariantRequired(asModelsMap(plain));

        plain.allVars.forEach(v -> assertTrue("non-union required fields stay required", v.required));
    }

    @Test
    public void pointerizesOmittableTimestampsInUnionModels() {
        final CodegenProperty uploadExpiration = property("uploadExpiration", false, "time.Time");
        final CodegenProperty createdAt = property("createdAt", true, "time.Time");
        final CodegenModel union = model("KnowledgeSourceTypes", uploadExpiration, createdAt);
        union.oneOf = new java.util.LinkedHashSet<>(Arrays.asList("FileSourceDetails"));
        final CodegenModel file = model("FileSourceDetails", property("type", true, "string"));

        generator.pointerizeOmittableTimestamps(asModelsMap(union, file));

        // omitempty is inert on a struct, so an omittable time.Time must become *time.Time.
        assertTrue("omittable timestamp becomes a pointer", uploadExpiration.isNullable);
        // A required timestamp is always transmitted, so it keeps its value type.
        assertFalse("required timestamp stays a value", createdAt.isNullable);
    }

    @Test
    public void doesNotPointerizeTimestampsOutsideUnionModels() {
        final CodegenProperty createdAt = property("createdAt", false, "time.Time");
        final CodegenModel plain = model("Knowledge", createdAt);

        generator.pointerizeOmittableTimestamps(asModelsMap(plain));

        // Widening this to every model would turn ~90 response fields into pointers for no gain.
        assertFalse("non-union timestamps keep their value type", createdAt.isNullable);
    }
}
