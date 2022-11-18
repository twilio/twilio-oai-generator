package com.twilio.oai.template;

import java.util.*;

public interface ITemplate {
    void clean();
    void add(String template);
    void addContextResources(HashMap<String, List<Object>> contextResourcesMap);
}
