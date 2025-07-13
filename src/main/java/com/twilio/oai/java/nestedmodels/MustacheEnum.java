package com.twilio.oai.java.nestedmodels;

import com.twilio.oai.common.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// Currently only String enums are Supported
public class MustacheEnum {
    String className;
    Map<String, Object> values;
    
    public MustacheEnum(String className, Map<String, Object> values) {
        if (org.apache.commons.lang3.StringUtils.isBlank(className) || values == null || values.isEmpty()) {
            throw new RuntimeException("MustacheEnum requires a non-empty className and values map.");
        }
        this.className = StringUtils.toPascalCase(className);
        this.values = new HashMap<>(values);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MustacheEnum that = (MustacheEnum) obj;
        return className != null && className.equals(that.className);
    }

    @Override
    public int hashCode() {
        return className != null ? className.hashCode() : 0;
    }
}

/*
# Fetching class name for enum
Example1:
---------
- in: query
  name: PauseBehaviorMethod
  required: true
  schema:
    type: string
    enum:
      - Trial
      - Full

name = PascalCase(codegenParameter.baseName)

Example2:
---------
- in: header
  name: X-Twilio-Webhook-Enabled
  schema:
    type: string
    $ref: '#/components/schemas/account_enum_X_Twilio_Webhook_Enabled'




Example3: this is passed as property (parameter.items --> codegenProperty)
---------
- in: header
  name: DummyStatus
  required: true
  schema:
    type: array
    items:
      $ref: '#/components/schemas/test_enum_status'
SplitEnum(codegenProperty.datatypeWithEnum)

Example4:
---------
- in: header
  name: SomeStatus
  required: true
  schema:
    type: array
    items:
      type: string
      enum:
       - active
       - inactive
       - pending

PascalCase(codegenParameter.baseName)
 */
