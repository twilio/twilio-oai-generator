package com.twilio.oai.template;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.openapitools.codegen.CodegenConfig;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.SupportingFile;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class AbstractApiActionTemplate implements IApiActionTemplate {
    public static final String API_TEMPLATE = "api";
    public static final String NESTED_MODELS = "nested_models";
    public static final String VERSION_TEMPLATE = "version";

    private final Map<String, List<String>> templates = mapping();
    protected final CodegenConfig codegen;

    // Store dynamic template data
    protected final Map<String, DynamicTemplateData> dynamicTemplates = new LinkedHashMap<>();

    // Inner class to hold dynamic template configuration
    protected static class DynamicTemplateData {
        String templateFile;
        String fileSuffix;
        Object apiResource;  // The full apiResource object (e.g., PhpApiResources)

        DynamicTemplateData(String templateFile, String fileSuffix, Object apiResource) {
            this.templateFile = templateFile;
            this.fileSuffix = fileSuffix;
            this.apiResource = apiResource;
        }
    }

    protected AbstractApiActionTemplate(CodegenConfig defaultCodegen) {
        this.codegen = initialise(defaultCodegen);
    }

    private CodegenConfig initialise(CodegenConfig codegen) {
        codegen.apiTemplateFiles().clear();
        codegen.apiTestTemplateFiles().clear();
        codegen.modelTestTemplateFiles().clear();
        codegen.apiDocTemplateFiles().clear();
        codegen.modelDocTemplateFiles().clear();
        return codegen;
    }

    @Override
    public void clean() {
        for (final List<String> entry : templates.values()) {
            codegen.apiTemplateFiles().remove(entry.get(0));
        }
        dynamicTemplates.clear();
    }

    @Override
    public void add(String template) {
        List<String> templateStrings = templates.get(template);
        codegen.apiTemplateFiles().put(templateStrings.get(0), templateStrings.get(1));
    }

    @Override
    public void addDynamicTemplates(String templateType, Object apiResource) {
        List<String> templateStrings = templates.get(templateType);
        if (templateStrings != null && apiResource != null) {
            // Check if apiResource has responseInstanceModels with more than 1 model
            Set<CodegenModel> models = getResponseInstanceModels(apiResource);
            if (models != null && models.size() > 1) {
                dynamicTemplates.put(templateType, new DynamicTemplateData(
                    templateStrings.get(0),  // template file
                    templateStrings.get(1),  // file suffix
                    apiResource
                ));
            }
        }
    }

    @Override
    public void generateDynamicFiles(Map<String, Object> baseContext, String outputDir) {
        for (Map.Entry<String, DynamicTemplateData> entry : dynamicTemplates.entrySet()) {
            DynamicTemplateData data = entry.getValue();

            // Extract responseInstanceModels from apiResource
            Set<CodegenModel> models = getResponseInstanceModels(data.apiResource);
            if (models == null || models.isEmpty()) {
                continue;
            }

            for (CodegenModel model : models) {
                try {
                    // Build context with apiResource as "resources"
                    // The template uses {{#resources}} wrapper, so we need to provide the apiResource
                    // but with responseInstanceModels containing only the current model
                    Map<String, Object> context = new HashMap<>(baseContext);

                    // Create a wrapper that provides all apiResource properties
                    // but overrides responseInstanceModels with just the current model
                    Map<String, Object> resourcesMap = new HashMap<>();
                    copyObjectProperties(data.apiResource, resourcesMap);
                    // Override responseInstanceModels with single model
                    resourcesMap.put("responseInstanceModels", Collections.singleton(model));
                    context.put("resources", resourcesMap);

                    // Read and compile template
                    String templateContent = readResourceTemplate(data.templateFile);
                    Template template = Mustache.compiler()
                        .withLoader(name -> new StringReader(readResourceTemplate(name + ".mustache")))
                        .defaultValue("")
                        .compile(templateContent);

                    // Render
                    String rendered = template.execute(context);

                    // Write file: {outputDir}/{ModelClassname}{suffix}
                    String filename = outputDir + File.separator + model.classname + data.fileSuffix;
                    writeFile(filename, rendered);

                    System.out.println("Generated dynamic file: " + filename);

                } catch (Exception e) {
                    System.err.println("Error generating dynamic file for " + model.classname + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Extracts responseInstanceModels from an apiResource object using reflection.
     */
    @SuppressWarnings("unchecked")
    private Set<CodegenModel> getResponseInstanceModels(Object apiResource) {
        if (apiResource == null) return null;

        try {
            Field field = findField(apiResource.getClass(), "responseInstanceModels");
            if (field != null) {
                field.setAccessible(true);
                Object value = field.get(apiResource);
                if (value instanceof Set) {
                    return (Set<CodegenModel>) value;
                }
            }
        } catch (Exception e) {
            // Ignore and return null
        }
        return null;
    }

    /**
     * Finds a field by name in the class hierarchy.
     */
    private Field findField(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    /**
     * Copies all properties from an object to a map for mustache rendering.
     * Uses reflection to access fields from the object and its superclasses.
     */
    private void copyObjectProperties(Object source, Map<String, Object> targetMap) {
        if (source == null) return;

        Class<?> clazz = source.getClass();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(source);
                    if (value != null) {
                        targetMap.put(field.getName(), value);
                    }
                } catch (IllegalAccessException e) {
                    // Skip inaccessible fields
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    protected String readResourceTemplate(String templatePath) {
        String fullPath = codegen.templateDir() + "/" + templatePath;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fullPath)) {
            if (is == null) {
                throw new RuntimeException("Template not found: " + fullPath);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read template: " + fullPath, e);
        }
    }

    protected void writeFile(String filename, String content) throws IOException {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write(content);
        }
    }

    @Override
    public void addSupportVersion() {
        final List<String> templateStrings = templates.get(VERSION_TEMPLATE);
        if (templateStrings == null) {
            return;
        }
        final String templateName = templateStrings.get(0);
        final String fileExtension = templateStrings.get(1);
        final String apiVersionClass = codegen.additionalProperties().get("apiVersionClass").toString();

        if (apiVersionClass.startsWith("V")) {
            codegen
                .supportingFiles()
                    .add(new SupportingFile(templateName,
                            getDestinationFilename(apiVersionClass, fileExtension)));
        } else {
            codegen.apiTemplateFiles().put(templateName, fileExtension);
        }
    }

    protected String getDestinationFilename(String apiVersionClass, String fileExtension){
        return ".." + File.separator + getVersionFilename(apiVersionClass) + fileExtension;
    }

    protected String getVersionFilename(final String apiVersionClass) {
        return apiVersionClass;
    }

    public String apiFilename(final String templateName, final String filename) {
        final List<String> templateStrings = templates.get(VERSION_TEMPLATE);
        final String apiVersionClass = codegen.additionalProperties().get("apiVersionClass").toString();

        if (apiVersionClass != null && templateStrings != null && templateName.equals(templateStrings.get(0))) {
            return codegen.apiFileFolder() + File.separator + getVersionFilename(apiVersionClass) +
                templateStrings.get(1);
        }

        return filename;
    }

    protected abstract Map<String, List<String>> mapping();
}
