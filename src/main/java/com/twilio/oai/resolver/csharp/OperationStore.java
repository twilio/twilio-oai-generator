package com.twilio.oai.resolver.csharp;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.codegen.IJsonSchemaValidationProperties;

import java.util.HashMap;

public final class OperationStore {

    private OperationStore() { }
    private static final ThreadLocal<OperationStore> instance = ThreadLocal.withInitial(OperationStore::new);

    public static OperationStore getInstance() {
        return instance.get();
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
