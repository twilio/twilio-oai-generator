package com.twilio.oai;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private static final String SERVER_PATTERN = "https://(?<domain>[^:/?\\n]+)\\.twilio\\.com";
    private static final String DEFAULT_URL = "/";
    private Map<String, Map<String, Boolean>> toggles = new HashMap();
    public static final String DEFAULT_CONFIG_TOGGLE_JSON_PATH = CONFIG_PATH + File.separator + "toggles.json";
    private final DefaultCodegen codegen;
    private final String name;
    private File toggleFile;

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
        try {
            if (codegen.additionalProperties().containsKey("toggles")) {
                String filePath = (String)codegen.additionalProperties().get("toggles");
                toggles = new ObjectMapper().readValue(new File(filePath), new TypeReference<>(){});
            } else {
                toggles = new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResource(DEFAULT_CONFIG_TOGGLE_JSON_PATH), new TypeReference<>(){});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getVersionFromOpenAPI(final OpenAPI openAPI) {
        String version = "";
        for (String path : openAPI.getPaths().keySet()) {
            Pattern pattern = Pattern.compile("/(\\d{4}-\\d{2}-\\d{2}|v\\d+)");
            Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                version = matcher.group(1);
                version = version.equals("2010-04-01") ? "v2010": version;
                break;
            }
        }
        return version;
    }

    public void setDomain(final String domain) {
        final String domainPackage = domain.replaceAll("[-.]", "");
        setOutputDir(domainPackage, getInputSpecVersion());

        codegen.additionalProperties().put("domainName", StringHelper.camelize(domain));
        codegen.additionalProperties().put("domainPackage", domainPackage);
    }
    
    public void setVersion(final String version) {
        codegen.additionalProperties().put("clientVersion", version);
        codegen.additionalProperties().put(DirectoryStructureService.API_VERSION, version);
        codegen.additionalProperties().put("apiVersionClass", StringHelper.toFirstLetterCaps(version));
    }

    public void setOutputDir(final String domain, final String version) {
        final String domainPackage = domain.replaceAll("[-.]", "");
        final String versionPackage = version.replaceAll("[-.]", "");
        codegen.setOutputDir(originalOutputDir + File.separator + domainPackage + File.separator + versionPackage);
    }

    public String toParamName(final String name) {
        return name.replace("<", "Before").replace(">", "After");
    }

    public String getDomainFromOpenAPI(final OpenAPI openAPI) {
        String domain = "";
        //fetch domain from server url present in openAPI
        if (openAPI.getServers() != null ) {
            Optional<String> url = openAPI.getServers().stream().findFirst().map(Server::getUrl);
            if (url.isPresent() && !url.get().equals(DEFAULT_URL)){
                domain =  url.get().replaceAll(SERVER_PATTERN, "${domain}");
                setDomain(domain);
                return domain;
            }
        }
        //fetch domain from server url present in openAPI.paths
        domain = openAPI
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
        setDomain(domain);
        return domain;
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
}
