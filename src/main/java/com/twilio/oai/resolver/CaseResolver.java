package com.twilio.oai.resolver;

public interface CaseResolver {
    String productOperation(String product);

    String pathOperation(String pathPart);

    String filenameOperation(String filename);
}
