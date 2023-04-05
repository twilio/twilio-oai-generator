package com.twilio.oai;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.openapitools.codegen.DefaultCodegen;

import static com.twilio.oai.common.ApplicationConstants.CONFIG_PATH;

@RequiredArgsConstructor
public class TwilioCodegenAdapter {

    private static final String INPUT_SPEC_PATTERN = "[^_]+_(?<domain>.+?)(_(?<version>[^_]+))?\\..+";
    // regex example : https://flex-api.twilio.com
    private static final String SERVER_PATTERN = "https://(?<domain>[^.]+)\\.twilio\\.com";
    private Map<String, Map<String, Boolean>> toggles = new HashMap();
    public static final String CONFIG_TOGGLE_JSON_PATH = CONFIG_PATH + File.separator + "toggles.json";
    private final DefaultCodegen codegen;
    private final String name;

    private String originalOutputDir;

    public void processOpts() {
        // Find the templates in the local resources dir.
        codegen.setTemplateDir(name);
        // Remove the "API" suffix from the API filenames.
        codegen.setApiNameSuffix("");
        codegen.setApiPackage("");
        codegen.setEnsureUniqueParams(false);

        originalOutputDir = codegen.getOutputDir();
        setDomain(getInputSpecDomain());

        final String version = StringHelper.camelize(getInputSpecVersion(), true);
        codegen.additionalProperties().put("clientVersion", version);
        codegen.additionalProperties().put("apiVersion", version);
        codegen.additionalProperties().put("apiVersionClass", StringHelper.toFirstLetterCaps(version));

        codegen.supportingFiles().clear();

        Arrays.asList("Configuration", "Parameter", "Version").forEach(word -> {
            codegen.reservedWords().remove(word);
            codegen.reservedWords().remove(word.toLowerCase());
        });
        toggles = getTogglesMap();
    }

    public void setDomain(final String domain) {
        final String domainPackage = domain.replace("-", "");
        setOutputDir(domainPackage, getInputSpecVersion());

        codegen.additionalProperties().put("domainName", StringHelper.camelize(domain));
        codegen.additionalProperties().put("domainPackage", domainPackage);
    }

    public void setOutputDir(final String domain, final String version) {
        codegen.setOutputDir(originalOutputDir + File.separator + domain + File.separator + version);
    }

    public String toParamName(final String name) {
        return name.replace("<", "Before").replace(">", "After");
    }

    public String getDomainFromOpenAPI(final OpenAPI openAPI) {
        return openAPI
            .getPaths()
            .values()
            .stream()
            .findFirst()
            .map(PathItem::getServers)
            .map(Collection::stream)
            .flatMap(Stream::findFirst)
            .map(Server::getUrl)
            .map(url -> url.replaceAll(SERVER_PATTERN, "${domain}"))
            .orElseThrow();
    }

    public Map<String, Boolean> getToggles(String value) {
        return this.toggles.get(value);
    }

    private String getInputSpecDomain() {
        return codegen.getInputSpec().replaceAll(INPUT_SPEC_PATTERN, "${domain}");
    }

    private String getInputSpecVersion() {
        return codegen.getInputSpec().replaceAll(INPUT_SPEC_PATTERN, "${version}");
    }

    private Map<String,Map<String, Boolean>> getTogglesMap() {
        try {
            return new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(CONFIG_TOGGLE_JSON_PATH), new TypeReference<>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
