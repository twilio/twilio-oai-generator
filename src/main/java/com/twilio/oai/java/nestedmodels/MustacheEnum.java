package com.twilio.oai.java.nestedmodels;

import com.twilio.oai.common.StringUtils;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenParameter;
import org.openapitools.codegen.CodegenProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MustacheEnum {
    String name;
    Map<String, Object> values;
    String isString;
    
    public MustacheEnum(CodegenProperty codegenProperty) {
        if (codegenProperty == null) throw new RuntimeException("Enum creation failed for CodegenProperty");
        this.name = StringUtils.toPascalCase(splitByEnum(codegenProperty.complexType));
        values = new HashMap<>(codegenProperty.allowableValues);
    }

    public MustacheEnum(CodegenParameter codegenParameter) {
        if (codegenParameter.isEnum) {
            name = StringUtils.toPascalCase(codegenParameter.baseName);
            values = new HashMap<>(codegenParameter.allowableValues);
        } else if (codegenParameter.isEnumRef) {
            name = StringUtils.toPascalCase(splitByEnum(codegenParameter.getSchema().complexType));
            values = new HashMap<>(codegenParameter.allowableValues);
        } else {
            throw new RuntimeException("Enum Name is not fetched correctly for " + codegenParameter);
        }
    }

    public MustacheEnum(CodegenModel codegenModel) {

    }
    
    private String splitByEnum(final String input) {
        String[] split = input.split("Enum");
        if (split.length >= 2) return split[1];
        return input;
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
