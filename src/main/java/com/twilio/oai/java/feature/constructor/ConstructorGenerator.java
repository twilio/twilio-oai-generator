package com.twilio.oai.java.feature.constructor;

import com.twilio.oai.StringHelper;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ConstructorGenerator {
    abstract void apply(CodegenOperation codegenOperation);
    abstract boolean shouldApply(CodegenOperation codegenOperation);
}
