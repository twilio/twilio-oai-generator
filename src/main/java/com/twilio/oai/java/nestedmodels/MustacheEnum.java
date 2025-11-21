package com.twilio.oai.java.nestedmodels;

import com.twilio.oai.common.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Currently only String enums are Supported
/*
    public enum Direction {
        INBOUND("inbound"),
        OUTBOUND_API("outbound-api"),
        OUTBOUND_CALL("outbound-call"),
        OUTBOUND_REPLY("outbound-reply");

        private final String value;

        private Direction(final String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }

        @JsonCreator
        public static Direction forValue(final String value) {
            return Promoter.enumFromString(value, Direction.values());
        }
    }
 */
public class MustacheEnum {
    String className;
    Map<String, Object> values;
    
    /*
    Allowed Keys:
    name: Enum value name, typically in capital letter (INBOUND, OUTBOUND_API, etc.)
    value: Enum value as in open api spec. (inbound, outbound-api, etc.)
     */
    List<Map<String, Object>> enumValues;
    
    public MustacheEnum(String className, List<Map<String, Object>> enumValues) {
        if (org.apache.commons.lang3.StringUtils.isBlank(className) || enumValues == null || enumValues.isEmpty()) {
            throw new RuntimeException("MustacheEnum requires a non-empty className and values map.");
        }
        this.className = StringUtils.toPascalCase(className);
        this.enumValues = new ArrayList<>(enumValues);
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
