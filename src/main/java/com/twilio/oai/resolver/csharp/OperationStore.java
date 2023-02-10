package com.twilio.oai.resolver.csharp;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.HashMap;

public final class OperationStore {

    private OperationStore() { }
    private static OperationStore instance;
    public static OperationStore getInstance()
    {
        if (instance == null)
            instance = new OperationStore();

        return instance;
    }

    public void clear() {
        className = "";
        enums.clear();
        isEnumPresentInResource = false;
        isEnumPresentInOptions = false;
    }

    @Getter
    @Setter
    private String className;
    @Getter
    @Setter
    private boolean isEnumPresentInResource;
    @Getter
    @Setter
    private boolean isEnumPresentInOptions;
    @Getter
    @Setter
    private HashMap<String, IJsonSchemaValidationProperties> enums = new HashMap<>();
}
